package com.Visa.My_First_Spring.service;

import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.AccountStatus;
import com.Visa.My_First_Spring.entity.AccountType;
import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void createAccount(User user, String accountTypeStr, Double initialDeposit, AccountStatus status) {
        Account account = new Account();
        account.setUser(user);

        // Convert String to AccountType enum
        AccountType accountType = AccountType.valueOf(accountTypeStr);
        account.setAccountType(accountType);

        // Set balance: use initialDeposit if >= 1000, else default 1000
        if (initialDeposit != null && initialDeposit >= 1000) {
            account.setBalance(BigDecimal.valueOf(initialDeposit));
        } else {
            account.setBalance(BigDecimal.valueOf(1000.0));
        }

        account.setStatus(status);

        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        account.setAccountNumber(accountNumber);

        accountRepository.save(account);
    }


    // Generates a random 10-digit account number (as string)
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    
    public void updateAccount(Long id, AccountType type, AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setAccountType(type);
        account.setStatus(status);
        accountRepository.save(account);
    }
    
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
    
}
