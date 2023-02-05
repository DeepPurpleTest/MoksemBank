package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.Dto;
import com.moksem.moksembank.model.dto.TransferDto;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
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

public class TransactionCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    PaymentService paymentService = AppContext.getInstance().getPaymentService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_TRANSFER;
        User user = (User) session.getAttribute("user");

//        Random random = new Random();
//        int value = random.nextInt(100);
//        System.out.println("---------------------------------------");
//        System.out.println("START TRANSACTION " + value);
        //todo Кринж....
        Payment payment = Payment.builder().build();
        String amount = req.getParameter("amount");
        String senderNumber = req.getParameter("card_sender");
        String receiverNumber = req.getParameter("card_receiver");

        Card cardSender = getCard(senderNumber);
        Card cardReceiver = getCard(receiverNumber);

        TransferDto dto = TransferDto.builder()
                .sender(CardDtoBuilder.getCardDto(cardSender))
                .receiver(CardDtoBuilder.getCardDto(cardReceiver))
                .amount(amount)
                .build();

        try {
//            Card card_sender = cardService.findUserCard(1, req.getParameter("card_sender"));
            cardSender = cardService.findByUserIdAndCardNumber(user.getId(), senderNumber);
            dto.setSender(CardDtoBuilder.getCardDto(cardSender));

            cardReceiver = cardService.findByNumber(receiverNumber);
            dto.setReceiver(CardDtoBuilder.getCardDto(cardReceiver));

            ValidatorsUtil.validateAmount(amount);
            BigDecimal transferAmount = new BigDecimal(amount);

            if (cardSender.isStatus() && cardReceiver.isStatus()) {
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
            }
//                System.out.println("DO TRANSACTION " + value);
        } catch (UserCardNotFoundException e) {
            dto.getErrors().add(new Dto.Param("sender", e.getMessage()));
        } catch (InvalidCardException e) {
            dto.getErrors().add(new Dto.Param("receiver", e.getMessage()));
        } catch (PaymentCreateException e) {
            dto.getErrors().add(new Dto.Param("payment", e.getMessage()));
        } catch (InvalidAmountException e) {
            dto.getErrors().add(new Dto.Param("amount", e.getMessage()));
        } catch (TransactionException e) {
            paymentService.delete(payment);
            dto.getErrors().add(new Dto.Param("transaction", e.getMessage()));
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        }

        ValidatorsUtil.validateTransaction(dto);


        req.setAttribute("dto", dto);
//        System.out.println("END TRANSACTION " + value);
//        System.out.println("---------------------------------------");
        return response;
    }

    public Card getCard(String number) {
        return Card.builder()
                .wallet(new BigDecimal(0))
                .number(number)
                .status(true)
                .build();
    }
}
