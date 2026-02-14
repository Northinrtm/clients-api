package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ContactCreateRequest;
import com.northinrtm.clientsapi.dto.ContactDto;
import com.northinrtm.clientsapi.dto.ContactUpdateRequest;
import com.northinrtm.clientsapi.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactDto toDto(Contact entity);

    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void update(ContactUpdateRequest request, @MappingTarget Contact entity);
}
