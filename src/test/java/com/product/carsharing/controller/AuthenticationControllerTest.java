package com.product.carsharing.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.carsharing.dto.user.UserLoginRequestDto;
import com.product.carsharing.dto.user.UserLoginResponseDto;
import com.product.carsharing.dto.user.UserRegistrationRequestDto;
import com.product.carsharing.dto.user.UserResponseDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Register a new user")
    @SneakyThrows
    void register_ValidRequest_ShouldReturnUserResponse() {
        UserRegistrationRequestDto registrationRequest = new UserRegistrationRequestDto(
                "test@test.com",
                "John",
                "Doe",
                "1234",
                "1234"
        );

        UserResponseDto expected = new UserResponseDto();
        expected.setId(1L);
        expected.setEmail("test@test.com");
        expected.setFirstName("John");
        expected.setLastName("Doe");
        String jsonRequest = objectMapper.writeValueAsString(registrationRequest);
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class);

        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
    }

    @Test
    @DisplayName("Login with valid credentials")
    @Sql(scripts = "classpath:database/user/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void login_ValidCredentials_ShouldReturnToken() {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto(
                "test@example.com",
                "1234"
        );

        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserLoginResponseDto.class);

        Assertions.assertNotNull(actual.token());
    }

}
