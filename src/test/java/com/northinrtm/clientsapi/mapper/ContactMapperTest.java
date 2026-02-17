package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ContactCreateRequest;
import com.northinrtm.clientsapi.dto.ContactDto;
import com.northinrtm.clientsapi.dto.ContactUpdateRequest;
import com.northinrtm.clientsapi.entity.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ContactMapperTest {
    ContactMapper contactMapper;

    @BeforeEach
    void setUp() {
        contactMapper = new ContactMapperImpl();
    }

    @Test
    void toEntity_mapsFields_andIgnoresId() {
        ContactCreateRequest req = new ContactCreateRequest("+7999", "a@b.com");

        Contact contact = contactMapper.toEntity(req);

        assertNotNull(contact);
        assertNull(contact.getId());
        assertEquals("+7999", contact.getPhone());
        assertEquals("a@b.com", contact.getEmail());
    }

    @Test
    void toDto_mapsFields() {
        Contact entity = new Contact();
        entity.setPhone("+7000");
        entity.setEmail("x@y.com");

        ContactDto dto = contactMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("+7000", dto.phone());
        assertEquals("x@y.com", dto.email());
    }

    @Test
    void update_updatesFields() {
        Contact entity = new Contact();
        entity.setPhone("old");
        entity.setEmail("old@old.com");

        ContactUpdateRequest req = new ContactUpdateRequest("+7000", "new@b.com");

        contactMapper.update(req, entity);

        assertEquals("+7000", entity.getPhone());
        assertEquals("new@b.com", entity.getEmail());
    }

    @Test
    void update_doesNothing_whenRequestNull() {
        Contact entity = new Contact();
        entity.setPhone("old");
        entity.setEmail("old@old.com");

        contactMapper.update(null, entity);

        assertEquals("old", entity.getPhone());
        assertEquals("old@old.com", entity.getEmail());
    }
}