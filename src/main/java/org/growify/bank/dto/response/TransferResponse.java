package org.growify.bank.dto.response;

import java.math.BigDecimal;

public record TransferResponse(
        String transactionId,
        String status,
        BigDecimal amount
) {
}
