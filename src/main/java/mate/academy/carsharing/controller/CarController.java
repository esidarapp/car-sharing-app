package mate.academy.carsharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.car.CarDto;
import mate.academy.carsharing.dto.car.CreateCarRequestDto;
import mate.academy.carsharing.dto.car.UpdateCarRequestDto;
import mate.academy.carsharing.service.car.CarService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @GetMapping
    @Operation(summary = "Get all cars",
            description = "Retrieve a list of all available cars with optional pagination.")
    public List<CarDto> findAll(@ParameterObject @PageableDefault Pageable pageable) {
        return carService.findAll(pageable);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID",
            description = "Retrieve details of a specific car by its ID.")
    public CarDto findById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @Operation(summary = "Create a new car",
            description = "Add a new car to the system.")
    public CarDto save(@RequestBody @Valid CreateCarRequestDto requestDto) {
        return carService.save(requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update car details",
            description = "Update the details of an existing car by its ID.")
    public CarDto updateById(@PathVariable Long id,
                             @RequestBody @Valid UpdateCarRequestDto requestDto) {
        return carService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car by ID",
            description = "Remove a car from the system by its ID.")
    public void deleteById(@PathVariable Long id) {
        carService.deleteById(id);
    }
}
