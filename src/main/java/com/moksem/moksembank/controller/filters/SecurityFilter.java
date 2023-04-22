package com.moksem.moksembank.controller.filters;

import com.moksem.moksembank.controller.Path;
import com.moksem.moksembank.model.entity.Role;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Security filter
 */
@Log4j2
public class SecurityFilter implements Filter {
    private static final CheckRoleAccess CHECK_ROLE_ACCESS = new CheckRoleAccess();

    /**
     * Init method
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("initialization starts");
        CHECK_ROLE_ACCESS.addToMap("admin", filterConfig.getInitParameter("admin"));
        CHECK_ROLE_ACCESS.addToMap("client", filterConfig.getInitParameter("client"));
        CHECK_ROLE_ACCESS.addToMap("common", filterConfig.getInitParameter("common"));
        CHECK_ROLE_ACCESS.addToMap("out", filterConfig.getInitParameter("out"));
        log.debug("initialization finished");
    }

    /**
     * Main method
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("starts");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (CHECK_ROLE_ACCESS.getAccess(req)) {
            log.debug("finished");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.trace("failed");
            String errorMessage = "error.permission";
            req.setAttribute("errorMessage", errorMessage);
            log.trace(errorMessage);
            req.getRequestDispatcher(Path.PAGE_ERROR).forward(req, resp);
        }
    }

    /**
     * Destroy method
     */
    @Override
    public void destroy() {
        log.debug("destroy starts");
        // do nothing
        log.debug("destroy finished");
    }
}

/**
 * Support class for Security filter
 */
class CheckRoleAccess {
    private static final Map<String, List<String>> commands = new HashMap<>();

    public void addToMap(String role, String s) {
        commands.put(role, asList(s));
    }

    public List<String> asList(String s) {
        return new ArrayList<>(List.of(s.split(" ")));
    }

    /**
     * Main check access method
     */
    public boolean getAccess(HttpServletRequest req) {
        String action = req.getParameter("action");

        if (action == null)
            return false;

        if (action.isEmpty())
            return false;

        if (commands.get("out").contains(action))
            return true;

        Role role = (Role) req.getSession().getAttribute("role");

        if (role == null)
            return false;

        if (role.toString().equals("admin") && commands.get("admin").contains(action))
            return true;

        if (role.toString().equals("user") && commands.get("client").contains(action)) {
            return true;
        }

        return commands.get("common").contains(action);
    }
}
