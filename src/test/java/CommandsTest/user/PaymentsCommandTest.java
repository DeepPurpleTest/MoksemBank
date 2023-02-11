package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.PaymentsCommand;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.PaymentDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.SessionAttributes;
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
    void PaymentsCommandShouldReturnPaymentsPageWithCardNotNull() throws InvalidCardException, UserNotFoundException {
        String page = "1";
        String cardId = "1";
        User user = User.builder().build();
        Card card = Card.builder()
                .user(user)
                .number("412345678912345")
                .build();
        card.setId(Long.parseLong(cardId));


        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<PaymentDtoBuilder> paymentDtoBuilderMockedStatic =
                     mockStatic(PaymentDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenAnswer((Answer<Void>) invocation -> null);
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardDto(Card.builder().build()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paymentDtoBuilderMockedStatic.when(() -> PaymentDtoBuilder.getPaymentsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("user")).thenReturn(user);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("card")).thenReturn(cardId);
            when(session.getAttribute("page")).thenReturn(page);
            when(paymentService.findByUserIdAndCardId(card, page, "natural"))
                    .thenReturn(new ArrayList<>());
            when(cardService.findById(cardId, user)).thenReturn(card);
            when(paymentService.findCountByCard(cardId)).thenReturn(1);

            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_PAYMENTS, paymentsCommand.execute(req, resp));
        }
    }

    @Test
    void PaymentsCommandShouldReturnPaymentsPageWithCardIsNull() throws InvalidCardException, UserNotFoundException {
        User user = User.builder().build();
        String page = "1";
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<PaymentDtoBuilder> paymentDtoBuilderMockedStatic =
                     mockStatic(PaymentDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenAnswer((Answer<Void>) invocation -> null);
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardDto(Card.builder().build()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paymentDtoBuilderMockedStatic.when(() -> PaymentDtoBuilder.getPaymentsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(req.getSession()).thenReturn(session);
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
    void PaymentsCommandShouldReturnErrorPageCausedByInvalidCardException() throws InvalidCardException, UserNotFoundException {
        User user = User.builder().build();
        String page = "1";
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                     mockStatic(CardDtoBuilder.class);
             MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<PaymentDtoBuilder> paymentDtoBuilderMockedStatic =
                     mockStatic(PaymentDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user))
                    .thenAnswer((Answer<Void>) invocation -> null);
            cardDtoBuilderMockedStatic.when(() -> CardDtoBuilder.getCardDto(Card.builder().build()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paymentDtoBuilderMockedStatic.when(() -> PaymentDtoBuilder.getPaymentsDto(anyList()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(req.getSession()).thenReturn(session);
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
