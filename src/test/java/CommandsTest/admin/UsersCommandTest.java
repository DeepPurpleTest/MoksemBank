package CommandsTest.admin;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.admin.UsersCommand;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.SessionAttributes;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersCommandTest {

    @InjectMocks
    UsersCommand usersCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void executeShouldReturnRedirect(){
//
//        when(()->SessionAttributesUtil.toSession(req, session)).thenAnswer((Answer<Void>) invocation -> null);
//        try (MockedStatic<SessionAttributesUtil> sessionAttributesUtil =
//                     Mockito.mockStatic(SessionAttributesUtil.class)) {
//            when(()->SessionAttributesUtil.toSession(req, session)).thenAnswer((Answer<Void>) invocation -> null);
//        }
//        when(req.getSession()).thenReturn(null);
//        when(session.getAttributeNames()).thenReturn(new Enumeration<String>() {
//            @Override
//            public boolean hasMoreElements() {
//                return false;
//            }
//
//            @Override
//            public String nextElement() {
//                return null;
//            }
//        });
//        when(req.getParameter("sort")).thenReturn("natural");
//        assertEquals(usersCommand.execute(req, resp), "redirect");
//    }


    @Test
    void executeWithSortPhoneShouldReturnPage() throws UserNotFoundException {
        String number = "+380960150636";

        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocate -> null);

            when(session.getAttribute("sort")).thenReturn("phone");
            when(session.getAttribute("page")).thenReturn(null);
            when(session.getAttribute("number")).thenReturn(number);
            when(req.getSession()).thenReturn(session);
            when(userService.findByNumber(number)).thenReturn(User.builder().build());
            doNothing().when(req).setAttribute(anyString(), any());
            doNothing().when(req).setAttribute(anyString(), anyList());

            assertEquals(Path.PAGE_ADMIN, usersCommand.execute(req, resp));
        }

    }

    @Test
    void executeShouldCatchException() throws UserNotFoundException {
        String number = "+380960150636";

        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocate -> null);

            when(session.getAttribute("sort")).thenReturn("phone");
            when(session.getAttribute("page")).thenReturn(null);
            when(session.getAttribute("number")).thenReturn(number);
            when(req.getSession()).thenReturn(session);
            when(userService.findByNumber(number)).thenThrow(UserNotFoundException.class);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_ADMIN, usersCommand.execute(req, resp));
        }
    }

    @Test
    void executeWithSortCardShouldReturnPage() throws InvalidCardException {
        String number = "4123456789123456";

        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocate -> null);

            when(session.getAttribute("sort")).thenReturn("card");
            when(session.getAttribute("page")).thenReturn(null);
            when(session.getAttribute("number")).thenReturn(number);
            when(req.getSession()).thenReturn(session);
            when(userService.findByCard(number)).thenReturn(User.builder().build());
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_ADMIN, usersCommand.execute(req, resp));
        }
    }

    @Test
    void executeWithSortRequestShouldReturnPage() {
        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocate -> null);

            when(session.getAttribute("sort")).thenReturn("request");
            when(session.getAttribute("page")).thenReturn(null);
            when(session.getAttribute("number")).thenReturn(null);
            when(req.getSession()).thenReturn(session);
            when(userService.findByRequest(anyString())).thenReturn(new ArrayList<>());
            when(userService.findUsersWithRequestCount()).thenReturn(1);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_ADMIN, usersCommand.execute(req, resp));
        }
    }

    @Test
    void executeWithSortEmptyShouldReturnPage() {
        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                     mockStatic(UserDtoBuilder.class);
             MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {

            userDtoBuilderMockedStatic.when(() -> UserDtoBuilder.getUsersDto(anyList()))
                    .thenReturn(new ArrayList<>());
            paginationMockedStatic.when(() -> Pagination.paginate(req, 1))
                    .thenAnswer((Answer<Void>) invocate -> null);
            when(session.getAttribute("sort")).thenReturn(null);
            when(session.getAttribute("page")).thenReturn(null);
            when(session.getAttribute("number")).thenReturn(null);
            when(req.getSession()).thenReturn(session);
            when(userService.findAll(anyString())).thenReturn(new ArrayList<>());
            when(userService.findUsersCount()).thenReturn(1);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_ADMIN, usersCommand.execute(req, resp));
        }
    }


}
