package CommandsTest.common;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.common.BlockClientCardCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockClientCardCommandTest {
    @InjectMocks
    BlockClientCardCommand blockClientCardCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void blockClientCardCommandShouldReturnRedirectWithRoleUser() throws UserNotFoundException, InvalidCardException {
        String cardId = "1";
        User user = User.builder().build();
        Card card = Card.builder()
                .user(user)
                .build();
        card.setId(Long.parseLong(cardId));

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardId);
        when(session.getAttribute("user")).thenReturn(user);
        when(session.getAttribute("role")).thenReturn(Role.USER);
        when(cardService.findById(cardId, user)).thenReturn(card);
        doNothing().when(cardService).update(card);

        assertEquals(Path.COMMAND_REDIRECT, blockClientCardCommand.execute(req, resp));
    }

    @Test
    void blockClientCardCommandShouldReturnRedirectWithRoleAdmin() throws UserNotFoundException, InvalidCardException {
        String cardId = "1";
        Card card = Card.builder()
                .build();
        card.setId(Long.parseLong(cardId));

        when(req.getSession()).thenReturn(session);
        when(req.getParameter("card")).thenReturn(cardId);
        when(session.getAttribute("role")).thenReturn(Role.ADMIN);
        when(cardService.findById(cardId)).thenReturn(card);
        doNothing().when(cardService).update(card);

        assertEquals(Path.COMMAND_REDIRECT, blockClientCardCommand.execute(req, resp));
    }
}
