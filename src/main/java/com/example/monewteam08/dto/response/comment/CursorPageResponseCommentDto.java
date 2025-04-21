package com.example.monewteam08.dto.response.comment;

import java.util.ArrayList;

public class CursorPageResponseCommentDto {

    private ArrayList<CommentDto> comments;
    private String nextCursor;
    private String nextAfter;
    private int size;
    private int totalElements;
    private Boolean hasNext;
}
