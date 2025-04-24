package com.example.monewteam08.service.impl;

import com.example.monewteam08.repository.UserActivityLogRepository;
import com.example.monewteam08.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserActivityLogServiceImplTest {

  @InjectMocks
  private UserActivityLogServiceImpl userActivityLogService;

  @Mock
  private UserActivityLogRepository userActivityLogRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("사용자 활동 내역을 전달한다.")
  void getUserActivityLog() {
    // given

    // when

    // then
//    Assertions.assertThat();
  }

}