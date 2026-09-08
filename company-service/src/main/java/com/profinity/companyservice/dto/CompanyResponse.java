package com.profinity.companyservice.dto;

import com.profinity.companyservice.entity.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {
    private UUID id;
    private UUID administratorId;
    private String name;
    private String description;
    private String logo;
    private String banner;
    private String overview;
    private String website;
    private String industry;
    private long followersCount;
    private long employeesCount;
    private String address;
    private CompanyType companyType;
    private LocalDateTime createdAt;
}
