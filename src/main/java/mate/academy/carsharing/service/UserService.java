package mate.academy.carsharing.service;

import mate.academy.carsharing.dto.user.UpdateProfileInfoDto;
import mate.academy.carsharing.dto.user.UpdateRoleDto;
import mate.academy.carsharing.dto.user.UserRegistrationRequestDto;
import mate.academy.carsharing.dto.user.UserResponseDto;
import mate.academy.carsharing.exception.RegisterException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegisterException;

    UserResponseDto findById(Long id);

    void updateRole(UpdateRoleDto updateRoleDto, Long id);

    UserResponseDto updateProfileInfo(UpdateProfileInfoDto dto, Long id);
}
