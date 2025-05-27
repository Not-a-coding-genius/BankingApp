package com.Visa.My_First_Spring.repository;


import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.Transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Optional<Transaction> findTopByOrderByIdDesc();
	List<Transaction> findByFromAccountInOrToAccountIn(List<Account> from, List<Account> to);
}

