package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Article;
import com.example.monewteam08.exception.article.ArticleExportFailedException;
import com.example.monewteam08.service.Interface.CsvService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

  @Override
  public Path exportArticlesToCsv(Path path, List<Article> articles) {

    try (CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()))) {
      String[] header = {"id", "source", "title", "summary", "sourceUrl", "publishDate",
          "interestId"};
      writer.writeNext(header);

      for (Article article : articles) {
        writer.writeNext(new String[]{
            article.getId().toString(),
            article.getSource(),
            article.getTitle(),
            article.getSummary(),
            article.getSourceUrl(),
            article.getPublishDate().toString(),
            article.getInterestId() != null ? article.getInterestId().toString() : null
        });
      }
    } catch (IOException e) {
      throw new ArticleExportFailedException(e.getMessage());
    }

    return path;
  }

  @Override
  public List<Article> importArticlesFromCsv(Path filePath) {
    List<Article> articles = new ArrayList<>();
    try (CSVReader reader = new CSVReader(new FileReader(filePath.toFile()))) {
      String[] header = reader.readNext();
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine.length >= 6) {
          Article article = Article.withId(UUID.fromString(nextLine[0]),
              nextLine[1],
              nextLine[2],
              nextLine[3],
              nextLine[4],
              LocalDateTime.parse(nextLine[5]),
              nextLine[6] != null && !nextLine[6].isBlank() && !nextLine[6].equals("null")
                  ? UUID.fromString(nextLine[6])
                  : null
          );
          articles.add(article);
          log.debug("Article imported: {}", article.getId());
        }
      }
    } catch (IOException | CsvValidationException e) {
      throw new ArticleExportFailedException(e.getMessage());
    }
    log.debug("CSV import completed. {} articles imported.", articles.size());
    return articles;

  }

}
