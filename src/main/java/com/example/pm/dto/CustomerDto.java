package com.example.pm.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerDto {
	
	private String userId;

	private String mailAddress;
}