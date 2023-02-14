package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.util.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for relocate parameters to session
 */
public class AdminMainCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        SessionAttributes.checkParameters(req, session);
        String response = Path.COMMAND_USERS;

        try {
            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }
        return response;
    }
}
