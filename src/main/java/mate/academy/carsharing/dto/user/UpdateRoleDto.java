package mate.academy.carsharing.dto.user;

import jakarta.validation.constraints.NotNull;
import mate.academy.carsharing.model.User;

public record UpdateRoleDto(
        @NotNull
        User.Role role
) {
}
