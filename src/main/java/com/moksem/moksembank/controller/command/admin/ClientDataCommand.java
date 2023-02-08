package com.moksem.moksembank.controller.command.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.moksem.moksembank.util.SessionAttributes.clearSession;
import static com.moksem.moksembank.util.SessionAttributes.toSession;

public class ClientDataCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_CLIENT;

        if (req.getParameter("sort") != null || req.getParameter("id") != null) {
            clearSession(session);
            toSession(req, session);
        }

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
