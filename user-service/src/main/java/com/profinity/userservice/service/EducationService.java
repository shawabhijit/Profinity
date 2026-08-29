package com.profinity.userservice.service;

import com.profinity.userservice.dto.EducationRequest;
import com.profinity.userservice.dto.EducationResponse;
import com.profinity.userservice.entity.Education;
import com.profinity.userservice.entity.User;
import com.profinity.userservice.kafka.UserEventProducer;
import com.profinity.userservice.repository.EducationRepository;
import com.profinity.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationService {

    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final UserEventProducer userEventProducer;

    public EducationResponse getUserEducationByEducationId(UUID educationId, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );
        Education education = educationRepository.findByIdAndUser(educationId, user);
        return mapToEducationResponse(education);
    }

    public List<EducationResponse> getUserAllEducations(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );
        return user.getEducations().stream()
                .map(this::mapToEducationResponse).toList();
    }

    @Transactional
    public EducationResponse createUserEducation(UUID userId, EducationRequest educationRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        Education education = Education.builder()
                .school(educationRequest.getSchool())
                .degree(educationRequest.getDegree())
                .fieldOfStudy(educationRequest.getFieldOfStudy())
                .startDate(educationRequest.getStartDate())
                .endDate(educationRequest.getEndDate())
                .currentlyStudying(educationRequest.isCurrentlyStudying())
                .grade(educationRequest.getGrade())
                .description(educationRequest.getDescription())
                .user(user)
                .build();

        user.getEducations().add(education);

        education = educationRepository.save(education);

        userEventProducer.sendUserUpdatedEvent(user);
        log.info("user updated event published: {}" , user.getId());

        return mapToEducationResponse(education);
    }

    @Transactional
    public EducationResponse updateUserEducation(UUID educationId, UUID userId , EducationRequest educationRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        Education existingEducation = educationRepository.findById(educationId).orElseThrow(
                () -> new IllegalArgumentException("Education not found with this id: " + educationId)
        );

        if(!existingEducation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Education does not belong to this user.");
        }

        existingEducation.setSchool(educationRequest.getSchool());
        existingEducation.setDegree(educationRequest.getDegree());
        existingEducation.setFieldOfStudy(educationRequest.getFieldOfStudy());
        existingEducation.setStartDate(educationRequest.getStartDate());
        existingEducation.setEndDate(educationRequest.getEndDate());
        existingEducation.setCurrentlyStudying(educationRequest.isCurrentlyStudying());
        existingEducation.setGrade(educationRequest.getGrade());
        existingEducation.setDescription(educationRequest.getDescription());

        userEventProducer.sendUserUpdatedEvent(user);
        log.info("user updated event published: {}" , user.getId());

        return mapToEducationResponse(existingEducation);
    }

    @Transactional
    public String deleteUserEducation(UUID educationId, UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        Education existingEducation = educationRepository.findById(educationId).orElseThrow(
                () -> new IllegalArgumentException("Education not found with this id: " + educationId)
        );

        if(!existingEducation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Education does not belong to this user.");
        }

        educationRepository.deleteById(educationId);

        userEventProducer.sendUserUpdatedEvent(user);
        log.info("user updated event published: {}" , user.getId());

        return "Education deleted successfully.";
    }

    private EducationResponse mapToEducationResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .school(education.getSchool())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .currentlyStudying(education.isCurrentlyStudying())
                .grade(education.getGrade())
                .description(education.getDescription())
                .build();
    }
}
