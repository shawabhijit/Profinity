package com.profinity.authservice.Controller;

import com.profinity.authservice.Service.AuthService;
import com.profinity.authservice.exchange.CreateRequest;
import com.profinity.authservice.exchange.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> createAccount(@RequestBody @Validated CreateRequest createRequest) {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest loginRequest) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "Validate token")
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().body("Auth Validation Success.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
