package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.Dto;
import com.moksem.moksembank.model.dto.RefillDto;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.PaymentCreateException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;
import com.moksem.moksembank.util.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Command for refill card
 */
public class RefillCommand implements MyCommand {
    private static final String REFILL_ID = "1";
    CardService cardService = AppContext.getInstance().getCardService();
    PaymentService paymentService = AppContext.getInstance().getPaymentService();


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String response = Path.COMMAND_ACCOUNT;
        Card card = Card.builder().build();

        try {
            String cardId = req.getParameter("card");
            card = cardService.findById(cardId);
            if (!card.isStatus())
                throw new InvalidCardException("client.error.card.blocked");

            String amount = req.getParameter("amount");
            Validator.validateAmount(amount);
            amount = amount.replace(",", ".");
            BigDecimal refillValue = new BigDecimal(amount);
            Card iBox = cardService.findById(REFILL_ID);

            Payment payment = Payment.builder()
                    .cardSender(iBox)
                    .cardReceiver(card)
                    .amount(refillValue)
                    .build();

            long id = paymentService.create(payment);
            card.setWallet(card.getWallet().add(refillValue));
            cardService.update(card);
            payment.setId(id);
            payment.setStatus("sent");
            paymentService.update(payment);

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidCardException | UserNotFoundException | PaymentCreateException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (InvalidAmountException e) {
            RefillDto refillDto = getRefillDto(req, card);
            refillDto.getErrors().add(new Dto.Param("amount", e.getMessage()));
            req.setAttribute("dto", refillDto);
            response = Path.PAGE_REFILL;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }

    public RefillDto getRefillDto(HttpServletRequest req, Card card) {
        String amount = req.getParameter("amount");
        return RefillDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .wallet(String.valueOf(card.getWallet()))
                .status(card.isStatus())
                .amount(amount)
                .build();
    }
}
