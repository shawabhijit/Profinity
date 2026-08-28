package com.profinity.userservice.service;

import com.profinity.userservice.dto.UpdateUserRequest;
import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return null;
    }

    public UserResponse getUserById(UUID userId) {
        return null;
    }

    public UserResponse getUserProfile(UUID userId, UUID targetUserId) {
        return null;
    }

    public UserResponse updateUserInfo(UUID userId, UpdateUserRequest userRequest) {
        return null;
    }

    public String deleteUser(UUID userId) {
        return null;
    }
}
