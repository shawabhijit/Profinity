package com.profinity.postservice.repositoty;

import com.profinity.postservice.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostId(UUID postId, Pageable pageable);

    List<Comment> findByPostIdAndCreatedAt(UUID postId, LocalDateTime createdAt , Pageable pageable);

    @Modifying
    void deleteByPostId(UUID postId);
}
