package com.northinrtm.clientsapi.repository;

import com.northinrtm.clientsapi.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByContact_Phone(String phone);
    Optional<Client> findByContact_Email(String email);
}
