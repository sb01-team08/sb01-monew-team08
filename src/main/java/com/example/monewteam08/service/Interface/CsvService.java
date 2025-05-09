package com.example.monewteam08.service.Interface;

import com.example.monewteam08.entity.Article;
import java.nio.file.Path;
import java.util.List;

public interface CsvService {

  Path exportArticlesToCsv(Path path, List<Article> articles);

  List<Article> importArticlesFromCsv(Path filePath);

}
