package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.TransactionException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UnlockClientCardCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();
    RequestService requestService = AppContext.getInstance().getRequestService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_CLIENT_DATA;

        try {
            Card card = cardService.findByNumber(req.getParameter("card"));
            Request request = requestService.findByCard(card);
            Admin admin = (Admin) session.getAttribute("user");
            if (request == null) {
                long id = requestService.create(card);
                request = requestService.findByUserId(id);
            }
            request.setAdminId(admin.getId());
            request.setStatus(true);
            card.setStatus(true);
            requestService.updateRequestTransaction(request, card);

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidCardException | TransactionException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
