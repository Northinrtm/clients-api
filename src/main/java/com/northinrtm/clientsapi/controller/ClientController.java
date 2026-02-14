package com.northinrtm.clientsapi.controller;

import com.northinrtm.clientsapi.dto.ClientCreateRequest;
import com.northinrtm.clientsapi.dto.ClientDto;
import com.northinrtm.clientsapi.dto.ClientUpdateRequest;
import com.northinrtm.clientsapi.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Validated
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDto> create(@RequestBody @Valid ClientCreateRequest request) {
        ClientDto created = clientService.create(request);
        return ResponseEntity
                .created(URI.create("/api/clients/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable Long id) {
        return clientService.getById(id);
    }

    @GetMapping
    public Page<ClientDto> getAll(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return clientService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ClientDto update(@PathVariable Long id, @RequestBody @Valid ClientUpdateRequest request) {
        return clientService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ClientDto> search(
            @RequestParam(required = false) @Pattern(regexp = "\\+?[0-9\\s-]{7,32}") String phone,
            @RequestParam(required = false) @Email String email
    ) {
        if (StringUtils.hasText(email)) {
            return ResponseEntity.ok(clientService.findByEmail(email));
        }
        if (StringUtils.hasText(phone)) {
            return ResponseEntity.ok(clientService.findByPhone(phone));
        }
        throw new IllegalArgumentException("phone or email is required");
    }

}
