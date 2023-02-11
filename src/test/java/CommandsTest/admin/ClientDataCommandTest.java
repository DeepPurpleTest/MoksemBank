package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.ClientCommand;
import com.moksem.moksembank.controller.command.admin.ClientDataCommand;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientDataCommandTest {
    @InjectMocks
    ClientDataCommand clientDataCommand;
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
    void executeShouldReturnRedirect() {
        try (MockedStatic<SessionAttributes> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributes.class)) {
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributes.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributes.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            assertEquals(Path.COMMAND_REDIRECT, clientDataCommand.execute(req, resp));
        }
    }

    @Test
    void executeShouldReturnErrorPageCausedByIOException() throws IOException {
        try (MockedStatic<SessionAttributes> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributes.class)) {
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributes.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributes.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            doThrow(IOException.class).when(resp).sendRedirect(anyString());
            assertEquals(Path.PAGE_ERROR, clientDataCommand.execute(req, resp));
        }
    }
}
