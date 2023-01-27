package CommandFactoryTest;

import com.moksem.moksembank.controller.command.CommandFactory;
import com.moksem.moksembank.controller.command.user.ClientAccountCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {
    CommandFactory commandFactory;

    @BeforeEach
    public void setUp(){
        commandFactory = CommandFactory.getFactory();
    }

    @Test
    void commandFactoryShouldReturnNullIfInvalidCommand(){
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("action")).thenReturn("");
        Object action = commandFactory.getCommand(req);
        assertNull(action);
    }

    @Test
    void commandFactoryShouldReturnAccountCommand(){
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("action")).thenReturn("account");
        Object action = commandFactory.getCommand(req);
        assertEquals(action.getClass(), ClientAccountCommand.class);
    }

    @Test
    void commandFactoryShouldBeSingleton(){
        assertSame(CommandFactory.getFactory(), CommandFactory.getFactory());
    }
}
