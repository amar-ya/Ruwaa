package org.example.ruwaa.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerRequest
{
    @Email
    @NotEmpty(message = "email is required")
    private String email;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
    private String phone_number;
}
