package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.UnlockClientCardCommand;
import com.moksem.moksembank.controller.command.admin.UnlockClientCommand;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnlockClientCommandTest {
    @InjectMocks
    UnlockClientCommand unlockClientCommand;
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
    void unlockClientCommandShouldReturnRedirect() throws UserNotFoundException, InvalidIdException {
        String id = "1";
        User user = User.builder().build();

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(id);
        when(userService.findById(id)).thenReturn(user);
        doNothing().when(userService).update(user);

        assertEquals(Path.COMMAND_REDIRECT, unlockClientCommand.execute(req, resp));
    }

    @Test
    void unlockClientCommandShouldReturnErrorPageCausedByUserNotFoundException() throws UserNotFoundException, InvalidIdException {
        String id = "1";

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(id);
        when(userService.findById(id)).thenThrow(UserNotFoundException.class);
        doNothing().when(req).setAttribute(anyString(), any());

        assertEquals(Path.PAGE_ERROR, unlockClientCommand.execute(req, resp));
    }

    @Test
    void unlockClientCommandShouldReturnErrorPageCausedByIOException() throws UserNotFoundException, InvalidIdException, IOException {
        String id = "1";
        User user = User.builder().build();

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(id);
        when(userService.findById(id)).thenReturn(user);
        doNothing().when(userService).update(user);
        doThrow(IOException.class).when(resp).sendRedirect(anyString());

        assertEquals(Path.PAGE_ERROR, unlockClientCommand.execute(req, resp));
    }
}
