package com.example.monewteam08.event;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.repository.ArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreateEventListener {

  private final ArticleRepository articleRepository;

  @Async
  @EventListener
  public void handleUserCreateEvent(UserCreateEvent event) {
    log.info("User with ID {} created", event.getUserId());
    List<Article> articles = articleRepository.findAll();
    articleRepository.saveAll(articles);
  }
}
