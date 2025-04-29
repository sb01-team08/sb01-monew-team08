package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.request.user.UserLoginRequest;
import com.example.monewteam08.dto.request.user.UserRequest;
import com.example.monewteam08.dto.request.user.UserUpdateRequest;
import com.example.monewteam08.dto.response.user.UserResponse;
import com.example.monewteam08.entity.User;
import com.example.monewteam08.entity.UserActivityLog;
import com.example.monewteam08.event.UserLoginEvent;
import com.example.monewteam08.exception.user.DeletedAccountException;
import com.example.monewteam08.exception.user.EmailAlreadyExistException;
import com.example.monewteam08.exception.user.LoginFailedException;
import com.example.monewteam08.exception.user.UserNotFoundException;
import com.example.monewteam08.mapper.UserMapper;
import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final ApplicationEventPublisher publisher;

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  private final UserActivityLogRepository userActivityLogRepository;

  @Transactional
  @Override
  public UserResponse create(UserRequest userRequest) {
    log.debug("사용자 생성 요청 - 요청 데이터: [email={}, nickname={}]", userRequest.email(),
        userRequest.nickname());
    if (!userRepository.existsUserByEmail(userRequest.email())) {
      User user = userMapper.toEntity(userRequest);
      User savedUser = userRepository.save(user);

      log.info("사용자가 성공적으로 생성되었습니다. - id: {}", savedUser.getId());
      userActivityLogRepository.save(new UserActivityLog(user));

      return userMapper.toResponse(savedUser);
    } else {
      log.warn("이미 존재하는 이메일입니다. - email: {}", userRequest.email());
      throw new EmailAlreadyExistException(userRequest.email());
    }
  }

  @Transactional
  @Override
  public UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest) {
    log.debug("사용자 닉네임 수정 요청 - 요청 id: {}, 수정 요청 닉네임: {}", userId, userUpdateRequest.nickname());
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    user.updateNickname(userUpdateRequest.nickname());
    log.info("성공적으로 수정되었습니다. - id: {}, nickname: {}", userId, user.getNickname());
    return userMapper.toResponse(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("사용자 논리 삭제 요청 - 요청 id: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    user.updateDeletedAt(LocalDateTime.now());
    user.updateIsActive(false);

    log.info("성공적으로 논리 삭제가 적용되었습니다. - id: {}", userId);
  }

  @Transactional
  @Override
  public void hardDelete(UUID userId) {
    log.debug("사용자 물리 삭제 요청 - 요청 id: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    userRepository.delete(user);

    log.info("성공적으로 삭제되었습니다. - id: {}", userId);
  }

  @Override
  public UserResponse login(UserLoginRequest userLoginRequest) {
    log.debug("사용자 로그인 요청 - 요청 email: {}", userLoginRequest.email());

    User user = userRepository.findUserByEmailAndPassword(userLoginRequest.email(),
        userLoginRequest.password());

    if (user == null) {
      log.warn("로그인 실패: 이메일 또는 비밀번호 불일치 - email={}", userLoginRequest.email());
      throw new LoginFailedException();
    }

    if (!user.isActive()) {
      log.warn("로그인 실패: 삭제된 계정입니다. - email={}", userLoginRequest.email());
      throw new DeletedAccountException();
    }

    log.info("성공적으로 로그인 되었습니다. - id: {}", user.getId());
    publisher.publishEvent(new UserLoginEvent(user.getId()));
    return userMapper.toResponse(user);
  }
}
