package CommandsTest.common;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.common.ChangeProfileCommand;
import com.moksem.moksembank.controller.command.common.ProfileCommand;
import com.moksem.moksembank.model.dto.AdminDto;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.dtobuilder.AdminDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileCommandTest {
    @InjectMocks
    ProfileCommand profileCommand;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void profileCommandShouldReturnProfilePageWithRoleUser(){
        User user = User.builder().build();
        UserDto userDto = UserDto.builder().build();
        try (MockedStatic<UserDtoBuilder> userDtoBuilderMockedStatic =
                mockStatic(UserDtoBuilder.class)){
            userDtoBuilderMockedStatic.when(()->UserDtoBuilder.getUserDto(user))
                    .thenReturn(userDto);
            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("role")).thenReturn(Role.USER);
            when(session.getAttribute("user")).thenReturn(user);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_PROFILE, profileCommand.execute(req, resp));
        }
    }

    @Test
    void profileCommandShouldReturnProfilePageWithRoleAdmin(){
        Admin admin = Admin.builder().build();
        AdminDto adminDto = AdminDto.builder().build();
        try (MockedStatic<AdminDtoBuilder> adminDtoBuilderMockedStatic =
                     mockStatic(AdminDtoBuilder.class)){
            adminDtoBuilderMockedStatic.when(()->AdminDtoBuilder.getAdminDto(admin))
                    .thenReturn(adminDto);
            when(req.getSession()).thenReturn(session);
            when(session.getAttribute("role")).thenReturn(Role.ADMIN);
            when(session.getAttribute("user")).thenReturn(admin);
            doNothing().when(req).setAttribute(anyString(), any());

            assertEquals(Path.PAGE_PROFILE, profileCommand.execute(req, resp));
        }
    }
}
