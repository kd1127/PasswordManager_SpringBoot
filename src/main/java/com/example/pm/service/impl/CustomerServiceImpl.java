package com.example.pm.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.pm.PmLogOutput;
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
	
	@Override
	public String mailSend(String mailAddress) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			mailMessage.setTo(mailAddress);
			mailMessage.setFrom("doragon_kd@yahoo.co.jp");
			mailMessage.setSubject("テスト送信");
			mailMessage.setText("http://localhost:8080/passwordReConfigure");
			
			mailSender.send(mailMessage);
		} catch (MailException e) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e);
			return "何らかの理由により、メール送信失敗しました。";
		} catch (Exception e2) {
			log.error("", "メール送信", "何らかの理由により、メール送信失敗しました。", e2);
			return "何らかの理由により、メール送信失敗しました。";
		}
		return "メール送信完了しました。";
	}
}