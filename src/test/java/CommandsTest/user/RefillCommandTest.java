package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.RefillCommand;
import com.moksem.moksembank.model.dto.Dto;
import com.moksem.moksembank.model.dto.RefillDto;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.PaymentCreateException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;
import com.moksem.moksembank.util.validator.Validator;
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
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//todo DecoratorSet fix percent?
@ExtendWith(MockitoExtension.class)
class RefillCommandTest {
    @InjectMocks
    RefillCommand refillCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    CardService cardService;
    @Mock
    PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void refillCommandShouldReturnRedirect() throws InvalidCardException, UserNotFoundException, PaymentCreateException {
        String cardId = "2";
        String refillId = "1";
        String amount = "200";
        long paymentId = 1;

        Card cardRefill = Card.builder()
                .wallet(BigDecimal.valueOf(0))
                .status(true)
                .build();
        Card cardBeforeUpdate = Card.builder()
                .wallet(BigDecimal.valueOf(0))
                .status(true)
                .build();
        Card cardAfterUpdate = Card.builder()
                .status(true)
                .wallet(new BigDecimal(amount))
                .build();
        Payment payment = Payment.builder()
                .cardSender(cardRefill)
                .cardReceiver(cardBeforeUpdate)
                .amount(new BigDecimal(amount))
                .build();

        cardBeforeUpdate.setId(1);
        cardAfterUpdate.setId(1);
        cardRefill.setId(2);

        try (MockedStatic<Validator> validatorsUtilMockedStatic =
                     mockStatic(Validator.class)) {
            validatorsUtilMockedStatic.when(() -> Validator.validateAmount(amount))
                    .thenAnswer((Answer<Void>) invocate -> null);
            when(req.getParameter("card")).thenReturn(cardId);
            when(req.getParameter("amount")).thenReturn(amount);
            when(cardService.findById(cardId)).thenReturn(cardBeforeUpdate);
            when(cardService.findById(refillId)).thenReturn(cardRefill);
            when(paymentService.create(payment)).thenReturn(paymentId);
            doNothing().when(cardService).update(cardAfterUpdate);

            assertEquals(Path.COMMAND_REDIRECT, refillCommand.execute(req, resp));
        }

    }

    @Test
    void refillCommandShouldReturnRefillPage() throws InvalidCardException, UserNotFoundException {
        String cardId = "1";
        String amount = "200";
        Card cardBeforeUpdate = Card.builder()
                .wallet(BigDecimal.valueOf(0))
                .status(true)
                .build();
        RefillDto refillDto = RefillDto.builder()
                .amount(amount)
                .wallet(String.valueOf(cardBeforeUpdate.getWallet()))
                .status(cardBeforeUpdate.isStatus())
                .build();

        InvalidAmountException invalidAmountException = new InvalidAmountException("123");
        refillDto.getErrors().add(new Dto.Param("amount", invalidAmountException.getMessage()));

        try (MockedStatic<Validator> validatorsUtilMockedStatic =
                     mockStatic(Validator.class)) {
            validatorsUtilMockedStatic.when(() -> Validator.validateAmount(amount))
                    .thenThrow(invalidAmountException);

            when(req.getParameter("card")).thenReturn(cardId);
            when(req.getParameter("amount")).thenReturn(amount);
            when(cardService.findById(cardId)).thenReturn(cardBeforeUpdate);
            doNothing().when(req).setAttribute("dto", refillDto);

            assertEquals(Path.PAGE_REFILL, refillCommand.execute(req, resp));
        }
    }

    @Test
    void refillCommandShouldReturnErrorPageCausedByInvalidCardException() throws InvalidCardException, UserNotFoundException {
        String cardId = "1";

        when(req.getParameter("card")).thenReturn(cardId);
        when(cardService.findById(cardId)).thenThrow(InvalidCardException.class);

        assertEquals(Path.PAGE_ERROR, refillCommand.execute(req, resp));
    }
}
