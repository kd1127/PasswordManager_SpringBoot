package com.example.pm.service;

public interface CustomerService {

	public String mailAddressFormatCheck(String mailAddress);
	public String mailSend(String mailAddress);
}