package com.example.pm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@CrossOrigin
	@GetMapping("/pdfOutput")
	public PdfDto pdfOutput(Model model) {
		pdfDto.setPdfOutputMessage(service.pdfOutput(accountInfoDto.getAccountInfoList()));
		if(pdfDto.getPdfOutputMessage() == null) {
			pdfDto.setPdfOutputMessage("システムエラーによりダウンロードできませんでした。");
		}
		return pdfDto;
	}
}