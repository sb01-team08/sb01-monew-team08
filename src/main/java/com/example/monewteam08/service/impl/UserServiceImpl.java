package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.exception.user.EmailAlreadyExistException;
import com.example.monewteam08.mapper.UserMapper;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponse create(UserRequest userRequest) {
        if (!userRepository.existsUserByEmail(userRequest.email())) {
            User user = userMapper.toEntity(userRequest);
            User savedUser = userRepository.save(user);

            return userMapper.toResponse(savedUser);
        } else {
            throw new EmailAlreadyExistException(userRequest.email());
        }
    }
}
