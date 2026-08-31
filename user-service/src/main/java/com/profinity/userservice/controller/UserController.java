package com.profinity.userservice.controller;

import com.profinity.userservice.dto.UpdateUserRequest;
import com.profinity.userservice.dto.UserResponse;
import com.profinity.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable UUID userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @GetMapping("{userId}/profile")
    public ResponseEntity<UserResponse> getUserProfile(
            @PathVariable UUID userId,
            @RequestHeader UUID targetUserId
    ) {
        return ResponseEntity.ok().body(userService.getUserProfile(userId, targetUserId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable UUID userId,
            @RequestHeader UUID requestingUserId,
            @RequestBody UpdateUserRequest userRequest) {

        if (!userId.equals(requestingUserId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(userService.updateUserInfo(userId, userRequest));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable  UUID userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }
}
