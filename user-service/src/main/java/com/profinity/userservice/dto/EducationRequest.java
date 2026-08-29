package com.profinity.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationRequest {
    @NotNull(message = "School name cannot be null")
    private String school;
    @NotNull(message = "Degree cannot be null")
    private String degree;
    @NotNull(message = "Field of study cannot be null")
    private String fieldOfStudy;
    private String startDate;
    private String endDate;
    @NotNull(message = "Currently studying cannot be null")
    private boolean currentlyStudying;
    @NotNull(message = "Grade cannot be null")
    private String grade;
    private String description;
}
