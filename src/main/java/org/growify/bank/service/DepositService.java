package org.growify.bank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.growify.bank.model.account.Account;
import org.growify.bank.dto.request.DepositRequest;
import org.growify.bank.repository.AccountRepository;
import org.growify.bank.dto.response.DepositResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;

    @Transactional
    public DepositResponse processDeposit(DepositRequest request) {
        Account account = accountRepository.findByCpf(request.cpf())
                 .orElseThrow(() -> new  RuntimeException("Conta não encontrada para o CPF" +request.cpf()));

        BigDecimal newBalance = account.getBalance().add(request.amount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        return new DepositResponse(
                UUID.randomUUID().toString(),
                "SUCCESS", newBalance);
    }

}
