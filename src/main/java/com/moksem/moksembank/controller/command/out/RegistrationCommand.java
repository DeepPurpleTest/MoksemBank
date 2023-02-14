package com.moksem.moksembank.controller.command.out;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.ClientDto;
import com.moksem.moksembank.model.dto.Dto;
import com.moksem.moksembank.model.dtobuilder.ClientDtoBuilder;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command for register new client
 */
public class RegistrationCommand implements MyCommand {
    UserService service = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.PAGE_REGISTRATION;

        User user = User.builder()
                .name(req.getParameter("name"))
                .surname(req.getParameter("surname"))
                .middleName(req.getParameter("middle-name"))
                .password(req.getParameter("password"))
                .phoneNumber(req.getParameter("phone-number"))
                .build();

        ClientDto clientDto = ClientDtoBuilder.getClientDto(user);
        Validator.validateNewUser(clientDto);

        try {
            if (clientDto.getErrors().isEmpty()) {
                service.findSameNumber(user);
                long id = service.create(user);
                user.setId(id);
                user.setStatus(true);
                session.setAttribute("user", user);
                session.setAttribute("role", Role.USER);

                response = Path.COMMAND_MAIN;
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            }
        } catch (UserCreateException | IOException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        } catch (InvalidPhoneNumberException | PhoneNumberAlreadyTakenException e) {
            e.printStackTrace();
            clientDto.getErrors()
                    .add(new Dto.Param("phone", e.getMessage()));
        }
        req.setAttribute("dto",clientDto);

        return response;
}

}
