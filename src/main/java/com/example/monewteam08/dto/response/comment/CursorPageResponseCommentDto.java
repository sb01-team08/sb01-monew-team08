package com.example.monewteam08.dto.response.comment;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursorPageResponseCommentDto {

  private ArrayList<CommentDto> content;
  private String nextCursor;
  private String nextAfter;
  private int size;
  private int totalElements;
  private Boolean hasNext;
}
