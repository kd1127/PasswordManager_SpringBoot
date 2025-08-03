package com.example.pm.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;
import com.example.pm.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired ApplicationService applicationService;
	@Autowired TableOperationMapper tableOperationMapper;
	
	@Override
	public List<String> validationCheck(UserInfoEntity userInfoEntity){
		List<String> errorMessage = new ArrayList<>();
		
		if(userInfoEntity.getUserId() == null) {
			userInfoEntity.setUserId("");
		}
		if(userInfoEntity.getPassWd() == null) {
			userInfoEntity.setPassWd("");
		}
		if(userInfoEntity.getPassKey() == null) {
			userInfoEntity.setPassKey("");
		}
					
		//	エラーがあるか検証する
		if(userInfoEntity.getUserId() != null && userInfoEntity.getPassWd() != null && userInfoEntity.getPassKey() != null) {
			errorMessage = applicationService.loginDataCheck(userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey());
		}
		return errorMessage;
	}
	
	@Override
	public void lastLoginDateUpdate(String userId, LocalDate lastLoginDate) {
		tableOperationMapper.lastLoginDateUpdate(userId, lastLoginDate);
	}
}
