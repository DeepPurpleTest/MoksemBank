package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.RequestUnlockCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserCardNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestUnlockCommandTest {
    @InjectMocks
    RequestUnlockCommand requestUnlockCommand;
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
    void requestCommandShouldReturnRedirect() throws UserNotFoundException, InvalidCardException {
        User user = User.builder().build();
        String cardId = "1";
        Card card = Card.builder()
                .build();
        card.setId(Long.parseLong(cardId));
        long value = 0;

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardId);
        when(session.getAttribute("user")).thenReturn(user);
        when(cardService.findById(cardId, user)).thenReturn(card);
        when(requestService.findByCard(card)).thenReturn(null);
        when(requestService.create(card)).thenReturn(value);

        assertEquals(Path.COMMAND_REDIRECT, requestUnlockCommand.execute(req, resp));
    }

    @Test
    void requestCommandShouldReturnErrorPage() throws UserNotFoundException, InvalidCardException {
        User user = User.builder().build();
        String cardId = "1";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardId);
        when(session.getAttribute("user")).thenReturn(user);
        when(cardService.findById(cardId, user))
                .thenThrow(InvalidCardException.class);

        assertEquals(Path.PAGE_ERROR, requestUnlockCommand.execute(req, resp));
    }
}
