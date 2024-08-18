package mate.academy.carsharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.user.UpdateProfileInfoDto;
import mate.academy.carsharing.dto.user.UpdateRoleDto;
import mate.academy.carsharing.dto.user.UserResponseDto;
import mate.academy.carsharing.model.User;
import mate.academy.carsharing.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management",
        description = "Endpoints for managing user profiles and roles")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @GetMapping("/me")
    @Operation(summary = "Get current user profile",
            description = "Retrieve the profile information of the currently authenticated user.")
    public UserResponseDto findById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.findById(user.getId());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role",
            description = "Update the role of a user by their ID.")
    public void updateUsersRole(@PathVariable Long id,
                                @RequestBody @Valid UpdateRoleDto updateRoleDto) {
        userService.updateRole(updateRoleDto, id);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    @PutMapping("/me")
    @Operation(summary = "Update current user profile",
            description = "Update the profile information of the currently authenticated user.")
    public UserResponseDto updateUserInfo(Authentication authentication,
                                          @RequestBody @Valid UpdateProfileInfoDto dto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateProfileInfo(dto, user.getId());
    }
}
