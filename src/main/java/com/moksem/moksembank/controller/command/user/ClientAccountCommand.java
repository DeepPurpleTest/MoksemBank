package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Client account command
 */
public class ClientAccountCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.PAGE_USER;

//            SessionAttributesUtil.getSessionAttributes(session);
        User user = (User) session.getAttribute("user");
        String sort = (String) Objects.requireNonNullElse(session.getAttribute("sort"), "natural");
        String page = (String) Objects.requireNonNullElse(session.getAttribute("page"), "");
//            System.out.println("FROM SESSION " + page);
        req.setAttribute("sort", sort);
        req.setAttribute("cards", CardDtoBuilder.getCardsDto(cardService.findByBalance(user.getId(), page, sort)));
        Pagination.paginate(req, cardService.findCount(user.getId()));
//            System.out.println("BEFORE PAGINATE " + page);

        return response;
    }


}
