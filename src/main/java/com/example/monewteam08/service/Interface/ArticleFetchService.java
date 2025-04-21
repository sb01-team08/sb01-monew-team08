package com.example.monewteam08.service.Interface;

import com.example.monewteam08.entity.Article;
import java.util.List;


public interface ArticleFetchService {

  List<Article> fetchNaverArticles();

  List<Article> fetchRssArticles(String source, String url);
}
