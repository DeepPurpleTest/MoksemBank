package com.moksem.moksembank.controller.command.user;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
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
    private final PaymentService paymentService = AppContext.getInstance().getPaymentService();
    private final UserService userService = AppContext.getInstance().getUserService();
    private final CardService cardService = AppContext.getInstance().getCardService();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String id = req.getParameter("id");
        String response = Path.COMMAND_PAYMENTS;
        try {
            Payment payment = paymentService.find(user.getId(), id);
            payment = toFullPayment(payment);
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();
            Font font = FontFactory.getFont(FontFactory.TIMES, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Receipt number " + payment.getId(), font);

            Chapter chapter = new Chapter(title, 1);
            chapter.setNumberDepth(0);

            Section section = chapter.addSection("Information about payment:");
            section.setNumberDepth(0);
            String cardSenderNumber = payment.getCardSender().getNumber();
            String cardReceiverNumber = payment.getCardReceiver().getNumber();
            String cardSenderUser = payment.getCardSender().getUser().getName() + " "
                    + payment.getCardSender().getUser().getSurname();
            String cardReceiverUser = payment.getCardReceiver().getUser().getName() + " "
                    + payment.getCardReceiver().getUser().getSurname();
            Paragraph mainInfo = new Paragraph("Payment date: " + payment.getDate().getTime() +
                    "\nCard sender: *" + cardSenderNumber.substring(cardSenderNumber.length() - 4) + " " +
                    cardSenderUser +
                    "\nCard receiver: *" + cardReceiverNumber.substring(cardReceiverNumber.length() - 4) + " " +
                    cardReceiverUser +
                    "\nAmount: " + payment.getAmount() + " UAH");
            section.add(mainInfo);

            document.add(chapter);
            document.close();

            openInBrowser(resp, baos);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (PaymentNotFoundException | InvalidIdException | UserNotFoundException | InvalidCardException e) {
            response = Path.PAGE_ERROR;
            req.setAttribute("errorMessage", e.getMessage());
        }

        return response;
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
    public Payment toFullPayment(Payment payment) throws InvalidCardException, UserNotFoundException, InvalidIdException {
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
