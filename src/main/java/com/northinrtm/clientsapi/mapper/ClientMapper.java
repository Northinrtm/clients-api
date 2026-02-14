package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = ContactMapper.class)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientCreateRequest request);

    ClientDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    void update(ClientUpdateRequest request, @MappingTarget Client client);
}
