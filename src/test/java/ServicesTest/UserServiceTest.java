package ServicesTest;

import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.UserRepo;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.moksem.moksembank.util.PasswordHash.encode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        when(userRepo.getClients(anyInt())).thenReturn(new ArrayList<>());
        assertNotNull(userService.findAll(anyString()));
    }

    @Test
    void findByRequestShouldReturnNotNull() {
        when(userRepo.getClientsByRequest(anyInt())).thenReturn(new ArrayList<>());
        assertNotNull(userService.findByRequest(anyString()));
    }

    @Test
    void createShouldReturnId() throws UserCreateException {
        User user = User.builder()
                .password("123")
                .build();
        long id = 1;
        when(userRepo.newUser(user)).thenReturn(id);
        assertEquals(userService.create(user), id);
    }

    @Test
    void updateShouldDoesNotThrowException() {
        User user = User.builder()
                .phoneNumber("+380960150636")
                .password("123")
                .build();
        user.setId(1);
        when(userRepo.getUserByPhoneAndId(user)).thenReturn(null);
        doNothing().when(userRepo).updateUser(user);

        assertDoesNotThrow(() -> userService.update(user));
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
                .password(encode(password))
                .status(true)
                .build();
        when(userRepo.getUser(number)).thenReturn(user);

        assertNotNull(userService.findByNumberAndPassword(number, password));
    }

    @Test
    void findByNumberAndPasswordShouldThrowInvalidLoginOrPasswordException() {
        String number = "+380960150636";
        String password = "456123mr";
        when(userRepo.getUser(number)).thenReturn(null);

        assertThrows(InvalidLoginOrPasswordException.class, () -> userService.findByNumberAndPassword(number, password));
    }

    @Test
    void findByNumberAndPasswordShouldThrowBlockedUserException() {
        String number = "+380960150636";
        String password = "456123mr";
        User user = User.builder()
                .password(encode(password))
                .build();
        when(userRepo.getUser(number)).thenReturn(user);

        assertThrows(BlockedUserException.class, () -> userService.findByNumberAndPassword(number, password));
    }

    @Test
    void findByCardShouldReturnNotNull() throws InvalidCardException {
        String number = "4123456789123456";
        when(userRepo.getUserByCard(number)).thenReturn(User.builder().build());

        assertNotNull(userService.findByCard(number));
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
