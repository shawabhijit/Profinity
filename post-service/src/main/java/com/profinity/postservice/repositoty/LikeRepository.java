package com.profinity.postservice.repositoty;

import com.profinity.postservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
}
