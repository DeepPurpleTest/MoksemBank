package com.moksem.moksembank.controller.command.user;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dtobuilder.CardDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.PaymentDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.util.Pagination;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Payments command
 */
public class PaymentsCommand implements MyCommand {
    PaymentService paymentService = AppContext.getInstance().getPaymentService();
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String response = Path.PAGE_PAYMENTS;

        User user = (User) session.getAttribute("user");
        String sort = (String) Objects.requireNonNullElse(session.getAttribute("sort"), "natural");
        String page = (String) Objects.requireNonNullElse(session.getAttribute("page"), "");
        String card = (String) Objects.requireNonNullElse(session.getAttribute("card"), "");
        Card sortCard = Card.builder().build();
        List<Payment> payments = new ArrayList<>();
        int maxPages = 0;

        try {
            if (!card.isEmpty()) {
                sortCard = cardService.findById(card, user);
                payments.addAll(paymentService.findByUserIdAndCardId(sortCard, page, sort));
                maxPages = paymentService.findCountByCard(card);
            } else {
                payments.addAll(paymentService.findByUser(user, page, sort));
                maxPages = paymentService.findCountByUser(user);
            }

        } catch (InvalidCardException | UserNotFoundException e) {
            e.printStackTrace();
            response = Path.PAGE_ERROR;
        }
        req.setAttribute("payments", PaymentDtoBuilder.getPaymentsDto(payments));
        req.setAttribute("cards", CardDtoBuilder.getCardsDto(cardService.findAllByUserId(user.getId())));
        req.setAttribute("card", CardDtoBuilder.getCardDto(sortCard));
        req.setAttribute("user", UserDtoBuilder.getUserDto(user));
        req.setAttribute("sort", sort);
        Pagination.paginate(req, maxPages);

        return response;
    }
}
