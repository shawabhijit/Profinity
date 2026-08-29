package com.profinity.userservice.dto;

import com.profinity.userservice.entity.Education;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotNull( message = "Username cannot be null")
    private String username;
    @NotNull( message = "Username cannot be null")
    private String headline;
    @NotNull( message = "Username cannot be null")
    private String about;
    private String location;
    private String profileUrl;
    private String coverUrl;
    private List<String> skills;
}
