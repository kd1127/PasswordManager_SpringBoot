package com.example.pm;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.pm.mapper.TableOperationMapper;

@SpringBootTest
@MapperScan(basePackageClasses = TableOperationMapper.class)
class PasswordManagerApplicationTests {

	@Test
	void contextLoads() {
	}
}
