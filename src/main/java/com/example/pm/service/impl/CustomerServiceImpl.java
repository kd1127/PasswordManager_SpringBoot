package com.example.pm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.pm.PmLogOutput;
import com.example.pm.dto.InquiryForm;
import com.example.pm.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private final JavaMailSender mailSender;
	@Autowired 
	private PmLogOutput log;
	
	public CustomerServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	public String mailAddressFormatCheck(String mailAddress) {
		String errorMessage = "";
		
		//	メールアドレスが空白文字かどうかチェック
		if(StringUtils.equals(mailAddress, "")) {
			log.warning("", "メールアドレス入力チェック", "メールアドレスを入力してください。");
			errorMessage = "メールアドレスを入力してください。";
			return errorMessage;
		}
		
		//	メールアドレスに@が含まれていなければ、エラーメッセージを格納する
		if(!StringUtils.contains(mailAddress, "@")){
			log.warning("", "メールアドレス形式チェック", "@を含んだメールアドレスの形式で入力してください。");
			errorMessage= "@を含んだメールアドレスの形式で入力してください。";
		}
		return errorMessage;
	}
	
	//	パスワード再設定に使用
	@Override
	public String mailSend(String mailAddress) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			mailMessage.setTo(mailAddress);
			mailMessage.setFrom("y.hashimoto1127@gmail.com");
//			mailMessage.setSubject("テスト送信");
			mailMessage.setSubject("パスワード再設定");
//			mailMessage.setText("http://localhost:8080/passwordReConfigure");
			mailMessage.setText("http://ec2-3-113-162-188.ap-northeast-1.compute.amazonaws.com//passwordReConfigure");
			
			mailSender.send(mailMessage);
			log.info("", "パスワード再設定メール送信");
		} catch (MailException e) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e);
			return "何らかの理由により、メール送信失敗しました。";
		} catch (Exception e2) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e2);
			return "何らかの理由により、メール送信失敗しました。";
		}
		return "メール送信完了しました。";
	}
	
	//	inquiryContentの問い合わせの文章の途中に改行コードを入れる
	@Override
	public InquiryForm insertToNewLineChar(InquiryForm inquiryForm) {
		int inquiryContentTotal = inquiryForm.getInquiryContent().length();
		char[] tmpInquiryContent = inquiryForm.getInquiryContent().toCharArray();
		List<Character> inquiryContentList = new ArrayList<Character>();
		//	char[]をList<Character>に変換
		for(int i=0; i<tmpInquiryContent.length; i++) {
			inquiryContentList.add(tmpInquiryContent[i]);
		}
		for(int i=0; i<inquiryContentTotal; i++) {
			if((i % 30) == 0) {
				inquiryContentList.add(i, '\n');
				inquiryContentTotal++;
			}
		}
		inquiryContentList.forEach(s -> System.out.println(s));
		String inquiryContent = "";
		for(int i=0; i<inquiryContentList.size(); i++) {
			inquiryContent.concat(String.valueOf(inquiryContentList.get(i)));
		}
		inquiryForm.setInquiryContent(inquiryContent);
		return inquiryForm;
	}
	
	/**
	 * 問い合わせメール送信に使用
	 * @param InquiryForm
	 */
	@Override
	public String mailSendInruiryForm(InquiryForm inquiryForm) {
		try {
			log.info("", "お問い合わせ内容の文中に改行コードを入れる処理：開始");
			inquiryForm = this.insertToNewLineChar(inquiryForm);
			log.info("", "お問い合わせ内容の文中に改行コードを入れる処理：正常終了");
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			mailMessage.setFrom(inquiryForm.getMailAddress());
			mailMessage.setTo("y.hashimoto1127@gmail.com");
			mailMessage.setSubject("お問い合わせ");
			mailMessage.setText("名前：" + inquiryForm.getName() + "\n 問い合わせ内容：\n" + inquiryForm.getInquiryContent());
			
			mailSender.send(mailMessage);
			log.info("", "ポートフォリオサイト：お問い合わせメール送信");
		} catch (MailException e) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e);
			return "何らかの理由により、メール送信失敗しました。";
		} catch (Exception e2) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e2);
			e2.printStackTrace();
			return "何らかの理由により、メール送信失敗しました。";
		}
		return "メール送信完了しました。";
	}
}