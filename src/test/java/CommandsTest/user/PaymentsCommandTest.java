package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.PaymentsCommand;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.SessionAttributesUtil;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentsCommandTest {
    @InjectMocks
    PaymentsCommand paymentsCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    PaymentService paymentService;
    @Mock
    CardService cardService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeShouldReturnRedirect() {
        try (MockedStatic<SessionAttributesUtil> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributesUtil.class)) {
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributesUtil.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributesUtil.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            assertEquals(Path.COMMAND_REDIRECT, paymentsCommand.execute(req, resp));
        }
    }

    @Test
    void executeShouldReturnErrorPageCausedByIOException() throws IOException {
        try (MockedStatic<SessionAttributesUtil> sessionAttributesUtilMockedStatic =
                     mockStatic(SessionAttributesUtil.class)) {
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributesUtil.toSession(req, session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            sessionAttributesUtilMockedStatic.when(() -> SessionAttributesUtil.clearSession(session))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(req.getParameter("sort")).thenReturn("natural");
            doThrow(IOException.class).when(resp).sendRedirect(anyString());
            assertEquals(Path.PAGE_ERROR, paymentsCommand.execute(req, resp));
        }
    }

    @Test
    void PaymentsCommandShouldReturnPaymentsPageWithCardNotNull() throws InvalidCardException, InvalidIdException, UserNotFoundException {
        User user = User.builder().build();
        String cardNumber = "412345678912345";
        String page = "1";
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class)) {
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenReturn(UserDto.builder().build());

            when(req.getSession()).thenReturn(session);
            when(req.getParameter("sort")).thenReturn(null);
            when(session.getAttribute("user")).thenReturn(user);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("card")).thenReturn(cardNumber);
            when(session.getAttribute("page")).thenReturn(page);
            when(paymentService.findByUserIdAndCardId(user.getId(), cardNumber, page, "natural")).thenReturn(new ArrayList<>());

            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_PAYMENTS, paymentsCommand.execute(req, resp));
        }
    }

    @Test
    void PaymentsCommandShouldReturnPaymentsPageWithCardIsNull() throws InvalidCardException, UserNotFoundException, InvalidIdException {
        User user = User.builder().build();
        String page = "1";
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class)) {
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenReturn(UserDto.builder().build());

            when(req.getSession()).thenReturn(session);
            when(req.getParameter("sort")).thenReturn(null);
            when(session.getAttribute("user")).thenReturn(user);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("card")).thenReturn(null);
            when(session.getAttribute("page")).thenReturn(page);
            when(paymentService.findByUser(user, page, "natural")).thenReturn(new ArrayList<>());

            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_PAYMENTS, paymentsCommand.execute(req, resp));
        }
    }

    @Test
    void PaymentsCommandShouldReturnErrorPageCausedByInvalidCardException() throws InvalidCardException, UserNotFoundException, InvalidIdException {
        User user = User.builder().build();
        String page = "1";
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class)) {
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(req.getSession()).thenReturn(session);
            when(req.getParameter("sort")).thenReturn(null);
            when(session.getAttribute("user")).thenReturn(user);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("card")).thenReturn(null);
            when(session.getAttribute("page")).thenReturn(page);
            when(paymentService.findByUser(user, page, "natural")).thenThrow(InvalidCardException.class);
            when(cardService.findAllByUserId(user.getId())).thenReturn(new ArrayList<>());
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_ERROR, paymentsCommand.execute(req, resp));
        }
    }
}
