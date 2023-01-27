package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.PaginationUtil;
import com.moksem.moksembank.util.SessionAttributesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class ClientAccountCommand implements MyCommand {
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_ACCOUNT;


        if (req.getParameter("sort") != null) {
            SessionAttributesUtil.clearSession(session);
            SessionAttributesUtil.toSession(req, session);
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
        } else {
//            SessionAttributesUtil.getSessionAttributes(session);
            User user = (User) session.getAttribute("user");
            String sort = (String) Objects.requireNonNullElse(session.getAttribute("sort"), "natural");
            String page = (String) Objects.requireNonNullElse(session.getAttribute("page"), "");
//            System.out.println("FROM SESSION " + page);
            PaginationUtil.paginate(req, cardService.findCount(user.getId()));
            req.setAttribute("sort", sort);
            req.setAttribute("cards", CardDtoBuilder.getCardsDto(cardService.findByBalance(user.getId(), page, sort)));
//            System.out.println("BEFORE PAGINATE " + page);
            response = Path.PAGE_USER;
        }

        return response;
    }


}
