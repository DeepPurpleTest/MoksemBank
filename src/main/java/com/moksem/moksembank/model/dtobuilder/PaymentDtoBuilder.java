package com.moksem.moksembank.model.dtobuilder;

import com.moksem.moksembank.model.dto.PaymentDto;
import com.moksem.moksembank.model.entity.Payment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment entity dto builder
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDtoBuilder {
    public static PaymentDto getPaymentDto(Payment payment){
        return PaymentDto.builder()
                .id(String.valueOf(payment.getId()))
                .senderId(String.valueOf(payment.getCardSender().getUser().getId()))
                .receiverId(String.valueOf(payment.getCardReceiver().getUser().getId()))
                .senderCardId(String.valueOf(payment.getCardSender().getId()))
                .receiverCardId(String.valueOf(payment.getCardReceiver().getId()))
                .senderCardNumber(payment.getCardSender().getNumber())
                .receiverCardNumber(payment.getCardReceiver().getNumber())
                .senderName(payment.getCardSender().getUser().getName())
                .receiverName(payment.getCardReceiver().getUser().getName())
                .senderSurname(payment.getCardSender().getUser().getSurname())
                .receiverSurname(payment.getCardReceiver().getUser().getSurname())
                .amount(payment.getAmount().toString())
                .time(payment.getDate().getTime().toString())
                .build();
    }

    public static List<PaymentDto> getPaymentsDto(List<Payment> payments){
        List<PaymentDto> paymentsDto = new ArrayList<>();
        if (payments.isEmpty())
            return paymentsDto;
        payments.forEach(payment -> paymentsDto.add(getPaymentDto(payment)));
        return paymentsDto;
    }
}
