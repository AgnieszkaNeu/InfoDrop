package com.example.demo.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AppUserDto(
        @NotBlank(message = "Email must not be empty") @Email(message = "Invalid mail") String email,
        @NotBlank(message = "Password must not be empty") String password
) {
}
