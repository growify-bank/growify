package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Criar conta
    public Account createAccount(Account account) {
        return accountRepository.save(account);  // Salva a conta no banco de dados
    }

    // Obter conta por ID
    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);  // Retorna a conta ou null se não encontrar
    }

    // Obter todas as contas
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();  // Retorna todas as contas
    }
}
