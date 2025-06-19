package com.example.pm.entity;

import java.sql.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;	

/*
 * ログイン用情報格納クラス
 */

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity {
	@NotBlank
	private String userId;
	
	@NotBlank
	private String passWd;
	
	@NotBlank
	private String re_PassWd;
	
	@NotBlank
	private String passKey;
	
	private Date inp_date;
	
	@Override
	public int hashCode() {
		return Objects.hash(this.passWd);
	}
}
