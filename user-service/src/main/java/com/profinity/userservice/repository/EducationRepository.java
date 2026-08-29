package com.profinity.userservice.repository;

import com.profinity.userservice.entity.Education;
import com.profinity.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationRepository extends JpaRepository<Education, UUID> {
    Education findByIdAndUser(UUID id, User user);
}
