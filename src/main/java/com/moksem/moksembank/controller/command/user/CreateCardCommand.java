package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CreateCardCommand implements MyCommand {
    //todo сделать колонку с visa и матеркард, где можно создать их 2-х типов
    // вторая не запрещает пересылать деньги, если их недостаточно, баланс в минус
    private final CardService cardService = AppContext.getInstance().getCardService();
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Card card = Card.builder()
                .user(user)
                .build();
        cardService.create(card);
        String response = Path.COMMAND_ACCOUNT;
        try {
            resp.sendRedirect(response);
            response = Path.COMMAND_REDIRECT;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
