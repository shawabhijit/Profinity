package com.profinity.userservice.service;

import com.profinity.userservice.dto.ConnectionResponse;
import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.entity.Connection;
import com.profinity.userservice.entity.enums.ConnectionStatus;
import com.profinity.userservice.kafka.ConnectionEventProducer;
import com.profinity.userservice.exceptions.ConnectionException;
import com.profinity.userservice.repository.ConnectionRepository;
import com.profinity.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.profinity.userservice.service.UserService.userToUserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionEventProducer connectionEventProducer;

    public ConnectionResponse sendConnectionRequest(UUID userId, UUID targetUserId) {

        if (connectionRepository.existsByRequesterIdAndReceiverId(userId, targetUserId)) {
            throw new IllegalArgumentException(
                    "Connection request already sent to this user");
        }

        Connection newConnection = Connection.builder()
                .requesterId(userId)
                .receiverId(targetUserId)
                .status(ConnectionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        newConnection = connectionRepository.save(newConnection);

        // producing kafka event
        connectionEventProducer.sendConnectionRequestEvent(newConnection);
        log.info("Connection request sent: {} -> {}" , userId, targetUserId);

        return new ConnectionResponse(
                "Connection request sent Successfully.",
                newConnection.getStatus().toString());
    }

    public ConnectionResponse acceptConnectionRequest(UUID connectionId) {

        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with this id: " + connectionId));

        if(connection.getStatus() != ConnectionStatus.PENDING) {
            throw new ConnectionException("Connection request is already accepted or rejected.");
        }
        connection.setStatus(ConnectionStatus.CONNECTED);
        connection = connectionRepository.save(connection);

        connectionEventProducer.sendConnectionAcceptedEvent(connection);
        log.info("Connection accepted: {} -> {}", connection.getRequesterId(), connection.getReceiverId());

        return new ConnectionResponse(
                "Connection accepted successfully.",
                connection.getStatus().toString()
        );
    }

    public ConnectionResponse rejectConnectionRequest(UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found with this id: " + connectionId));

        if(connection.getStatus() != ConnectionStatus.PENDING) {
            throw new ConnectionException("Connection request is already accepted or rejected.");
        }

        connection.setStatus(ConnectionStatus.REJECTED);
        connection = connectionRepository.save(connection);

        connectionEventProducer.sendConnectionRejectedEvent(connection);

        return new ConnectionResponse(
                "Connection rejected successfully.",
                connection.getStatus().toString()
        );
    }

    public List<UserResponse> getPendingRequests(UUID userId) {
        List<Connection> pendingConnections = connectionRepository.findByRequesterIdAndStatus(userId, ConnectionStatus.PENDING);
        return pendingConnections.stream()
                .map(connection -> userToUserResponse(userRepository.findById(connection.getReceiverId()).orElseThrow(
                        () -> new IllegalArgumentException("User not found with this id: " + connection.getReceiverId())
                )))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getConnections(UUID userId) {
        List<Connection> pendingConnections = connectionRepository.findByRequesterIdAndStatus(userId, ConnectionStatus.CONNECTED);
        return pendingConnections.stream()
                .map(connection -> userToUserResponse(userRepository.findById(connection.getReceiverId()).orElseThrow(
                        () -> new IllegalArgumentException("User not found with this id: " + connection.getReceiverId())
                )))
                .collect(Collectors.toList());
    }

}
