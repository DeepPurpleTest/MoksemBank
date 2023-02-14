package com.moksem.moksembank.appcontext;

import com.moksem.moksembank.model.repo.*;
import com.moksem.moksembank.model.service.*;
import lombok.Getter;

/**
 * Class creates all repositories and services on app starts.
 */
public class AppContext {
    private static final AppContext appContext = new AppContext();
    //repos
    private final AdminRepo adminRepo = new AdminRepo();
    private final UserRepo userRepo = new UserRepo();
    private final CardRepo cardRepo = new CardRepo();
    private final PaymentRepo paymentRepo = new PaymentRepo();
    private final RequestRepo requestRepo = new RequestRepo();


    //services
    @Getter
    private final AdminService adminService = new AdminService(adminRepo);
    @Getter
    private final UserService userService = new UserService(userRepo);
    @Getter
    private final CardService cardService = new CardService(cardRepo, userService);
    @Getter
    private final PaymentService paymentService = new PaymentService(paymentRepo, cardService);
    @Getter
    private final RequestService requestService = new RequestService(requestRepo);



    public static AppContext getInstance() {
        return appContext;
    }

}
