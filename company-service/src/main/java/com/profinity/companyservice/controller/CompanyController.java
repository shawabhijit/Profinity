package com.profinity.companyservice.controller;

import com.profinity.companyservice.dto.AddEmployeeRequest;
import com.profinity.companyservice.dto.CompanyRequest;
import com.profinity.companyservice.dto.CompanyResponse;
import com.profinity.companyservice.entity.enums.CompanyType;
import com.profinity.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Creates a company, for now proceeding with only one authorised administrator
     * should publish a company.created event to kafka topic
     *
     * @param authorId should save as administrator
     * @param companyRequest
     * @return
     */
    @PostMapping("/{authorId}/create")
    public ResponseEntity<CompanyResponse> createCompany(
            @PathVariable UUID authorId,
            @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(authorId, companyRequest));
    }

    /**
     * Updates a company profile
     * @param authorId should be match with existing administrator id
     * @param companyId
     * @param companyRequest
     * @return
     */
    @PutMapping("/{authorId}/update")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId,
            @RequestBody CompanyRequest companyRequest
    ) {
        return ResponseEntity.ok().body(companyService.updateCompany(authorId, companyId, companyRequest));
    }

    @PatchMapping("/{authorId}/update/logo")
    public ResponseEntity<String> updateLogoOfCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId,
            @RequestBody MultipartFile file
    ) {
        return ResponseEntity.ok().body(companyService.updateLogo(authorId, companyId, file));
    }

    @PatchMapping("/{authorId}/update/banner")
    public ResponseEntity<String> updateBannerOfCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId,
            @RequestBody MultipartFile file
    ) {
        return ResponseEntity.ok().body(companyService.updateBanner(authorId, companyId, file));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok().body(companyService.getAllCompanies(userId));
    }

    @GetMapping("/industry/{industry}")
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesByIndustry(
            @PathVariable String industry,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(companyService.getAllCompaniesByIndustry(industry, page, 10));
    }

    @GetMapping("/type/{companyType}")
    public ResponseEntity<List<CompanyResponse>> getAllCompaniesByType(
            @PathVariable CompanyType companyType,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(companyService.findAllCompaniesByType(companyType, page, 10));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompany(
            @RequestParam UUID userId,
            @PathVariable UUID companyId
    ) {
        return ResponseEntity.ok().body(companyService.getCompany(userId, companyId));
    }

    @DeleteMapping("/{authorId}/delete")
    public ResponseEntity<String> deleteCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId
    ) {
        return ResponseEntity.ok().body(companyService.deleteCompany(authorId, companyId));
    }

    /**
     * Follow end points start from here
     */
    @PostMapping("/{companyId}/follow")
    public ResponseEntity<String> followCompany(
            @PathVariable UUID companyId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok().body(companyService.followCompany(companyId, userId));
    }

    @GetMapping("/{companyId}/followers")
    public ResponseEntity<Page<Map<String, Object>>> getFollowers(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(companyService.getAllFollowers(companyId,page,20));
    }

    @DeleteMapping("/{companyId}/unfollow")
    public ResponseEntity<String> unfollowCompany(
            @PathVariable UUID companyId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok().body(companyService.followCompany(companyId, userId));
    }

    /**
     * Employee end points start from here
     */

    /**
     * Supports adding multiple users in one go
     * Validate user ids with user service
     * create employee records
     *
     * @param companyId
     * @param request
     * @return
     */
    @PostMapping("/{companyId}/employees")
    public ResponseEntity<String> addEmployeeToCompany(
            @PathVariable UUID companyId,
            @RequestBody AddEmployeeRequest request
    ) {
        return ResponseEntity.ok().body(companyService.addEmployeesToCompany(companyId, request));
    }

    /**
     * found list of user ids then call user-service to get he user details
     * return needed details from company-service
     *
     * @param companyId
     * @return
     */
    @GetMapping("/{companyId}/employees")
    public ResponseEntity<Page<Map<String, Object>>> getAllEmployeesOfACompany(
            @PathVariable UUID companyId,
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok().body(companyService.getAllEmployees(companyId, userId, page, 20));
    }


    @DeleteMapping("/{companyId}/employees/{requesterId}")
    public ResponseEntity<String> deleteEmployeeFromCompany(
            @PathVariable UUID companyId,
            @PathVariable UUID requesterId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok().body(companyService.deleteEmployeeOfACompany(companyId, requesterId, userId));
    }
}
