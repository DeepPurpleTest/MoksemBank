package ServicesTest;

import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.UserRepo;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.PasswordHash;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static com.moksem.moksembank.util.PasswordHash.encode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    UserService userService;
    @Mock
    UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepo);
    }

    @Test
    void findAllShouldReturnNotNull() {
        try (MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {
            paginationMockedStatic.when(() -> Pagination.getPage(anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getClients(anyInt())).thenReturn(new ArrayList<>());

            assertNotNull(userService.findAll(anyString()));
        }

    }

    @Test
    void findByRequestShouldReturnNotNull() {
        try (MockedStatic<Pagination> paginationMockedStatic =
                     mockStatic(Pagination.class)) {
            paginationMockedStatic.when(() -> Pagination.getPage(anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getClientsByRequest(anyInt())).thenReturn(new ArrayList<>());

            assertNotNull(userService.findByRequest(anyString()));
        }
    }

    @Test
    void createShouldReturnId() throws UserCreateException {
        User user = User.builder()
                .password("123")
                .build();
        long id = 1;

        try (MockedStatic<PasswordHash> passwordHashMockedStatic =
                     mockStatic(PasswordHash.class)) {
            passwordHashMockedStatic.when(() -> PasswordHash.encode(anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.newUser(user)).thenReturn(id);

            assertEquals(userService.create(user), id);
        }
    }

    @Test
    void updateShouldDoesNotThrowException() {
        User user = User.builder()
                .phoneNumber("+380960150636")
                .password("123")
                .build();
        user.setId(1);

        try (MockedStatic<PasswordHash> passwordHashMockedStatic =
                     mockStatic(PasswordHash.class);
        MockedStatic<Validator> validatorMockedStatic =
                mockStatic(Validator.class)) {
            passwordHashMockedStatic.when(() -> PasswordHash.encode(anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            validatorMockedStatic.when(()->Validator.validatePhoneNumber(user.getPhoneNumber()))
                            .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getUserByPhoneAndId(user)).thenReturn(null);
            doNothing().when(userRepo).updateUser(user);

            assertDoesNotThrow(() -> userService.update(user));
        }
    }

    @Test
    void findByIdShouldReturnNotNull() throws UserNotFoundException {
        String id = "1";
        when(userRepo.getUser(anyInt())).thenReturn(User.builder().build());

        assertNotNull(userService.findById(id));
    }

    @Test
    void findByIdShouldThrowException() {
        String id = "1";
        when(userRepo.getUser(anyInt())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findByNumberShouldReturnNotNull() throws UserNotFoundException {
        String number = "+380960150636";
        when(userRepo.getUser(number)).thenReturn(User.builder().build());

        assertNotNull(userService.findByNumber(number));
    }

    @Test
    void findByNumberShouldThrowException() {
        String number = "+380960150636";
        when(userRepo.getUser(number)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findByNumber(number));
    }

//    @Test
//    void findSameNumberShouldReturnNotNull() throws InvalidPhoneNumberException {
//        String number = "+380960150636";
//        User user = User.builder()
//                .phoneNumber(number)
//                .build();
//        when(userRepo.getUserByPhone(user)).thenReturn(User.builder().build());
//
//        assertNotNull(userService.findSameNumber(user));
//    }

    @Test
    void findByNumberAndPasswordShouldReturnNotNull() throws InvalidLoginOrPasswordException, BlockedUserException {
        String number = "+380960150636";
        String password = "456123mr";
        User user = User.builder()
                .password(password)
                .status(true)
                .build();

        try (MockedStatic<PasswordHash> passwordHashMockedStatic =
                     mockStatic(PasswordHash.class)) {
            passwordHashMockedStatic.when(() -> PasswordHash.verify(anyString(), anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getUser(number)).thenReturn(user);

            assertNotNull(userService.findByNumberAndPassword(number, password));
        }
    }

    @Test
    void findByNumberAndPasswordShouldThrowInvalidLoginOrPasswordException() {
        String number = "+380960150636";
        String password = "456123mr";

        try (MockedStatic<PasswordHash> passwordHashMockedStatic =
                     mockStatic(PasswordHash.class)) {
            passwordHashMockedStatic.when(() -> PasswordHash.verify(anyString(), anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getUser(number)).thenReturn(null);

            assertThrows(InvalidLoginOrPasswordException.class, () -> userService.findByNumberAndPassword(number, password));
        }
    }

    @Test
    void findByNumberAndPasswordShouldThrowBlockedUserException() {
        String number = "+380960150636";
        String password = "456123mr";
        User user = User.builder()
                .password(password)
                .build();

        try (MockedStatic<PasswordHash> passwordHashMockedStatic =
                     mockStatic(PasswordHash.class)) {
            passwordHashMockedStatic.when(() -> PasswordHash.verify(anyString(), anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getUser(number)).thenReturn(user);

            assertThrows(BlockedUserException.class, () -> userService.findByNumberAndPassword(number, password));
        }
    }

    @Test
    void findByCardShouldReturnNotNull() throws InvalidCardException {
        String number = "4123456789123456";

        try (MockedStatic<Validator> validatorMockedStatic =
                     mockStatic(Validator.class)) {
            validatorMockedStatic.when(() -> Validator.validateCardNumber(number))
                    .thenAnswer((Answer<Void>) invocation -> null);
            when(userRepo.getUserByCard(number)).thenReturn(User.builder().build());

            assertNotNull(userService.findByCard(number));
        }
    }

    @Test
    void findUsersCountShouldReturnPositiveValue() {
        when(userRepo.getUsersCount()).thenReturn(1);
        assertTrue(userService.findUsersCount() >= 0);
    }

    @Test
    void findUsersWithRequestCountShouldReturnPositiveValue() {
        when(userRepo.getUsersRequestCount()).thenReturn(1);
        assertTrue(userService.findUsersWithRequestCount() >= 0);
    }
}
