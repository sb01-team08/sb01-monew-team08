package com.example.monewteam08.dto.response.article;

import java.util.UUID;

public record ArticleInterestCount(
    UUID interestId,
    String interestName,
    int articleCount
) {

}
