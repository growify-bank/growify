package org.growify.bank.service;

import org.growify.bank.dto.request.TransferRequest;
import org.growify.bank.dto.response.TransferResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransferService {

    public TransferResponse processTransferCPF(TransferRequest request) {

        String transactionId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal(request.amount());
        return new TransferResponse(transactionId, "SUCCESS", amount);
    }
}
