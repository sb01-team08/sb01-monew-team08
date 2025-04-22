package com.example.monewteam08.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRegisterRequest {

  @NotBlank(message = "기사 ID는 필수입니다.")
  private String articleId;
  @NotBlank(message = "사용자 ID는 필수입니다.")
  private String userId;
  @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
  private String content;
}
