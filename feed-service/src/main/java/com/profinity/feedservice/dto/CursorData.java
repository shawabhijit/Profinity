package com.profinity.feedservice.dto;

import java.util.UUID;

public record CursorData (
        long timestamp,
        UUID postID
) {
}
