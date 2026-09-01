package com.profinity.postservice.event;

import com.profinity.postservice.entity.PostAttachment;

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
