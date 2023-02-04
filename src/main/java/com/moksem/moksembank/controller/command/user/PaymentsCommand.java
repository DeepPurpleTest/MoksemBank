package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.PaymentDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.PaginationUtil;
import com.moksem.moksembank.util.SessionAttributesUtil;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentsCommand implements MyCommand {
    PaymentService paymentService = AppContext.getInstance().getPaymentService();
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.COMMAND_PAYMENTS;

        if(req.getParameter("sort") != null){
            SessionAttributesUtil.clearSession(session);
            System.out.println("REQ.GETPARAMETER(CARD) " + req.getParameter("card"));
            SessionAttributesUtil.toSession(req, session);
            try {
                resp.sendRedirect(response);
                response = Path.COMMAND_REDIRECT;
            } catch (IOException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
        } else {
            System.out.println("SESSION.GETATTRIBUTE(CARD) " + session.getAttribute("card"));
            User user = (User) session.getAttribute("user");
            String sort = (String) Objects.requireNonNullElse(session.getAttribute("sort"), "natural");
            String page = (String) Objects.requireNonNullElse(session.getAttribute("page"), "");
            String card = (String) Objects.requireNonNullElse(session.getAttribute("card"), "");

            List<Payment> payments = new ArrayList<>();
            int maxPages = 0;

            //todo переделать обработку эксепшенов!
            try {
                if (!card.isEmpty()) {
                    System.out.println("!card.isEmpty() " + card);
                    payments.addAll(paymentService.findByUserIdAndCardId(user.getId(), card, page, sort));
                    maxPages = paymentService.findCountByCard(card);
                } else {
                    payments.addAll(paymentService.findByUser(user, page, sort));
                    maxPages = paymentService.findCountByUser(user);
                }
                response = Path.PAGE_PAYMENTS;
            } catch (InvalidCardException | UserNotFoundException e) {
                e.printStackTrace();
                response = Path.PAGE_ERROR;
            }
            req.setAttribute("payments", PaymentDtoBuilder.getPaymentsDto(payments));
            req.setAttribute("cards", CardDtoBuilder.getCardsDto(cardService.findAllByUserId(user.getId())));
            req.setAttribute("sort", sort);
            req.setAttribute("card", card);
            req.setAttribute("user", UserDtoBuilder.getUserDto(user));

            PaginationUtil.paginate(req, maxPages);
        }

        return response;
    }
}
