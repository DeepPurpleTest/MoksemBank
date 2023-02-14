package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout command
 */
public class LogoutCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if(session != null)
            session.invalidate();
        return Path.PAGE_LOGIN;
    }
}
