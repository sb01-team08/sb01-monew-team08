package com.example.monewteam08.dto.response.article;

import com.example.monewteam08.dto.response.article.item.NaverNewsItem;
import java.util.List;

public record NaverNewsResponse(
    List<NaverNewsItem> items
) {

}
