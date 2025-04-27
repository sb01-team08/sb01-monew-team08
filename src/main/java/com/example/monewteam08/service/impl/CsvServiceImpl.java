package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.exception.article.ArticleExportFailedException;
import com.example.monewteam08.service.Interface.CsvService;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

  @Override
  public Path exportArticlesToCsv(LocalDate date, List<Article> articles) {
    String fileName = "articles_" + date + ".csv";
    Path path = Path.of(System.getProperty("java.io.tmpdir"), fileName);

    try (CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()))) {
      String[] header = {"id", "source", "title", "summary", "sourceUrl", "publishDate"};
      writer.writeNext(header);

      for (Article article : articles) {
        writer.writeNext(new String[]{
            article.getId().toString(),
            article.getSource(),
            article.getTitle(),
            article.getSummary(),
            article.getSourceUrl(),
            article.getPublishDate().toString(),
        });
      }
    } catch (IOException e) {
      throw new ArticleExportFailedException(e.getMessage());
    }

    return path;
  }

  @Override
  public void importArticlesFromCsv(String filePath) {

  }
}
