package com.example.monewteam08.dto.request.user;

import com.example.monewteam08.common.RegexPatternConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserLoginRequest(@Email(regexp = RegexPatternConstants.EMAIL) @NotBlank String email,
                               @Pattern(regexp = RegexPatternConstants.PASSWORD) @NotBlank String password) {

}
