package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.ClientAccountCommand;
import com.moksem.moksembank.controller.command.user.ClientMainCommand;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.SessionAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientMainCommandTest {
    @InjectMocks
    ClientMainCommand clientMainCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;

    @Test
    void executeShouldReturnRedirect(){
        try (MockedStatic<SessionAttributes> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributes.class)){
            sessionAttributesUtilMockedStatic.when(()-> SessionAttributes.checkParameters(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);

            assertEquals(Path.COMMAND_REDIRECT, clientMainCommand.execute(req, resp));
        }
    }

    @Test
    void executeShouldReturnErrorPageCausedByIOException() throws IOException {
        try (MockedStatic<SessionAttributes> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributes.class)){
            sessionAttributesUtilMockedStatic.when(()-> SessionAttributes.checkParameters(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);

            doThrow(IOException.class).when(resp).sendRedirect(anyString());
            assertEquals(Path.PAGE_ERROR, clientMainCommand.execute(req, resp));
        }
    }
}
