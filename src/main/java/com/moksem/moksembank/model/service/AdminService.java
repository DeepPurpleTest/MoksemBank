package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.repo.AdminRepo;
import com.moksem.moksembank.util.PasswordHashUtil;
import com.moksem.moksembank.util.exceptions.InvalidLoginOrPasswordException;

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
        PasswordHashUtil.verify(tempAdmin.getPassword(), admin.getPassword());

        return admin;
    }

    public Admin findSameLogin(Admin admin) {
        return adminRepo.getSameAdmin(admin);
    }

    public void update(Admin admin){
        if(admin.getPassword().length() != 32)
            admin.setPassword(PasswordHashUtil.encode(admin.getPassword()));
        adminRepo.update(admin);
    }
}
