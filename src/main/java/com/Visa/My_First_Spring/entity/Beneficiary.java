package com.Visa.My_First_Spring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedStoredProcedureQuery(
    name = "Beneficiary.addBeneficiary",
    procedureName = "add_beneficiary",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_account_id", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_name", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_relationship", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_age", type = Integer.class)
    }
)
@Table(name = "beneficiary")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String name;

    private String relationship;

    @Column(nullable = false)
    private int age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
