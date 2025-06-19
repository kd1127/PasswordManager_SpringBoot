package com.example.pm;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.service.impl.CustomerServiceImpl;

@SpringBootApplication
@ComponentScan(basePackageClasses = UserInfoEntity.class)
@ComponentScan(basePackageClasses = AccountInfoEntity.class)
@ComponentScan(basePackages = "com/example/pm/controller")
@ComponentScan(basePackageClasses = CustomerServiceImpl.class)
@MapperScan(basePackageClasses = TableOperationMapper.class)
public class PasswordManagerApplication {
	@Autowired private UserInfoEntity uiEntity;
	@Autowired private TableOperationMapper mapper;

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args)
		.getBean(PasswordManagerApplication.class).adminAccountRegister();
	}
	
	// admin用のアカウントを登録する
	private void adminAccountRegister() {
		LocalDate nowDate = LocalDate.now();
		uiEntity.setUserId("ADMIN");
		uiEntity.setPassWd("ADMIN");
		uiEntity.setPassKey("AD999");
		uiEntity.setInp_date(Date.valueOf(nowDate));
		
		List<String> userIdList = new ArrayList<>();
		userIdList = mapper.userIdAllGet();
		
		//	uiEntityのuserIdと一致する文字列があればtrueになる
		boolean match_flag = false;
		
		for(String user_id : userIdList) {
			if(user_id.equals(uiEntity.getUserId())) {
				match_flag = true;
				break;
			}
		}
		
		//	一致した文字列がなければデータ登録
		if(match_flag == false) {
			mapper.userInfoInsert(uiEntity);
		}
	}
}
