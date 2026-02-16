package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.entity.Contact;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public abstract class ClientMapper {

    @Autowired
    protected ContactMapper contactMapperDelegate;

    @Mapping(target = "id", ignore = true)
    public abstract Client toEntity(ClientCreateRequest request);

    public abstract ClientDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    public abstract void update(ClientUpdateRequest request, @MappingTarget Client client);

    @AfterMapping
    protected void afterUpdateContact(ClientUpdateRequest request, @MappingTarget Client client) {
        if (request == null || request.contact() == null) return;

        if (client.getContact() == null) {
            client.setContact(new Contact());
        }
        contactMapperDelegate.update(request.contact(), client.getContact());
    }
}
