package com.northinrtm.clientsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactCreateRequest(
        @NotBlank @Size(max = 32) String phone,
        @NotBlank @Email @Size(max = 100) String email
) {
}
