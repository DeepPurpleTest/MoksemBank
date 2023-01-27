package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder(toBuilder = true)
public class PaymentDto {
    private String id;
    private String senderId;
    private String receiverId;
    private String senderCardId;
    private String receiverCardId;
    private String senderCardNumber;
    private String receiverCardNumber;
    private String senderName;
    private String receiverName;
    private String senderSurname;
    private String receiverSurname;
    private String amount;
    private String time;
}
