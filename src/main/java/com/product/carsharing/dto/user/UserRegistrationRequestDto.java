package com.product.carsharing.dto.user;

import com.product.carsharing.validate.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password", second = "repeatPassword")
public record UserRegistrationRequestDto(
        @Email
        String email,
        @Length(max = 20)
        @NotBlank
        String firstName,
        @Length(max = 20)
        @NotBlank
        String lastName,
        @Length(min = 3, max = 16)
        @NotBlank
        String password,
        @Length(min = 3, max = 16)
        @NotBlank
        String repeatPassword
) {
}
