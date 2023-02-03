package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.PaymentRepo;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import java.util.List;

import static com.moksem.moksembank.util.PaginationUtil.getPage;

public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final CardService cardService;

    public PaymentService(PaymentRepo paymentRepo, CardService cardService) {
        this.paymentRepo = paymentRepo;
        this.cardService = cardService;
    }

    public List<Payment> findByUserIdAndCardId(long userId, String cardId, String page, String sort) throws InvalidCardException, UserNotFoundException {
        int pageValue = getPage(page);

        Card card = cardService.findById(cardId);
        if (card.getUser().getId() != userId)
            throw new InvalidCardException();

        List<Payment> payments = sort.equals("asc") ? paymentRepo.getPaymentsByCardASC(card, pageValue) :
                paymentRepo.getPaymentsByCardDESC(card, pageValue);

        toFullCards(payments);
        return payments;
    }

    public List<Payment> findByUser(User user, String page, String sort) throws InvalidCardException, UserNotFoundException {
        int pageValue = getPage(page);

        List<Payment> payments = sort.equals("asc") ? paymentRepo.getPaymentsByUserASC(user, pageValue) :
                paymentRepo.getPaymentsByUserDESC(user, pageValue);

        toFullCards(payments);
        return payments;
    }

    public int findCountByUser(User user) {
        return paymentRepo.getCountByUser(user);
    }

    public int findCountByCard(String cardId) throws InvalidCardException, UserNotFoundException {
        Card card = cardService.findById(cardId);
        return paymentRepo.getCountByCard(card.getNumber());
    }

    public Payment find(long userId, String paymentId) throws InvalidIdException, PaymentNotFoundException {
        ValidatorsUtil.validateId(paymentId);
        Payment payment = paymentRepo.getPayment(userId, Integer.parseInt(paymentId));
        if (payment == null)
            throw new PaymentNotFoundException("Payment not found");
        return payment;
    }

    public long create(Payment payment) throws PaymentCreateException {
        if (payment.getCardSender().equals(payment.getCardReceiver()))
            throw new PaymentCreateException("Cards is equals");
        long id = paymentRepo.createPayment(payment);
        if (id < 0)
            throw new PaymentCreateException("Payment create is failed");
        return id;
    }

    public void update(Payment payment) {
        paymentRepo.update(payment);
    }

    public void delete(Payment payment) {
        paymentRepo.delete(payment);
    }

    public void toFullCards(List<Payment> payments) throws InvalidCardException, UserNotFoundException {
        for (Payment payment : payments) {
            Card cardSender = payment.getCardSender();
            Card cardReceiver = payment.getCardReceiver();
            payment.setCardSender(cardService.findByNumber(cardSender.getNumber()));
            payment.setCardReceiver(cardService.findByNumber(cardReceiver.getNumber()));
//            cardSender.setUser(userService.findByCard(cardSender.getNumber()));
//            cardReceiver.setUser(userService.findByCard(cardReceiver.getNumber()));
        }
    }
}
