package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.PaginationUtil;
import com.moksem.moksembank.util.SessionAttributesUtil;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

public class UsersCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_USERS;

        if(req.getParameter("sort") != null){
            SessionAttributesUtil.clearSession(session);
            SessionAttributesUtil.toSession(req, session);
            System.out.println("AFTER TOSESSION()");
            SessionAttributesUtil.getSessionAttributes(session);
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
        } else {
            System.out.println("ELSE");
//            getSessionAttributes(session);
            String sort = (String) requireNonNullElse(session.getAttribute("sort"), "natural");
            String number = (String) requireNonNullElse(session.getAttribute("number"), "");
            String page = (String) requireNonNullElse(session.getAttribute("page"), "");
            List<User> clients = new ArrayList<>();
            int maxPages = 0;
            try {
                switch (sort) {
                    case "phone" -> clients.add(userService.findByNumber(number));
                    case "card" -> clients.add(userService.findByCard(number));
                    case "request" -> {
                        clients.addAll(userService.findByRequest(page));
                        maxPages = userService.findUsersWithRequestCount();
                    }
                    default -> {
                        clients.addAll(userService.findAll(page));
                        maxPages = userService.findUsersCount();
                    }
                }
            }   catch (InvalidPhoneNumberException | InvalidCardException | UserNotFoundException e) {
                req.setAttribute("exception", e.getMessage());
            }
            req.setAttribute("sort", sort);
            req.setAttribute("clients", UserDtoBuilder.getUsersDto(clients));
            req.setAttribute("cardOrPhoneNumber", number);
            PaginationUtil.paginate(req, maxPages);
//            req.setAttribute("pages", getPages(maxPages));
            response = Path.PAGE_ADMIN;
        }

        return response;
    }
}
