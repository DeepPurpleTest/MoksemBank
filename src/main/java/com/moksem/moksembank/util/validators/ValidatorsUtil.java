package com.moksem.moksembank.util.validators;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.model.dto.entitydto.CardDto;
import com.moksem.moksembank.model.dto.TransferDto;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidIdException;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorsUtil {
    public static boolean validateLogin(String login) {
        return login == null || login.isEmpty();
    }

//    public static boolean validateCardsSort(String sort) {
//        if (sort == null)
//            return false;
//        return sort.equals("asc") || sort.equals("desc") || sort.equals("natural");
//    }

    public static void validatePhoneNumber(String number) throws InvalidPhoneNumberException {
        if (number == null || !number.matches("^\\+380[(67)(96)(97)(98)]{2}\\d{7}$"))
            throw new InvalidPhoneNumberException();
    }

    public static void validateCardNumber(String number) throws InvalidCardException {
        if (number == null || !number.matches("^\\d{16}$"))
            throw new InvalidCardException();
    }

    public static void validateId(String id) throws InvalidIdException {
        if (id == null || !id.matches("^\\d+$"))
            throw new InvalidIdException("Invalid id");
    }

    public static Set<String> validateUser(User user) {
        UserService userService = AppContext.getInstance().getUserService();
        Set<String> set = new HashSet<>();
        try {
            if (userService.findSameNumber(user) != null)
                set.add("phone_number");
        } catch (InvalidPhoneNumberException e) {
            set.add("phone_number");
        }
        if (!validateRegexp(user.getName()))
            set.add("name");
        if (!validateRegexp(user.getSurname()))
            set.add("surname");
        if (!validatePasswordLength(user.getPassword()))
            set.add("password");


        if (user.getMiddleName() != null &&
                !user.getMiddleName().isEmpty() &&
                !validateRegexp(user.getMiddleName())) {

            set.add("middle_name");
        }
        return set;
    }

    private static boolean validateRegexp(String s) {
        if (s == null)
            return false;
        return s.matches("[A-Z][a-z]+");
    }

    private static boolean validatePasswordLength(String s) {
        if (s == null)
            return false;
        if (s.length() == 32)
            return true;
        return (s.length() < 20 && s.length() > 7);
    }

    private static boolean validateLoginLength(String s) {
        if (s == null)
            return false;
        return (s.length() < 20 && s.length() > 3);
    }

    public static Set<String> validateAdmin(Admin admin) {
        AdminService adminService = AppContext.getInstance().getAdminService();
        Set<String> set = new HashSet<>();
        if (adminService.findSameLogin(admin) != null)
            set.add("login");
        if (validateLogin(admin.getLogin()))
            set.add("login");
        if (!validateLoginLength(admin.getLogin()))
            set.add("login");

        if (!validatePasswordLength(admin.getPassword()))
            set.add("password");

        return set;
    }

    public static void validateTransaction(TransferDto transferDto) {
        CardDto sender = transferDto.getSender();
        CardDto receiver = transferDto.getReceiver();
        Set<TransferDto.Param> errors = transferDto.getErrors();
        String amount = transferDto.getAmount();
        if (!sender.isStatus())
            errors.add(new TransferDto.Param("sender", "Sender card is blocked"));
        if (!receiver.isStatus())
            errors.add(new TransferDto.Param("receiver", "Receiver card is blocked"));
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            errors.add(new TransferDto.Param("amount", "Invalid amount format"));
        else {
            if(sender.getWallet() != null) {
                BigDecimal senderWallet = new BigDecimal(sender.getWallet());
                BigDecimal amountValue = new BigDecimal(amount);
                if (senderWallet.compareTo(amountValue) < 0)
                    errors.add(new TransferDto.Param("amount", "Not enough money"));
            }
        }
    }

    public static void validateAmount(String amount) throws InvalidAmountException {
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            throw new InvalidAmountException("Invalid amount format");
    }
}
