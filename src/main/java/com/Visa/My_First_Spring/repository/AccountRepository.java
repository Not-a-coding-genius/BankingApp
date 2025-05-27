package com.Visa.My_First_Spring.repository;

import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
    boolean existsByAccountNumber(String accountNumber);
}



