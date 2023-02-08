package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BlockClientCardCommand implements MyCommand {
    private final CardService service = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_ACCOUNT;
        try {
            Role role = (Role) session.getAttribute("role");
            Card card = service.findByNumber(req.getParameter("card"));
            if (role.toString().equals("admin")) {
                response = Path.COMMAND_CLIENT_DATA;
            }

            if (card != null) {
                card.setStatus(false);
                service.update(card);
            }

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidCardException | UserNotFoundException | IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
