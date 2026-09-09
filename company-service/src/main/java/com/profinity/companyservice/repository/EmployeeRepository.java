package com.profinity.companyservice.repository;

import com.profinity.companyservice.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee , UUID> {
    boolean existsById(UUID id);
    Page<Employee> findByCompanyId(UUID companyId , Pageable pageable);

    Employee findByUserIdAndCompanyId(UUID userId, UUID companyId);
}
