package mate.academy.carsharing.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mate.academy.carsharing.model.User;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UpdateRoleDto {
    @NotNull
    private User.Role role;
}

