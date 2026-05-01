package com.example.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pm.PmLogOutput;
import com.example.pm.dto.AccountInfoDto;
import com.example.pm.dto.PdfDto;
import com.example.pm.service.ApplicationService;

@RestController
@RequestMapping("api")
public class RestApiController {
	
	@Autowired
	private PdfDto pdfDto; 
	@Autowired
	private AccountInfoDto accountInfoDto;
	@Autowired
	private ApplicationService service;
	@Autowired 
	private PmLogOutput log;
	
	@CrossOrigin
	@GetMapping("/pdfOutput")
	public PdfDto pdfOutput(Model model) {
		log.info("", "service.pdfOutput ---> 開始");
		pdfDto = service.pdfOutput(accountInfoDto.getAccountInfoList());
		log.info("", "service.pdfOutput ---> 正常終了");
		if(pdfDto.getPdfOutputMessage() == null) {
			log.warning("", "PDF出力", "システムエラーによりダウンロードできませんでした。");
			pdfDto.setPdfOutputMessage("システムエラーによりダウンロードできませんでした。");
		}
		return pdfDto;
	}
}