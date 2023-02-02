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
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegistrationCommand implements MyCommand {
    private final UserService service = AppContext.getInstance().getUserService();

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
        ValidatorsUtil.validateNewUser(clientDto);
        if(clientDto.getErrors().isEmpty()) {
            try {
                service.findSameNumber(user);
                long id = service.create(user);
                user.setId(id);
                user.setStatus(true);
                session.setAttribute("user", user);
                session.setAttribute("role", Role.USER);

                response = Path.COMMAND_ACCOUNT;
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (UserCreateException | IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            } catch (InvalidPhoneNumberException | PhoneNumberAlreadyTakenException e) {
                e.printStackTrace();
                clientDto.getErrors()
                        .add(new Dto.Param("phone", e.getMessage()));
            } catch (InvalidPasswordException e) {
                e.printStackTrace();
                clientDto.getErrors()
                        .add(new Dto.Param("pass", e.getMessage()));
            } catch (InvalidStringFormat e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("dto", clientDto);

        return response;
    }

}
