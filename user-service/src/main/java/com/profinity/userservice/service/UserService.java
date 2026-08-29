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

import java.util.List;
import java.util.UUID;

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

    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );
        return userToUserResponse(user);
    }

    public UserResponse getUserProfile(UUID userId, UUID targetUserId) {
        return null;
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

        userRepository.save(user);
    }

    public UserResponse updateUserInfo(UUID userId, UpdateUserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with this id: " + userId)
        );

        user = User.builder()
                .username(userRequest.getUsername())
                .headline(userRequest.getHeadline())
                .about(userRequest.getAbout())
                .profileUrl(userRequest.getProfileUrl())
                .coverUrl(userRequest.getCoverUrl())
                .skills(userRequest.getSkills())
                .build();

        user = userRepository.save(user);

        // public user updated event
        // this event should consume by search service
        userEventProducer.sendUserUpdatedEvent(user);
        log.info("user updated event published: {}" , user.getId());

        return userToUserResponse(user);
    }

    public String deleteUser(UUID userId) {
        return null;
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
