package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

public class I18NCommand implements MyCommand {
    //todo доделать интернационализацию
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        String response = req.getContextPath() + "controller?action=" + req.getParameter("redirect_action");
        String fmtLocale = "javax.servlet.jsp.jstl.fmt.locale";

        String locale = null;
        if (req.getParameter("language") != null)
            locale = req.getParameter("language").trim();

        if (checkLocale(locale))
            Config.set(session, fmtLocale, locale);
        else
            Config.set(session, fmtLocale, Path.LOCALE_NAME_EN);

        try {
            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }

    public boolean checkLocale(String locale) {
        if (locale == null)
            return false;
        return locale.equals(Path.LOCALE_NAME_EN) || locale.equals(Path.LOCALE_NAME_UA);
    }
}
