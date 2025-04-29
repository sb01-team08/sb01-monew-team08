package com.example.monewteam08.service.impl;

import com.example.monewteam08.service.Interface.S3Service;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

//  private final S3Client s3Client;
//  private final String bucketName = ""; // TODO: S3 버킷 이름 추가
//  private final BackupS3Properties backupS3Properties;

  @Override
  public void upload(Path path, LocalDate date) {
//    String key = generateKey(date);
//    PutObjectRequest request = PutObjectRequest.builder()
//        .bucket(backupS3Properties.getBucket())
//        .key(key)
//        .contentType("text/csv")
//        .build();
//
//    s3Client.putObject(request, path);
//    log.info("Uploaded file to S3: {}", key);
  }

  @Override
  public Path download(LocalDate date) {
    String key = generateKey(date);
    Path downloadPath = Path.of(System.getProperty("java.io.tmpdir"), key.replace("/", "_"));

//    GetObjectRequest request = GetObjectRequest.builder()
//        .bucket(bucketName)
//        .key(key)
//        .build();
//
//    s3Client.getObject(request, downloadPath);
//    log.info("Downloaded file from S3: {}", downloadPath);

    return downloadPath;
  }

  private String generateKey(LocalDate date) {
    String datePath = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String fileName = "articles_" + date.format(DateTimeFormatter.ISO_DATE) + ".csv";
//    return backupS3Properties.getPrefix() + datePath + "/" + fileName;
    return "";
  }
}
