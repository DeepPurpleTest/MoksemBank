package com.moksem.moksembank.util.validator;

import com.moksem.moksembank.model.dto.*;
import com.moksem.moksembank.util.exceptions.InvalidAmountException;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.InvalidPhoneNumberException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Validator util class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {

    /**
     * Check is empty or not String param
     *
     * @param   s string
     * @return  boolean value
     */
    public static boolean checkString(String s) {
        if (s == null)
            return true;
        return s.isEmpty();
    }

    /**
     * Validate all fields of new client
     *
     * @param clientDto created dto from command
     */
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

    /**
     * Validate changes in client profile
     *
     * @param clientDto created dto from command
     */
    public static void validateChangedClient(ClientDto clientDto) {
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

    /**
     * Validate changes in admin profile
     *
     * @param adminDto created dto from command
     */
    public static void validateChangedAdmin(AdminDto adminDto) {
        Set<Dto.Param> set = adminDto.getErrors();
        if (adminDto.getLogin() != null && validateLoginLength(adminDto.getLogin()))
            set.add(new Dto.Param("login", "Invalid login length"));
        String pass = adminDto.getPassword();
        if (pass != null && (pass.length() >= 30 || pass.length() <= 7))
            set.add(new Dto.Param("pass", "Invalid password length"));
    }

    /**
     * Validate transaction between cards
     *
     * @param transferDto created dto from command
     */
    public static void validateTransaction(TransferDto transferDto) {
        CardDto sender = transferDto.getSender();
        CardDto receiver = transferDto.getReceiver();
        Set<Dto.Param> errors = transferDto.getErrors();
        String amount = transferDto.getAmount();

        validateCard(sender, errors, "sender");
        validateCard(receiver, errors, "receiver");

        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$") || amount.equals("0"))
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

    /**
     * Validate the card involved in the transaction
     *
     * @param card      the card involved in the transaction
     * @param errors    container of validation errors
     * @param errorName error name in container
     */
    public static void validateCard(CardDto card, Set<Dto.Param> errors, String errorName) {
        String number = card.getNumber();
        String cardRole = errorName.replaceFirst("\\w", errorName.substring(0, 1).toUpperCase());
        if (number == null || number.isEmpty())
            errors.add(new Dto.Param(errorName, "This field must be filled"));
        else if (!number.matches("^\\d{16}$"))
            errors.add(new Dto.Param(errorName, "Invalid number format"));
        else if (!card.isStatus())
            errors.add(new Dto.Param(errorName,
                    cardRole + " card is blocked"));
    }

    /**
     * Validate name for client
     *
     * @param name  new name for client
     * @return      boolean
     */
    public static boolean validateName(String name) {
        return !name.matches("^[A-Z\u0400-\u042F\u0490][a-z\u0430-\u045F\u0491]+$");
    }

    /**
     * Validate client phone number
     *
     * @param number                        client number
     * @throws InvalidPhoneNumberException  Invalid phone number format exception
     */
    public static void validatePhoneNumber(String number) throws InvalidPhoneNumberException {
        if (number == null || !number.matches("^\\+380[(67)(96)(97)(98)]{2}\\d{7}$"))
            throw new InvalidPhoneNumberException("Invalid phone number format");
    }

    /**
     * Validate card number
     *
     * @param cardNumber             card number
     * @throws InvalidCardException  Invalid phone number format exception
     */
    public static void validateCardNumber(String cardNumber) throws InvalidCardException {
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$"))
            throw new InvalidCardException();
    }

    /**
     * Validate login length for admin
     *
     * @param login admin login
     * @return      boolean value
     */
    private static boolean validateLoginLength(String login) {
        return login.length() >= 20 || login.length() <= 3;
    }

    /**
     * Validate amount for transaction
     *
     * @param amount                    refill or transaction amount
     * @throws InvalidAmountException   Invalid amount exception
     */
    public static void validateAmount(String amount) throws InvalidAmountException {
        if (amount == null || !amount.matches("^\\d+([.,]\\d{1,2})?$") || amount.equals("0"))
            throw new InvalidAmountException("Invalid amount format");
    }
}
