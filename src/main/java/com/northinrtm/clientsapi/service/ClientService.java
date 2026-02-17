package com.northinrtm.clientsapi.service;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientDto create(ClientCreateRequest request);
    ClientDto getById(Long id);
    Page<ClientDto> getAll(Pageable pageable);
    ClientDto update(Long id, ClientUpdateRequest request);
    void delete(Long id);
    ClientDto findByPhone(String phone);
    ClientDto findByEmail(String email);
}
