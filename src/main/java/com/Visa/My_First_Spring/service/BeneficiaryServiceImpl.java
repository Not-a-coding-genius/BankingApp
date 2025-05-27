package com.Visa.My_First_Spring.service;

import com.Visa.My_First_Spring.entity.Beneficiary;
import com.Visa.My_First_Spring.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Override
    public void addBeneficiary(Long accountId, String name, String relationship, int age) {
        beneficiaryRepository.addBeneficiary(accountId, name, relationship, age);
    }

    @Override
    public List<Beneficiary> getBeneficiariesByAccountId(Long accountId) {
        return beneficiaryRepository.findByAccountId(accountId);
    }

    @Override
    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.deleteById(id);
    }

    @Override
    public Beneficiary getById(Long id) {
        return beneficiaryRepository.findById(id).orElse(null);
    }

    @Override
    public void updateBeneficiary(Beneficiary beneficiary) {
        beneficiaryRepository.save(beneficiary);
    }
}
