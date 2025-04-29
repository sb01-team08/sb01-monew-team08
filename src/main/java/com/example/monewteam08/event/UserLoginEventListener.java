package com.example.monewteam08.event;

import com.example.monewteam08.service.Interface.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserLoginEventListener {

  private final ArticleService articleService;

  @Async
  @EventListener
  public void handleUserLoginEvent(UserLoginEvent event) {
    log.info("User with ID {} logged in", event.getUserId());
    articleService.fetchAndSave(event.getUserId());
  }
}
