package com.example.monewteam08.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.config.BackupS3Properties;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private BackupS3Properties backupS3Properties;

  @InjectMocks
  private S3ServiceImpl s3Service;

  @Test
  void S3_업로드_성공() {
    // given
    LocalDate date = LocalDate.of(2025, 4, 29);
    Path filePath = Path.of("/tmp/articles_2025-04-29.csv");

    when(backupS3Properties.getBucket()).thenReturn("monew-storage");
    when(backupS3Properties.getPrefix()).thenReturn("backups/articles/");

    // when
    s3Service.upload(filePath, date);

    // then
    verify(s3Client).putObject(any(PutObjectRequest.class), eq(filePath));
  }

  @Test
  void S3_다운로드_성공() {
    // given
    LocalDate date = LocalDate.of(2025, 4, 29);

    when(backupS3Properties.getBucket()).thenReturn("monew-backup-storage");
    when(backupS3Properties.getPrefix()).thenReturn("backups/articles/");

    // when
    Path result = s3Service.download(date);

    // then
    assertThat(result).isNotNull();
    verify(s3Client).getObject(any(GetObjectRequest.class), any(Path.class));
  }
}