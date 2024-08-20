package mate.academy.carsharing.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.carsharing.dto.car.CarDto;
import mate.academy.carsharing.dto.car.CreateCarRequestDto;
import mate.academy.carsharing.dto.car.UpdateCarRequestDto;
import mate.academy.carsharing.model.Car;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
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
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/car/add-cars.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/car/delete-cars.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER", "MANAGER"})
    @DisplayName("Get all cars")
    void findAll_Cars_ShouldReturnAllCars() throws Exception {
        CarDto car1 = new CarDto();
        car1.setId(1L);
        car1.setModel("Model S");
        car1.setBrand("Tesla");
        car1.setCarType(Car.CarType.SEDAN);
        car1.setInventory(10);
        car1.setDailyFee(BigDecimal.valueOf(100.11));

        CarDto car2 = new CarDto();
        car2.setId(2L);
        car2.setModel("CX-5");
        car2.setBrand("Mazda");
        car2.setCarType(Car.CarType.SUV);
        car2.setInventory(7);
        car2.setDailyFee(BigDecimal.valueOf(85.11));

        CarDto car3 = new CarDto();
        car3.setId(3L);
        car3.setModel("Civic");
        car3.setBrand("Honda");
        car3.setCarType(Car.CarType.HATCHBACK);
        car3.setInventory(15);
        car3.setDailyFee(BigDecimal.valueOf(60.11));

        CarDto car4 = new CarDto();
        car4.setId(4L);
        car4.setModel("Passat");
        car4.setBrand("Volkswagen");
        car4.setCarType(Car.CarType.UNIVERSAL);
        car4.setInventory(8);
        car4.setDailyFee(BigDecimal.valueOf(75.11));

        List<CarDto> expected = new ArrayList<>();
        expected.add(car1);
        expected.add(car2);
        expected.add(car3);
        expected.add(car4);

        MvcResult result = mockMvc.perform(get("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CarDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CarDto[].class);
        Assertions.assertEquals(4, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER", "MANAGER"})
    @DisplayName("Find car by id")
    void findById_ValidId_ShouldReturnBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CarDto.class);

        Assertions.assertNotNull(actual.getId(), "ID should not be null");
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("Tesla", actual.getBrand());
        Assertions.assertEquals("Model S", actual.getModel());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    @DisplayName("Create a new car")
    @Sql(scripts = "classpath:database/car/delete-car.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidRequestDto_Success() throws Exception {
        CreateCarRequestDto carRequestDto = new CreateCarRequestDto(
                "Mustang",
                "Ford",
                Car.CarType.SEDAN,
                5,
                BigDecimal.valueOf(120.00)
        );

        CarDto expected = new CarDto();
        expected.setId(1L);
        expected.setModel(carRequestDto.model());
        expected.setBrand(carRequestDto.brand());
        expected.setCarType(carRequestDto.carType());
        expected.setInventory(carRequestDto.inventory());
        expected.setDailyFee(carRequestDto.dailyFee());

        String jsonRequest = objectMapper.writeValueAsString(carRequestDto);

        MvcResult result = mockMvc.perform(post("/cars")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    @DisplayName("Update car by id")
    @Sql(scripts = "classpath:database/car/update-car.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateById_ValidRequestDto_Success() throws Exception {
        UpdateCarRequestDto requestDto = new UpdateCarRequestDto(
                "Model XS",
                "Tesla4ka",
                Car.CarType.SEDAN,
                4,
                BigDecimal.valueOf(130.00)
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/cars/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CarDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDto.class);

        Assertions.assertEquals(requestDto.model(), actual.getModel());
        Assertions.assertEquals(requestDto.brand(), actual.getBrand());
        Assertions.assertEquals(requestDto.carType(), actual.getCarType());
        Assertions.assertEquals(requestDto.inventory(), actual.getInventory());
        Assertions.assertEquals(requestDto.dailyFee(), actual.getDailyFee());
    }

    @WithMockUser(roles = {"MANAGER"})
    @Test
    @DisplayName("Delete car by id")
    @Sql(scripts = "classpath:database/car/undelete-car.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/cars/4")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

}

