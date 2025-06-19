package com.example.pm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.*;

import com.example.pm.LoginControllerTestAnnotation;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;

import static com.example.pm.controller.LoginController.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@LoginControllerTestAnnotation
public class LoginControllerTest {
	
	@Autowired private MockMvc mockMvc;
	@Autowired private UserInfoEntity userInfoEntity;
	@Autowired private ApplicationService applicationService;
	@Autowired private LoginController loginController;
	
	@BeforeEach
	public void setUp() {
		loginController = new LoginController();
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).alwaysDo(log()).build();
		applicationService = Mockito.mock(ApplicationService.class);
	}
	
	@Test
	@DisplayName("loginControllerメソッドのテスト")
	public void loginControllerTest() throws Exception {
		userInfoEntity = new UserInfoEntity(null, null, null, null, null);
		mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login/login"))
			.andExpect(model().attribute("userInfoEntity", userInfoEntity));
	}
	
	@Test
	@DisplayName("passKeyCertificationメソッドのテスト")
	public void passKeyCertificationTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/passKeyCertification"))
			.andExpect(status().isOk())
			.andExpect(view().name("login/passKeyCertification"))
			.andExpect(model().attributeExists("userInfoEntity"))
			.andReturn();
	}
	
	@Test
	@DisplayName("topPageメソッド:次画面遷移する場合のテスト")
	public void topPageTest() throws Exception {
		loginController.applicationService = applicationService;
		userInfoEntity = new UserInfoEntity("ADMIN", "ADMIN", null, "AD999", null);
		lenient().when(applicationService.loginDataCheck(any(), any(), any())).thenReturn(new ArrayList<>());
		
		MockHttpServletRequestBuilder request = 
				MockMvcRequestBuilders.get("/topPage")
				.content("passwordManager/topPage")
				.content("userInfoEntity")
				.param("userInfoEntity", userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey())
				.contentType(MediaType.ALL_VALUE);
		
		MvcResult mvcResult = mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(view().name("passwordManager/topPage"))
			.andExpect(model().attributeExists("userInfoEntity"))
			.andReturn();
		verify(applicationService, times(1)).loginDataCheck(anyString(), anyString(), anyString());
	}
	
	@Test
	@DisplayName("topPageメソッド:自画面遷移場合のテスト")
	public void topPageTest2() throws Exception {
		loginController.applicationService = applicationService;
		userInfoEntity = new UserInfoEntity("ADMIN", "ADMIN", null, "AD999", null);
		List<String> errorMsg = List.of("ユーザーIDを入力してください。", "パスワードを入力してください。", "パスキーを入力してください。");
		lenient().when(applicationService.loginDataCheck(any(), any(), any())).thenReturn(errorMsg);
		errorMsg = applicationService.loginDataCheck(userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey());
		
		mockMvc.perform(get("/topPage").param(userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey()))
			.andExpect(status().isOk())
			.andExpect(view().name("login/passKeyCertification"))
			.andExpect(model().attributeExists("userInfoEntity"));
		
		verify(applicationService, times(2)).loginDataCheck(anyString(), anyString(), anyString());
	}
	
	@Test
	@DisplayName("userRegisterメソッドのテスト")
	public void userRegisterTest() throws Exception  {
		userInfoEntity = new UserInfoEntity();
		mockMvc.perform(post("/userRegister").param("userInfoEntity", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("login/userRegister"))
			.andExpect(model().attribute("userInfoEntity", userInfoEntity));
	}
	
	@Test
	@DisplayName("passKeyGetメソッドのテスト")
	public void passKeyGetTest() throws Exception{
		userInfoEntity = new UserInfoEntity("ADMIN", "ADMIN", null, "AD999", null);
		loginController.applicationService = this.applicationService;
		String errorMsg = "";
		lenient().when(applicationService.passWdMatchCheckProcess(userInfoEntity, null)).thenReturn(errorMsg);
		errorMsg = applicationService.passWdMatchCheckProcess(userInfoEntity, null);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post("/passKeyGet")
				.content("userInfoEntity")
				.param("userInfoEntity", userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey())
				.content("login/userRegister")
				.contentType(MediaType.ALL_VALUE);
		
		mockMvc.perform(request)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("login/userRegister"))
			.andExpect(model().attributeExists("userInfoEntity"));
	}
	
	@Test
	@DisplayName("userRegisterCheckメソッドのテスト")
	public void userRegisterCheckTest()throws Exception {
		userInfoEntity = new UserInfoEntity("ADMIN", "ADMIN", null, "AD999", null);
		loginController.userInfoEntity = userInfoEntity;
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post("/userRegisterCheck")
				.param("userInfoEntity", "")
				.content("login/userRegisterCheck")
				.contentType(MediaType.ALL_VALUE);
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(view().name("login/userRegisterCheck"))
			.andExpect(model().attributeHasNoErrors("userInfoEntity"));
	}
	
	@Test
	@DisplayName("userRegisterCompletionメソッドのテスト")
	public void userRegisterCompletionTest() throws Exception {
		loginController.applicationService = this.applicationService;
		userInfoEntity = new UserInfoEntity();
		loginController.userInfoEntity = userInfoEntity;
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get("/userRegisterCompletion")
				.content("login/userRegisterCheck")
				.contentType(MediaType.ALL_VALUE);
		mockMvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(view().name("login/userRegisterCheck"));
	}
}
