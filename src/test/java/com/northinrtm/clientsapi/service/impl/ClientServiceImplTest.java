package com.northinrtm.clientsapi.service.impl;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.exception.NotFoundException;
import com.northinrtm.clientsapi.mapper.ClientMapper;
import com.northinrtm.clientsapi.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
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

    ClientCreateRequest createReq;
    ClientUpdateRequest updateReq;

    Client entity;
    Client saved;

    ClientDto dto;

    @BeforeEach
    void init() {
        createReq = mock(ClientCreateRequest.class);
        updateReq = mock(ClientUpdateRequest.class);

        entity = new Client();
        saved = new Client();

        dto = new ClientDto(1L, "A", "B", null);
    }

    @Test
    void create_mapsSaves_returnsDto() {
        when(clientMapper.toEntity(createReq)).thenReturn(entity);
        when(clientRepository.save(entity)).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        ClientDto result = clientService.create(createReq);

        assertEquals(dto, result);
        verify(clientMapper).toEntity(createReq);
        verify(clientRepository).save(entity);
        verify(clientMapper).toDto(saved);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void getById_returnsDto_whenFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(clientMapper.toDto(saved)).thenReturn(dto);

        ClientDto result = clientService.getById(1L);

        assertEquals(dto, result);
        verify(clientRepository).findById(1L);
        verify(clientMapper).toDto(saved);
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
        when(clientRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(clientRepository.save(saved)).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        ClientDto result = clientService.update(1L, updateReq);

        assertEquals(dto, result);
        verify(clientRepository).findById(1L);
        verify(clientMapper).update(updateReq, saved);
        verify(clientRepository).save(saved);
        verify(clientMapper).toDto(saved);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void delete_deletes_whenFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(saved));

        clientService.delete(1L);

        verify(clientRepository).findById(1L);
        verify(clientRepository).delete(saved);
        verifyNoMoreInteractions(clientRepository, clientMapper);
    }
}