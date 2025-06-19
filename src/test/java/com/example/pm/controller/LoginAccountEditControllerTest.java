package com.example.pm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.pm.entity.UserInfoEditEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginAccountEditControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private UserInfoEditEntity userInfoEditEntity;
	@Autowired private LoginController loginController;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(LoginAccountEditController.class).build();
	}
	
	@Test
	@DisplayName("loginAccountEditメソッドのテスト")
	public void loginAccountEditTest() throws Exception {
		userInfoEditEntity.setUserId("ADMIN");
		userInfoEditEntity.setPassWd("ADMIN");
		mockMvc.perform(get("/loginAccountEdit").param("userInfoEditEntity", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("loginAccountEdit/loginAccountEdit"))
			.andExpect(model().attribute("userInfoEditEntity", userInfoEditEntity));
	}
}
