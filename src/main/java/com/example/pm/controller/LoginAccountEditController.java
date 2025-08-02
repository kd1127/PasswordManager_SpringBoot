package com.example.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pm.entity.UserInfoEditEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;

@Controller
public class LoginAccountEditController {
	private TableOperationMapper tableOperationMapper;
	private LoginController loginController;
	private ApplicationService applicationService;
	private UserInfoEntity userInfoEntity;
	
	@Autowired
	public LoginAccountEditController(TableOperationMapper tableOperationMapper, LoginController loginController, 
			ApplicationService applicationService, UserInfoEntity userInfoEntity) {
		if(applicationService == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(tableOperationMapper == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(userInfoEntity == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(loginController == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		this.tableOperationMapper = tableOperationMapper;
		this.loginController = loginController;
		this.applicationService = applicationService;
		this.userInfoEntity = userInfoEntity;
	}

	@GetMapping("/loginAccountEdit")
	public String loginAccountEdit(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		if(loginController.loginFlag) {
			userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
			userInfoEditEntity.setPassWd(loginController.userInfoEntity.getPassWd());
			model.addAttribute("userInfoEditEntity", userInfoEditEntity);
			return "loginAccountEdit/loginAccountEdit";
		}
		else {
			return "login/login";
		}
	}
	
	@PostMapping("/loginAccountEditCompletion")
	public String loginAccountEditCompletion(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		if(loginController.loginFlag) {
			String errorMsg = "";
			String password = userInfoEditEntity.getPassWd();
			errorMsg = applicationService.passWdMatchCheckProcess(null, userInfoEditEntity);
			userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
			UserInfoEntity userInfoEntity = new UserInfoEntity(null, userInfoEditEntity.getPassWd(), null, null, null, null);
			userInfoEditEntity.setPassWd(String.valueOf(userInfoEntity.hashCode()));
			tableOperationMapper.passWdUpdate(userInfoEditEntity.getUserId(), userInfoEditEntity.getPassWd());
			model.addAttribute("password", password);
			return "loginAccountEdit/loginAccountEditCompletion";
		}
		else {
			return "login/login";
		}
	}
	
	@GetMapping("/passKeyEdit")
	public String passKeyEdit(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model){
		if(loginController.loginFlag) {
			//	グローバル変数からpassKeyの数値を取得する
			userInfoEditEntity.setPassKey(loginController.userInfoEntity.getPassKey());
			model.addAttribute("userInfoEditEntity", userInfoEditEntity);
			return "loginAccountEdit/passKeyEdit";
		}
		else {
			return "login/login";
		}
	}
	
	@PostMapping("/passKeyEditCompletion")
	public String passKeyEditCompletion(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		if(loginController.loginFlag) {
			//	グローバル変数からuserId, passKeyの数値を取得する
			userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
			userInfoEntity.setPassKey(userInfoEditEntity.getPassKey());
			//	passKeyの数字部分をサービスクラスのメソッドから取得する
			userInfoEditEntity.setPassKey(applicationService.passKeyNumberRandomGet(userInfoEntity));
			tableOperationMapper.passKeyUpdate(userInfoEditEntity.getUserId(), userInfoEditEntity.getPassKey());
			model.addAttribute("userInfoEditEntity", userInfoEditEntity);
			return "loginAccountEdit/passKeyEditCompletion";
		}
		else {
			return "login/login";
		}
	}
}

//	System.out.println("userId: " + uieEntity.getUserId());
//	System.out.println("passWd: " + uieEntity.getPassWd());
//  System.out.println("passKey: " + uieEntity.getPassKey());