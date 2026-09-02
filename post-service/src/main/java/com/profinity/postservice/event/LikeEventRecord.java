package com.profinity.postservice.event;

import java.util.UUID;

public record LikeEventRecord (
        UUID postId,
        UUID userId,
        UUID authorId
) {
}
