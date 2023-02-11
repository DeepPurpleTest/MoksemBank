package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.ClientCommand;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCommandTest {
    @InjectMocks
    ClientCommand clientCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    UserService userService;
    @Mock
    CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeShouldReturnClientPage() throws UserNotFoundException, InvalidCardException {
        String id = "1";
        User user = User.builder()
                .build();
        user.setId(1);

        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(session.getAttribute("id")).thenReturn(id);
            when(req.getSession()).thenReturn(session);
            when(userService.findById(id)).thenReturn(user);
            when(cardService.findByParameters(user.getId(), "", "natural", "")).thenReturn(new ArrayList<>());
            when(cardService.findCount(user.getId(), "natural")).thenReturn(1);

            assertEquals(Path.PAGE_CLIENT, clientCommand.execute(req, resp));
        }
    }

    @Test
    void executeShouldReturnErrorPageCausedByUserNotFoundException() throws UserNotFoundException {
        String id = "1";

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("id")).thenReturn(id);
        when(userService.findById(id)).thenThrow(UserNotFoundException.class);
        doNothing().when(req).setAttribute(anyString(), any());

        assertEquals(Path.PAGE_ERROR, clientCommand.execute(req, resp));
    }

    @Test
    void executeShouldReturnClientPageCausedByInvalidCardException() throws UserNotFoundException, InvalidCardException {
        String id = "1";
        User user = User.builder()
                .build();
        user.setId(1);
        String sort = "card";
        String number = "465";
        String page = "";
        UserDto userDto = UserDto.builder().build();

        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUserDto(user)).thenReturn(userDto);
            when(session.getAttribute("id")).thenReturn(id);
            when(session.getAttribute("sort")).thenReturn(sort);
            when(session.getAttribute("number")).thenReturn(number);
            when(session.getAttribute("page")).thenReturn(page);
            doNothing().when(req).setAttribute("client", userDto);
            doNothing().when(req).setAttribute("errorMessage", null);
            when(req.getSession()).thenReturn(session);
            when(userService.findById(id)).thenReturn(user);
            when(cardService.findByParameters(user.getId(), page, sort, number)).thenThrow(InvalidCardException.class);
//        when(cardService.findCount(user.getId(), sort)).thenReturn(1);

            assertEquals(Path.PAGE_CLIENT, clientCommand.execute(req, resp));
        }

    }
}
