package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Admin login page command
 */
public class AdminLoginPageCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return Path.PAGE_LOGIN_ADMIN;
    }
}
