package com.example.pm.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pm.entity.UserInfoEditEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;

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
	public List<String> loginDataCheck(String userId, String passWd, String passKey) {
		List<String> errorMessage = new ArrayList<String>();
		
		//	ユーザーIDの検証
		if(StringUtils.isEmpty(userId)) {
			errorMessage.add("ユーザーIDを入力してください。");
		}
		else {
			String userIdVerify = tableOperationMapper.userIdOneVerify(userId);
			
			if(!StringUtils.equals(userId, userIdVerify)) {
				errorMessage.add("ユーザーIDが一致しません。");
			}
		}
		
		//	パスワードの検証、入力値有りの場合、パスワードをハッシュ化してテーブルから取得したデータと突き合わせ
		if(StringUtils.isEmpty(passWd)) {
			errorMessage.add("パスワードを入力してください。");
		}
		else {
			if (!StringUtils.equals(passWd, "ADMIN")) {
				UserInfoEntity userInfoEntity = new UserInfoEntity(null, passWd, null, null, null, null);
				passWd = String.valueOf(userInfoEntity.hashCode());
			}
			String passWdVerify = tableOperationMapper.passWdOneVerify(passWd);
			
			if(!StringUtils.equals(passWd, passWdVerify)) {
				errorMessage.add("パスワードが一致しません。");
			}
		}
		
		//	パスキーの検証
		if(StringUtils.isEmpty(passKey)) {
			errorMessage.add("パスキーを入力してください。");
		}
		else {
			String passKeyVerify = tableOperationMapper.passKeyOneVerify(passKey);
			
			if(!StringUtils.equals(passKey, passKeyVerify)) {
				errorMessage.add("パスキーが一致しません。");
			}
		}
		
		return errorMessage;
	}
	
	//	パスワード・パスワード（確認）が一致しているか検証するメソッド
	@Override
	public String passWdMatchCheckProcess(UserInfoEntity userInfoEntity, UserInfoEditEntity userInfoEditEntity) {
		String errorMsg = "";
		if(userInfoEntity != null) {
			if(!userInfoEntity.getPassWd().equals(userInfoEntity.getRe_PassWd())) {
				errorMsg = "パスワード・パスワード（確認）が一致しません";
			}
		}
		if(userInfoEditEntity != null) {
			if(!userInfoEditEntity.getPassWd().equals(userInfoEditEntity.getRe_PassWd())) {
				errorMsg = "パスワード・パスワード（確認）が一致しません";
			}
		}
		return errorMsg;
	}	
	
	//	ユーザー登録画面で入力したデータをuserinfoテーブルに登録
	@Override
	public String userInfoInsertOperation(UserInfoEntity userInfoEntity) {
		int insertCount = tableOperationMapper.userInfoInsert(userInfoEntity);
		if(insertCount < 1) {
			String errorMsg = "データ登録に失敗しました。";
			return errorMsg;
		}
		return "";
	}
	
	@Override
	public String passWdUpdateDbOperation(String userId, String passWd) {
		int updateCount = tableOperationMapper.passWdUpdate(userId, passWd);
		String message = "";
		
		if(updateCount < 1) {
			message = "何らかの理由により、パスワード更新に失敗しました。";
		}		
		return message;
	}
}
