package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
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
import java.util.HashSet;

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

        TransferDto transferDto = TransferDto.builder()
                .sender(CardDtoBuilder.getCardDto(cardSender))
                .receiver(CardDtoBuilder.getCardDto(cardReceiver))
                .amount(amount)
                .errors(new HashSet<>())
                .build();

        try {
//            Card card_sender = cardService.findUserCard(1, req.getParameter("card_sender"));
            cardSender = cardService.findByUserIdAndCardNumber(user.getId(), senderNumber);
            cardReceiver = cardService.findByNumber(receiverNumber);

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
            transferDto.getErrors().add(new TransferDto.Param("sender", e.getMessage()));
        } catch (InvalidCardException e) {
            transferDto.getErrors().add(new TransferDto.Param("receiver", e.getMessage()));
        } catch (PaymentCreateException e) {
            transferDto.getErrors().add(new TransferDto.Param("payment", e.getMessage()));
        } catch (InvalidAmountException e) {
            transferDto.getErrors().add(new TransferDto.Param("amount", e.getMessage()));
        } catch (TransactionException e) {
            paymentService.delete(payment);
            transferDto.getErrors().add(new TransferDto.Param("transaction", e.getMessage()));
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        }

        transferDto = transferDto.toBuilder()
                .sender(CardDtoBuilder.getCardDto(cardSender))
                .receiver(CardDtoBuilder.getCardDto(cardReceiver))
                .amount(amount)
                .build();
        ValidatorsUtil.validateTransaction(transferDto);

        req.setAttribute("transferDto", transferDto);
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
