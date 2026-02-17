package com.northinrtm.clientsapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;

@Entity
@Table(name = "contacts",
        indexes = {
                @Index(name = "idx_contacts_phone", columnList = "phone"),
                @Index(name = "idx_contacts_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_contacts_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uq_contacts_email", columnNames = "email")
        })
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    @Column(length = 32, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;

    public Contact() {
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact contact)) return false;
        return id != null && Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
