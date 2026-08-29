package com.profinity.userservice.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID authUserId,
        String email,
        String username,
        LocalDateTime createdAt
) {
}
