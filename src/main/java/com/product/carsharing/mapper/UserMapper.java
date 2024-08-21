package com.product.carsharing.mapper;

import com.product.carsharing.config.MapperConfig;
import com.product.carsharing.dto.user.UpdateProfileInfoDto;
import com.product.carsharing.dto.user.UserRegistrationRequestDto;
import com.product.carsharing.dto.user.UserResponseDto;
import com.product.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    void updateUserFromDto(UpdateProfileInfoDto dto, @MappingTarget User user);
}
