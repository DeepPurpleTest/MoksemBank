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
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

/**
 * Client page command
 */
public class ClientCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.PAGE_CLIENT;

//            System.out.println("ELSE " + session.getAttributeNames());
        String id = (String) requireNonNullElse(session.getAttribute("id"), "");
        String page = (String) requireNonNullElse(session.getAttribute("page"), "");
        String sort = (String) requireNonNullElse(session.getAttribute("sort"), "natural");
        String number = (String) requireNonNullElse(session.getAttribute("number"), "");

        User client = User.builder().build();
        int pages;
        try {
            client = userService.findById(id);
            List<Card> cards = new ArrayList<>
                    (cardService.findByParameters(client.getId(), page, sort, number));
            pages = cardService.findCount(client.getId(), sort);
            req.setAttribute("client", UserDtoBuilder.getUserDto(client));
            req.setAttribute("cards", CardDtoBuilder.getCardsDto(cards));
            req.setAttribute("sort", sort);
//                req.setAttribute("number", number);
            Pagination.paginate(req, pages);
        } catch (UserNotFoundException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (InvalidCardException e) {
            req.setAttribute("client", UserDtoBuilder.getUserDto(client));
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_CLIENT;
        }

        return response;
    }
}
