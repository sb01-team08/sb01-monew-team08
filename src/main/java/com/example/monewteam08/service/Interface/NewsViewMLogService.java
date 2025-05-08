package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.useractivitylog.NewsViewLogResponse;
import com.example.monewteam08.entity.NewsViewMLog;
import java.util.List;
import java.util.UUID;

public interface NewsViewMLogService {

  void addArticleView(UUID userId, UUID articleId);

  List<NewsViewLogResponse> getNewsViewLogs(List<NewsViewMLog> newsViewMLogs);

}
