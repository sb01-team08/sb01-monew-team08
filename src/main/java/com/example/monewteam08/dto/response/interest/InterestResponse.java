package com.example.monewteam08.dto.response.interest;

import java.util.List;
import java.util.UUID;

public record InterestResponse(
    UUID id,
    String name,
    List<String> keywords,
    int subscriberCount
) {

}
