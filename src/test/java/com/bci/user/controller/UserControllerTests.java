package com.bci.user.controller;

import com.bci.user.entity.User;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		// Esto hace que el mapper ignore las restricciones de WRITE_ONLY en los tests
		objectMapper = JsonMapper.builder()
				.disable(MapperFeature.USE_ANNOTATIONS)
				.build();	}
	@Test
	public void testUserSignUp() throws Exception {
		User user = new User("name","julio@testsswe.cl","Hunter2*", new ArrayList<>());

		mockMvc.perform(
				MockMvcRequestBuilders.put("/user/sign-up")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(user)))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/user/login")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}


}
