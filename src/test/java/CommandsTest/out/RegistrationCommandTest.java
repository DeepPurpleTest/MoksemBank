package CommandsTest.out;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.out.RegistrationCommand;
import com.moksem.moksembank.model.dto.ClientDto;
import com.moksem.moksembank.model.dtobuilder.ClientDtoBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import com.moksem.moksembank.util.exceptions.PhoneNumberAlreadyTakenException;
import com.moksem.moksembank.util.exceptions.UserCreateException;
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
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationCommandTest {
    @InjectMocks
    RegistrationCommand registrationCommand;
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

    @Test
    void registrationCommandShouldReturnRedirect() throws PhoneNumberAlreadyTakenException, InvalidPhoneNumberException, UserCreateException {
        User user = User.builder()
                .name("Moksem")
                .surname("Viskovatov")
                .middleName("Andreich")
                .phoneNumber("+380960150636")
                .password("45125564")
                .build();

        ClientDto clientDto = ClientDto.builder()
                .build();

        try (MockedStatic<ClientDtoBuilder> clientDtoBuilderMockedStatic =
                     mockStatic(ClientDtoBuilder.class);
             MockedStatic<Validator> validatorMockedStatic =
                     mockStatic(Validator.class)) {

            clientDtoBuilderMockedStatic.when(() -> ClientDtoBuilder.getClientDto(user))
                    .thenReturn(clientDto);
            validatorMockedStatic.when(() -> Validator.validateNewUser(clientDto))
                    .thenAnswer((Answer<Void>) invocation -> null);

            when(req.getSession()).thenReturn(session);
            when(req.getParameter("name")).thenReturn(user.getName());
            when(req.getParameter("surname")).thenReturn(user.getSurname());
            when(req.getParameter("middle-name")).thenReturn(user.getMiddleName());
            when(req.getParameter("phone-number")).thenReturn(user.getPhoneNumber());
            when(req.getParameter("password")).thenReturn(user.getPassword());
            when(userService.create(user)).thenReturn(user.getId());
            doNothing().when(session).setAttribute(anyString(), any());
            doNothing().when(userService).findSameNumber(user);

            assertEquals(Path.COMMAND_REDIRECT, registrationCommand.execute(req, resp));
        }

    }
}
