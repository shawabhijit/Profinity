package com.profinity.postservice.event;

import java.util.UUID;

public record CommentEventRecord(
        UUID postId,
        UUID userId,
        UUID commentId,
        UUID authorId
) {
}
