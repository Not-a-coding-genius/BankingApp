package com.Visa.My_First_Spring.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.Visa.My_First_Spring.entity.Beneficiary;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    @Procedure(name = "Beneficiary.addBeneficiary")
    void addBeneficiary(
        @Param("p_account_id") Long accountId,
        @Param("p_name") String name,
        @Param("p_relationship") String relationship,
        @Param("p_age") Integer age
    );

    List<Beneficiary> findByAccountId(Long accountId);
}
