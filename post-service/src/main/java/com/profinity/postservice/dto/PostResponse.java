package com.profinity.postservice.dto;

import com.profinity.postservice.entity.PostAttachment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private UUID id;
    private UUID authorId;
    private String content;
    private List<PostAttachment> attachments;
    private long likeCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private boolean likedByCurrentUser;
}
