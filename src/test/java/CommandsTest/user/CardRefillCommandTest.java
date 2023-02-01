package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.UnlockClientCommand;
import com.moksem.moksembank.controller.command.user.CardRefillCommand;
import com.moksem.moksembank.controller.command.user.RefillCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardRefillCommandTest {
    @InjectMocks
    CardRefillCommand cardRefillCommand;
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
    void CardRefillShouldReturnRefillPage() throws InvalidCardException, UserNotFoundException {
        String cardId = "1";

        when(req.getParameter("card")).thenReturn(cardId);
        when(cardService.findById(cardId)).thenReturn(Card.builder().build());
        doNothing().when(req).setAttribute(anyString(), any());

        assertEquals(Path.PAGE_REFILL, cardRefillCommand.execute(req, resp));
    }

    @Test
    void CardRefillShouldReturnErrorPageCausedByInvalidCardIdException() throws InvalidCardException, UserNotFoundException {
        String cardId = "1";

        when(req.getParameter("card")).thenReturn(cardId);
        when(cardService.findById(cardId)).thenThrow(InvalidIdException.class);

        assertEquals(Path.PAGE_ERROR, cardRefillCommand.execute(req, resp));
    }
}
