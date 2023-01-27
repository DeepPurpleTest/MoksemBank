package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

public class I18NCommand implements MyCommand {
    //todo доделать интернационализацию
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        String fmtLocale = "javax.servlet.jsp.jstl.fmt.locale";

        String locale = null;
        if(req.getParameter("language") != null)
            locale = req.getParameter("language").trim();

        if(checkLocale(locale))
            Config.set(session, fmtLocale, locale);
        else
            Config.set(session, fmtLocale, Path.LOCALE_NAME_EN);

        Role role = (Role) session.getAttribute("role");

        if(role == null)
            return Path.PAGE_LOGIN;

        return role.equals(Role.USER)? Path.COMMAND_ACCOUNT : Path.COMMAND_USERS;
    }

    public boolean checkLocale(String locale){
        if(locale == null)
            return false;
        return locale.equals(Path.LOCALE_NAME_EN) || locale.equals(Path.LOCALE_NAME_UA);
    }
}
