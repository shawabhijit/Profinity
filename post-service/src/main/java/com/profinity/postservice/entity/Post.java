package com.profinity.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID authorId;

    private String content;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostAttachment> attachments = new ArrayList<>();

    private long likeCount = 0;

    private long commentCount = 0;

    private boolean likedByCurrentUser = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void addAttachment(PostAttachment attachment) {
        attachment.setPost(this);
        attachments.add(attachment);
    }
}
