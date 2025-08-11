package com.example.pm.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.pm.entity.UserInfoEntity;

public interface LoginService {
	void lastLoginDateUpdate(String userId, LocalDateTime lastLoginDate);
	List<String> validationCheck(UserInfoEntity userInfoEntity);
}
