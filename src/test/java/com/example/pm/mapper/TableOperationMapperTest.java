package com.example.pm.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;

import com.example.pm.entity.UserInfoEntity;

import lombok.RequiredArgsConstructor;

@SpringBootTest(classes = TableOperationMapper.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TableOperationMapperTest {

	@MockBean private TableOperationMapper tableOperationMapper;
	
	@BeforeEach
	public void dataRegisterSetUp() {
		tableOperationMapper = Mockito.mock(TableOperationMapper.class);
		List<Map<Integer, UserInfoEntity>> userInfoEntityList = new ArrayList<>();
		Map<Integer, UserInfoEntity> userInfoEntityMap = new HashMap<>();
		List<String> registerUseId = new ArrayList<>(Arrays.asList("ADMIN", "dohentai_hashimotoke@samie.com"));
		List<String> registerPassWd = new ArrayList<>(Arrays.asList("ADMIN", "dohentai_eiichi"));
		List<String> registerPassKey = new ArrayList<>(Arrays.asList("MI000815", "EI490216"));
		
		for(int i=0; i<registerUseId.size(); i++) {
			LocalDate nowTime = LocalDate.now();
			String bufferDate = String.valueOf(nowTime);
			java.sql.Date nowDate = java.sql.Date.valueOf(bufferDate);
			UserInfoEntity userInfoEntity = new UserInfoEntity(registerUseId.get(i), registerPassWd.get(i), "null", registerPassKey.get(i), nowDate, null);
			userInfoEntityMap.put(i, userInfoEntity);
		}
		userInfoEntityList.add(userInfoEntityMap);
		int insertCount = 1;
		for(int i=0; i<userInfoEntityMap.size(); i++) {
			Mockito.lenient().when(tableOperationMapper.userInfoInsert(userInfoEntityMap.get(i))).thenReturn(insertCount++);
			insertCount = tableOperationMapper.userInfoInsert(userInfoEntityMap.get(i));
		}
	}	
	
	@Test
	@DisplayName("userIdAllGet")
	public void userIdAllGetTest() {
		List<String> userIdExpected = List.of("ADMIN", "dohentai_hashimotoke@samie.com");
		List<String> userIdActual = List.of("ADMIN", "dohentai_hashimotoke@samie.com");
		Mockito.lenient().when(tableOperationMapper.userIdAllGet()).thenReturn(userIdActual);
		userIdActual = tableOperationMapper.userIdAllGet();
		Assert.assertEquals(userIdExpected, userIdActual);
	}
	
	@Test
	@DisplayName("passWdAllGet")
	public void passWdAllGetTest() {
		List<String> passWdExpected = List.of("ADMIN", "dohentai_king");
		List<String> passWdActual = List.of("ADMIN", "dohentai_king");
		Mockito.lenient().when(tableOperationMapper.passWdAllGet()).thenReturn(passWdActual);
		passWdActual = tableOperationMapper.passWdAllGet();
		Assert.assertEquals(passWdExpected, passWdActual);
	}
	
	@Test
	@DisplayName("passKeyAllGetメ")
	public void passKeyAllGetTest() {
		List<String> passKeyExpected = List.of("AD999", "DH33713756");
		List<String> passKeyActual = List.of("AD999", "DH33713756");
		Mockito.lenient().when(tableOperationMapper.passKeyAllGet()).thenReturn(passKeyActual);
		passKeyActual = tableOperationMapper.passKeyAllGet();
		Assert.assertEquals(passKeyExpected, passKeyActual);
	}
	
	@Test
	@DisplayName("idOneGet")
	public void idOneGetTest() {
		int idExpected = 1;
		int idActual = 1;
		Mockito.lenient().when(tableOperationMapper.idOneGet("ADMIN")).thenReturn(idActual);
		idActual = tableOperationMapper.idOneGet("ADMIN");
		Assert.assertEquals(idExpected, idActual);
	}
}
