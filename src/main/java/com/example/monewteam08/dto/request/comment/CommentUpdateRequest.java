package com.example.monewteam08.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentUpdateRequest {

  @NotBlank(message = "댓글 내용은 필수입니다.")
  private String content;
}
