package CommandsTest.user;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.user.TransferCommand;
import com.moksem.moksembank.model.dto.TransferDto;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferCommandTest {
    @InjectMocks
    TransferCommand transferCommand;
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
    void transferCommandShouldReturnTransferPage(){
        User user = User.builder().build();
        TransferDto dto = TransferDto.builder().build();
        try (MockedStatic<CardDtoBuilder> cardDtoBuilderMockedStatic =
                mockStatic(CardDtoBuilder.class)){

            cardDtoBuilderMockedStatic.when(()-> CardDtoBuilder.getCardsDto(anyList()))
                    .thenReturn(new ArrayList<>());

            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("user")).thenReturn(user);
            when(cardService.findAllByUserId(user.getId())).thenReturn(new ArrayList<>());
            when(req.getAttribute("dto")).thenReturn(null);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_TRANSFER, transferCommand.execute(req, resp));
        }
    }
}
