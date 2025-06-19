package com.example.pm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.ApplicationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@SessionAttributes(types=UserInfoEntity.class)
@RequiredArgsConstructor
public class LoginController {
	
	public ApplicationService applicationService;
	private HttpSession session;
	
	//	値保持用 LoginAccountEditControllerクラスなどが使用
	public UserInfoEntity userInfoEntity;
	
	//	セッション
	@ModelAttribute
	UserInfoEntity registrySessionUserInfoEntity() {
		return new UserInfoEntity();
	}
	
	//	コンストラクタインジェクション
	@Autowired
	public LoginController(ApplicationService applicationService, HttpSession session) {
		this.applicationService = applicationService;
		this.session = session;
	}
	
	@GetMapping("/login")
	public String loginController(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		this.userInfoEntity = new UserInfoEntity();
		model.addAttribute("userInfoEntity", userInfoEntity);
		return "login/login";
	}
	
	@PostMapping("/passKeyCertification")
	public String passKeyCertification(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		this.userInfoEntity = userInfoEntity;
		model.addAttribute("userInfoEntity", userInfoEntity);
		return "login/passKeyCertification";
	}
	
	@RequestMapping(value="/topPage", method= {RequestMethod.GET, RequestMethod.POST})
	public String topPage(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		session.removeAttribute("userInfoEntity");
		//	バリデーションチェック
		if(userInfoEntity.getUserId() == null) {
			userInfoEntity.setUserId("");
		}
		if(userInfoEntity.getPassWd() == null) {
			userInfoEntity.setPassWd("");
		}
		if(userInfoEntity.getPassKey() == null) {
			userInfoEntity.setPassKey("");
		}
		
		List<String> errorMessage = new ArrayList<>();
		
		//	エラーがあるか検証する
		if(userInfoEntity.getUserId() != null && userInfoEntity.getPassWd() != null && userInfoEntity.getPassKey() != null) {
			errorMessage = applicationService.loginDataCheck(userInfoEntity.getUserId(), userInfoEntity.getPassWd(), userInfoEntity.getPassKey());
		}
		
		if(errorMessage.isEmpty()) {
			AccountInfoEntity accountInfoEntity = new AccountInfoEntity();
			model.addAttribute("accountInfoEntity", accountInfoEntity);
			return "passwordManager/topPage";
		}
		else {
			model.addAttribute("userInfoEntity", userInfoEntity);
			model.addAttribute("errorMessage", errorMessage);
			return "login/passKeyCertification";
		}
	}
	
	@PostMapping("/userRegister")
	public String userRegister(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		model.addAttribute("userInfoEntity", userInfoEntity);
		return "login/userRegister";
	}
	
	@PostMapping("/passKeyGet")
	public String passKeyGet(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		String errorMsg = applicationService.passWdMatchCheckProcess(userInfoEntity, null);
		model.addAttribute("userInfoEntity", userInfoEntity);
		this.userInfoEntity = userInfoEntity;
		
		try {
			if(!errorMsg.equals("")) {
				model.addAttribute("errorMsg", errorMsg);
				return "login/userRegister";
			}
			else{
				return "login/passKeyGet";
			}
		}catch(NullPointerException e) {
			errorMsg = "何らかの理由によりエラーが発生しました。";
			model.addAttribute("errorMsg", errorMsg);
			return "login/userRegister";
		}
	}
	
	@PostMapping("/userRegisterCheck")
	public String userRegisterCheck(@ModelAttribute UserInfoEntity userInfoEntity, Model model) {
		userInfoEntity.setUserId(this.userInfoEntity.getUserId());
		userInfoEntity.setPassWd(this.userInfoEntity.getPassWd());
		userInfoEntity.setRe_PassWd(this.userInfoEntity.getRe_PassWd());
		this.userInfoEntity.setPassKey(userInfoEntity.getPassKey());
		model.addAttribute("userInfoEntity", userInfoEntity);
		return "login/userRegisterCheck";
	}
	
	/*
	 * ユーザー登録確認画面から遷移した場合に処理を行うメソッド
	 * インスタンス変数にinp_dateとpassKeyをセットし、DB登録を行うサービスクラスのメソッドに値を渡す
	 * 
	 * @param model 
	 */	
	@GetMapping("/userRegisterCompletion")
	public String userRegisterCompletion(Model model) {
		String passWdTemporary = String.valueOf(userInfoEntity.hashCode());
		userInfoEntity.setPassWd(passWdTemporary);
		userInfoEntity.setInp_date(applicationService.tableInsertpreparation());
		userInfoEntity.setPassKey(applicationService.passKeyNumberRandomGet(userInfoEntity));
		String errorMsg = applicationService.userInfoInsertOperation(userInfoEntity);
		
		try {
			if(!errorMsg.equals("")) {
				model.addAttribute("errorMsg", errorMsg);
				return "login/userRegisterCheck";
			}
		} catch (NullPointerException e) {
			errorMsg = "何らかの理由によりエラーが発生しました。";
			model.addAttribute("errorMsg", errorMsg);
			return "login/userRegisterCheck";
		}
		return "login/userRegisterCompletion";
	}
}

//	デバッグツール
//System.out.println("3回目のチェック");
//System.out.println("userId: " + this.uiEntity.getUserId());
//System.out.println("passWd: " + this.uiEntity.getPassWd());
//System.out.println("re_PassWd: " + this.uiEntity.getRe_PassWd());
//System.out.println("passKey: " + this.uiEntity.getPassKey());
//System.out.println("userId: " + uiEntity.getUserId());
//System.out.println("passWd: " + uiEntity.getPassWd());
//System.out.println("re_PassWd: " + uiEntity.getRe_PassWd());
//System.out.println("passKey: " + uiEntity.getPassKey());