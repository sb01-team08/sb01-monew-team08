package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.response.article.NaverNewsResponse;
import com.example.monewteam08.entity.Article;
import com.example.monewteam08.service.Interface.ArticleFetchService;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ArticleFetchServiceImpl implements ArticleFetchService {

  private final RestTemplate restTemplate;
  private final String naverClientId;
  private final String naverClientSecret;

  public ArticleFetchServiceImpl(RestTemplateBuilder builder,
      @Value("${naver.api.client-id}") String naverClientId,
      @Value("${naver.api.client-secret}") String naverClientSecret) {
    this.restTemplate = builder.build();
    this.naverClientId = naverClientId;
    this.naverClientSecret = naverClientSecret;
  }

  public List<Article> fetchAllArticles() {
    List<Article> articles = new ArrayList<>();
    articles.addAll(fetchNaverArticles());
    articles.addAll(fetchRssArticles("YONHAP", "https://www.yonhapnewstv.co.kr/browse/feed/"));
    articles.addAll(
        fetchRssArticles("CHOSUN", "https://www.chosun.com/arc/outboundfeeds/rss/?outputType=xml"));
    articles.addAll(fetchRssArticles("HANKYUNG", "https://www.hankyung.com/feed/all-news"));
    return articles;
  }

  protected List<Article> fetchNaverArticles() {

    String uri = UriComponentsBuilder
        .fromHttpUrl("https://openapi.naver.com/v1/search/news.json")
        .queryParam("query", "뉴스")
        .queryParam("display", 10)
        .queryParam("start", 1)
        .queryParam("sort", "date")
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Naver-Client-Id", naverClientId);
    headers.set("X-Naver-Client-Secret", naverClientSecret);

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<NaverNewsResponse> response = restTemplate.exchange(
        uri,
        HttpMethod.GET,
        entity,
        NaverNewsResponse.class
    );

    return response.getBody().items().stream().map(item ->
        new Article(
            "NAVER",
            item.title(),
            item.description(),
            item.originalLink(),
            parsePubDate(item.pubDate())
        )).toList();
  }

  protected List<Article> fetchRssArticles(String source, String url) {
    List<Article> articles = new ArrayList<>();

    try (XmlReader reader = new XmlReader(new URL(url))) {
      SyndFeed feed = new SyndFeedInput().build(reader);
      for (SyndEntry entry : feed.getEntries()) {
        String description = entry.getDescription() != null
            ? entry.getDescription().getValue()
            : "";
        LocalDateTime publishedAt = entry.getPublishedDate()
            .toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        articles.add(
            new Article(source, entry.getTitle(), description,
                entry.getLink(),
                publishedAt));
      }
    } catch (IOException | FeedException e) {
      throw new RuntimeException(source + " 수집 중 에러", e);
    }
    return articles;
  }

  private static LocalDateTime parsePubDate(String pubDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z",
        Locale.ENGLISH);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, formatter);
    return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
  }
  
}



