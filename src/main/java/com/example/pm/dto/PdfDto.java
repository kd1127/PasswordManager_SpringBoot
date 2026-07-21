package com.example.pm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PdfDto {
	//	HTTPステータス
	private Integer httpStatus;
	//	PDF出力時のメッセージ
	private String pdfOutputMessage;
}