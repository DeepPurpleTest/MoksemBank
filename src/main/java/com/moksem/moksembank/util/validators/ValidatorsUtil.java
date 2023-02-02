package com.moksem.moksembank.util.validators;

import com.moksem.moksembank.model.dto.*;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.util.exceptions.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorsUtil {

//    public static boolean validateCardsSort(String sort) {
//        if (sort == null)
//            return false;
//        return sort.equals("asc") || sort.equals("desc") || sort.equals("natural");
//    }

    public static boolean checkString(String s) {
        if (s == null)
            return true;
        return s.isEmpty();
    }

    public static void validateNewUser(ClientDto clientDto) {
        Set<Dto.Param> set = clientDto.getErrors();
        if (checkString(clientDto.getName()))
            set.add(new Dto.Param("name", "This field must be filled"));
        else if (validateName(clientDto.getName()))
            set.add(new Dto.Param("name", "Invalid name format"));

        if (checkString(clientDto.getSurname()))
            set.add(new Dto.Param("surname", "This field must be filled"));
        else if (validateName(clientDto.getSurname()))
            set.add(new Dto.Param("surname", "Invalid surname format"));

        String middle = clientDto.getMiddleName();
        if (middle != null && !middle.isEmpty() && validateName(middle))
            set.add(new Dto.Param("middleName", "Invalid middle name"));

        String pass = clientDto.getPassword();
        if (checkString(pass))
            set.add(new Dto.Param("pass", "This field must be filled"));
        else if (pass.length() > 30 || pass.length() < 7)
            set.add(new Dto.Param("pass", "Invalid password length"));

        String phone = clientDto.getPhoneNumber();
        if (checkString(phone))
            set.add(new Dto.Param("phone", "This field must be filled"));
        else if (!phone.matches("^\\+380[(67)(96)(97)(98)]{2}\\d{7}$"))
            set.add(new Dto.Param("phone", "Invalid phone number"));
    }

    public static void validateChangedUser(ClientDto clientDto) {
        Set<Dto.Param> set = clientDto.getErrors();
        String name = clientDto.getName();
        String surname = clientDto.getSurname();
        String middle = clientDto.getMiddleName();
        if (name != null && !name.isEmpty() && validateName(name))
            set.add(new Dto.Param("name", "Invalid name"));
        if (surname != null && !surname.isEmpty() && validateName(surname))
            set.add(new Dto.Param("surname", "Invalid surname"));
        if (middle != null && !middle.isEmpty() && validateName(middle))
            set.add(new Dto.Param("middleName", "Invalid middle name"));

        String pass = clientDto.getPassword();
        if (pass != null && !pass.isEmpty() && (pass.length() > 30 || pass.length() < 7))
            set.add(new Dto.Param("pass", "Invalid password length"));
        String phone = clientDto.getPhoneNumber();
        if (phone != null && !phone.isEmpty() && !phone.matches("^\\+380[(67)(96)(97)(98)]{2}\\d{7}$"))
            set.add(new Dto.Param("phone", "Invalid phone number"));
    }

    public static void validateChangedAdmin(AdminDto adminDto) {
        Set<Dto.Param> set = adminDto.getErrors();
        if (adminDto.getLogin() != null && validateLoginLength(adminDto.getLogin()))
            set.add(new Dto.Param("login", "Invalid login length"));
        String pass = adminDto.getPassword();
        if (pass != null && (pass.length() >= 30 || pass.length() <= 7))
            set.add(new Dto.Param("pass", "Invalid password length"));
    }

    public static void validateTransaction(TransferDto transferDto) {
        CardDto sender = transferDto.getSender();
        CardDto receiver = transferDto.getReceiver();
        Set<Dto.Param> errors = transferDto.getErrors();
        String amount = transferDto.getAmount();
        if (!sender.isStatus())
            errors.add(new Dto.Param("sender", "Sender card is blocked"));
        if (!receiver.isStatus())
            errors.add(new Dto.Param("receiver", "Receiver card is blocked"));
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            errors.add(new Dto.Param("amount", "Invalid amount format"));
        else {
            if (sender.getWallet() != null) {
                BigDecimal senderWallet = new BigDecimal(sender.getWallet());
                BigDecimal amountValue = new BigDecimal(amount);
                if (senderWallet.compareTo(amountValue) < 0)
                    errors.add(new Dto.Param("amount", "Not enough money"));
            }
        }
    }

    public static void validateNewUser(User user) throws InvalidStringFormat, InvalidPasswordException {
        validateRegexp(user.getName());
        validateRegexp(user.getSurname());
        if (user.getMiddleName() != null && !user.getMiddleName().isEmpty())
            validateRegexp(user.getMiddleName());
        validatePasswordLength(user.getPassword());
    }

    public static boolean validateLogin(String login) {
        return login == null || login.isEmpty();
    }

    public static boolean validateName(String s) {
        return !s.matches("[A-Z][a-z]+");
    }

    public static void validatePhoneNumber(String number) throws InvalidPhoneNumberException {
        if (number == null || !number.matches("^\\+380[(67)(96)(97)(98)]{2}\\d{7}$"))
            throw new InvalidPhoneNumberException("Invalid phone number format");
    }

    public static void validateCardNumber(String number) throws InvalidCardException {
        if (number == null || !number.matches("^\\d{16}$"))
            throw new InvalidCardException();
    }

    public static void validateId(String id) throws InvalidIdException {
        if (id == null || !id.matches("^\\d+$"))
            throw new InvalidIdException("Invalid id");
    }

    private static void validateRegexp(String s) throws InvalidStringFormat {
        if (s == null || !s.matches("[A-Z][a-z]+"))
            throw new InvalidStringFormat();
    }

    public static void validatePasswordLength(String s) throws InvalidPasswordException {
        if (s == null || s.length() > 30 || s.length() < 7)
            throw new InvalidPasswordException("Invalid password length");
    }

    private static boolean validateLoginLength(String s) {
        return s.length() >= 20 || s.length() <= 3;
    }
//    public static void validateNewAdmin(AdminDto adminDto) {
//        Set<Dto.Param> set = adminDto.getErrors();
//        if (adminDto.getLogin() == null || validateLoginLength(adminDto.getLogin()))
//            set.add(new Dto.Param("login", "Invalid login length"));
//        String pass = adminDto.getPassword();
//        if (pass.length() > 20 || pass.length() < 7)
//            set.add(new Dto.Param("pass", "Invalid password length"));

//    }

    public static void validateAmount(String amount) throws InvalidAmountException {
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$"))
            throw new InvalidAmountException("Invalid amount format");
    }
}
