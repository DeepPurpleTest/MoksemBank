package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.repo.AdminRepo;
import com.moksem.moksembank.util.PasswordHash;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;
import com.moksem.moksembank.util.exceptions.LoginAlreadyTakenException;

public class AdminService {
    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Admin find(Admin tempAdmin) throws InvalidLoginOrPasswordException {
//        ValidatorsUtil.validateLogin(tempAdmin.getLogin());
        Admin admin = adminRepo.getAdmin(tempAdmin.getLogin());
        if(admin == null)
            throw new InvalidLoginOrPasswordException();
        PasswordHash.verify(tempAdmin.getPassword(), admin.getPassword());

        return admin;
    }

    public void findSameLogin(Admin adminToChange) throws LoginAlreadyTakenException {
        Admin admin;
        if(adminToChange.getId() != 0)
            admin = adminRepo.getSameAdmin(adminToChange);
        else
            admin = adminRepo.getAdmin(adminToChange.getLogin());
        if(admin != null)
            throw new LoginAlreadyTakenException("Login already taken");
    }

    public void update(Admin admin) throws LoginAlreadyTakenException {
        findSameLogin(admin);
        if(admin.getPassword().length() != 32)
            admin.setPassword(PasswordHash.encode(admin.getPassword()));
        adminRepo.update(admin);
    }
}
