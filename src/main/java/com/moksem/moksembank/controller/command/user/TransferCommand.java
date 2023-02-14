package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.CardDto;
import com.moksem.moksembank.model.dto.TransferDto;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Transfer page command
 */
public class TransferCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<Card> cards = cardService.findAllByUserId(user.getId());
        List<CardDto> cardsDto = CardDtoBuilder.getCardsDto(cards);
        TransferDto dto = (TransferDto) req.getAttribute("dto");

        if (dto == null)
            dto = TransferDto.builder().build();

        dto.setCards(cardsDto);
        req.setAttribute("dto", dto);
        return Path.PAGE_TRANSFER;
    }
}
