package com.profinity.companyservice.controller;

import com.profinity.companyservice.dto.CompanyRequest;
import com.profinity.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createCompany(
            @PathVariable UUID authorId,
            @RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.ok().build();
    }

    /**
     * Updates a company profile
     * @param authorId should be match with existing administrator id
     * @param companyId
     * @param companyRequest
     * @return
     */
    @PutMapping("/{authorId}/update")
    public ResponseEntity<?> updateCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId,
            @RequestBody CompanyRequest companyRequest
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCompany(
            @PathVariable UUID userId,
            @RequestParam UUID companyId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{authorId}/delete")
    public ResponseEntity<?> deleteCompany(
            @PathVariable UUID authorId,
            @RequestParam UUID companyId
    ) {
        return ResponseEntity.ok().build();
    }
}
