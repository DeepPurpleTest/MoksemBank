package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.PaginationUtil;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.moksem.moksembank.util.SessionAttributesUtil.clearSession;
import static com.moksem.moksembank.util.SessionAttributesUtil.toSession;
import static java.util.Objects.requireNonNullElse;

public class ClientCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_CLIENT;

        if (req.getParameter("sort") != null || req.getParameter("id") != null) {
            clearSession(session);
            toSession(req, session);
//            System.out.println("AFTER TOSESSION() " + session.getAttributeNames());
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
        } else {
//            System.out.println("ELSE " + session.getAttributeNames());
            String id = (String) requireNonNullElse(session.getAttribute("id"), "");
            String page = (String) requireNonNullElse(session.getAttribute("page"), "");
            String sort = (String) requireNonNullElse(session.getAttribute("sort"), "natural");
            String number = (String) requireNonNullElse(session.getAttribute("number"), "");

            User client = User.builder().build();
            List<Card> cards = new ArrayList<>();
            int pages;
            try {
                client = userService.findById(id);
                cards.addAll(cardService.findByParameters(client.getId(), page, sort, number));
                pages = cardService.findCount(client.getId(), sort);
                req.setAttribute("client", UserDtoBuilder.getUserDto(client));
                req.setAttribute("cards", CardDtoBuilder.getCardsDto(cards));
                req.setAttribute("sort", sort);
//                req.setAttribute("number", number);
                PaginationUtil.paginate(req, pages);

                response = Path.PAGE_CLIENT;
            } catch (UserNotFoundException | InvalidIdException e) {
                req.setAttribute("errorMessage", e.getMessage());
                response = Path.PAGE_ERROR;
            } catch (InvalidCardException e) {
                req.setAttribute("client", UserDtoBuilder.getUserDto(client));
                req.setAttribute("errorMessage", e.getMessage());
                response = Path.PAGE_CLIENT;
            }
        }

        return response;
    }
}
