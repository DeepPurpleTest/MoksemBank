package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.ClientAccountCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.SessionAttributesUtil;
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
public class ClientAccountCommandTest {
    @InjectMocks
    ClientAccountCommand clientAccountCommand;
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
    void executeShouldReturnRedirect(){
        try (MockedStatic<SessionAttributesUtil> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributesUtil.class)){
            sessionAttributesUtilMockedStatic.when(()->SessionAttributesUtil.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(()->SessionAttributesUtil.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            assertEquals(Path.COMMAND_REDIRECT, clientAccountCommand.execute(req, resp));
        }
    }

    @Test
    void executeShouldReturnErrorPageCausedByIOException() throws IOException {
        try (MockedStatic<SessionAttributesUtil> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributesUtil.class)){
            sessionAttributesUtilMockedStatic.when(()->SessionAttributesUtil.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(()->SessionAttributesUtil.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            doThrow(IOException.class).when(resp).sendRedirect(anyString());
            assertEquals(Path.PAGE_ERROR, clientAccountCommand.execute(req, resp));
        }
    }

    @Test
    void ClientAccountShouldReturnClientPage(){
        User user = User.builder().build();
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                mockStatic(CardDtoBuilder.class)){
            cardDtoBuilderMockedStatic.when(()->CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocate -> null);

            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("user")).thenReturn(user);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("page")).thenReturn(null);
            when(cardService.findCount(user.getId())).thenReturn(1);

            doNothing().when(req).setAttribute(anyString(), any());
            doNothing().when(req).setAttribute(anyString(), anyList());

            assertEquals(Path.PAGE_USER, clientAccountCommand.execute(req, resp));
        }

    }
}
