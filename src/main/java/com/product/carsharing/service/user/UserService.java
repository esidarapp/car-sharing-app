package com.product.carsharing.service.user;

import com.product.carsharing.dto.user.UpdateProfileInfoDto;
import com.product.carsharing.dto.user.UpdateRoleDto;
import com.product.carsharing.dto.user.UserRegistrationRequestDto;
import com.product.carsharing.dto.user.UserResponseDto;
import com.product.carsharing.exception.RegisterException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegisterException;

    UserResponseDto findById(Long id);

    void updateRole(UpdateRoleDto updateRoleDto, Long id);

    UserResponseDto updateProfileInfo(UpdateProfileInfoDto dto, Long id);
}
