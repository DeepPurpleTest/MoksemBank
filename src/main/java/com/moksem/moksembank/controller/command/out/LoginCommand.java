package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.entity.Entity;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.BlockedUserException;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Log4j2
public class LoginCommand implements MyCommand {
    UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String phoneNumber = req.getParameter("phone_number");
        String pass = req.getParameter("password");

        String response = Path.PAGE_LOGIN;

        try {
            User user = userService.findByNumberAndPassword(phoneNumber, pass);
            Role role = Role.USER;
            toSession(user, role, session);
            response = Path.COMMAND_MAIN;

            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (InvalidLoginOrPasswordException e) {
            UserDto userDto = UserDto.builder()
                    .phoneNumber(phoneNumber)
                    .build();

            req.setAttribute("userDto", userDto);
            req.setAttribute("errorMessage", e.getMessage());
        } catch (BlockedUserException e) {
            req.setAttribute("errorMessage", e.getMessage());
            response = Path.PAGE_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }

        return response;
    }

    public void toSession(Entity entity, Role role, HttpSession session) {
        session.setAttribute("user", entity);
        session.setAttribute("role", role);
    }
}
