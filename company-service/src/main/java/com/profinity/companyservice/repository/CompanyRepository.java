package com.profinity.companyservice.repository;

import com.profinity.companyservice.dto.CompanyResponse;
import com.profinity.companyservice.entity.Company;
import com.profinity.companyservice.entity.enums.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Page<Company> findAllByIndustry(String industry, Pageable pageable);
    Page<Company> findAllByType(CompanyType type , Pageable pageable);

    boolean existsByIdAndAdministratorId(UUID id, UUID administratorId);
    boolean existsById(UUID id);
}
