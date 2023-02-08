package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.util.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClientMainCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_ACCOUNT;
        SessionAttributes.checkParameters(req, session);

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
