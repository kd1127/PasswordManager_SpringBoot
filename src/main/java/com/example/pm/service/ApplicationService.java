package com.example.pm.service;

import java.sql.Date;
import java.util.List;

import com.example.pm.entity.UserInfoEditEntity;
import com.example.pm.entity.UserInfoEntity;

public interface ApplicationService {

	Date tableInsertpreparation();
	String passKeyNumberRandomGet(UserInfoEntity uiEntity);
	List<String> loginDataCheck(String userId, String passWd, String passKey);
	List<String> passWdMatchCheckProcess(UserInfoEntity userInfoEntity, UserInfoEditEntity userInfoEditEntity);
	String userInfoInsertOperation(UserInfoEntity userInfoEntity);
	String passWdUpdateDbOperation(String userId, String passWd);
	List<String> userIdDuplicateCheck(String userId, List<String> errorMsgList);
}
