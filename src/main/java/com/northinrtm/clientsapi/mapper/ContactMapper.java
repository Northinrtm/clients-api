package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ContactCreateRequest;
import com.northinrtm.clientsapi.dto.ContactDto;
import com.northinrtm.clientsapi.dto.ContactUpdateRequest;
import com.northinrtm.clientsapi.entity.Contact;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "id", source = "id")
    ContactDto toDto(Contact entity);

    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(ContactUpdateRequest request, @MappingTarget Contact entity);
}