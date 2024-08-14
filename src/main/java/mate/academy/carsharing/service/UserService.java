package mate.academy.carsharing.service;

import mate.academy.carsharing.dto.user.UserRegistrationRequestDto;
import mate.academy.carsharing.dto.user.UserResponseDto;
import mate.academy.carsharing.exception.RegisterException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegisterException;
}
