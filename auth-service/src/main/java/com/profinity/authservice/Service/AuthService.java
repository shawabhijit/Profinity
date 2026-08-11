package com.profinity.authservice.Service;

import com.profinity.authservice.Config.JwtUtil;
import com.profinity.authservice.Entity.User;
import com.profinity.authservice.Entity.enums.AuthProvider;
import com.profinity.authservice.Repository.UserRepository;
import com.profinity.authservice.exchange.CreateRequest;
import com.profinity.authservice.exchange.LoginRequest;
import com.profinity.authservice.exchange.LoginResponse;
import com.profinity.authservice.exchange.SignupResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponse signUp(CreateRequest request , HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            throw new RuntimeException("User already exists");
        }

        user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .provider(AuthProvider.EMAIL)
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);
        log.info("User created: {}", user);

        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        log.info("Token generated: {}", token);

        saveToCookie(refreshToken, response);

        return SignupResponse.builder()
                .email(user.getEmail())
                .userName(user.getUsername())
                .token(token)
                .build();
    }

    public LoginResponse login(LoginRequest request , HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        if(user == null) {
            log.error("User is null");
            throw new BadCredentialsException("User not found");
        }

        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        saveToCookie(refreshToken, response);

        return new LoginResponse(token, "success");
    }

    public LoginResponse refresh(String refreshToken, HttpServletResponse response) {
        if(refreshToken == null) {
            throw new BadCredentialsException("Refresh token is null");
        }
        if(!validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh token is invalid");
        }

        Claims claims = jwtUtil.extractClaims(refreshToken);

        User user = userRepository.findByEmail(claims.getSubject()).orElseThrow(
                () -> new BadCredentialsException("User not found")
        );

        String newToken = jwtUtil.generateToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        saveToCookie(newRefreshToken, response);

        return new LoginResponse(newToken, "success");
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }



    private void saveToCookie(String refreshToken, HttpServletResponse response) {
        log.info("Inside saveToCookie method");
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict") // or "Lax"/"None" depending on your frontend
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        log.info("Cookie set successfully");
    }
}
