package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.AdminMainCommand;
import com.moksem.moksembank.util.SessionAttributes;
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
class AdminMainCommandTest {
    @InjectMocks
    AdminMainCommand adminMainCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adminMainCommandShouldReturnRedirect(){
        try (MockedStatic<SessionAttributes> sessionAttributesMockedStatic =
                mockStatic(SessionAttributes.class)){

            sessionAttributesMockedStatic.when(()->SessionAttributes.checkParameters(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getSession()).thenReturn(session);

            assertEquals(Path.COMMAND_REDIRECT, adminMainCommand.execute(req, resp));
        }
    }
}
