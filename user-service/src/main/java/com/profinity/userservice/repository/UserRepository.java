package com.profinity.userservice.repository;

import com.profinity.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User , UUID> {
    boolean existsByAuthUserId(UUID authUserId);
}
