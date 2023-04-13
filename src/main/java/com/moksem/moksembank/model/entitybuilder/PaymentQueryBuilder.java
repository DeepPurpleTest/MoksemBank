package com.moksem.moksembank.model.entitybuilder;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment entity query builder
 */
public class PaymentQueryBuilder extends QueryBuilder<Payment> {
    public List<Payment> getListOfResult(ResultSet rs) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        while (rs.next()) {
            Card cardSender = Card.builder().number(rs.getString(1)).build();
            Card cardReceiver = Card.builder().number(rs.getString(2)).build();

            Payment payment = Payment.builder()
                    .cardSender(cardSender) // service.find(number)
                    .cardReceiver(cardReceiver)
                    .amount(rs.getBigDecimal(3).setScale(2, RoundingMode.DOWN))
                    .status(rs.getString(4))
                    .date(getTimeStamp(rs.getTimestamp(5)))
                    .build();

            payment.setId(rs.getInt(6));
            payments.add(payment);
        }
        return payments;
    }

    public Payment getResult(ResultSet rs) throws SQLException {
        Payment payment = null;
        while (rs.next()) {
            Card cardSender = Card.builder().number(rs.getString(1)).build();
            Card cardReceiver = Card.builder().number(rs.getString(2)).build();

            payment = Payment.builder()
                    .cardSender(cardSender) // service.find(number)
                    .cardReceiver(cardReceiver)
                    .amount(rs.getBigDecimal(3).setScale(2, RoundingMode.DOWN))
                    .status(rs.getString(4))
                    .date(getTimeStamp(rs.getTimestamp(5)))
                    .build();

            payment.setId(rs.getInt(6));
        }
        return payment;
    }

    public LocalDateTime getTimeStamp(Timestamp timestamp) {
        return LocalDateTime.of(timestamp.toLocalDateTime().toLocalDate(),
                timestamp.toLocalDateTime().toLocalTime());
    }
}
