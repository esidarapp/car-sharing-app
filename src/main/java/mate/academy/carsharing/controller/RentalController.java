package mate.academy.carsharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.rental.CreateRentalDto;
import mate.academy.carsharing.dto.rental.RentalDto;
import mate.academy.carsharing.dto.rental.RentalSearchParams;
import mate.academy.carsharing.dto.rental.ReturnRentalDto;
import mate.academy.carsharing.model.User;
import mate.academy.carsharing.service.RentalService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental management", description = "Endpoints for managing rentals")
@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Create a new rental",
            description = "Create a new rental record.")
    public RentalDto save(@RequestBody @Valid CreateRentalDto createRentalDto,
                          Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return rentalService.save(createRentalDto, user.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @Operation(summary = "Get all active user rentals s",
            description = "Retrieve a list of active user`s rentals.")
    public List<RentalDto> getActiveRentals(
            @ParameterObject @PageableDefault RentalSearchParams params,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (params.user_id() == null && params.is_active() == null) {
            return rentalService.getActiveRentals(user.getId());
        } else if (user.getRole() == User.Role.MANAGER) {
            return rentalService.search(params);
        } else {
            throw new AccessDeniedException("You do not have permission to filter rentals.");
        }

    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get rentals by user ID and status",
            description = "Retrieve a list of rentals filtered by user ID and status.")
    public List<RentalDto> search(@ParameterObject @PageableDefault RentalSearchParams params) {
        return rentalService.search(params);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get a rental by ID",
            description = "Retrieve details of a specific rental by its ID.")
    public RentalDto findRentalById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @PostMapping("/return")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Return a rental",
            description = "Set the actual return date for a rental "
                    + "and increase the car inventory by 1..")
    public void returnRental(@RequestBody @Valid ReturnRentalDto returnRentalDto) {
        rentalService.returnRental(returnRentalDto);
    }
}
