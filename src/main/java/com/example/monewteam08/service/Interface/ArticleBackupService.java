package com.example.monewteam08.service.Interface;

import java.time.LocalDate;

public interface ArticleBackupService {

  void backup();

  void restore(LocalDate date);

}
