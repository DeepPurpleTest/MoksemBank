package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.CreateCardCommand;
import com.moksem.moksembank.controller.command.user.RequestUnlockCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.RequestService;
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
class CreateCardCommandTest {
    @InjectMocks
    CreateCardCommand createCardCommand;
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
    void createCardCommandShouldReturnRedirect(){
        User user = User.builder().build();
        Card card = Card.builder()
                .user(user)
                .build();
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        doNothing().when(cardService).create(card);

        assertEquals(Path.COMMAND_REDIRECT, createCardCommand.execute(req, resp));
    }
}
