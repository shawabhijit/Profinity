package com.profinity.companyservice.repository;

import com.profinity.companyservice.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, UUID> {
}
