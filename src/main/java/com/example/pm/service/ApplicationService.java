package com.example.pm.service;

import java.sql.Date;
import java.util.List;

import com.example.pm.dto.AccountInfoDto;
import com.example.pm.dto.PdfDto;
import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;

public interface ApplicationService {

	Date tableInsertpreparation();
	String passKeyNumberRandomGet(UserInfoEntity uiEntity);
	List<String> loginDataCheck(String userId, String passWd, String passKey) throws Exception;
	List<String> passWdMatchCheckProcess(UserInfoEntity userInfoEntity);
	String userInfoInsertOperation(UserInfoEntity userInfoEntity);
	String passWdUpdateDbOperation(String userId, String passWd);
	List<String> userIdDuplicateCheck(String userId, List<String> errorMsgList);
	AccountInfoDto displayDataOfPaging(AccountInfoDto accountInfoDto);
	Long findMaximumPage(AccountInfoDto accountInfoDto);
	AccountInfoDto nextDataOfPaging(AccountInfoDto accountInfoDto);
	AccountInfoDto prevDataOfPaging(AccountInfoDto accountInfoDto, int displayCount);
	List<AccountInfoEntity> search(int id, String siteName);
	PdfDto pdfOutput(List<AccountInfoEntity> accountInfoEntityList);
}
