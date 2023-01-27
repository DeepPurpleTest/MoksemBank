package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.TransactionCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validators.ValidatorsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionCommandTest {
    @InjectMocks
    TransactionCommand transactionCommand;
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
    void transactionCommandShouldReturnPaymentsPage() throws InvalidCardException, PaymentCreateException, TransactionException, UserNotFoundException, InvalidIdException {
        User userSender = User.builder().name("Moksem").build();
        User userReceiver = User.builder().name("Vodem").build();

        userSender.setId(1);
        userReceiver.setId(2);

        Card cardSender = Card.builder().user(userSender).number("4123456789123456")
                .wallet(BigDecimal.valueOf(5000))
                .build();
        Card cardReceiver = Card.builder().user(userReceiver).number("4789456123789456")
                .wallet(BigDecimal.valueOf(2500))
                .build();

        String amount = "200";
        BigDecimal bd = new BigDecimal(amount);

        Payment beforeUpdatePayment = Payment.builder().cardSender(cardSender)
                .cardReceiver(cardReceiver)
                .amount(bd)
                .build();
        Card updatedCardSender = Card.builder().user(userSender)
                .number("4123456789123456")
                .wallet(BigDecimal.valueOf(4800))
                .build();
        Card updatedCardReceiver = Card.builder().user(userReceiver)
                .number("4789456123789456")
                .wallet(BigDecimal.valueOf(2700))
                .build();
        Payment afterUpdatePayment = Payment.builder().cardSender(updatedCardSender)
                .cardReceiver(updatedCardReceiver)
                .amount(bd)
                .status("sent")
                .build();
        afterUpdatePayment.setId(1);
        long id = 1;
        try (MockedStatic<ValidatorsUtil> validatorsUtilMockedStatic =
                mockStatic(ValidatorsUtil.class)){
            validatorsUtilMockedStatic.when(()->ValidatorsUtil.validateTransaction(cardSender, cardReceiver, amount))
                    .thenReturn(new HashMap<>());
            when(req.getSession()).thenReturn(session);
            when(req.getParameter("card_sender")).thenReturn(cardSender.getNumber());
            when(req.getParameter("card_receiver")).thenReturn(cardReceiver.getNumber());
            when(req.getParameter("amount")).thenReturn(amount);
            when(session.getAttribute("user")).thenReturn(userSender);
            when(cardService.findByUserIdAndCardNumber(userSender.getId(), cardSender.getNumber())).thenReturn(cardSender);
            when(cardService.findByNumber(cardReceiver.getNumber())).thenReturn(cardReceiver);
            when(paymentService.create(beforeUpdatePayment)).thenReturn(id);
            doNothing().when(cardService).update(cardSender, cardReceiver);
            doNothing().when(paymentService).update(afterUpdatePayment);
            assertEquals(Path.COMMAND_REDIRECT, transactionCommand.execute(req, resp));
        }
    }

    @Test
    void transactionCommandReturnPageErrorCausedByIOException() throws InvalidCardException, PaymentCreateException, TransactionException, IOException, UserNotFoundException, InvalidIdException {
        User userSender = User.builder().name("Moksem").build();
        User userReceiver = User.builder().name("Vodem").build();

        userSender.setId(1);
        userReceiver.setId(2);

        Card cardSender = Card.builder().user(userSender).number("4123456789123456")
                .wallet(BigDecimal.valueOf(5000))
                .build();
        Card cardReceiver = Card.builder().user(userReceiver).number("4789456123789456")
                .wallet(BigDecimal.valueOf(2500))
                .build();

        String amount = "200";
        BigDecimal bd = new BigDecimal(amount);

        Payment beforeUpdatePayment = Payment.builder().cardSender(cardSender)
                .cardReceiver(cardReceiver)
                .amount(bd)
                .build();
        Card updatedCardSender = Card.builder().user(userSender)
                .number("4123456789123456")
                .wallet(BigDecimal.valueOf(4800))
                .build();
        Card updatedCardReceiver = Card.builder().user(userReceiver)
                .number("4789456123789456")
                .wallet(BigDecimal.valueOf(2700))
                .build();
        Payment afterUpdatePayment = Payment.builder().cardSender(updatedCardSender)
                .cardReceiver(updatedCardReceiver)
                .amount(bd)
                .status("sent")
                .build();
        afterUpdatePayment.setId(1);
        long id = 1;
        try (MockedStatic<ValidatorsUtil> validatorsUtilMockedStatic =
                     mockStatic(ValidatorsUtil.class)) {
            validatorsUtilMockedStatic.when(() -> ValidatorsUtil.validateTransaction(cardSender, cardReceiver, amount))
                    .thenReturn(new HashMap<>());
            when(req.getSession()).thenReturn(session);
            when(req.getParameter("card_sender")).thenReturn(cardSender.getNumber());
            when(req.getParameter("card_receiver")).thenReturn(cardReceiver.getNumber());
            when(req.getParameter("amount")).thenReturn(amount);
            when(session.getAttribute("user")).thenReturn(userSender);
            when(cardService.findByUserIdAndCardNumber(userSender.getId(), cardSender.getNumber())).thenReturn(cardSender);
            when(cardService.findByNumber(cardReceiver.getNumber())).thenReturn(cardReceiver);
            when(paymentService.create(beforeUpdatePayment)).thenReturn(id);
            doNothing().when(cardService).update(cardSender, cardReceiver);
            doNothing().when(paymentService).update(afterUpdatePayment);
            doThrow(IOException.class).when(resp).sendRedirect(anyString());
            assertEquals(Path.PAGE_ERROR, transactionCommand.execute(req, resp));
        }
    }

}
