package com.profinity.userservice.repository;

import com.profinity.userservice.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
}
