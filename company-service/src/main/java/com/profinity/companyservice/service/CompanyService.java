package com.profinity.companyservice.service;

import com.profinity.companyservice.client.UserServiceClient;
import com.profinity.companyservice.dto.AddEmployeeRequest;
import com.profinity.companyservice.dto.CompanyRequest;
import com.profinity.companyservice.dto.CompanyResponse;
import com.profinity.companyservice.entity.Company;
import com.profinity.companyservice.entity.Follower;
import com.profinity.companyservice.entity.enums.CompanyType;
import com.profinity.companyservice.exceptions.CompanyException;
import com.profinity.companyservice.kafka.CompanyEventProducers;
import com.profinity.companyservice.repository.CompanyRepository;
import com.profinity.companyservice.repository.EmployeeRepository;
import com.profinity.companyservice.repository.FollowerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final FollowerRepository followerRepository;
    private final EmployeeRepository employeeRepository;

    private final CompanyEventProducers companyEventProducers;
    private final UserServiceClient userServiceClient;


    public CompanyResponse createCompany(UUID authorId , CompanyRequest companyRequest) {
        Company company = new Company();
        company.setAdministratorId(authorId);
        company.setName(companyRequest.getName());
        company.setDescription(companyRequest.getDescription());
        company.setOverview(companyRequest.getOverview());
        company.setWebsite(companyRequest.getWebsite());
        company.setAddress(companyRequest.getAddress());
        company.setIndustry(companyRequest.getIndustry());
        company.setType(companyRequest.getCompanyType());
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        company = companyRepository.save(company);

        // publish company.created kafka event
        companyEventProducers.sendCompanyCreatedEvent(company);

        return mapToCompanyResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompany(UUID authorId, UUID companyId, CompanyRequest companyRequest) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new CompanyException("Company not fount this provided ID: " + companyId)
        );

        if(!company.getAdministratorId().equals(authorId)){
            throw new CompanyException("Permission denied to update this company as you are not a Administrator");
        }

        company.setName(companyRequest.getName());
        company.setDescription(companyRequest.getDescription());
        company.setOverview(companyRequest.getOverview());
        company.setWebsite(companyRequest.getWebsite());
        company.setAddress(companyRequest.getAddress());
        company.setIndustry(companyRequest.getIndustry());
        company.setType(companyRequest.getCompanyType());

        // publish company.updated event to kafka
        companyEventProducers.sendCompanyUpdatedEvent(company);

        return mapToCompanyResponse(company);
    }

    public List<CompanyResponse> getAllCompanies(UUID userId) {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(this::mapToCompanyResponse).collect(Collectors.toList());
    }

    public List<CompanyResponse> getAllCompaniesByIndustry(String industry, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companies = companyRepository.findAllByIndustry(industry , pageable);
        return companies.stream().map(this::mapToCompanyResponse).collect(Collectors.toList());
    }

    public List<CompanyResponse> findAllCompaniesByType(CompanyType type, int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companies = companyRepository.findAllByType(type, pageable);
        return companies.stream().map(this::mapToCompanyResponse).collect(Collectors.toList());
    }

    public CompanyResponse getCompany(UUID userId, UUID companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new CompanyException("Company not fount this provided ID: " + companyId)
        );
        return mapToCompanyResponse(company);
    }

    public String deleteCompany(UUID authorId, UUID companyId) {
        if(!companyRepository.existsByIdAndAdministratorId(companyId,authorId)){
            throw new CompanyException("Company not fount this provided Administrator ID: " + authorId + " and Company ID: " + companyId);
        }
        companyRepository.deleteById(companyId);
        return "Company deleted successfully";
    }

    /**
     * Follow end points start from here
     *
     */

    public String followCompany(UUID companyId , UUID userId) {
        if(!companyRepository.existsById(companyId)){
            throw new CompanyException("Company not fount this provided ID: " + companyId);
        }

        Optional<Follower> follower = followerRepository.findByFollowerIdAndCompanyId(userId, companyId);

        if(follower.isPresent()) {
            followerRepository.delete(follower.get());
            return "Company Unfollowed successfully.";
        }
        else {
            Follower newFollower = new Follower();
            newFollower.setFollowerId(userId);
            newFollower.setCompanyId(companyId);
            newFollower.setFollowedAt(LocalDateTime.now());

            followerRepository.save(newFollower);

            return "Company followed successfully.";
        }
    }

    public Page<Map<String, Object>> getAllFollowers(UUID companyId, int page, int size) {
        if(!companyRepository.existsById(companyId)){
            throw new CompanyException("Company not fount this provided ID: " + companyId);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Follower> followers = followerRepository.findByCompanyId(companyId, pageable);

        List<UUID> userIds = followers
                .getContent()
                .stream()
                .map(Follower::getFollowerId)
                .toList();

        List<Map<String, Object>> users =
                userServiceClient.getUsers(userIds);

        return new PageImpl<>(
                users,
                pageable,
                followers.getTotalElements()
        );
    }

    /**
     * Employee end points start from here
     */

    public String addEmployeesToCompany(UUID companyId, AddEmployeeRequest request) {
        return null;
    }

    public List<Map<String, Object>> getAllEmployees(UUID userId, UUID companyId) {
        return null;
    }

    public Map<String, Object> getEmployeeOfACompany(UUID companyId, UUID requesterId, UUID employeeId) {
        return null;
    }

    public String deleteEmployeeOfACompany(UUID companyId, UUID requesterId, UUID authorId) {
        return null;
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .administratorId(company.getAdministratorId())
                .name(company.getName())
                .description(company.getDescription())
                .logo(company.getLogo())
                .banner(company.getBanner())
                .overview(company.getOverview())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .followersCount(company.getFollowersCount())
                .employeesCount(company.getEmployeesCount())
                .companyType(company.getType())
                .createdAt(company.getCreatedAt())
                .build();
    }
}
