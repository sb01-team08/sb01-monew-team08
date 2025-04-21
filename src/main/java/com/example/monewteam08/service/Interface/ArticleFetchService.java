package com.example.monewteam08.service.Interface;

import com.example.monewteam08.dto.response.article.item.NaverNewsItem;
import java.util.List;


public interface ArticleFetchService {

  List<NaverNewsItem> fetchNaverArticles();
}
