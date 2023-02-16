package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for block client card
 */
public class BlockClientCardCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_ACCOUNT;

        try {
            Role role = (Role) session.getAttribute("role");
            Card card;
            if (role.equals(Role.ADMIN)) {
                card = cardService.findById(req.getParameter("card"));
                response = Path.COMMAND_CLIENT_DATA;
            } else {
                User user = (User) session.getAttribute("user");
                card = cardService.findById(req.getParameter("card"), user);
            }

            if (card.isStatus()) {
                card.setStatus(false);
                cardService.update(card);
            }

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidCardException | UserNotFoundException | IOException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
