package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.UserRepo;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validator.Validator;

import java.util.List;

import static com.moksem.moksembank.util.Pagination.getPage;
import static com.moksem.moksembank.util.PasswordHash.encode;
import static com.moksem.moksembank.util.PasswordHash.verify;

/**
 * User service
 */
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll(String page) {
        int pageValue = getPage(page);
        return userRepo.getClients(pageValue);
    }

    public List<User> findByRequest(String page) {
        int pageValue = getPage(page);
        return userRepo.getClientsByRequest(pageValue);
    }

    public long create(User user) throws UserCreateException {
//        ValidatorsUtil.validateNewUser(user);
        String pass = user.getPassword();
        user.setPassword(encode(pass));
        long id = userRepo.newUser(user);
        if (id < 1)
            throw new UserCreateException();
        return id;
    }

    public void update(User user) throws PhoneNumberAlreadyTakenException, InvalidPhoneNumberException {
        findSameNumber(user);
        if (user.getPassword().length() != 32)
            user.setPassword(encode(user.getPassword()));
        userRepo.updateUser(user);
    }

    public User findById(String id) throws UserNotFoundException {
        User user = userRepo.getUser(Integer.parseInt(id));
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public User findByNumber(String number) throws UserNotFoundException {
        User user = userRepo.getUser(number);
        if (user == null)
            throw new UserNotFoundException();

        return user;
    }

    public void findSameNumber(User userToFind) throws InvalidPhoneNumberException, PhoneNumberAlreadyTakenException {
        Validator.validatePhoneNumber(userToFind.getPhoneNumber());
        User user;
        if (userToFind.getId() != 0)
            user = userRepo.getUserByPhoneAndId(userToFind);
        else
            user = userRepo.getUser(userToFind.getPhoneNumber());

        if (user != null)
            throw new PhoneNumberAlreadyTakenException("client.error.phone.already_taken");
    }

    public User findByNumberAndPassword(String number, String pass) throws InvalidLoginOrPasswordException, BlockedUserException {
        User user = userRepo.getUser(number);
        if (user == null)
            throw new InvalidLoginOrPasswordException("login.error");
        verify(pass, user.getPassword());

        if (!user.isStatus())
            throw new BlockedUserException();

        return user;
    }

    public User findByCard(String cardNumber) throws InvalidCardException {
        Validator.validateCardNumber(cardNumber);
        User user = userRepo.getUserByCard(cardNumber);
        if(user == null)
            throw new InvalidCardException("admin.error.card_not_found");
        return user;
    }

    public int findUsersCount() {
        return userRepo.getUsersCount();
    }

    public int findUsersWithRequestCount() {
        return userRepo.getUsersRequestCount();
    }
}
