package mate.academy.carsharing.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.carsharing.dto.user.UserRegistrationRequestDto;
import mate.academy.carsharing.dto.user.UserResponseDto;
import mate.academy.carsharing.exception.RegisterException;
import mate.academy.carsharing.mapper.UserMapper;
import mate.academy.carsharing.model.User;
import mate.academy.carsharing.repository.UserRepository;
import mate.academy.carsharing.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto request) throws RegisterException {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegisterException("User with email "
                    + request.email() + " already exists.");
        }
        User user = userMapper.toModel(request);
        user.setRole(User.Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
