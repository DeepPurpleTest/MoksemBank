package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Entity;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.BlockedUserException;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements MyCommand {
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String login = req.getParameter("login");
        String pass = req.getParameter("password");

        String response = Path.PAGE_LOGIN;

        try {
            User user = userService.findByNumberAndPassword(login, pass);
            Role role = Role.USER;
            toSession(user, role, session);
            response = Path.COMMAND_ACCOUNT;

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidLoginOrPasswordException | InvalidPhoneNumberException e) {
            req.setAttribute("errorMessage", e.getMessage());
        } catch (BlockedUserException e) {
            response = Path.PAGE_ERROR;
            req.setAttribute("errorMessage", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void toSession(Entity entity, Role role, HttpSession session) {
        session.setAttribute("user", entity);
        session.setAttribute("role", role);
    }
}
