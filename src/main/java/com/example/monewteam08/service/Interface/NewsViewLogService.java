package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.entity.UserActivityLog;
import java.util.List;
import java.util.UUID;

public interface NewsViewLogService {

  // news view 추가 시 함께 추가
  void addNewsViewLog(UUID userId, Article article);

  // 뉴스 삭제 시 로그도 함께 삭제
  void removeNewsViewLog(UUID userId, UUID articleId);

  // 뉴스 시청 기록 로그 조회 (최대 10개, 최신)
  List<NewsViewLogResponse> getNewsViewLogs(UserActivityLog userActivityLog);

}
