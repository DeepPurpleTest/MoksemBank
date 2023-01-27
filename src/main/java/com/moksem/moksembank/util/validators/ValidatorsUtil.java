package com.moksem.moksembank.util.validators;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.AdminService;
import com.moksem.moksembank.model.service.UserService;
import com.moksem.moksembank.util.exceptions.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

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

    public static Map<String, String> validateTransaction(Card sender, Card receiver, String amount) {
        Map<String, String> map = new HashMap<>();
        if (sender.equals(receiver))
            map.put("sender", "Cards in transaction is identical");
        if (!sender.isStatus())
            map.put("sender", "Sender card is blocked");
        if (!receiver.isStatus())
            map.put("receiver", "Receiver card is blocked");
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            map.put("amount", "Invalid amount format");
        else {
            BigDecimal bd = new BigDecimal(amount);
            if (sender.getWallet().compareTo(bd) <= 0)
                map.put("amount", "Not enough money for transaction");
        }

        return map;
    }

    public static void validateAmount(String amount) throws InvalidAmountException {
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            throw new InvalidAmountException();
    }
}
