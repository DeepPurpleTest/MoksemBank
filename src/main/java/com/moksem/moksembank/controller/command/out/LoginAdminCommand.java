package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Entity;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for login admin
 */
public class LoginAdminCommand implements MyCommand {
    AdminService adminService = AppContext.getInstance().getAdminService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String login = req.getParameter("login");
        String pass = req.getParameter("password");

        String response = Path.PAGE_LOGIN_ADMIN;
        try {
            Admin tempAdmin = Admin.builder()
                    .login(login)
                    .password(pass)
                    .build();

            Admin admin = adminService.find(tempAdmin);
            Role role = Role.ADMIN;
            toSession(admin, role, session);
            response = Path.COMMAND_ADMIN_MAIN;

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidLoginOrPasswordException e) {
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
