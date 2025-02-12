package org.growify.bank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growify.bank.dto.request.TransferRequest;
import org.growify.bank.dto.response.TransferResponse;
import org.growify.bank.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferCPFService;

    @PostMapping("/cpf")
    public ResponseEntity<TransferResponse> transferCPF(@Valid @RequestBody TransferRequest request) {
            TransferResponse response = transferCPFService.processTransferCPF(request);
        return ResponseEntity.ok(response);
    }
}
