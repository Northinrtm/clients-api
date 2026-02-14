package com.northinrtm.clientsapi.service.impl;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.exception.NotFoundException;
import com.northinrtm.clientsapi.mapper.ClientMapper;
import com.northinrtm.clientsapi.repository.ClientRepository;
import com.northinrtm.clientsapi.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDto create(ClientCreateRequest request) {
        Client entity = clientMapper.toEntity(request);
        Client saved = clientRepository.save(entity);
        return clientMapper.toDto(saved);
    }

    @Override
    public ClientDto getById(Long id) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        return clientMapper.toDto(entity);
    }

    @Override
    public List<ClientDto> getAll() {
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    @Override
    public ClientDto update(Long id, ClientUpdateRequest request) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        clientMapper.update(request, entity);
        Client saved = clientRepository.save(entity);
        return clientMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Client not found"));
        clientRepository.delete(entity);
    }

    @Override
    public ClientDto findByPhone(String phone) {
        Client entity = clientRepository.findByContact_Phone(phone)
                .orElseThrow(()-> new NotFoundException("Client not found"));
        return clientMapper.toDto(entity);
    }

    @Override
    public ClientDto findByEmail(String email) {
        Client entity = clientRepository.findByContact_Email(email)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        return clientMapper.toDto(entity);
    }
}
