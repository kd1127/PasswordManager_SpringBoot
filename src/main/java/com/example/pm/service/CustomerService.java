package com.example.pm.service;

import com.example.pm.dto.InquiryForm;

public interface CustomerService {

	String mailAddressFormatCheck(String mailAddress);
	String mailSend(String mailAddress);
	String mailSendInruiryForm(InquiryForm inquiryForm);
	InquiryForm insertToNewLineChar(InquiryForm inquiryForm);
}