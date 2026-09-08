package com.profinity.companyservice.event;

import com.profinity.companyservice.entity.enums.CompanyType;

public record CompanyCreatedRecord (
        String name,
        String description,
        String overview,
        String website,
        String industry,
        String address,
        CompanyType companyType
     ) {
}
