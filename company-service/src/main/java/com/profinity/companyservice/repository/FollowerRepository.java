package com.profinity.companyservice.repository;

import com.profinity.companyservice.entity.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, UUID> {
    Optional<Follower> findByFollowerIdAndCompanyId(UUID followerId, UUID companyId);

    Page<Follower> findByCompanyId(UUID companyId, Pageable pageable);
}
