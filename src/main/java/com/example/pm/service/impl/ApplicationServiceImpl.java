package com.example.pm.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pm.PmLogOutput;
import com.example.pm.dto.AccountInfoDto;
import com.example.pm.dto.PdfDto;
import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import lombok.RequiredArgsConstructor;

/*
 * ビジネスロジッククラス
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
	@Autowired
	public TableOperationMapper tableOperationMapper;
	@Autowired
	private PdfDto pdfDto;
	@Autowired 
	private PmLogOutput log;
	
	//	inp_dateに日付を格納
	@Override
	public Date tableInsertpreparation() {
		LocalDate local = LocalDate.now();
		Date inp_date = Date.valueOf(local);
		return inp_date;
	}
	
	//	passKeyの数字部分を一桁ずつ乱数で得る
	@Override
	public String passKeyNumberRandomGet(UserInfoEntity uiEntity) {
		String passKey = uiEntity.getPassKey();
		Random random = new Random();
		int rand = -1;
		
		for(int i=0; i<8; i++) {
			rand = random.nextInt(10);
			passKey = passKey.concat(String.valueOf(rand));			
		}		
		return passKey;
	}
	
	/*
	 * ログイン機能
	 * 各カラムのデータを取得してきたListの中身と入力内容を比較してエラーメッセージを格納して呼び出し元に返す
	 * 各リクエストパラーメーターは未入力だと、空文字になる
	 */
	@Override
	public List<String> loginDataCheck(String userId, String passWd, String passKey) throws Exception{
		List<String> errorMessage = new ArrayList<String>();
		log.info("", userId, passWd, passKey);
		//	ユーザーIDの検証
		if(StringUtils.isEmpty(userId)) {
			errorMessage.add("ユーザーIDを入力してください。");
			log.warning("", "ユーザーIDの検証", "ユーザーIDを入力してください。");
		}
		else {
			log.info("", "tableOperationMapper.userIdOneVerify処理 ---> 開始");
			String userIdVerify = tableOperationMapper.userIdOneVerify(userId);
			log.info("userIdVerify: " + userIdVerify, "tableOperationMapper.userIdOneVerify処理 ---> 正常終了");
			
			if(!StringUtils.equals(userId, userIdVerify)) {
				errorMessage.add("ユーザーIDが一致しません。");
				log.warning("", "ユーザーIDの検証", "ユーザーIDが一致しません。");
			}
		}
		
		//	パスワードの検証、入力値有りの場合、パスワードをハッシュ化してテーブルから取得したデータと突き合わせ
		if(StringUtils.isEmpty(passWd)) {
			errorMessage.add("パスワードを入力してください。");
			log.warning("", "パスワードの検証", "パスワードを入力してください。");
		}
		else {
			if (!StringUtils.equals(passWd, "ADMIN")) {
				UserInfoEntity userInfoEntity = new UserInfoEntity(null, passWd, null, null, null, null);
				passWd = String.valueOf(userInfoEntity.hashCode());
			}
			log.info("", "tableOperationMapper.passWdOneVerify処理 ---> 開始");
			String passWdVerify = tableOperationMapper.passWdOneVerify(passWd);
			log.info("passWdVerify: " + passWdVerify, "tableOperationMapper.passWdOneVerify処理 ---> 正常終了");
			
			if(!StringUtils.equals(passWd, passWdVerify)) {
				errorMessage.add("パスワードが一致しません。");
				log.warning("", "パスワードの検証", "パスワードが一致しません。");
			}
		}
		
		//	パスキーの検証
		if(StringUtils.isEmpty(passKey)) {
			errorMessage.add("パスキーを入力してください。");
			log.warning("", "パスキーの検証", "パスキーを入力してください。");
		}
		else {
			log.info("", "tableOperationMapper.passKeyOneVerify処理 ---> 開始");
			String passKeyVerify = tableOperationMapper.passKeyOneVerify(passKey);
			log.info("passKeyVerify: " + passKeyVerify, "tableOperationMapper.passKeyOneVerify処理 ---> 正常終了");
			
			if(!StringUtils.equals(passKey, passKeyVerify)) {
				log.warning("", "パスキーの検証", "パスキーが一致しません。");
				errorMessage.add("パスキーが一致しません。");
			}
		}		
		return errorMessage;
	}
	
	//	パスワード・パスワード（確認）が一致しているか検証するメソッド
	@Override
	public List<String> passWdMatchCheckProcess(UserInfoEntity userInfoEntity) {
		List<String> errorMsgList = new ArrayList<>();
		log.info("", "ApplicationService.userIdDuplicateCheck処理 ---> 開始");
		errorMsgList = this.userIdDuplicateCheck(userInfoEntity.getUserId(), errorMsgList);
		errorMsgList.forEach(s -> log.info(s));
		log.info("", "ApplicationService.userIdDuplicateCheck処理 ---> 正常終了");
		if(userInfoEntity != null) {
			if(!userInfoEntity.getPassWd().equals(userInfoEntity.getRe_PassWd())) {
				errorMsgList.add("・パスワード・パスワード（確認）が一致しません");
				log.warning("", "パスワード・パスワード（確認）の検証", "パスワード・パスワード（確認）が一致しません");
			}
		}
		return errorMsgList;
	}	
	
	//	ユーザー登録画面で入力したデータをuserinfoテーブルに登録
	@Override
	public String userInfoInsertOperation(UserInfoEntity userInfoEntity) {
		log.info("", "tableOperationMapper.userInfoInsert処理 ---> 開始");
		int insertCount = tableOperationMapper.userInfoInsert(userInfoEntity);
		log.info("登録件数： " + insertCount, "tableOperationMapper.userInfoInsert処理 ---> 正常終了");
		if(insertCount < 1) {
			log.warning("", "データ登録", "データ登録に失敗しました。");
			String errorMsg = "データ登録に失敗しました。";
			return errorMsg;
		}
		return "";
	}
	
	/**
	 * ログインパスワード変更メソッド
	 * @param userId ユーザーID
	 * @param passWd パスワード
	 */
	@Override
	public String passWdUpdateDbOperation(String userId, String passWd) {
		log.info("", "tableOperationMapper.passWdUpdate処理 ---> 開始");
		int updateCount = tableOperationMapper.passWdUpdate(userId, passWd);
		log.info("更新件数: " + updateCount, "tableOperationMapper.passWdUpdate処理 ---> 正常終了");
		String message = "";
		
		if(updateCount < 1) {
			log.warning("", "ログインパスワード更新", "何らかの理由により、パスワード更新に失敗しました。");
			message = "何らかの理由により、パスワード更新に失敗しました。";
		}		
		return message;
	}
	
	/**
	 * ユーザー登録時、ユーザーIDの重複を調べるメソッド
	 * @param userId ユーザーID
	 * @param errorMessageList エラーメッセージを格納するリスト
	 */
	@Override
	public List<String> userIdDuplicateCheck(String userId, List<String> errorMessageList) {
		if(userId == null) {
			errorMessageList.add("・不正なユーザーIDです。");
		}
		log.info("", "tableOperationMapper.userIdAllGet処理 ---> 開始");
		List<String> userIdList = tableOperationMapper.userIdAllGet();
		log.info("", "tableOperationMapper.userIdAllGet処理 ---> 正常終了");
		if(userIdList.contains(userId)) {
			errorMessageList.add("・他のユーザーが登録しているユーザーIDです。別のユーザーIDを登録してください。");
			log.warning("", "ユーザーIDの重複の検証", "他のユーザーが登録しているユーザーIDです。別のユーザーIDを登録してください。");
		}		
		return errorMessageList;
	}
	
	// 登録データ表示画面で、表示ボタンを押下して動作するメソッド
	@Override
	public AccountInfoDto displayDataOfPaging(AccountInfoDto accountInfoDto){
		List<AccountInfoEntity> returnToAccountInfoList = new ArrayList<>();
		int i = accountInfoDto.getShowCompletionCount();
		int displayCount = accountInfoDto.getDisplayCount();
		
		while(i<displayCount) {
			returnToAccountInfoList.add(accountInfoDto.getAccountInfoList().get((int)i));
			i++;
			
			if(i == accountInfoDto.getAccountInfoList().size()) {
				break;
			}
		}
		accountInfoDto.setPagingAccountInfoList(returnToAccountInfoList);
		accountInfoDto.setShowCompletionCount(i);
		accountInfoDto.setMaxPage(this.findMaximumPage(accountInfoDto));
		accountInfoDto.setPage(1L);
		if(accountInfoDto.getAccountInfoList().size() > accountInfoDto.getDisplayCount()) {
			accountInfoDto.setNextButtonFlag(true);
		}
		else {
			accountInfoDto.setNextButtonFlag(false);
		}
		return accountInfoDto;
	}
	
	//	最大ページを調べる
	@Override
	public Long findMaximumPage(AccountInfoDto accountInfoDto){
		Integer maximumPage = accountInfoDto.getAccountInfoList().size() / accountInfoDto.getDisplayCount() + 1;
		return maximumPage.longValue(); 
	}
	
	@Override
	public AccountInfoDto nextDataOfPaging(AccountInfoDto accountInfoDto) {
		List<AccountInfoEntity> nextPageAccountInfo = new ArrayList<>();
		int i = accountInfoDto.getShowCompletionCount();
		
		while(i < accountInfoDto.getDisplayCount()) {
			if(i == accountInfoDto.getAccountInfoList().size()) {
				accountInfoDto.setNextButtonFlag(true);
				break;
			}
			nextPageAccountInfo.add(accountInfoDto.getAccountInfoList().get(i));
			i++;
		}
		accountInfoDto.setPagingAccountInfoList(nextPageAccountInfo);
		int showCompletionCount = accountInfoDto.getShowCompletionCount() + i;
		accountInfoDto.setShowCompletionCount(showCompletionCount);
		accountInfoDto.setPrevButtonFlag(true);
		
		long page = accountInfoDto.getPage() + 1;
		accountInfoDto.setPage(page);
		
		//	ページが最大ページと同じなら「次へ」ボタンを非活性にする
		if(accountInfoDto.getPage() == accountInfoDto.getMaxPage()) {
			accountInfoDto.setNextButtonFlag(false);
		}
		return accountInfoDto;
	}
	
	@Override
	public AccountInfoDto prevDataOfPaging(AccountInfoDto accountInfoDto, int displayCount) {
		List<AccountInfoEntity> prevPageAccountInfo = new ArrayList<>();
		//	初期値用displayCount
		Integer initDisplayCount = accountInfoDto.getDisplayCount() - displayCount * 2;
		//	終値用displayCount
		Integer closingDisplayCount = accountInfoDto.getDisplayCount() - displayCount;
		int i = initDisplayCount;
		//	displayCountを表示件数分、値をマイナスにする
		int count = 0;
		while(i<closingDisplayCount) {
			if(i == accountInfoDto.getAccountInfoList().size()) {
				break;
			}
			prevPageAccountInfo.add(accountInfoDto.getAccountInfoList().get(i));
			i++;
			count++;
		}
		accountInfoDto.setPagingAccountInfoList(prevPageAccountInfo);
		accountInfoDto.setShowCompletionCount(i);
		long page = accountInfoDto.getPage() - 1;
		accountInfoDto.setPage(page);
		accountInfoDto.setNextButtonFlag(true);
		int showCompletionCount = accountInfoDto.getShowCompletionCount() - i;
		accountInfoDto.setShowCompletionCount(showCompletionCount);
		int setDisplayCount = accountInfoDto.getDisplayCount() - count;
		accountInfoDto.setDisplayCount(setDisplayCount);
		
		//	pageが1ページ目なら「前へボタン」を非活性にする
		if(accountInfoDto.getPage() == 1) {
			accountInfoDto.setPrevButtonFlag(false);
		}
		return accountInfoDto;
	}
	
	public List<AccountInfoEntity> search(int id, String siteName) {
		log.info("", "tableOperationMapper.searchSiteName処理 ---> 開始");
		List<AccountInfoEntity> searchResultList = tableOperationMapper.searchSiteName(id, siteName);
		log.info("", "tableOperationMapper.searchSiteName処理 ---> 正常終了");
		return searchResultList;
	}
	
	public PdfDto pdfOutput(List<AccountInfoEntity> accountInfoEntityList) {
		String dest = "RegisteredData.pdf";
		try {
			// 出力先ファイルの準備
            File file = new File(dest);
            if(file.exists()) {
            	throw new FileAlreadyExistsException(dest);
            }
			//	PDFオブジェクト作成
			PdfWriter writer = new PdfWriter(file);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			//	フォントの設定
			PdfFont font = PdfFontFactory.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", pdfDocument);
			document.setFont(font);
			//	タイトル
			document.add(new Paragraph("登録データ"));
			float[] columnWidths = {150F, 150F, 150F, 150F, 150F, 150F};
            Table table = new Table(columnWidths);
			//	ヘッダーをセット
			table.addHeaderCell(new Paragraph("No."));
			table.addHeaderCell(new Paragraph("ユーザーID"));	
			table.addHeaderCell(new Paragraph("パスワード"));
			table.addHeaderCell(new Paragraph("サイト名"));
			table.addHeaderCell(new Paragraph("登録日時"));
			table.addHeaderCell(new Paragraph("更新日時"));
			
			int rowCount = 1;
			for(AccountInfoEntity accountInfoEntity : accountInfoEntityList) {
				table.addCell(new Paragraph(String.valueOf(rowCount)));
				table.addCell(new Paragraph(accountInfoEntity.getUserId()));
				table.addCell(new Paragraph(accountInfoEntity.getPassWd()));
				table.addCell(new Paragraph(accountInfoEntity.getSiteName()));
				table.addCell(new Paragraph(String.valueOf(accountInfoEntity.getInp_date())));
				table.addCell(new Paragraph(String.valueOf(accountInfoEntity.getUpd_date())));
				rowCount++;
			}
			
			document.add(table);
			document.close();
			pdfDto.setPdfOutputMessage("PDF出力完了");
			pdfDto.setHttpStatus(200);
			return pdfDto;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("", "PDF出力", "何らかの理由によりファイルが見つかりませんでした。", e);
			pdfDto.setPdfOutputMessage("何らかの理由によりファイルが見つかりませんでした。");
			pdfDto.setHttpStatus(500);
			return pdfDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", "PDF出力", "何らかの理由によりダウンロードできませんでした。", e);
			pdfDto.setPdfOutputMessage("何らかの理由によりダウンロードできませんでした。");
			pdfDto.setHttpStatus(500);
			return pdfDto;
		} catch (Throwable e) {
			log.error("", "PDF出力", "システムエラー・ハードウェアエラーによりダウンロードできませんでした。", e);
			pdfDto.setPdfOutputMessage("システムエラーによりダウンロードできませんでした。");
			pdfDto.setHttpStatus(500);
			return pdfDto;
		}
	}
}
