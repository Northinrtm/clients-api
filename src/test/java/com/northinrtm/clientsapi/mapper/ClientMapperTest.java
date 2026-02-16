package com.northinrtm.clientsapi.mapper;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.dto.ContactCreateRequest;
import com.northinrtm.clientsapi.dto.ContactUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.entity.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientMapperTest {
    ClientMapperImpl clientMapper;
    ContactMapper contactMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapperImpl();
        contactMapper = new ContactMapperImpl();

        // поле в ClientMapperImpl (используется в toEntity/toDto)
        inject(clientMapper, ClientMapperImpl.class, "contactMapper", contactMapper);
        // поле в abstract ClientMapper (используется в afterUpdateContact)
        inject(clientMapper, ClientMapper.class, "contactMapperDelegate", contactMapper);
    }

    @Test
    void toEntity_mapsClientAndContact_andIgnoresIds() {
        ClientCreateRequest req = new ClientCreateRequest(
                "John",
                "Doe",
                new ContactCreateRequest("+79991234567", "a@b.com")
        );

        Client entity = clientMapper.toEntity(req);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("John", entity.getName());
        assertEquals("Doe", entity.getLastName());

        assertNotNull(entity.getContact());
        assertNull(entity.getContact().getId());
        assertEquals("+79991234567", entity.getContact().getPhone());
        assertEquals("a@b.com", entity.getContact().getEmail());
    }

    @Test
    void toDto_mapsClientAndContact() {
        Client client = new Client();
        client.setName("John");
        client.setLastName("Doe");

        Contact contact = new Contact();
        contact.setPhone("+7999");
        contact.setEmail("a@b.com");
        client.setContact(contact);

        ClientDto dto = clientMapper.toDto(client);

        assertNotNull(dto);
        assertEquals("John", dto.name());
        assertEquals("Doe", dto.lastName());

        assertNotNull(dto.contact());
        assertEquals("+7999", dto.contact().phone());
        assertEquals("a@b.com", dto.contact().email());
    }

    @Test
    void update_updatesNameLastName_andUpdatesExistingContact() {
        Client client = new Client();
        client.setName("Old");
        client.setLastName("OldLast");

        Contact contact = new Contact();
        contact.setPhone("oldPhone");
        contact.setEmail("old@old.com");
        client.setContact(contact);

        ClientUpdateRequest req = new ClientUpdateRequest(
                "New",
                "NewLast",
                new ContactUpdateRequest("+7000", "new@b.com")
        );

        clientMapper.update(req, client);

        assertEquals("New", client.getName());
        assertEquals("NewLast", client.getLastName());

        assertNotNull(client.getContact());
        assertEquals("+7000", client.getContact().getPhone());
        assertEquals("new@b.com", client.getContact().getEmail());
    }

    @Test
    void update_createsContact_whenClientContactIsNull() {
        Client client = new Client();
        client.setName("Old");
        client.setLastName("OldLast");
        client.setContact(null);

        ClientUpdateRequest req = new ClientUpdateRequest(
                "New",
                "NewLast",
                new ContactUpdateRequest("+7000", "new@b.com")
        );

        clientMapper.update(req, client);

        assertEquals("New", client.getName());
        assertEquals("NewLast", client.getLastName());

        assertNotNull(client.getContact());
        assertEquals("+7000", client.getContact().getPhone());
        assertEquals("new@b.com", client.getContact().getEmail());
    }

    @Test
    void update_doesNotTouchContact_whenRequestContactIsNull() {
        Client client = new Client();
        client.setName("Old");
        client.setLastName("OldLast");

        Contact contact = new Contact();
        contact.setPhone("oldPhone");
        contact.setEmail("old@old.com");
        client.setContact(contact);

        ClientUpdateRequest req = new ClientUpdateRequest("New", "NewLast", null);

        clientMapper.update(req, client);

        assertEquals("New", client.getName());
        assertEquals("NewLast", client.getLastName());

        assertNotNull(client.getContact());
        assertEquals("oldPhone", client.getContact().getPhone());
        assertEquals("old@old.com", client.getContact().getEmail());
    }

    @Test
    void update_doesNothing_whenRequestIsNull() {
        Client client = new Client();
        client.setName("Old");
        client.setLastName("OldLast");

        clientMapper.update(null, client);

        assertEquals("Old", client.getName());
        assertEquals("OldLast", client.getLastName());
    }

    private static void inject(Object target, Class<?> owner, String fieldName, Object value) {
        try {
            Field f = owner.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot inject " + owner.getSimpleName() + "." + fieldName, e);
        }
    }
}