package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Payment entity dto class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class PaymentDto extends Dto{
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
