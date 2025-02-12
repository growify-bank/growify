package org.growify.bank.dto.request;

public record TransferRequest(

        String senderCpf,
        String recipientCpf,
        String amount
)
 {
}
