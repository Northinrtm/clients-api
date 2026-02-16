package com.northinrtm.clientsapi.service.impl;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.exception.NotFoundException;
import com.northinrtm.clientsapi.mapper.ClientMapper;
import com.northinrtm.clientsapi.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;

    @InjectMocks
    ClientServiceImpl clientService;

    @Test
    void create_mapsSaves_returnsDto() {
        ClientCreateRequest req = mock(ClientCreateRequest.class);
        Client entity = new Client();
        Client saved = new Client();
        ClientDto dto = new ClientDto(1L, "A", "B", null);

        when(clientMapper.toEntity(req)).thenReturn(entity);
        when(clientRepository.save(entity)).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        ClientDto result = clientService.create(req);

        assertEquals(dto, result);
        verify(clientMapper).toEntity(req);
        verify(clientRepository).save(entity);
        verify(clientMapper).toDto(saved);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void getById_returnsDto_whenFound() {
        Client entity = new Client();
        ClientDto dto = new ClientDto(1L, "A", "B", null);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(clientMapper.toDto(entity)).thenReturn(dto);

        ClientDto result = clientService.getById(1L);

        assertEquals(dto, result);
        verify(clientRepository).findById(1L);
        verify(clientMapper).toDto(entity);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> clientService.getById(99L));
        assertTrue(ex.getMessage().contains("99"));

        verify(clientRepository).findById(99L);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void getAll_mapsPage() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Client c1 = new Client();
        Client c2 = new Client();
        Page<Client> page = new PageImpl<>(List.of(c1, c2), pageable, 2);

        ClientDto d1 = new ClientDto(1L, "A", "B", null);
        ClientDto d2 = new ClientDto(2L, "C", "D", null);

        when(clientRepository.findAll(pageable)).thenReturn(page);
        when(clientMapper.toDto(c1)).thenReturn(d1);
        when(clientMapper.toDto(c2)).thenReturn(d2);

        Page<ClientDto> result = clientService.getAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(d1, d2), result.getContent());

        verify(clientRepository).findAll(pageable);
        verify(clientMapper).toDto(c1);
        verify(clientMapper).toDto(c2);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void update_updatesEntity_saves_returnsDto() {
        Long id = 1L;
        ClientUpdateRequest req = mock(ClientUpdateRequest.class);

        Client entity = new Client();
        Client saved = entity;
        ClientDto dto = new ClientDto(1L, "A", "B", null);

        when(clientRepository.findById(id)).thenReturn(Optional.of(entity));
        when(clientRepository.save(entity)).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        ClientDto result = clientService.update(id, req);

        assertEquals(dto, result);
        verify(clientRepository).findById(id);
        verify(clientMapper).update(req, entity);
        verify(clientRepository).save(entity);
        verify(clientMapper).toDto(saved);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void update_throwsNotFound_whenMissing() {
        when(clientRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.update(7L, mock(ClientUpdateRequest.class)));

        verify(clientRepository).findById(7L);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void delete_deletes_whenFound() {
        Client entity = new Client();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(entity));

        clientService.delete(1L);

        verify(clientRepository).findById(1L);
        verify(clientRepository).delete(entity);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void delete_throwsNotFound_whenMissing() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.delete(1L));

        verify(clientRepository).findById(1L);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void findByPhone_returnsDto_whenFound() {
        Client entity = new Client();
        ClientDto dto = new ClientDto(1L, "A", "B", null);

        when(clientRepository.findByContact_Phone("+79991234567")).thenReturn(Optional.of(entity));
        when(clientMapper.toDto(entity)).thenReturn(dto);

        ClientDto result = clientService.findByPhone("+79991234567");

        assertEquals(dto, result);
        verify(clientRepository).findByContact_Phone("+79991234567");
        verify(clientMapper).toDto(entity);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void findByPhone_throwsNotFound_whenMissing() {
        when(clientRepository.findByContact_Phone("x")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.findByPhone("x"));

        verify(clientRepository).findByContact_Phone("x");
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void findByEmail_returnsDto_whenFound() {
        Client entity = new Client();
        ClientDto dto = new ClientDto(1L, "A", "B", null);

        when(clientRepository.findByContact_Email("a@b.com")).thenReturn(Optional.of(entity));
        when(clientMapper.toDto(entity)).thenReturn(dto);

        ClientDto result = clientService.findByEmail("a@b.com");

        assertEquals(dto, result);
        verify(clientRepository).findByContact_Email("a@b.com");
        verify(clientMapper).toDto(entity);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void findByEmail_throwsNotFound_whenMissing() {
        when(clientRepository.findByContact_Email("missing@b.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.findByEmail("missing@b.com"));

        verify(clientRepository).findByContact_Email("missing@b.com");
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }
}