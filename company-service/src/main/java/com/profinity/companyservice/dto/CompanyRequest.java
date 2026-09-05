package com.profinity.companyservice.dto;

import com.profinity.companyservice.entity.enums.CompanyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {
    @NotNull(message = "Name should be not null.")
    private String name;
    @NotNull(message = "Description should be not null.")
    private String description;
    private String logo;
    private String banner;
    private String overview;
    private String website;
    @NotNull(message = "Industry should be specified.")
    private String industry;
    private String address;
    @NotNull(message = "Company type should be specified.")
    private CompanyType companyType;
}
