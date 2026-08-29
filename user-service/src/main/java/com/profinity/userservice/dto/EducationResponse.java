package com.profinity.userservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationResponse {
    private UUID id;
    private String school;
    private String degree;
    private String fieldOfStudy;
    private String startDate;
    private String endDate;
    private boolean currentlyStudying;
    private String grade;
    private String description;
}
