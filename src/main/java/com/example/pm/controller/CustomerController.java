package com.example.pm.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pm.entity.UserInfoEditEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.service.ApplicationService;
import com.example.pm.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

	private UserInfoEntity userInfoEntity;
	private UserInfoEditEntity userInfoEditEntity;
	private HttpSession session;
	private CustomerService customerService;
	private ApplicationService applicationService;
	
	//	メール送信したらtrueになり、それ以外は常にfalse falseの時はURL直打ちで遷移できない
	private boolean transitionFromFlag = false;
	
	@Autowired 
	public CustomerController(HttpSession session, UserInfoEntity userInfoEntity, CustomerService customerService, 
			UserInfoEditEntity userInfoEditEntity, ApplicationService applicationService) {
		this.session = session;
		this.userInfoEntity = userInfoEntity;
		this.customerService = customerService;
		this.userInfoEditEntity = userInfoEditEntity;
		this.applicationService = applicationService;
	}

	@GetMapping("/inputMailAddress")
	public String inputMailAddress(Model model) {
		model.addAttribute("userInfoEntity", userInfoEntity);
		return "customer/inputMailAddress";
	}
	
	@GetMapping("/mailSend")
	public String mailSend(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		String errorMessage = customerService.mailAddressFormatCheck(userInfoEntity.getUserId());
		
		if(!StringUtils.equals(errorMessage, "")) {
			model.addAttribute("errorMessage", errorMessage);
			return "customer/inputMailAddress";
		}
		transitionFromFlag = true;
		this.userInfoEntity = userInfoEntity;
		String message = customerService.mailSend(userInfoEntity.getUserId());
		model.addAttribute("message", message);
		session.removeAttribute("userInfoEntity");
		return "customer/mailSend";
	}
	
	@GetMapping("/passwordReConfigure")
	public String passwordReConfigure(Model model) {
		if(transitionFromFlag == false) {
			model.addAttribute("userInfoEntity", userInfoEntity);
			return "login/login";
		}
		model.addAttribute("userInfoEditEntity", userInfoEditEntity);
		return "customer/passwordReConfigure";
	}
	
	@PostMapping("/passwordReConfigureConfirm")
	public String passwordReConfigureConfirm(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		String errorMessage = applicationService.passWdMatchCheckProcess(null, userInfoEditEntity);
		
		if(!errorMessage.equals("")) {
			model.addAttribute("userInfoEditEntity", userInfoEditEntity);
			model.addAttribute("errorMessage", errorMessage);
			return "customer/passwordReConfigure";
		}
		this.userInfoEditEntity = userInfoEditEntity;
		return "customer/passwordReConfigureConfirm";
	}
	
	@GetMapping("/passwordReConfigureCompletion")
	public String passwordReConfigureCompletion(Model model) {
		UserInfoEntity userInfoEntity = new UserInfoEntity(null, userInfoEditEntity.getPassWd(), null, null, null, null);
		userInfoEditEntity.setPassWd(String.valueOf(userInfoEntity.hashCode()));
		String message = applicationService.passWdUpdateDbOperation(this.userInfoEntity.getUserId(), this.userInfoEditEntity.getPassWd());
		
		if(!message.equals("")) {
			model.addAttribute("message", message);
			return "customer/passwordReConfigureConfirm";
		}
		return "customer/passwordReConfigureCompletion";
	}
}
