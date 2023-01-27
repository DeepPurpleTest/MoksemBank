package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    PaymentService paymentService = AppContext.getInstance().getPaymentService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_TRANSFER;
        User user = (User) session.getAttribute("user");
        Map<String, String> exceptions = new HashMap<>();
//        Random random = new Random();
//        int value = random.nextInt(100);
//        System.out.println("---------------------------------------");
//        System.out.println("START TRANSACTION " + value);
        Payment payment = Payment.builder().build();
        try {
            Card cardSender = cardService.findByUserIdAndCardNumber(user.getId(), req.getParameter("card_sender"));
//            Card card_sender = cardService.findUserCard(1, req.getParameter("card_sender"));
            Card cardReceiver = cardService.findByNumber(req.getParameter("card_receiver"));
            String amount = req.getParameter("amount");
            exceptions.putAll(ValidatorsUtil.validateTransaction(cardSender, cardReceiver, amount));

            if (exceptions.isEmpty()) {
                BigDecimal transferAmount = new BigDecimal(amount);
                payment = Payment.builder()
                        .cardSender(cardSender)
                        .cardReceiver(cardReceiver)
                        .amount(transferAmount)
                        .build();

                long id = paymentService.create(payment);
                payment.setId(id);
                cardSender.setWallet(cardSender.getWallet().subtract(transferAmount));
                cardReceiver.setWallet(cardReceiver.getWallet().add(transferAmount));
                cardService.update(cardSender, cardReceiver);
                payment.setStatus("sent");
                paymentService.update(payment);

                response = Path.COMMAND_PAYMENTS;
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
//                System.out.println("DO TRANSACTION " + value);
            }
        } catch (InvalidCardException e) {
            exceptions.put("card", e.getMessage());
        } catch (PaymentCreateException e) {
            exceptions.put("payment", e.getMessage());
        } catch (TransactionException e) {
            paymentService.delete(payment);
            exceptions.put("transaction", e.getMessage());
        } catch (IOException | UserNotFoundException | InvalidIdException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }
        req.setAttribute("exceptions", exceptions);
//        System.out.println("END TRANSACTION " + value);
//        System.out.println("---------------------------------------");
        return response;
    }
}
