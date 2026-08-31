package com.profinity.postservice.entity;

import com.profinity.postservice.entity.enums.AttachmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "post_attachment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttachmentType type;

    private String url;

    private String filename;

    private Long fileSize;
}
