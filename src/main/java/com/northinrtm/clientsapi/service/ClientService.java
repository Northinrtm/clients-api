package com.northinrtm.clientsapi.service;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;

import java.util.List;

public interface ClientService {

    ClientDto create(ClientCreateRequest request);
    ClientDto getById(Long id);
    List<ClientDto> getAll();
    ClientDto update(Long id, ClientUpdateRequest request);
    void delete(Long id);
    ClientDto findByPhone(String phone);
    ClientDto findByEmail(String email);
}
