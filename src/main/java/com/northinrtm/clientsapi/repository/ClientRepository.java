package com.northinrtm.clientsapi.repository;

import com.northinrtm.clientsapi.entity.Client;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @EntityGraph(attributePaths = "contact")
    Optional<Client> findByContact_Phone(String phone);
    @EntityGraph(attributePaths = "contact")
    Optional<Client> findByContact_Email(String email);

    @Override
    @EntityGraph(attributePaths = "contact")
    @NonNull Page<Client> findAll(@NonNull Pageable pageable);
}
