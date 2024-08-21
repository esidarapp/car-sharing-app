package com.product.carsharing.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.carsharing.dto.rental.CreateRentalDto;
import com.product.carsharing.dto.rental.RentalDto;
import com.product.carsharing.dto.rental.ReturnRentalDto;
import com.product.carsharing.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/add-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/car/add-cars.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/rental/add-rentals.sql")
            );
        }
    }

    @Test
    @DisplayName("Create a new rental with custom User")
    void save_ValidRequestDto_Success() throws Exception {
        User customUser = new User();
        customUser.setId(2L);
        customUser.setEmail("manager@example.com");
        customUser.setPassword("$2a$10$9iiGZqPYgZRSK3X.fJcHW./YBHYVIJNe7y5ruIzjFl7qGXcTCQI5C");
        customUser.setRole(User.Role.MANAGER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        CreateRentalDto rentalDto = new CreateRentalDto(
                LocalDate.now().plusDays(1),
                1L
        );

        RentalDto expected = new RentalDto(
                1L,
                LocalDate.now(),
                rentalDto.returnDate(),
                rentalDto.carId(),
                2L
        );
        String jsonRequest = objectMapper.writeValueAsString(rentalDto);
        MvcResult result = mockMvc.perform(post("/rentals")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        RentalDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Get active rentals for Manager with filters")
    void getActiveRentals_ManagerWithFilters_Success() throws Exception {
        RentalDto rentalDto1 = new RentalDto(1L,
                LocalDate.parse("2024-08-01"),
                LocalDate.parse("2024-08-07"),
                1L, 1L);
        RentalDto rentalDto2 = new RentalDto(1L,
                LocalDate.parse("2024-08-12"),
                LocalDate.parse("2024-08-20"),
                4L, 1L);

        List<RentalDto> expected = new ArrayList<>();
        expected.add(rentalDto1);
        expected.add(rentalDto2);

        User customUser = new User();
        customUser.setId(2L);
        customUser.setEmail("manager@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.MANAGER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult result = mockMvc.perform(get("/rentals")
                        .param("is_active", String.valueOf(true))
                        .param("user_id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        RentalDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), RentalDto[].class);

        Assertions.assertEquals(expected.size(), actual.length);
    }

    @Test
    @DisplayName("Get active rentals for Manager without filters")
    void getActiveRentals_ManagerWithoutFilters_Success() throws Exception {
        RentalDto rentalDto1 = new RentalDto(1L,
                LocalDate.parse("2024-08-01"),
                LocalDate.parse("2024-08-07"),
                1L, 1L);

        RentalDto rentalDto2 = new RentalDto(4L,
                LocalDate.parse("2024-08-12"),
                LocalDate.parse("2024-08-20"),
                4L, 1L);

        RentalDto rentalDto3 = new RentalDto(2L,
                LocalDate.parse("2024-08-05"),
                LocalDate.parse("2024-08-06"),
                2L, 1L);

        RentalDto rentalDto4 = new RentalDto(3L,
                LocalDate.parse("2024-08-10"),
                LocalDate.parse("2024-08-15"),
                3L, 2L);

        RentalDto rentalDto5 = new RentalDto(1L,
                LocalDate.parse("2024-08-15"),
                LocalDate.parse("2024-08-22"),
                1L, 2L);

        List<RentalDto> expected = new ArrayList<>();
        expected.add(rentalDto1);
        expected.add(rentalDto2);
        expected.add(rentalDto3);
        expected.add(rentalDto4);
        expected.add(rentalDto5);

        User customUser = new User();
        customUser.setId(2L);
        customUser.setEmail("manager@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.MANAGER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult result = mockMvc.perform(get("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        RentalDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), RentalDto[].class);

        Assertions.assertEquals(expected.size(), actual.length);
    }

    @Test
    @DisplayName("Get active rentals for Customer")
    void getActiveRentals_Customer_Success() throws Exception {
        RentalDto rentalDto1 = new RentalDto(4L,
                LocalDate.parse("2024-08-12"),
                LocalDate.parse("2024-08-20"),
                4L, 1L);

        RentalDto rentalDto2 = new RentalDto(2L,
                LocalDate.parse("2024-08-05"),
                LocalDate.parse("2024-08-06"),
                2L, 1L);

        List<RentalDto> expected = new ArrayList<>();
        expected.add(rentalDto1);
        expected.add(rentalDto2);

        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("customer@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.CUSTOMER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MvcResult result = mockMvc.perform(get("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        RentalDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), RentalDto[].class);

        Assertions.assertEquals(expected.size(), actual.length);
    }

    @Test
    @DisplayName("Get active rentals for Customer")
    void getActiveRentals_Customer_IsForbiddenStatus() throws Exception {
        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("customer@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.CUSTOMER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/rentals")
                        .param("is_active", String.valueOf(true))
                        .param("user_id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get active rentals for Customer")
    void findRentalById_Manager_Success() throws Exception {
        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("manager@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.MANAGER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        RentalDto expected = new RentalDto(1L,
                LocalDate.parse("2024-08-01"),
                LocalDate.parse("2024-08-07"),
                1L, 1L);

        MvcResult result = mockMvc.perform(get("/rentals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        RentalDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), RentalDto.class);

        Assertions.assertEquals(expected.id(), actual.id());
        Assertions.assertEquals(expected.rentalDate(), actual.rentalDate());
        Assertions.assertEquals(expected.returnDate(), actual.returnDate());
        Assertions.assertEquals(expected.carId(), actual.carId());
        Assertions.assertEquals(expected.userId(), actual.userId());
    }

    @Test
    @DisplayName("Get active rentals for Customer")
    void returnRental_Manager_Success() throws Exception {
        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("manager@example.com");
        customUser.setPassword("password");
        customUser.setRole(User.Role.MANAGER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ReturnRentalDto returnRentalDto = new ReturnRentalDto(
                5L,
                LocalDate.parse("2024-08-30")
        );

        String jsonRequest = objectMapper.writeValueAsString(returnRentalDto);

        mockMvc.perform(put("/rentals/return")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
