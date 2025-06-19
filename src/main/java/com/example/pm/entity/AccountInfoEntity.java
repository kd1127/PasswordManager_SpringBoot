package com.example.pm.entity;

import java.sql.Date;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * アカウント情報格納用クラス
 */

@Data
@Component
public class AccountInfoEntity {
	@NotBlank
	private Integer id;
	
	@NotBlank
	private Integer id_user;
	
	@NotBlank
	private String userId;
	
	@NotBlank
	private String passWd;
	
	private String siteName;
	
	private Date inp_date;
	
	private Date upd_date;
}
