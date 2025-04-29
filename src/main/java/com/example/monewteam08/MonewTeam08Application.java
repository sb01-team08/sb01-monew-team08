package com.example.monewteam08;

import com.example.monewteam08.config.BackupS3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(BackupS3Properties.class)
public class MonewTeam08Application {

  public static void main(String[] args) {
    SpringApplication.run(MonewTeam08Application.class, args);
  }

}
