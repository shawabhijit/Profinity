package com.profinity.authservice.Controller;

import com.profinity.authservice.Service.AuthService;
import com.profinity.authservice.exchange.CreateRequest;
import com.profinity.authservice.exchange.LoginRequest;
import com.profinity.authservice.exchange.LoginResponse;
import com.profinity.authservice.exchange.SignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
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

    @Operation(summary = "Create new account", description = "Creates new account, " +
            "accept username, email and password in the request body.")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createAccount(@RequestBody @Validated CreateRequest createRequest,
                                                        HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.signUp(createRequest, response));
    }

    @Operation(summary = "Login to existing account" , description = "Login to existing account using email & password.")
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest,HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.login(loginRequest, response));
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

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }
}
