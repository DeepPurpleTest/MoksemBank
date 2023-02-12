package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RequestUnlockCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    RequestService requestService = AppContext.getInstance().getRequestService();
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_ACCOUNT;
        String cardId = req.getParameter("card");
        User user = (User) session.getAttribute("user");

        try {
            Card card = cardService.findById(cardId, user);
            if(!card.isStatus()) {
                Request request = requestService.findByCard(card);
                if(request == null)
                    requestService.create(card);
            }
            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (IOException | UserNotFoundException | InvalidCardException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
