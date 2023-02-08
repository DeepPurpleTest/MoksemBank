package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.PhoneNumberAlreadyTakenException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BlockClientCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String id = (String) session.getAttribute("id");
        String response = Path.COMMAND_CLIENT_DATA;

        try {
            User user = userService.findById(id);
            user.setStatus(false);
            userService.update(user);

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (UserNotFoundException | PhoneNumberAlreadyTakenException | InvalidPhoneNumberException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }
}
