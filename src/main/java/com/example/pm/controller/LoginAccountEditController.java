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
	@Autowired private TableOperationMapper mapper;
	@Autowired private LoginController controller;
	@Autowired private ApplicationService service;
	@Autowired private UserInfoEntity uiEntity;

	@GetMapping("/loginAccountEdit")
	public String loginAccountEdit(@ModelAttribute UserInfoEditEntity uieEntity, Model model) {
		uieEntity.setUserId(controller.userInfoEntity.getUserId());
		uieEntity.setPassWd(controller.userInfoEntity.getPassWd());
		model.addAttribute("uieEntity", uieEntity);
		return "loginAccountEdit/loginAccountEdit";
	}
	
	@PostMapping("/loginAccountEditCompletion")
	public String loginAccountEditCompletion(@ModelAttribute UserInfoEditEntity uieEntity, Model model) {
		String errorMsg = "";
		errorMsg = service.passWdMatchCheckProcess(null, uieEntity);
		uieEntity.setUserId(controller.userInfoEntity.getUserId());
		mapper.passWdUpdate(uieEntity.getUserId(), uieEntity.getPassWd());
		model.addAttribute("uieEntity", uieEntity);
		return "loginAccountEdit/loginAccountEditCompletion";
	}
	
	@GetMapping("/passKeyEdit")
	public String passKeyEdit(@ModelAttribute UserInfoEditEntity uieEntity, Model model){
		//	グローバル変数からpassKeyの数値を取得する
		uieEntity.setPassKey(controller.userInfoEntity.getPassKey());
		model.addAttribute("uieEntity", uieEntity);
		return "loginAccountEdit/passKeyEdit";
	}
	
	@PostMapping("/passKeyEditCompletion")
	public String passKeyEditCompletion(@ModelAttribute UserInfoEditEntity uieEntity, Model model) {
		//	グローバル変数からuserId, passKeyの数値を取得する
		uieEntity.setUserId(controller.userInfoEntity.getUserId());
		uiEntity.setPassKey(uieEntity.getPassKey());
		//	passKeyの数字部分をサービスクラスのメソッドから取得する
		uieEntity.setPassKey(service.passKeyNumberRandomGet(uiEntity));
		mapper.passKeyUpdate(uieEntity.getUserId(), uieEntity.getPassKey());
		model.addAttribute("uieEntity", uieEntity);
		return "loginAccountEdit/passKeyEditCompletion";
	}
}

//	System.out.println("userId: " + uieEntity.getUserId());
//	System.out.println("passWd: " + uieEntity.getPassWd());
//  System.out.println("passKey: " + uieEntity.getPassKey());