package com.profinity.userservice.service;

import com.profinity.userservice.dto.UpdateUserRequest;
import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.entity.User;
import com.profinity.userservice.event.UserCreatedEvent;
import com.profinity.userservice.kafka.UserEventProducer;
import com.profinity.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;


    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserService::userToUserResponse).toList();
    }

    public List<UserResponse> getUsersByIds(List<UUID> userIds) {

        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        return StreamSupport
                .stream(userRepository.findAllById(userIds).spliterator(), false)
                .map(UserService::userToUserResponse)
                .toList();
    }

    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );
        return userToUserResponse(user);
    }

    public UserResponse getUserProfile(UUID userId, UUID targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + targetUserId)
        );

        return userToUserResponse(targetUser);
    }

    public void createUser(UserCreatedEvent userCreatedEvent) {
        if(userRepository.existsByAuthUserId(userCreatedEvent.authUserId())) {
            throw new IllegalArgumentException("User already exists with this authUserId: " + userCreatedEvent.authUserId());
        }

        User user = User.builder()
                .authUserId(userCreatedEvent.authUserId())
                .email(userCreatedEvent.email())
                .username(userCreatedEvent.username())
                .createdAt(userCreatedEvent.createdAt())
                .build();

        user = userRepository.save(user);

        // public user created event
        // this event should consume by search service
        userEventProducer.sendUserCreatedEvent(user);
        log.info("user created event published: {}" , user.getId());
    }

    public UserResponse updateUserInfo(UUID userId, UpdateUserRequest userRequest) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        // Update mutable fields
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setHeadline(userRequest.getHeadline());
        existingUser.setAbout(userRequest.getAbout());
        existingUser.setProfileUrl(userRequest.getProfileUrl());
        existingUser.setCoverUrl(userRequest.getCoverUrl());
        existingUser.setSkills(userRequest.getSkills());

        // Save updated user
        existingUser = userRepository.save(existingUser);

        // public user updated event
        // this event should consume by search service
        userEventProducer.sendUserUpdatedEvent(existingUser);
        log.info("user updated event published: {}" , existingUser.getId());

        return userToUserResponse(existingUser);
    }

    public String deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        return "User deleted successfully.";
    }

    public static UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .headline(user.getHeadline())
                .about(user.getAbout())
                .profileUrl(user.getProfileUrl())
                .coverUrl(user.getCoverUrl())
                .skills(user.getSkills())
                .educations(user.getEducations())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
