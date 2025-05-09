package com.example.monewteam08.event;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserCreateEvent {

  private final UUID userId;

}
