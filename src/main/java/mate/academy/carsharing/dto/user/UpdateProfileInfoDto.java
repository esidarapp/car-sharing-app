package mate.academy.carsharing.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileInfoDto(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName
) {
}
