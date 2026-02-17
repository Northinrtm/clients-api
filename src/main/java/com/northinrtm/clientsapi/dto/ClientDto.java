package com.northinrtm.clientsapi.dto;

public record ClientDto(
        Long id,
        String name,
        String lastName,
        ContactDto contact
) {
}
