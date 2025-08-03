package com.example.pm.service;

import java.time.LocalDate;
import java.util.List;

import com.example.pm.entity.UserInfoEntity;

public interface LoginService {
	void lastLoginDateUpdate(String userId, LocalDate lastLoginDate);
	List<String> validationCheck(UserInfoEntity userInfoEntity);
}
