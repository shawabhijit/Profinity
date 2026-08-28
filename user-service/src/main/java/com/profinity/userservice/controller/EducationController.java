package com.profinity.userservice.controller;

import com.profinity.userservice.dto.EducationRequest;
import com.profinity.userservice.dto.EducationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/education")
public class EducationController {

    @GetMapping("/{educationId}")
    public ResponseEntity<EducationResponse> getUserEducation(
            @PathVariable UUID educationId,
            @RequestHeader UUID userId
    ) {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping
    public ResponseEntity<EducationResponse> createUserEducation(
            @RequestBody EducationRequest educationRequest
    ) {
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/update/{educationId}")
    public ResponseEntity<EducationResponse> updateUserEducation(
            @PathVariable UUID educationId,
            @RequestBody EducationRequest educationRequest
    ) {
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/delete/{educationId}")
    public ResponseEntity<EducationResponse> deleteUserEducation(
            @PathVariable UUID educationId,
            @RequestHeader UUID userId
    ) {
        return ResponseEntity.ok().body(null);
    }

}
