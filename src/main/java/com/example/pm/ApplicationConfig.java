package com.example.pm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.example.pm.service.ApplicationService;
import com.example.pm.service.impl.ApplicationServiceImpl;
import com.example.pm.service.impl.CustomerServiceImpl;

/*
 * Spring DIコンテナ登録クラス
 * @Autowiredを付与すればエラー使用できる
 */

@Configuration
public class ApplicationConfig {
	
	public JavaMailSender mailSender;
	
	@Bean
	@Primary
	public ApplicationService beanConfig() {
		return new ApplicationServiceImpl();
	}
	
	@Bean
	@Primary
	public CustomerServiceImpl customerServiceConfig() {
		return new CustomerServiceImpl(mailSender);
	}
	
	@Bean
	public JavaMailSender javaMailSenderSetup() {
		return new JavaMailSenderImpl();
	}
	
//	@Bean
//	public UserInfoEntity uiEntityBeanConfig() {
//		return new UserInfoEntity();
//	}
}
