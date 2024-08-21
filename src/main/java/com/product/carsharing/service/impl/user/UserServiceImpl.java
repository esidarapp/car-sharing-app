package com.product.carsharing.service.impl.user;

import com.product.carsharing.dto.user.UpdateProfileInfoDto;
import com.product.carsharing.dto.user.UpdateRoleDto;
import com.product.carsharing.dto.user.UserRegistrationRequestDto;
import com.product.carsharing.dto.user.UserResponseDto;
import com.product.carsharing.exception.EntityNotFoundException;
import com.product.carsharing.exception.RegisterException;
import com.product.carsharing.mapper.UserMapper;
import com.product.carsharing.model.User;
import com.product.carsharing.repository.user.UserRepository;
import com.product.carsharing.service.user.UserService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserResponseDto findById(Long id) {
        return userMapper.toDto(findUserById(id));
    }

    @Override
    public void updateRole(UpdateRoleDto updateRoleDto, Long id) {
        User user = findUserById(id);
        user.setRole(updateRoleDto.getRole());
        userRepository.save(user);
    }

    @Override
    public UserResponseDto updateProfileInfo(UpdateProfileInfoDto dto, Long id) {
        User user = findUserById(id);
        userMapper.updateUserFromDto(dto,user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id " + id)
        );
    }
}
