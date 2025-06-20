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
	@Autowired private TableOperationMapper talbeOperationMapper;
	@Autowired private LoginController loginController;
	@Autowired private ApplicationService applicationService;
	@Autowired private UserInfoEntity userInfoEntity;

	@GetMapping("/loginAccountEdit")
	public String loginAccountEdit(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
		userInfoEditEntity.setPassWd(loginController.userInfoEntity.getPassWd());
		model.addAttribute("userInfoEditEntity", userInfoEditEntity);
		return "loginAccountEdit/loginAccountEdit";
	}
	
	@PostMapping("/loginAccountEditCompletion")
	public String loginAccountEditCompletion(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		String errorMsg = "";
		String password = userInfoEditEntity.getPassWd();
		errorMsg = applicationService.passWdMatchCheckProcess(null, userInfoEditEntity);
		userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
		UserInfoEntity userInfoEntity = new UserInfoEntity(null, userInfoEditEntity.getPassWd(), null, null, null, null);
		userInfoEditEntity.setPassWd(String.valueOf(userInfoEntity.hashCode()));
		talbeOperationMapper.passWdUpdate(userInfoEditEntity.getUserId(), userInfoEditEntity.getPassWd());
		model.addAttribute("password", password);
		return "loginAccountEdit/loginAccountEditCompletion";
	}
	
	@GetMapping("/passKeyEdit")
	public String passKeyEdit(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model){
		//	グローバル変数からpassKeyの数値を取得する
		userInfoEditEntity.setPassKey(loginController.userInfoEntity.getPassKey());
		model.addAttribute("userInfoEditEntity", userInfoEditEntity);
		return "loginAccountEdit/passKeyEdit";
	}
	
	@PostMapping("/passKeyEditCompletion")
	public String passKeyEditCompletion(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		//	グローバル変数からuserId, passKeyの数値を取得する
		userInfoEditEntity.setUserId(loginController.userInfoEntity.getUserId());
		userInfoEntity.setPassKey(userInfoEditEntity.getPassKey());
		//	passKeyの数字部分をサービスクラスのメソッドから取得する
		userInfoEditEntity.setPassKey(applicationService.passKeyNumberRandomGet(userInfoEntity));
		talbeOperationMapper.passKeyUpdate(userInfoEditEntity.getUserId(), userInfoEditEntity.getPassKey());
		model.addAttribute("userInfoEditEntity", userInfoEditEntity);
		return "loginAccountEdit/passKeyEditCompletion";
	}
}

//	System.out.println("userId: " + uieEntity.getUserId());
//	System.out.println("passWd: " + uieEntity.getPassWd());
//  System.out.println("passKey: " + uieEntity.getPassKey());