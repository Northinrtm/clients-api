package com.northinrtm.clientsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClientsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientsApiApplication.class, args);
    }

}
