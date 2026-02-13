package com.northinrtm.clientsapi.entity;

import jakarta.persistence.*;

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
    private Long contactId;

    @Column(length = 32)
    private String phone;

    @Column(length = 255)
    private String email;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
}
