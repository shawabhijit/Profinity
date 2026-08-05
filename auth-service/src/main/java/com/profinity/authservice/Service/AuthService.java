package com.profinity.authservice.Service;

import com.profinity.authservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
