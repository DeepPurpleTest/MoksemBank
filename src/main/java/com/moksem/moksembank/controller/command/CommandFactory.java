package com.moksem.moksembank.controller.command;

import com.moksem.moksembank.controller.command.admin.*;
import com.moksem.moksembank.controller.command.common.ChangeProfileCommand;
import com.moksem.moksembank.controller.command.common.LogoutCommand;
import com.moksem.moksembank.controller.command.common.ProfileCommand;
import com.moksem.moksembank.controller.command.out.*;
import com.moksem.moksembank.controller.command.user.ClientAccountCommand;
import com.moksem.moksembank.controller.command.common.BlockClientCardCommand;
import com.moksem.moksembank.controller.command.user.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static CommandFactory factory;
    private static final Map<String, MyCommand> commands = new HashMap<>();
    private CommandFactory(){}

    static {
        //Client
        commands.put("client_payments", new ClientPaymentsCommand());
        commands.put("main", new ClientMainCommand());
        commands.put("account", new ClientAccountCommand());
        commands.put("payments", new PaymentsCommand());
        commands.put("get_refill", new CardRefillCommand());
        commands.put("refill", new RefillCommand());
        commands.put("transfer", new TransferCommand());
        commands.put("transaction", new TransactionCommand());
        commands.put("create", new CreateCardCommand());
        commands.put("request_unlock", new RequestUnlockCommand());
        commands.put("createPDF", new CreatePDFCommand());

        //Admin
        commands.put("client_data", new ClientDataCommand());
        commands.put("admin_main", new AdminMainCommand());
        commands.put("users", new UsersCommand());
        commands.put("client_info", new ClientCommand());
        commands.put("unlock", new UnlockClientCardCommand());
        commands.put("block_client", new BlockClientCommand());
        commands.put("unlock_client", new UnlockClientCommand());

        //Commons
        commands.put("block", new BlockClientCardCommand());
        commands.put("log_out", new LogoutCommand());
        commands.put("profile", new ProfileCommand());
        commands.put("change_user", new ChangeProfileCommand());

        //Out
        commands.put("client_login_page", new ClientLoginPageCommand());
        commands.put("admin_login_page", new AdminLoginPageCommand());
        commands.put("login_client", new LoginClientCommand());
        commands.put("login_admin", new LoginAdminCommand());
        commands.put("i18n", new I18NCommand());
        commands.put("registration_page", new RegisterCommand());
        commands.put("registration", new RegistrationCommand());
        //todo Сделать класс под эррор пейджу?
    }

    public static CommandFactory getFactory(){
        if(factory == null)
            factory = new CommandFactory();
        return factory;
    }

    public MyCommand getCommand(HttpServletRequest request){
        String action = request.getParameter("action");
        return commands.get(action);
    }
}
