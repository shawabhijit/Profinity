package com.profinity.userservice.controller;

import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> sendConnectionRequest(
            @PathVariable UUID userId,
            @RequestHeader UUID targetUserId) {
        return ResponseEntity.ok().body(connectionService.sendConnectionRequest(userId, targetUserId));
    }

    @PutMapping("/accept-request/{userId}")
    public ResponseEntity<?> acceptConnectionRequest(
            @PathVariable UUID userId,
            @RequestHeader UUID targetUserId
    ) {
        return ResponseEntity.ok().body(connectionService.acceptConnectionRequest(userId, targetUserId));
    }

    @PutMapping("/reject-request/{userId}")
    public ResponseEntity<?> rejectConnectionRequest(
            @PathVariable UUID userId,
            @RequestHeader UUID targetUserId
    ) {
        return ResponseEntity.ok().body(connectionService.rejectConnectionRequest(userId, targetUserId));
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<UserResponse>> getConnections(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok().body(connectionService.getConnections(userId));
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<UserResponse>> getPendingRequests(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok().body(connectionService.getPendingRequests(userId));
    }
}
