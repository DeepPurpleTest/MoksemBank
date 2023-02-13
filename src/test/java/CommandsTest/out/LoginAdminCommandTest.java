package CommandsTest.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.out.LoginAdminCommand;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.service.AdminService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAdminCommandTest {
    @InjectMocks
    LoginAdminCommand loginAdminCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginAdminCommandShouldReturnRedirect() throws InvalidLoginOrPasswordException {
        String login = "ave1";
        String pass = "456123456";
        Admin admin = Admin.builder()
                .login(login)
                .password(pass)
                .build();

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("login")).thenReturn(login);
        when(req.getParameter("password")).thenReturn(pass);
        when(adminService.find(admin)).thenReturn(admin);
        doNothing().when(session).setAttribute(anyString(), any());

        assertEquals(Path.COMMAND_REDIRECT, loginAdminCommand.execute(req, resp));
    }
}
