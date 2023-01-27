package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.UnlockClientCardCommand;
import com.moksem.moksembank.controller.command.admin.UsersCommand;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.TransactionException;
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
public class UnlockClientCardCommandTest {
    @InjectMocks
    UnlockClientCardCommand unlockClientCardCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    CardService cardService;
    @Mock
    RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeShouldReturnRedirectWithNoyNullRequest() throws InvalidCardException, TransactionException, UserNotFoundException, InvalidIdException {
        String cardNumber = "412345678912345";
        Card card = Card.builder().build();
        Request request = Request.builder().build();
        Admin admin = Admin.builder().build();

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardNumber);
        when(cardService.findByNumber(cardNumber)).thenReturn(card);
        when(requestService.findByCard(card)).thenReturn(request);
        when(session.getAttribute("user")).thenReturn(admin);
        doNothing().when(requestService).updateRequestTransaction(request, card);
        assertEquals(Path.COMMAND_REDIRECT, unlockClientCardCommand.execute(req, resp));
    }

    @Test
    void executeShouldReturnRedirectWithNullRequest() throws InvalidCardException, TransactionException, UserNotFoundException, InvalidIdException {
        String cardNumber = "412345678912345";
        Card card = Card.builder().build();
        Request request = Request.builder().build();
        Admin admin = Admin.builder().build();
        long id = 1;

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardNumber);
        when(cardService.findByNumber(cardNumber)).thenReturn(card);
        when(requestService.findByCard(card)).thenReturn(null);
        when(session.getAttribute("user")).thenReturn(admin);
        when(requestService.create(card)).thenReturn(id);
        when(requestService.findByUserId(id)).thenReturn(request);
        doNothing().when(requestService).updateRequestTransaction(request, card);
        assertEquals(Path.COMMAND_REDIRECT, unlockClientCardCommand.execute(req, resp));
    }

    @Test
    void executeShouldReturnErrorPage() throws InvalidCardException, UserNotFoundException, InvalidIdException {
        String cardNumber = "412345678912345";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardNumber);
        when(cardService.findByNumber(cardNumber)).thenThrow(InvalidCardException.class);
        doNothing().when(req).setAttribute(anyString(), any());

        assertEquals(Path.PAGE_ERROR, unlockClientCardCommand.execute(req, resp));
    }

    @Test
    void executeShouldReturnErrorPageCausedByIOException() throws InvalidCardException, TransactionException, IOException, UserNotFoundException, InvalidIdException {
        String cardNumber = "412345678912345";
        Card card = Card.builder().build();
        Request request = Request.builder().build();
        Admin admin = Admin.builder().build();

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardNumber);
        when(cardService.findByNumber(cardNumber)).thenReturn(card);
        when(requestService.findByCard(card)).thenReturn(request);
        when(session.getAttribute("user")).thenReturn(admin);
        doThrow(IOException.class).when(resp).sendRedirect(anyString());
        doNothing().when(requestService).updateRequestTransaction(request, card);

        assertEquals(Path.PAGE_ERROR, unlockClientCardCommand.execute(req, resp));
    }
}
