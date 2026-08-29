package com.profinity.userservice.event;

import java.util.List;
import java.util.UUID;

public record UserEventRecord (
    UUID userId,
    String username,
    String headline,
    String location,
    List<String> skills
) {
}
