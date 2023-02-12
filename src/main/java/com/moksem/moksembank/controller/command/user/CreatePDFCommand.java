package com.moksem.moksembank.controller.command.user;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.CardService;
import com.moksem.moksembank.model.service.PaymentService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.PaymentNotFoundException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CreatePDFCommand implements MyCommand {
    PaymentService paymentService = AppContext.getInstance().getPaymentService();
    UserService userService = AppContext.getInstance().getUserService();
    CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String id = req.getParameter("id");
        String response = Path.COMMAND_REDIRECT;
        try {
            Payment payment = paymentService.find(user.getId(), id);
            payment = toFullPayment(payment);
            Document document = new Document();
//            boolean check = document.;
//            System.out.println("CHECK " + check);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();
            BaseFont arial = BaseFont.createFont("C:\\Users\\maksv\\Desktop\\Fonts\\arial.ttf", "cp1251",
                    BaseFont.EMBEDDED);

//            Font font = FontFactory.getFont(FontFactory.TIMES, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Receipt number " + payment.getId(), new Font(arial, 16));
            Paragraph number = new Paragraph("Information about payment: " , new Font(arial, 16));

            Chapter chapter = new Chapter(title, 1);
            chapter.setNumberDepth(0);

            Section section = chapter.addSection(number);
            section.setNumberDepth(0);

            Card cardSender = payment.getCardSender();
            Card cardReceiver = payment.getCardReceiver();
            StringBuilder cardSenderUser = new StringBuilder();
            StringBuilder cardReceiverUser = new StringBuilder();

            if (!cardSender.getUser().getName().equals("IBoX"))
                cardSenderUser
                        .append("\nCard sender: *")
                        .append(getSubString(cardSender))
                        .append(" ")
                        .append(cardSender.getUser().getName()).append(" ")
                        .append(cardSender.getUser().getSurname());
            else
                cardSenderUser
                        .append("\nRefilling from IBoX ")
                        .append(cardSender.getUser().getSurname());

            cardReceiverUser.append("\nCard receiver: *")
                    .append(getSubString(cardReceiver))
                    .append(" ")
                    .append(cardReceiver.getUser().getName()).append(" ")
                    .append(cardReceiver.getUser().getSurname());

            System.out.println(cardSenderUser);
            System.out.println(cardReceiverUser);
            Paragraph mainInfo = new Paragraph( "Payment date: " + payment.getDate().getTime() +
                    cardSenderUser +
                    cardReceiverUser +
                    "\nAmount: " + payment.getAmount() + " UAH", new Font(arial, 16));
            section.add(mainInfo);

            document.add(chapter);
            document.close();

            openInBrowser(resp, baos);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (PaymentNotFoundException | InvalidIdException | UserNotFoundException | InvalidCardException e) {
            response = Path.PAGE_ERROR;
            req.setAttribute("errorMessage", e.getMessage());
        } catch (IOException e) {
            response = Path.PAGE_ERROR;
        }

        return response;
    }

    public String getSubString(Card card) {
        return card.getNumber().substring(card.getNumber().length() - 4);
    }

    private static void openInBrowser(HttpServletResponse response, ByteArrayOutputStream baos) {
        response.setContentType("application/pdf");
        // the content length
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os;
        try {
            os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //todo Перенести в сервис
    public Payment toFullPayment(Payment payment) throws InvalidCardException, UserNotFoundException {
        String cardSender = payment.getCardSender().getNumber();
        String cardReceiver = payment.getCardReceiver().getNumber();
        payment.setCardSender(cardService.findByNumber(cardSender));
        payment.setCardReceiver(cardService.findByNumber(cardReceiver));
        long userSender = payment.getCardSender().getUser().getId();
        long userReceiver = payment.getCardReceiver().getUser().getId();
        payment.getCardSender().setUser(userService.findById(String.valueOf(userSender)));
        payment.getCardReceiver().setUser(userService.findById(String.valueOf(userReceiver)));
        return payment;
    }
}
