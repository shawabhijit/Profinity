package com.profinity.postservice.repositoty;

import com.profinity.postservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByPostIdAndUserId(UUID postId, UUID userId);

    @Modifying
    void deleteByPostId(UUID postId);
}
