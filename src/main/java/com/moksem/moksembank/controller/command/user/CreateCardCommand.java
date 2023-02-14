package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for create new client card
 */
public class CreateCardCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Card card = Card.builder()
                .user(user)
                .build();
        cardService.create(card);
        String response = Path.COMMAND_ACCOUNT;
        try {
            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
