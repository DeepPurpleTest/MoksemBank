package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.UserRepo;
import com.moksem.moksembank.util.exceptions.*;
import com.moksem.moksembank.util.validators.ValidatorsUtil;

import java.util.List;

import static com.moksem.moksembank.util.PaginationUtil.getPage;
import static com.moksem.moksembank.util.PasswordHashUtil.encode;
import static com.moksem.moksembank.util.PasswordHashUtil.verify;
import static com.moksem.moksembank.util.validators.ValidatorsUtil.validatePhoneNumber;

public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll(String page) {
        int pageValue = getPage(page);
        //todo сделать вытягивание по юзеру и сортировку привязать
        return userRepo.getClients(pageValue);
    }

    public List<User> findByRequest(String page) {
        int pageValue = getPage(page);
        return userRepo.getClientsByRequest(pageValue);
    }

    public long create(User user) throws UserCreateException {
        String pass = user.getPassword();
        user.setPassword(encode(pass));
        long id = userRepo.newUser(user);
        if (id < 1)
            throw new UserCreateException();
        return id;
    }

    public void update(User user) {
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

    public User findByNumber(String number) throws InvalidPhoneNumberException, UserNotFoundException {
        validatePhoneNumber(number);
        User user = userRepo.getUser(number);
        if (user == null)
            throw new UserNotFoundException();

        return user;
    }

    public User findSameNumber(User user) throws InvalidPhoneNumberException {
        validatePhoneNumber(user.getPhoneNumber());
        return userRepo.getUserByPhoneAndId(user);
    }

    public User findByNumberAndPassword(String number, String pass) throws InvalidLoginOrPasswordException, BlockedUserException {
        User user = userRepo.getUser(number);
        if (user == null)
            throw new InvalidLoginOrPasswordException();
        verify(pass, user.getPassword());

        if (!user.isStatus())
            throw new BlockedUserException();

        return user;
    }

    public User findByCard(String cardNumber) throws InvalidCardException {
        ValidatorsUtil.validateCardNumber(cardNumber);
        return userRepo.getUserByCard(cardNumber);
    }

    public int findUsersCount() {
        return userRepo.getUsersCount();
    }

    public int findUsersWithRequestCount() {
        return userRepo.getUsersRequestCount();
    }

//    public void toFullUser(HttpSession session, User user) throws InvalidPhoneNumberException, UserNotFoundException {
//        User updatedUser = findByNumber(user.getPhone_number());
//        session.setAttribute("user", updatedUser);
//    }
}
