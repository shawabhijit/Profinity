package com.profinity.postservice.dto;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public class LikeResponse {
    private UUID id;
    private UUID postId;
    private LocalDateTime createdAt;
}
