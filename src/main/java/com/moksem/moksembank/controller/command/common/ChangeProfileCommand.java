package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Entity;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ChangeProfileCommand implements MyCommand {
    private final UserService userService = AppContext.getInstance().getUserService();
    private final AdminService adminService = AppContext.getInstance().getAdminService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //todo сделать профиль изменение
        HttpSession session = req.getSession();
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String middleName = req.getParameter("middle_name");
        String phoneNumber = req.getParameter("phone_number");
        String password = req.getParameter("password");
        String login = req.getParameter("login");
        String response = Path.COMMAND_PROFILE;

        Role role = (Role) session.getAttribute("role");
        Set<String> set;
        if (role.equals(Role.USER)) {
            User client = (User) session.getAttribute("user");
            User clientToChange = User.builder()
                    .name(name.isEmpty() ? client.getName() : name)
                    .surname(surname.isEmpty() ? client.getSurname() : surname)
                    .middleName(middleName.isEmpty() ? client.getMiddleName() : middleName)
                    .phoneNumber(phoneNumber.isEmpty() ? client.getPhoneNumber() : phoneNumber)
                    .password(password.isEmpty() ? client.getPassword() : password)
                    .build();
            clientToChange.setStatus(client.isStatus());
            clientToChange.setId(client.getId());
            set = ValidatorsUtil.validateUser(clientToChange);
            response = update(req, resp, set, clientToChange, response);
        } else {
            Admin admin = (Admin) session.getAttribute("user");
            Admin adminToChange = Admin.builder()
                    .login(login.isEmpty() ? admin.getLogin() : login)
                    .password(password.isEmpty() ? admin.getPassword() : password)
                    .build();
            adminToChange.setId(admin.getId());
            set = ValidatorsUtil.validateAdmin(adminToChange);
            response = update(req, resp, set, adminToChange, response);
        }

        return response;
    }

    public String update(HttpServletRequest req, HttpServletResponse resp, Set<String> set, Entity entity, String response) {
        HttpSession session = req.getSession();
        Role role = (Role) session.getAttribute("role");
        if (!set.isEmpty())
            req.setAttribute("errorMessages", set);
        else {
            if (role.equals(Role.USER))
                userService.update((User) entity);
            else
                adminService.update((Admin) entity);
            session.setAttribute("user", entity);
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

}
