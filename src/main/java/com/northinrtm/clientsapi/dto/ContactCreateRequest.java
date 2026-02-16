package com.northinrtm.clientsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactCreateRequest(
        @NotBlank @Pattern(regexp = "\\+?[0-9\\s-]{7,32}") @Size(max = 32) String phone,
        @NotBlank @Email @Size(max = 100) String email
) {
}