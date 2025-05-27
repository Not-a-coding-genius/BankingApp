package com.Visa.My_First_Spring.service;

import com.Visa.My_First_Spring.entity.*;
import com.Visa.My_First_Spring.repository.AccountRepository;
import com.Visa.My_First_Spring.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.Visa.My_First_Spring.util.HashUtil;

import java.util.List;
import java.util.Optional;
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        // Validation
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        // Set timestamp if not already set
        if (transaction.getCreatedAt() == null) {
            transaction.setCreatedAt(LocalDateTime.now());
        }

        // Set initial status to PENDING
        transaction.setTransactionStatus(TransactionStatus.PENDING);

        // Handle transaction types
        if (transaction.getTransactionType() == TransactionType.TRANSFER) {
            Account from = accountRepository.findById(transaction.getFromAccount().getId())
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));

            Account to = accountRepository.findById(transaction.getToAccount().getId())
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));

            if (!from.getStatus().equals(AccountStatus.ACTIVE) || !to.getStatus().equals(AccountStatus.ACTIVE)) {
                throw new IllegalStateException("Both accounts must be ACTIVE");
            }

            if (from.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new IllegalStateException("Insufficient balance in from account");
            }

            from.setBalance(from.getBalance().subtract(transaction.getAmount()));
            to.setBalance(to.getBalance().add(transaction.getAmount()));

            accountRepository.save(from);
            accountRepository.save(to);

        } else if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            Account to = accountRepository.findById(transaction.getToAccount().getId())
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));

            if (!to.getStatus().equals(AccountStatus.ACTIVE)) {
                throw new IllegalStateException("To account must be ACTIVE");
            }

            to.setBalance(to.getBalance().add(transaction.getAmount()));
            accountRepository.save(to);

        } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            Account from = accountRepository.findById(transaction.getFromAccount().getId())
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));

            if (!from.getStatus().equals(AccountStatus.ACTIVE)) {
                throw new IllegalStateException("From account must be ACTIVE");
            }

            if (from.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new IllegalStateException("Insufficient balance");
            }

            from.setBalance(from.getBalance().subtract(transaction.getAmount()));
            accountRepository.save(from);
        }

        // Blockchain-style Hashing Section

        // 1. Get previous hash (last transaction)
        Optional<Transaction> previousTxOpt = transactionRepository.findTopByOrderByIdDesc();
        String previousHash = previousTxOpt.map(Transaction::getTransactionHash).orElse("GENESIS");

        // 2. Create data string to hash
        String dataToHash = transaction.getAmount().toString()
                + (transaction.getFromAccount() != null ? transaction.getFromAccount().getId() : "")
                + (transaction.getToAccount() != null ? transaction.getToAccount().getId() : "")
                + transaction.getCreatedAt().toString()
                + previousHash;

        // 3. Generate hash
        String txHash = HashUtil.sha256(dataToHash);

        // 4. Set hashes
        transaction.setPreviousTransactionHash(previousHash);
        transaction.setTransactionHash(txHash);

        // Mark transaction as COMPLETED after successful processing
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());

        // Save transaction
        transactionRepository.save(transaction);
    }

    
    public List<Transaction> getTransactionsForUser(List<Account> accounts) {
        return transactionRepository.findByFromAccountInOrToAccountIn(accounts, accounts);
    }

}
