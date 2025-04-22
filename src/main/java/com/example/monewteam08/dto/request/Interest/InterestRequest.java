package com.example.monewteam08.dto.request.Interest;

import java.util.List;

public record InterestRequest(
    String name,
    List<String> keywords
) {

}
