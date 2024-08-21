package com.product.carsharing.dto.user;

import com.product.carsharing.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UpdateRoleDto {
    @NotNull
    private User.Role role;
}

