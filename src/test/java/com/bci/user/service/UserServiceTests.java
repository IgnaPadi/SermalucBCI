package com.bci.user.service;

import com.bci.user.entity.User;
import com.bci.user.response.ResponseApi;
import com.bci.user.response.ResponseApiMessage;
import com.bci.user.response.ResponseApiMessageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTests {

	@Autowired
	private UserService userService;

	@Test
	public void testSetUserOk(){
		User user = new User("name", "julio@testssw.cl", "Hunter2*", new ArrayList<>());
		ResponseEntity<Object> responseEntity = userService.setUser(user);

		User userResponseApi = (User) responseEntity.getBody();

		Assertions.assertNotNull(userResponseApi);
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Assertions.assertEquals(user.getEmail(), userResponseApi.getEmail());
		Assertions.assertEquals(user.getPassword(), userResponseApi.getPassword());
	}

	@Test
	public void testSetUserNoOk(){
		User user = new User("name", null, null, new ArrayList<>());

		ResponseEntity<Object> responseEntity= userService.setUser(user);
		ResponseApi responseApi = (ResponseApi) responseEntity.getBody();

		Assertions.assertNotNull(responseApi);
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

		Optional <ResponseApiMessage> optionalResponseApiMessageEmail = responseApi.getResponseApiMessageList().stream().filter(
				i-> i.getCodigo() == ResponseApiMessageEnum.ERROR_NOT_NULL.getCode()).findFirst() ;
		Assertions.assertTrue(optionalResponseApiMessageEmail.isPresent());

		ResponseEntity<Object> responseEntityException = userService.setUser(null);
		Assertions.assertEquals(responseEntityException.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testSetUserNoOkUserExists(){
		User user = new User("name", "julio@testsswa.cl", "Hunter2*", new ArrayList<>());
		ResponseEntity<Object> responseEntity = userService.setUser(user);
		User userResponseApi = (User) responseEntity.getBody();

		Assertions.assertNotNull(userResponseApi);
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> responseEntityExists = userService.setUser(user);
		ResponseApi responseApi = (ResponseApi) responseEntityExists.getBody();

		Assertions.assertNotNull(responseApi);
		Assertions.assertEquals(responseEntityExists.getStatusCode(), HttpStatus.BAD_REQUEST);

		Optional <ResponseApiMessage> optionalResponseApiMessageUserExists = responseApi.getResponseApiMessageList().stream().filter(
				i-> i.getCodigo() == ResponseApiMessageEnum.ERROR_USER_EXISTS.getCode()).findFirst() ;

		Assertions.assertTrue(optionalResponseApiMessageUserExists.isPresent());

	}

	@Test
	public void testSetUserNoOkBusinessValidation(){
		//Email sin @ y password con solamente un número
		User user = new User("name", "juliotestsswd.cl", "a2asfGffdf", new ArrayList<>());

		ResponseEntity<Object> responseEntity= userService.setUser(user);
		ResponseApi responseApi = (ResponseApi) responseEntity.getBody();

		Assertions.assertNotNull(responseApi);
		Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

		Optional <ResponseApiMessage> optionalResponseApiMessageEmail = responseApi.getResponseApiMessageList().stream().filter(
				i-> i.getCodigo() == ResponseApiMessageEnum.ERROR_EMAIL.getCode()).findFirst() ;
		Assertions.assertTrue(optionalResponseApiMessageEmail.isPresent());

		Optional <ResponseApiMessage> optionalResponseApiMessagePassword = responseApi.getResponseApiMessageList().stream().filter(
				i-> i.getCodigo() == ResponseApiMessageEnum.ERROR_PASSWORD.getCode()).findFirst() ;
		Assertions.assertTrue(optionalResponseApiMessagePassword.isPresent());

	}

	@Test
	public void testLoginOk(){
		User user = new User("name", "julio@testsswb.cl", "Hunter2*", new ArrayList<>());
		ResponseEntity<Object> responseEntitySet = userService.setUser(user);

		User userResponseApiSet = (User) responseEntitySet.getBody();

		Assertions.assertNotNull(userResponseApiSet);
		Assertions.assertEquals(responseEntitySet.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> responseEntityLogin = userService.login("Bearer ".concat(userResponseApiSet.getToken()));
		Assertions.assertEquals(responseEntityLogin.getStatusCode(), HttpStatus.OK);

		User userResponseApiLogin = (User) responseEntityLogin.getBody();
		Assertions.assertNotNull(userResponseApiLogin);
		Assertions.assertNotNull(userResponseApiLogin.getLastLogin());
	}

	@Test
	public void testLoginNoOk(){
		String bearerErrorTest = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoianVsaW9AdGVzdHNzYXcuY2wiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzE0NDMyNDg5LCJleHAiOjE3MTQ0MzMwODl9";

		ResponseEntity<Object> responseEntityLoginBearerNull = userService.login(null);
		Assertions.assertEquals(responseEntityLoginBearerNull.getStatusCode(), HttpStatus.BAD_REQUEST);

		ResponseEntity<Object> responseEntityLoginBearerError = userService.login(bearerErrorTest);
		Assertions.assertEquals(responseEntityLoginBearerError.getStatusCode(), HttpStatus.FORBIDDEN);
	}
}
