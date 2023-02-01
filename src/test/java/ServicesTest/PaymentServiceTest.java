package ServicesTest;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.PaymentRepo;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    PaymentService paymentService;
    @Mock
    PaymentRepo paymentRepo;
    @Mock
    CardService cardService;

    @BeforeEach
    public void setUp(){
        paymentService = new PaymentService(paymentRepo, cardService);
    }

    @Test
    void findByCardShouldReturnNotNull() throws InvalidCardException, InvalidIdException, UserNotFoundException {
        String cardId = "654654654";
        String page = "1";

        User user = User.builder().build();
        Card card = Card.builder()
                .user(user)
                .build();
        when(cardService.findById(cardId)).thenReturn(card);
        when(paymentRepo.getPaymentsByCardDESC(eq(card), anyInt())).thenReturn(new ArrayList<>());
        when(paymentRepo.getPaymentsByCardASC(eq(card), anyInt())).thenReturn(new ArrayList<>());

        assertNotNull(paymentService.findByUserIdAndCardId(user.getId(), cardId, page, "asc"));
        assertNotNull(paymentService.findByUserIdAndCardId(user.getId(), cardId, page, "desc"));
    }

    @Test
    void findByUserShouldReturnNotNull() throws InvalidCardException, UserNotFoundException, InvalidIdException {
        String page = "1";
        User user = User.builder().build();
        when(paymentRepo.getPaymentsByUserDESC(eq(user), anyInt())).thenReturn(new ArrayList<>());
        when(paymentRepo.getPaymentsByUserASC(eq(user), anyInt())).thenReturn(new ArrayList<>());

        assertNotNull(paymentService.findByUser(user, page, "asc"));
        assertNotNull(paymentService.findByUser(user, page, "desc"));
    }

    @Test
    void findCountByUserShouldReturnPositiveCount(){
        User user = User.builder().build();
        when(paymentRepo.getCountByUser(user)).thenReturn(1);

        assertTrue(paymentService.findCountByUser(user) >= 0);
    }

    @Test
    void findCountByCardShouldReturnPositiveCount() throws InvalidCardException, InvalidIdException, UserNotFoundException {
        String cardId = "1";
        Card card = Card.builder().build();
        when(cardService.findById(cardId)).thenReturn(card);
        when(paymentRepo.getCountByCard(card.getNumber())).thenReturn(1);

        assertTrue(paymentService.findCountByCard(cardId) >= 0);
    }

    @Test
    void findShouldDoesNotThrowException() {
        long userId = 1;
        String paymentId = "1";
        when(paymentRepo.getPayment(eq(userId), anyInt())).thenReturn(Payment.builder().build());

        assertDoesNotThrow(()->paymentService.find(userId, paymentId));
    }

    @Test
    void findShouldThrowException() {
        long userId = 1;
        String paymentId = "1";
        when(paymentRepo.getPayment(eq(userId), anyInt())).thenReturn(null);

        assertThrows(PaymentNotFoundException.class, ()->paymentService.find(userId, paymentId));
    }

    @Test
    void createShouldReturnPositivePaymentId() throws PaymentCreateException {
        Payment payment = Payment.builder()
                .cardSender(Card.builder()
                        .number("123123")
                        .build())
                .cardReceiver(Card.builder()
                        .number("323231")
                        .build())
                .build();
        long id = 1;
        when(paymentRepo.createPayment(payment)).thenReturn(id);

        assertTrue(paymentService.create(payment) >= 0);
    }

    @Test
    void createShouldThrowException() {
        Payment payment = Payment.builder()
                .cardSender(Card.builder().build())
                .cardReceiver(Card.builder().build())
                .build();
        assertThrows(PaymentCreateException.class, ()->paymentService.create(payment));
    }

    @Test
    void updateShouldDoesNotThrowException(){
        Payment payment = Payment.builder().build();
        doNothing().when(paymentRepo).update(payment);

        assertDoesNotThrow(()->paymentService.update(payment));
    }

    @Test
    void deleteShouldNotThrowException(){
        Payment payment = Payment.builder().build();
        doNothing().when(paymentRepo).delete(payment);

        assertDoesNotThrow(()->paymentService.delete(payment));
    }

    @Test
    void toFullCardShouldDoesNotThrowException() throws InvalidCardException, UserNotFoundException, InvalidIdException {
        String cardNumber = "4123456789123456";
        Payment payment = Payment.builder()
                .cardSender(Card.builder()
                        .number(cardNumber)
                        .build())
                .cardReceiver(Card.builder()
                        .number(cardNumber)
                        .build())
                .build();
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        when(cardService.findByNumber(cardNumber)).thenReturn(Card.builder().build());

        assertDoesNotThrow(()->paymentService.toFullCards(paymentList));

    }
}
