package com.example.monewteam08.event;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserLoginEvent {

  private final UUID userId;

}
