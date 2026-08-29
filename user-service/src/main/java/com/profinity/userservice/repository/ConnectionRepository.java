package com.profinity.userservice.repository;

import com.profinity.userservice.entity.Connection;
import com.profinity.userservice.entity.enums.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    boolean existsByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);
    List<Connection> findByRequesterIdAndStatus(UUID requesterId, ConnectionStatus status);
}
