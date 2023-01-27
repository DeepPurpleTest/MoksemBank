package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.RefillCommand;
import com.moksem.moksembank.controller.command.user.TransactionCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefillCommandTest {
    @InjectMocks
    RefillCommand refillCommand;
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
    void refillCommandShouldReturnRedirect() throws InvalidCardException, InvalidIdException {
        String cardId = "1";
        Card cardBeforeUpdate = Card.builder()
                .wallet(BigDecimal.valueOf(0))
                .status(true)
                .build();
        Card cardAfterUpdate = Card.builder()
                .status(true)
                .wallet(BigDecimal.valueOf(200))
                .build();

        when(req.getParameter("card")).thenReturn(cardId);
        when(req.getParameter("amount")).thenReturn("200");
        when(cardService.findById(cardId)).thenReturn(cardBeforeUpdate);
        doNothing().when(cardService).update(cardAfterUpdate);

        assertEquals(Path.COMMAND_REDIRECT, refillCommand.execute(req, resp));
    }

    @Test
    void refillCommandShouldReturnRefillPage() throws InvalidCardException, InvalidIdException {
        String cardId = "1";
        String amount = "200";
        Card cardBeforeUpdate = Card.builder()
                .wallet(BigDecimal.valueOf(0))
                .status(true)
                .build();
        try (MockedStatic<ValidatorsUtil> validatorsUtilMockedStatic =
                     mockStatic(ValidatorsUtil.class)) {
            validatorsUtilMockedStatic.when(() -> ValidatorsUtil.validateAmount(amount))
                    .thenThrow(InvalidAmountException.class);
            when(req.getParameter("card")).thenReturn(cardId);
            when(req.getParameter("amount")).thenReturn(amount);
            when(cardService.findById(cardId)).thenReturn(cardBeforeUpdate);

            assertEquals(Path.PAGE_REFILL, refillCommand.execute(req, resp));
        }
    }

    @Test
    void refillCommandShouldReturnErrorPageCausedByInvalidCardException() throws InvalidCardException, InvalidIdException {
        String cardId = "1";

        when(req.getParameter("card")).thenReturn(cardId);
        when(cardService.findById(cardId)).thenThrow(InvalidCardException.class);

        assertEquals(Path.PAGE_ERROR, refillCommand.execute(req, resp));
    }
}
