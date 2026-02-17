package com.northinrtm.clientsapi.service.impl;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.entity.Client;
import com.northinrtm.clientsapi.exception.NotFoundException;
import com.northinrtm.clientsapi.mapper.ClientMapper;
import com.northinrtm.clientsapi.repository.ClientRepository;
import com.northinrtm.clientsapi.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public ClientDto create(ClientCreateRequest request) {
        Client entity = clientMapper.toEntity(request);
        Client saved = clientRepository.save(entity);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto getById(Long id) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        return clientMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDto> getAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toDto);
    }

    @Override
    @Transactional
    public ClientDto update(Long id, ClientUpdateRequest request) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        clientMapper.update(request, entity);
        Client saved = clientRepository.save(entity);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        clientRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findByPhone(String phone) {
        Client entity = clientRepository.findByContact_Phone(phone)
                .orElseThrow(() -> new NotFoundException("Client not found by phone"));
        return clientMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findByEmail(String email) {
        Client entity = clientRepository.findByContact_Email(email)
                .orElseThrow(() -> new NotFoundException("Client not found by email"));
        return clientMapper.toDto(entity);
    }
}
