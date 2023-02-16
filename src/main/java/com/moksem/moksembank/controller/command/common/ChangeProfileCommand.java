package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.AdminDto;
import com.moksem.moksembank.model.dto.ClientDto;
import com.moksem.moksembank.model.dto.Dto;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.LoginAlreadyTakenException;
import com.moksem.moksembank.util.exceptions.PhoneNumberAlreadyTakenException;
import com.moksem.moksembank.util.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for change client and admin account information
 */
public class ChangeProfileCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();
    AdminService adminService = AppContext.getInstance().getAdminService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_PROFILE;

        Role role = (Role) session.getAttribute("role");
        Dto dto;

        if (role.equals(Role.USER)) {
            dto = getClientDto(req);
            Validator.validateChangedClient((ClientDto) dto);
            try {
                if (dto.getErrors().isEmpty()) {
                    User client = (User) session.getAttribute("user");
                    User clientToChange = getUserToChange(client, req);
                    userService.update(clientToChange);
                    session.setAttribute("user", clientToChange);
                }
            } catch (PhoneNumberAlreadyTakenException | InvalidPhoneNumberException e) {
                dto.getErrors().add(new Dto.Param("phone", e.getMessage()));

            }
        } else {
            dto = getAdminDto(req);
            Validator.validateChangedAdmin((AdminDto) dto);
            try {
                if (dto.getErrors().isEmpty()) {
                    Admin admin = (Admin) session.getAttribute("user");
                    Admin adminToChange = getAdminToChange(admin, req);
                    adminService.update(adminToChange);
                    session.setAttribute("user", adminToChange);
                }
            } catch (LoginAlreadyTakenException e) {
                dto.getErrors().add(new Dto.Param("login", e.getMessage()));
            }
        }

        if (dto.getErrors().isEmpty()) {
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
        }

        req.setAttribute("dto", dto);
        return response;
    }

    public ClientDto getClientDto(HttpServletRequest req) {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String middleName = req.getParameter("middle_name");
        String phoneNumber = req.getParameter("phone_number");
        String password = req.getParameter("password");
        return ClientDto.builder()
                .name(name)
                .surname(surname)
                .middleName(middleName)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    public AdminDto getAdminDto(HttpServletRequest req) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        return AdminDto.builder()
                .login(login)
                .password(password)
                .build();
    }

    public User getUserToChange(User client, HttpServletRequest req) {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String middleName = req.getParameter("middle_name");
        String phoneNumber = req.getParameter("phone_number");
        String password = req.getParameter("password");
        User clientToChange = User.builder()
                .name(name.isEmpty() ? client.getName() : name)
                .surname(surname.isEmpty() ? client.getSurname() : surname)
                .middleName(middleName.isEmpty() ? client.getMiddleName() : middleName)
                .phoneNumber(phoneNumber.isEmpty() ? client.getPhoneNumber() : phoneNumber)
                .password(password.isEmpty() ? client.getPassword() : password)
                .build();
        clientToChange.setStatus(client.isStatus());
        clientToChange.setId(client.getId());
        return clientToChange;
    }

    public Admin getAdminToChange(Admin admin, HttpServletRequest req) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        Admin adminToChange = Admin.builder()
                .login(login.isEmpty() ? admin.getLogin() : login)
                .password(password.isEmpty() ? admin.getPassword() : password)
                .build();
        adminToChange.setId(admin.getId());
        return adminToChange;
    }
}
