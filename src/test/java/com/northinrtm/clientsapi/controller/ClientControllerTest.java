package com.northinrtm.clientsapi.controller;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ClientControllerTest {
    ClientService clientService;
    ClientController controller;

    ClientCreateRequest createRequest;
    ClientUpdateRequest updateRequest;

    ClientDto createdDto;
    ClientDto dtoById;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientService.class);
        controller = new ClientController(clientService);

        createRequest = mock(ClientCreateRequest.class);
        updateRequest = mock(ClientUpdateRequest.class);

        createdDto = new ClientDto(10L, "John", "Doe", null);
        dtoById = new ClientDto(1L, "A", "B", null);
    }

    @Test
    void create_returns201_andLocationHeader() {
        when(clientService.create(createRequest)).thenReturn(createdDto);

        var response = controller.create(createRequest);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(createdDto, response.getBody());
        assertEquals(URI.create("/api/clients/10"), response.getHeaders().getLocation());

        verify(clientService).create(createRequest);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void getById_delegatesToService() {
        when(clientService.getById(1L)).thenReturn(dtoById);

        ClientDto result = controller.getById(1L);

        assertEquals(dtoById, result);
        verify(clientService).getById(1L);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void update_delegatesToService() {
        when(clientService.update(1L, updateRequest)).thenReturn(dtoById);

        ClientDto result = controller.update(1L, updateRequest);

        assertEquals(dtoById, result);
        verify(clientService).update(1L, updateRequest);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void delete_delegatesToService() {
        controller.delete(5L);

        verify(clientService).delete(5L);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void search_prefersEmail_overPhone() {
        when(clientService.findByEmail("a@b.com")).thenReturn(dtoById);

        var response = controller.search("+79991234567", "a@b.com");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dtoById, response.getBody());

        verify(clientService).findByEmail("a@b.com");
        verify(clientService, never()).findByPhone(anyString());
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void search_usesPhone_whenEmailBlank() {
        when(clientService.findByPhone("+79991234567")).thenReturn(dtoById);

        var response = controller.search("+79991234567", "   ");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dtoById, response.getBody());

        verify(clientService).findByPhone("+79991234567");
        verify(clientService, never()).findByEmail(anyString());
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void search_throwsIllegalArgument_whenNoPhoneAndNoEmail() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.search("   ", null)
        );
        assertEquals("phone or email is required", ex.getMessage());

        verifyNoInteractions(clientService);
    }
}