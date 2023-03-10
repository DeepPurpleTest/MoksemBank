package ServicesTest;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.CardRepo;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    CardService cardService;
    @Mock
    CardRepo cardRepo;
    @Mock
    UserService userService;

    @BeforeEach
    public void setUp() {
        cardService = new CardService(cardRepo, userService);
    }


    @Test
    void findByBalanceShouldReturnNotNull() {
        String page = "1";
        int pageValue = 1;
        try (MockedStatic<Pagination> paginationMockedStatic =
                mockStatic(Pagination.class)){
            paginationMockedStatic.when(()->Pagination.getPage(page))
                    .thenReturn(pageValue);

            when(cardRepo.getCardsByWalletASC(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getCardsByWalletDESC(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getCards(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());

            assertNotNull(cardService.findByBalance(anyLong(), page, "asc"));
            assertNotNull(cardService.findByBalance(anyLong(), page, "desc"));
            assertNotNull(cardService.findByBalance(anyLong(), page, "qweqwe"));
        }

    }

    @Test
    void findByParametersShouldReturnNotNull() throws InvalidCardException {
        String cardNumber = "4123456789123456";
        Card card = Card.builder()
                .number(cardNumber)
                .build();
        String page = "1";
        int pageValue = 1;

        try (MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {
            paginationMockedStatic.when(() -> Pagination.getPage(page))
                    .thenReturn(pageValue);
            when(cardRepo.getCardsByRequest(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getBlockedCards(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getUnlockedCards(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getCards(anyLong(), eq(pageValue))).thenReturn(new ArrayList<>());
            when(cardRepo.getCard(anyLong(), eq(cardNumber))).thenReturn(card);

            assertNotNull(cardService.findByParameters(anyLong(), page, "request", cardNumber));
            assertNotNull(cardService.findByParameters(anyLong(), page, "blocked", cardNumber));
            assertNotNull(cardService.findByParameters(anyLong(), page, "unlocked", cardNumber));
            assertNotNull(cardService.findByParameters(anyLong(), page, "card", cardNumber));
            assertNotNull(cardService.findByParameters(anyLong(), page, "asda", cardNumber));
        }
    }

    @Test
    void findByParametersShouldThrowException() {
        String cardNumber = "4123456789123456";
        String page = "1";
        int pageValue = 1;

        try (MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {
            paginationMockedStatic.when(() -> Pagination.getPage(page))
                    .thenReturn(pageValue);
            when(cardRepo.getCard(anyLong(), anyString())).thenReturn(null);

            assertThrows(InvalidCardException.class, () -> cardService.findByParameters(anyLong(), page, "card", cardNumber));
        }
    }

    @Test
    void findCountShouldReturnPositiveCount() {
        long value = 1;

        when(cardRepo.getCardsCount(anyLong())).thenReturn(1);
        when(cardRepo.getBlockedCardsCount(anyLong())).thenReturn(1);
        when(cardRepo.getUnlockedCardsCount(anyLong())).thenReturn(1);
        when(cardRepo.getCardsWithRequestCount(anyLong())).thenReturn(1);

        assertTrue(cardService.findCount(value) >= 0);
        assertTrue(cardService.findCount(anyLong(), "request") >= 0);
        assertTrue(cardService.findCount(anyLong(), "blocked") >= 0);
        assertTrue(cardService.findCount(anyLong(), "unlocked") >= 0);
        assertTrue(cardService.findCount(anyLong(), "card") >= 0);
        assertTrue(cardService.findCount(value, "qwe") >= 0);
    }

    @Test
    void findAllByUserIdShouldReturnNotNull() {
        when(cardRepo.getCards(anyLong())).thenReturn(new ArrayList<>());

        assertNotNull(cardService.findAllByUserId(anyLong()));
    }

    @Test
    void findByUserIdAndCardNumberShouldReturnNotNull() throws UserNotFoundException, UserCardNotFoundException {
        String cardNumber = "4123456789123456";
        long id = 1;

        User user = User.builder().build();
        user.setId(id);
        Card card = Card.builder()
                .number(cardNumber)
                .user(user)
                .build();

        card.setId(id);

        when(cardRepo.getCard(id, cardNumber)).thenReturn(card);
        when(userService.findById(String.valueOf(id))).thenReturn(user);

        assertNotNull(cardService.findByUserIdAndCardNumber(id, cardNumber));
    }

    @Test
    void findByUserIdAndCardNumberShouldThrowException() {
        String cardNumber = "4123456789123456";
        long id = 1;

        when(cardRepo.getCard(id, cardNumber)).thenReturn(null);

        assertThrows(UserCardNotFoundException.class, () -> cardService.findByUserIdAndCardNumber(id, cardNumber));
    }

    @Test
    void findByNumberShouldReturnNotNull() throws InvalidCardException, UserNotFoundException {
        String cardNumber = "4123456789123456";
        User user = User.builder()
                .build();
        user.setId(1);
        Card card = Card.builder()
                .user(user)
                .build();

        when(cardRepo.getCard(cardNumber)).thenReturn(card);
        when(userService.findById(String.valueOf(user.getId()))).thenReturn(user);

        assertNotNull(cardService.findByNumber(cardNumber));
    }

    @Test
    void findByNumberShouldThrowException() {
        String cardNumber = "4123456789123456";

        when(cardRepo.getCard(cardNumber)).thenReturn(null);

        assertThrows(InvalidCardException.class, () -> cardService.findByNumber(cardNumber));
    }

    @Test
    void findByIdShouldReturnNotNull() throws InvalidCardException, UserNotFoundException {
        User user = User.builder().build();
        Card card = Card.builder()
                .user(user)
                .build();

        when(cardRepo.getCard(anyLong())).thenReturn(card);
        when(userService.findById(String.valueOf(user.getId()))).thenReturn(user);

        assertNotNull(cardService.findById("1"));
    }

    @Test
    void findByIdShouldThrowException() {
        when(cardRepo.getCard(anyLong())).thenReturn(null);

        assertThrows(InvalidCardException.class, () -> cardService.findById("1"));
    }

    @Test
    void createShouldDoesNotThrowException() {
        Card card = Card.builder().build();

        when(cardRepo.getCard(anyString())).thenReturn(null);
        doNothing().when(cardRepo).addCard(card);

        assertDoesNotThrow(() -> cardService.create(card));
    }

    @Test
    void createShouldDoRecursive() {
        Card card = Card.builder().build();

        when(cardRepo.getCard(anyString())).thenReturn(card, (Card) null);
        doNothing().when(cardRepo).addCard(card);

        assertDoesNotThrow(() -> cardService.create(card));
        verify(cardRepo, times(2)).getCard(anyString());
    }

    @Test
    void updateCardsShouldDoesNotThrowException() throws TransactionException {
        Card first = Card.builder().build();
        Card second = Card.builder().build();

        doNothing().when(cardRepo).updateCards(first, second);

        assertDoesNotThrow(() -> cardService.update(first, second));
    }

    @Test
    void updateCardsShouldThrowException() throws TransactionException {
        Card first = Card.builder().build();
        Card second = Card.builder().build();

        doThrow(TransactionException.class).when(cardRepo).updateCards(first, second);

        assertThrows(TransactionException.class, () -> cardService.update(first, second));
    }

    @Test
    void updateCardShouldDoesNotThrowException() {
        Card card = Card.builder().build();

        doNothing().when(cardRepo).updateCard(card);

        assertDoesNotThrow(() -> cardService.update(card));
    }

    @Test
    void deleteShouldDoesNotThrowException() {
        String card = "123123123123";

        doNothing().when(cardRepo).deleteCard(card);

        assertDoesNotThrow(() -> cardService.delete(card));
    }
}
