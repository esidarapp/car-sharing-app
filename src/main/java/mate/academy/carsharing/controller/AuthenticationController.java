package mate.academy.carsharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.user.UserLoginRequestDto;
import mate.academy.carsharing.dto.user.UserLoginResponseDto;
import mate.academy.carsharing.dto.user.UserRegistrationRequestDto;
import mate.academy.carsharing.dto.user.UserResponseDto;
import mate.academy.carsharing.exception.RegisterException;
import mate.academy.carsharing.security.AuthenticationService;
import mate.academy.carsharing.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Management",
        description = "Endpoints for managing user authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping({"/register"})
    @Operation(summary = "Create a new user",
            description = "Create a new user")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request) throws RegisterException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticate a user with email and password and return a JWT token"
    )
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
