package com.profinity.userservice.event;

import com.profinity.userservice.entity.Education;

import java.util.List;
import java.util.UUID;

public record UserEventRecord (
    UUID userId,
    String username,
    String headline,
    String location,
    List<String> skills,
    List<EducationEvent> educations
) {
}
