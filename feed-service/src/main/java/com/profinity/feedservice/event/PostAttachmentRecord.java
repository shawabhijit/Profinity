package com.profinity.feedservice.event;

public record PostAttachmentRecord (
        String url,
        String fileName,
        long fileSize,
        String type
) {
}

