package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.RefillDto;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Refill page command
 */
public class CardRefillCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String response = Path.PAGE_REFILL;
        String cardId = req.getParameter("card");

        try {
            Card card = cardService.findById(cardId);
            req.setAttribute("dto", getRefillDto(card));
        } catch (InvalidCardException | UserNotFoundException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        }

        return response;
    }

    public RefillDto getRefillDto(Card card){
        return RefillDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .wallet(String.valueOf(card.getWallet()))
                .status(card.isStatus())
                .build();
    }

}
