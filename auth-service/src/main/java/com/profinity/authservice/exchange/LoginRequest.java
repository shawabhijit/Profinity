package com.profinity.authservice.exchange;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Email should not be null")
    @Email(message = "Email should be a valid email address")
    private String email;
    @NotNull(message = "Password should not be null.")
    private String password;
}
