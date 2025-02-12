package org.growify.bank.dto.request;

import java.math.BigDecimal;

public record DepositRequest(
        String cpf,
        BigDecimal amount
) {
}
