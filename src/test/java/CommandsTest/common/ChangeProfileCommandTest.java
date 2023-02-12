package CommandsTest.common;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.common.BlockClientCardCommand;
import com.moksem.moksembank.controller.command.common.ChangeProfileCommand;
import com.moksem.moksembank.model.dto.AdminDto;
import com.moksem.moksembank.model.dto.ClientDto;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.LoginAlreadyTakenException;
import com.moksem.moksembank.util.exceptions.PhoneNumberAlreadyTakenException;
import com.moksem.moksembank.util.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeProfileCommandTest {
    @InjectMocks
    ChangeProfileCommand changeProfileCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    UserService userService;
    @Mock
    AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void changeProfileCommandShouldReturnRedirectWithRoleUser() throws PhoneNumberAlreadyTakenException, InvalidPhoneNumberException {
        String name = "Moksem";
        String surname = "Viskovatov";
        String middle = "Andreich";
        String phone = "+380960150636";
        String pass = "456456456";

        User user = User.builder().build();
        User userToChange = User.builder()
                .name(name)
                .surname(surname)
                .middleName(middle)
                .phoneNumber(phone)
                .password(pass)
                .build();

        ClientDto dto = ClientDto.builder()
                .name(name)
                .surname(surname)
                .middleName(middle)
                .phoneNumber(phone)
                .password(pass)
                .build();

        try (MockedStatic<Validator> validatorMockedStatic =
                     mockStatic(Validator.class)) {
            validatorMockedStatic.when(() -> Validator.validateChangedUser(dto))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("role")).thenReturn(Role.USER);
            when(session.getAttribute("user")).thenReturn(user);
            when(req.getParameter("name")).thenReturn(name);
            when(req.getParameter("surname")).thenReturn(surname);
            when(req.getParameter("middle_name")).thenReturn(middle);
            when(req.getParameter("phone_number")).thenReturn(phone);
            when(req.getParameter("password")).thenReturn(pass);
            doNothing().when(userService).update(userToChange);
            doNothing().when(session).setAttribute(anyString(), any());

            assertEquals(Path.COMMAND_REDIRECT, changeProfileCommand.execute(req, resp));
        }
    }

    @Test
    void changeProfileCommandShouldReturnRedirectWithRoleAdmin() throws LoginAlreadyTakenException {
        String login = "ave1";
        String pass = "456456456";

        Admin admin = Admin.builder().build();
        Admin adminToChange = Admin.builder()
                .login(login)
                .password(pass)
                .build();
        AdminDto dto = AdminDto.builder()
                .login(login)
                .password(pass)
                .build();

        try (MockedStatic<Validator> validatorMockedStatic =
                     mockStatic(Validator.class)) {
            validatorMockedStatic.when(() -> Validator.validateChangedAdmin(dto))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("role")).thenReturn(Role.ADMIN);
            when(session.getAttribute("user")).thenReturn(admin);
            when(req.getParameter("login")).thenReturn(login);
            when(req.getParameter("password")).thenReturn(pass);
            doNothing().when(adminService).update(adminToChange);
            doNothing().when(session).setAttribute(anyString(), any());

            assertEquals(Path.COMMAND_REDIRECT, changeProfileCommand.execute(req, resp));
        }
    }
}
