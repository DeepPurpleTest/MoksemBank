package ServicesTest;

import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.repo.AdminRepo;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.util.PasswordHashUtil;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import com.moksem.moksembank.util.exceptions.LoginAlreadyTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    AdminService adminService;
    @Mock
    AdminRepo adminRepo;

    @BeforeEach
    public void setUp(){
        adminService = new AdminService(adminRepo);
    }

    @Test
    void getAdminShouldReturnNotNull() throws InvalidLoginOrPasswordException {
        Admin adminFromDB = Admin.builder()
                .password(PasswordHashUtil.encode("qqweqwe"))
                .build();
        Admin adminFromForm = Admin.builder()
                .login(anyString())
                .password("qqweqwe")
                .build();
        when(adminRepo.getAdmin(adminFromForm.getLogin())).thenReturn(adminFromDB);
        assertNotNull(adminService.find(adminFromForm));
    }

    @Test
    void getAdminShouldThrowException() {
        Admin admin = Admin.builder().build();
        when(adminRepo.getAdmin(admin.getLogin())).thenReturn(null);
        assertThrows(InvalidLoginOrPasswordException.class, () -> adminService.find(admin));
    }

    @Test
    void findSameLoginShouldThrowLoginAlreadyTakenException(){
        Admin admin = Admin.builder().build();
        when(adminRepo.getSameAdmin(admin)).thenReturn(admin);
        assertThrows(LoginAlreadyTakenException.class, ()->adminService.findSameLogin(admin));
    }

    @Test
    void updateShouldNotThrowException(){
        Admin admin = Admin.builder()
                .password("123123123")
                .build();
        doNothing().when(adminRepo).update(admin);
        assertDoesNotThrow(() -> adminService.update(admin));
    }

}
