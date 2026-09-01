package com.profinity.postservice.event;

import com.profinity.postservice.entity.enums.AttachmentType;

public record PostAttachmentRecord (
        String url,
        String fileName,
        long fileSize,
        AttachmentType type
) {
}
