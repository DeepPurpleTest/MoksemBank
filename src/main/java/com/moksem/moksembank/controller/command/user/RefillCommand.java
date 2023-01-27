package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class RefillCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String response = Path.COMMAND_ACCOUNT;
        Card card = Card.builder().build();
        try {
            String cardId = req.getParameter("card");
            card = cardService.findById(cardId);
            if (!card.isStatus())
                throw new InvalidCardException("This card is blocked");

            String amount = req.getParameter("amount");
            ValidatorsUtil.validateAmount(amount);
            BigDecimal refillValue = new BigDecimal(amount);
            card.setWallet(card.getWallet().add(refillValue));
            cardService.update(card);

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidCardException | InvalidIdException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (InvalidAmountException e) {
            req.setAttribute("card", card);
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_REFILL;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
