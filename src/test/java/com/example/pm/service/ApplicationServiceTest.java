package com.example.pm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicationServiceTest {

	@Autowired private ApplicationService applicationService;
	@SpyBean private ApplicationService spyApplicationService;
	@Autowired private TableOperationMapper tableOperationMapper;
	
	@BeforeEach
	public void setUp() {
		applicationService = Mockito.mock(ApplicationService.class);
		spyApplicationService = Mockito.spy(ApplicationService.class);
		tableOperationMapper = Mockito.spy(TableOperationMapper.class);
	}
	
	@Test
	@DisplayName("tableInsertpreparationメソッドのテスト")
	public void tableInsertpreparationTest() {
		LocalDate nowDateExpected = LocalDate.now();
		Date dateActual = Date.valueOf(nowDateExpected);
		lenient().when(spyApplicationService.tableInsertpreparation()).thenReturn(dateActual);
		Date bufferDate = spyApplicationService.tableInsertpreparation();
		LocalDate nowDateActual = bufferDate.toLocalDate();
		assertEquals(nowDateExpected, nowDateActual);
	}
	
	@Test
	@DisplayName("passKeyNumberRandomGetメソッドのテスト")
	public void passKeyNumberRandomGetTest() {
		var randomExpected = "AD12345678";
		var userInfoEntity = new UserInfoEntity();
		userInfoEntity.setPassKey("AD");
		lenient().doReturn("AD12345678").when(applicationService).passKeyNumberRandomGet(userInfoEntity);
		String randomActual = applicationService.passKeyNumberRandomGet(userInfoEntity);
		assertEquals(randomExpected, randomActual);
		verify(applicationService, times(1)).passKeyNumberRandomGet(userInfoEntity);
	}
	
	@Test
	@DisplayName("loginDataCheckメソッドのテスト")
	public void loginDataCheckTest() {
//		applicationService.tableOperationMapper = tableOperationMapper;
		List<String> errorMsgExpected = new ArrayList<>(Arrays.asList("ユーザーIDが一致しません。"));
		lenient().doReturn(new ArrayList<>(Arrays.asList("ユーザーIDが一致しません。"))).when(applicationService).loginDataCheck(any(), any(), any());
		List<String> errorMsgActual = applicationService.loginDataCheck("ADMIN", "ADMIN", "AD999");
		assertEquals(errorMsgExpected, errorMsgActual);
		verify(applicationService, times(1)).loginDataCheck("ADMIN", "ADMIN", "AD999");
	}
	
	@Test
	@DisplayName("パスワードとパスワード（確認）が一致した場合のテスト")
	public void passWdMatchCheckProcessTest() {
		UserInfoEntity userInfoEntity = new UserInfoEntity(null, "ADMIN", "ADMIN", null, null, null);
		String errorMsgExpected = "";
		List<String> errorMsgActualStub = new ArrayList<>(Arrays.asList(""));
		lenient().when(spyApplicationService.passWdMatchCheckProcess(userInfoEntity)).thenReturn(errorMsgActualStub);
		List<String> errorMsgActual = spyApplicationService.passWdMatchCheckProcess(userInfoEntity);
		System.out.println(errorMsgActual.get(0));
		assertEquals(errorMsgExpected, errorMsgActual.get(0));
	}
	
	@Test
	@DisplayName("パスワードとパスワード（確認）が一致しない場合のテスト")
	public void passWdMatchCheckProcessTest2() {
		UserInfoEntity userInfoEntity = new UserInfoEntity(null, "eiichi", "sadao", null, null, null);
		String errorMsgExpected = "パスワード・パスワード（確認）が一致しません";
		List<String> errorMsgActualStub = new ArrayList<>(Arrays.asList("パスワード・パスワード（確認）が一致しません"));
		lenient().when(spyApplicationService.passWdMatchCheckProcess(userInfoEntity)).thenReturn(errorMsgActualStub);
		List<String> errorMsgActual = spyApplicationService.passWdMatchCheckProcess(userInfoEntity);
		assertEquals(errorMsgExpected, errorMsgActual.get(0));
	}
	
	@Test
	@DisplayName("ユーザー登録メソッドで正しく登録ができた際のテスト")
	public void userInfoInsertOperationTest() {
		UserInfoEntity userInfoEntity = new UserInfoEntity("dohentai_eiichi@samie.com", "rorikon0216", "rorikon0216", "EI19490216", Date.valueOf(LocalDate.now()), null);
		String errorMsgExpected = "";
		String errorMsgActual = "";
		lenient().when(applicationService.userInfoInsertOperation(userInfoEntity)).thenReturn("");
		errorMsgActual = applicationService.userInfoInsertOperation(userInfoEntity);
		assertEquals(errorMsgExpected, errorMsgActual);
	}
}
