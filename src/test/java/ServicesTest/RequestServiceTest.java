package ServicesTest;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.RequestRepo;
import com.moksem.moksembank.model.service.RequestService;
import com.moksem.moksembank.util.exceptions.TransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    RequestService requestService;
    @Mock
    RequestRepo requestRepo;

    @BeforeEach
    public void setUp(){
        requestService = new RequestService(requestRepo);
    }

    @Test
    void findByCardShouldReturnNotNull(){
        Card card = Card.builder().build();
        when(requestRepo.getRequest(card)).thenReturn(Request.builder().build());
        assertNotNull(requestService.findByCard(card));
    }

    @Test
    void findByUserIdShouldReturnNotNull(){
        long id = anyLong();
        when(requestRepo.getRequest(id)).thenReturn(Request.builder().build());
        assertNotNull(requestService.findByUserId(id));
    }

    @Test
    void findAllByUserShouldReturnNotNull(){
        User user = User.builder().build();
        when(requestRepo.getRequests(user)).thenReturn(anyList());
        assertNotNull(requestService.findAllByUser(user));
    }

    @Test
    void createShouldReturnPositiveValue(){
        Card card = Card.builder().build();
        long id = 1;
        when(requestRepo.addRequest(card)).thenReturn(id);
        assertTrue(requestService.create(card) >= 0);
    }

    @Test
    void updateShouldDoesNotThrowException(){
        Request request = Request.builder().build();
        doNothing().when(requestRepo).updateRequest(request);
        assertDoesNotThrow(()->requestService.update(request));
    }

    @Test
    void requestTransactionShouldDoesNotThrowException() throws TransactionException {
        Request request = Request.builder().build();
        Card card = Card.builder().build();
        doNothing().when(requestRepo).updateDoubleTransaction(request, card);

        assertDoesNotThrow(()->requestService.updateRequestTransaction(request, card));
    }

    @Test
    void requestTransactionShouldThrowException() throws TransactionException {
        Request request = Request.builder().build();
        Card card = Card.builder().build();
        doThrow(TransactionException.class).when(requestRepo).updateDoubleTransaction(request, card);

        assertThrows(TransactionException.class, ()->requestService.updateRequestTransaction(request, card));
    }
}
