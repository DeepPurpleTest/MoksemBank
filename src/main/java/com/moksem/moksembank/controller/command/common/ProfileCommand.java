package com.moksem.moksembank.controller.command.common;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.controller.command.MyCommand;
import com.moksem.moksembank.model.dto.AdminDto;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.dtobuilder.AdminDtoBuilder;
import com.moksem.moksembank.model.dtobuilder.UserDtoBuilder;
import com.moksem.moksembank.model.entity.Admin;
import com.moksem.moksembank.model.entity.Role;
import com.moksem.moksembank.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProfileCommand implements MyCommand {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        Role role = (Role) session.getAttribute("role");

        if (role.equals(Role.USER)) {
            User user = (User) session.getAttribute("user");
            UserDto userdto = UserDtoBuilder.getUserDto(user);
            req.setAttribute("client", userdto);
        } else {
            Admin admin = (Admin) session.getAttribute("user");
            AdminDto adminDto = AdminDtoBuilder.getAdminDto(admin);
            req.setAttribute("admin", adminDto);
        }
        req.setAttribute("role", session.getAttribute("role").toString());
        return Path.PAGE_PROFILE;
    }
}
