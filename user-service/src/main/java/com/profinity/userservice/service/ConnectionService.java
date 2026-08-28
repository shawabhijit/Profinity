package com.profinity.userservice.service;

import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.repository.ConnectionRepository;
import com.profinity.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;

    public String sendConnectionRequest(UUID userId, UUID targetUserId) {
        return null;
    }

    public String acceptConnectionRequest(UUID userId, UUID targetUserId) {
        return null;
    }

    public String rejectConnectionRequest(UUID userId, UUID targetUserId) {
        return null;
    }

    public List<UserResponse> getPendingRequests(UUID userId) {
        return null;
    }

    public List<UserResponse> getConnections(UUID userId) {
        return null;
    }

}
