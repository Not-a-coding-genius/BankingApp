package com.Visa.My_First_Spring.service;
import com.Visa.My_First_Spring.entity.Beneficiary;

import java.util.List;

public interface BeneficiaryService {

    void addBeneficiary(Long accountId, String name, String relationship, int age);

    List<Beneficiary> getBeneficiariesByAccountId(Long accountId);

    void deleteBeneficiary(Long id);

    Beneficiary getById(Long id);

    void updateBeneficiary(Beneficiary beneficiary);
}
