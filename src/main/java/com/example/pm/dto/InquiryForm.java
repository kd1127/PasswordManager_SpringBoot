package com.example.pm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryForm {
	//	問い合わせたクライアントの名前
	private String name;
	//	メールアドレス
	private String mailAddress;
	//	問い合わせ内容
	private String inquiryContent;
}
