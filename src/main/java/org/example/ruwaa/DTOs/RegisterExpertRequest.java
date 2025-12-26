package org.example.ruwaa.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterExpertRequest
{
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
    @Email
    @NotEmpty(message = "email is required")
    private String email;
    private String linkedin_url;
    private String phone_number;
    @NotEmpty(message = "name is required")
    private String name;
    @Pattern(regexp = "^(?i)(sport|art|science|business|media|law|trading)$", message = "invalid category choice")
    @NotEmpty(message = "category is required")
    private String category;
}
