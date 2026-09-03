package com.profinity.feedservice.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostCreatedRecord (
        UUID postId,
        UUID authorId,
        String content,
        List<PostAttachmentRecord> attachments,
        LocalDateTime createdAt
) {
}
