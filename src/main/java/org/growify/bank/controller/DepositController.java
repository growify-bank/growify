package org.growify.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<DepositResponse> deposit(@Valid @RequestBody DepositRequest request) {
        DepositResponse response = depositService.processDeposit(request);
        return ResponseEntity.ok(response);
    }

}
