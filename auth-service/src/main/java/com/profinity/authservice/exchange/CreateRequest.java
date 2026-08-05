package com.profinity.authservice.exchange;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequest {
    @NotNull(message = "User name should not be Null")
    private String username;
    @NotNull(message = "Email should not be null")
    @Email(message = "Email should be a valid email address")
    private String email;
    @NotNull(message = "Password should not be null.")
    @Size(min = 8 , max = 20 ,
            message = "Password should have minimum 8 characters and maximum 20 characters.")
    private String password;

}
