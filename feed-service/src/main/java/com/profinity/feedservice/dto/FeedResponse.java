package com.profinity.feedservice.dto;

import java.util.List;
import java.util.UUID;

public record FeedResponse (
        List<UUID> postIds,
        String nextCursor,
        boolean hasNext
) {
}
