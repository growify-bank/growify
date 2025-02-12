package org.growify.bank.dto.response;

import java.math.BigDecimal;

public record DepositResponse(
        String transactionId,
        String status,
        BigDecimal newBalance
) {
}
