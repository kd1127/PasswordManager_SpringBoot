package com.example.pm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.pm.service.ApplicationService;
import com.example.pm.service.impl.ApplicationServiceImpl;

@Configuration
public class TestApplicationConfig {
	
	@Bean
	public ApplicationService ApplicationServiceSetup() {
		return new ApplicationServiceImpl();
	}
}
