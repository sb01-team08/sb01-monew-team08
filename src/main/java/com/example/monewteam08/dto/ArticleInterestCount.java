package com.example.monewteam08.dto;

import java.util.UUID;

public record ArticleInterestCount(
    UUID interestId,
    String interestName,
    int articleCount
) {

}
