package com.example.monewteam08.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "backup.s3")
public class BackupS3Properties {

  private final String bucket;
  private final String prefix;

  public BackupS3Properties(String bucket, String prefix) {
    this.bucket = bucket;
    this.prefix = prefix;
  }

}
