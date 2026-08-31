package com.profinity.postservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(
        columnNames = {"post_id", "user_id"}
))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false , name = "post_id")
    private UUID postId;
    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
