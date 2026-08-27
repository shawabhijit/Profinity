package com.profinity.userservice.dto;

import com.profinity.userservice.entity.Education;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String headline;
    private String about;
    private String location;
    private String profileUrl;
    private String coverUrl;
    private List<String> skills;
    private List<Education> educations;
    private LocalDateTime createdAt;
}
