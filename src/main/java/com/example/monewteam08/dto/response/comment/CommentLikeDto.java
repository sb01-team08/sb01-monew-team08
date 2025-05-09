package com.example.monewteam08.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeDto {

  private String id;
  private String likedBy;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;
  private String commentId;
  private String articleId;
  private String commentUserId;
  private String commentUserNickname;
  private String commentContent;
  private int commentLikeCount;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
  private LocalDateTime commentCreatedAt;
}
