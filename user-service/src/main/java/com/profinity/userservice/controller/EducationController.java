package com.profinity.userservice.controller;

import com.profinity.userservice.dto.EducationRequest;
import com.profinity.userservice.dto.EducationResponse;
import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping("/{educationId}")
    public ResponseEntity<EducationResponse> getUserEducationByEducationId(
            @PathVariable UUID educationId,
            @RequestHeader UUID userId
    ) {
        return ResponseEntity.ok().body(educationService.getUserEducationByEducationId(educationId, userId));
    }

    @GetMapping
    public ResponseEntity<List<EducationResponse>> getUserAllEducations(@RequestHeader UUID userId) {
        return ResponseEntity.ok().body(educationService.getUserAllEducations(userId));
    }

    @PostMapping("{userId}")
    public ResponseEntity<EducationResponse> createUserEducation(
            @PathVariable UUID userId,
            @RequestBody EducationRequest educationRequest
    ) {
        return ResponseEntity.ok().body(educationService.createUserEducation(userId, educationRequest));
    }

    @PutMapping("/update/{educationId}")
    public ResponseEntity<EducationResponse> updateUserEducation(
            @PathVariable UUID educationId,
            @RequestHeader UUID userId,
            @RequestBody EducationRequest educationRequest
    ) {
        return ResponseEntity.ok().body(educationService.updateUserEducation(educationId, userId, educationRequest));
    }

    @DeleteMapping("/delete/{educationId}")
    public ResponseEntity<String> deleteUserEducation(
            @PathVariable UUID educationId,
            @RequestHeader UUID userId
    ) {
        return ResponseEntity.ok().body(educationService.deleteUserEducation(educationId,userId));
    }
}
