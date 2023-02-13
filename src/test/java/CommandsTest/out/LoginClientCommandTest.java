package CommandsTest.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.out.LoginClientCommand;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.BlockedUserException;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginClientCommandTest {
    @InjectMocks
    LoginClientCommand loginClientCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginCommandShouldReturnRedirect() throws InvalidLoginOrPasswordException, BlockedUserException {
        String number = "+380960150636";
        String pass = "456123456123";
        User user = User.builder()
                .phoneNumber(number)
                .password(pass)
                .build();

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("phone_number")).thenReturn(number);
        when(req.getParameter("password")).thenReturn(pass);
        when(userService.findByNumberAndPassword(number, pass)).thenReturn(user);
        doNothing().when(session).setAttribute(anyString(), any());

        assertEquals(Path.COMMAND_REDIRECT, loginClientCommand.execute(req, resp));
    }

    @Test
    void loginCommandShouldReturnPageLogin() throws InvalidLoginOrPasswordException, BlockedUserException {
        String number = "+380960150636";
        String pass = "456123456123";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("phone_number")).thenReturn(number);
        when(req.getParameter("password")).thenReturn(pass);
        when(userService.findByNumberAndPassword(number, pass)).thenThrow(InvalidLoginOrPasswordException.class);
        doNothing().when(req).setAttribute(anyString(), any());

        assertEquals(Path.PAGE_LOGIN, loginClientCommand.execute(req, resp));
    }
}
