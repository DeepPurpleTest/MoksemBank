package com.moksem.moksembank.controller.filters;

import com.moksem.moksembank.model.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleCommandChecker {
    private static final Map<String, List<String>> commands = new HashMap<>();

    public void addToMap(String role, String s) {
        commands.put(role, asList(s));
    }

    public List<String> asList(String s) {
        return new ArrayList<>(List.of(s.split(" ")));
    }

    public boolean getAccess(HttpServletRequest req) {
        String action = req.getParameter("action");
        System.out.println("ACTION = " + action);

        if (action.isEmpty())
            return false;

        System.out.println("before out");
        if (commands.get("out").contains(action))
            return true;

        System.out.println("before role");

        Role role = (Role) req.getSession().getAttribute("role");

        System.out.println("ROLE " + role);
        if (role == null)
            return false;

        if (role.toString().equals("admin") && commands.get("admin").contains(action))
            return true;

        if (role.toString().equals("user") && commands.get("client").contains(action)) {
            System.out.println("role.equals(user)");
            return true;
        }

        System.out.println("last return");

        return commands.get("common").contains(action);
    }
}
