package com.example.monewteam08.service.impl;

import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.repository.UserRepository;
import com.example.monewteam08.service.Interface.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl implements UserActivityLogService {

  private final UserActivityLogRepository userActivityLogRepository;
  private final UserRepository userRepository;

}
