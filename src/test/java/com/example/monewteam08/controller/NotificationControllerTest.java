package com.example.monewteam08.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.monewteam08.dto.response.notification.CursorPageResponseNotificationDto;
import com.example.monewteam08.service.Interface.NotificationService;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private NotificationService notificationService;

  private final String USER_ID = UUID.randomUUID().toString();
  private final String NOTIFICATION_ID = UUID.randomUUID().toString();

  @Test
  void 알림_목록_조회_성공() throws Exception {
    when(notificationService.getUnreadNotifications(any(), any(), any(), anyInt()))
        .thenReturn(CursorPageResponseNotificationDto.builder()
            .content(new ArrayList<>())
            .nextCursor(null)
            .nextAfter(null)
            .size(10)
            .totalElements(0)
            .hasNext(false)
            .build());

    mockMvc.perform(get("/api/notifications")
            .param("cursor", "2025-04-23T00:00:00")
            .param("after", "2025-04-22T00:00:00")
            .param("limit", "10")
            .header("Monew-Request-User-ID", USER_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size").value(10));
  }

  @Test
  void 전체_알림_읽음_처리_성공() throws Exception {
    doNothing().when(notificationService).confirmAllNotifications(USER_ID);

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/notifications")
            .header("Monew-Request-User-ID", USER_ID))
        .andExpect(status().isOk());
  }

  @Test
  void 개별_읽음_처리_성공() throws Exception {
    doNothing().when(notificationService).confirmNotification(USER_ID, NOTIFICATION_ID);

    mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/notifications/{notificationId}", NOTIFICATION_ID)
                .header("Monew-Request-User-ID", USER_ID))
        .andExpect(status().isOk());
  }
}