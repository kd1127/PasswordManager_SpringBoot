package com.example.pm.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pm.PmCommon.CustomerDisplay;
import com.example.pm.PmLogOutput;
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
	@Autowired 
	private PmLogOutput log;
	
	//	メール送信したらtrueになり、それ以外は常にfalse falseの時はURL直打ちで遷移できない
	private boolean transitionFromFlag = false;
	
	@Autowired 
	public CustomerController(HttpSession session, UserInfoEntity userInfoEntity, CustomerService customerService, 
			UserInfoEditEntity userInfoEditEntity, ApplicationService applicationService) {
		if(applicationService == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(session == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(userInfoEntity == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(userInfoEditEntity == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		if(customerService == null) {
			throw new NullPointerException("何らかの理由によりエラーが発生しました。");
		}
		this.session = session;
		this.userInfoEntity = userInfoEntity;
		this.customerService = customerService;
		this.userInfoEditEntity = userInfoEditEntity;
		this.applicationService = applicationService;
	}

	@GetMapping("/inputMailAddress")
	public String inputMailAddress(Model model) {
		model.addAttribute("userInfoEntity", userInfoEntity);
		log.info(CustomerDisplay.InputMailAddress);
		return "customer/inputMailAddress";
	}
	
	@GetMapping("/mailSend")
	public String mailSend(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		log.info(CustomerDisplay.MailSend);
		log.info("", "customerService.mailAddressFormatCheck処理 ---> 開始");
		String errorMessage = customerService.mailAddressFormatCheck(userInfoEntity.getUserId());
		log.info("", "customerService.mailAddressFormatCheck処理 ---> 正常終了");
		
		if(!StringUtils.equals(errorMessage, "")) {
			model.addAttribute("errorMessage", errorMessage);
			return "customer/inputMailAddress";
		}
		transitionFromFlag = true;
		this.userInfoEntity = userInfoEntity;
		log.info("", "customerService.mailSend処理 ---> 開始");
		String message = customerService.mailSend(userInfoEntity.getUserId());
		log.info("処理結果: " + message, "customerService.mailSend処理 ---> 正常終了");
		model.addAttribute("message", message);
		session.removeAttribute("userInfoEntity");
		return "customer/mailSend";
	}
	
	@GetMapping("/passwordReConfigure")
	public String passwordReConfigure(Model model) {
		log.info(CustomerDisplay.PasswordReConfigure);
		if(transitionFromFlag == false) {
			model.addAttribute("userInfoEntity", userInfoEntity);
			return "login/login";
		}
		model.addAttribute("userInfoEditEntity", userInfoEditEntity);
		return "customer/passwordReConfigure";
	}
	
	@PostMapping("/passwordReConfigureConfirm")
	public String passwordReConfigureConfirm(@ModelAttribute UserInfoEditEntity userInfoEditEntity, Model model) {
		this.userInfoEditEntity = userInfoEditEntity;
		log.info(CustomerDisplay.PasswordReConfigureConfirm);
		return "customer/passwordReConfigureConfirm";
	}
	
	@GetMapping("/passwordReConfigureCompletion")
	public String passwordReConfigureCompletion(Model model) {
		if(transitionFromFlag == false) {
			model.addAttribute("userInfoEntity", userInfoEntity);
			return "login/login";
		}
		UserInfoEntity userInfoEntity = new UserInfoEntity(null, userInfoEditEntity.getPassWd(), null, null, null, null);
		userInfoEditEntity.setPassWd(String.valueOf(userInfoEntity.hashCode()));
		log.info("", "applicationService.passWdUpdateDbOperation処理 ---> 開始");
		String message = applicationService.passWdUpdateDbOperation(this.userInfoEntity.getUserId(), this.userInfoEditEntity.getPassWd());
		log.info("処理結果: " + message, "applicationService.passWdUpdateDbOperation処理 ---> 正常終了");
		
		if(!message.equals("")) {
			model.addAttribute("message", message);
			return "customer/passwordReConfigureConfirm";
		}
		return "customer/passwordReConfigureCompletion";
	}
}
