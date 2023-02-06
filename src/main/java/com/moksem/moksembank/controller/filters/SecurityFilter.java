package com.moksem.moksembank.controller.filters;

import com.moksem.moksembank.controller.Path;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class SecurityFilter implements Filter {
    private static final RoleCommandChecker roleCommandChecker = new RoleCommandChecker();

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("initialization starts");
        roleCommandChecker.addToMap("admin", filterConfig.getInitParameter("admin"));
        roleCommandChecker.addToMap("client", filterConfig.getInitParameter("client"));
        roleCommandChecker.addToMap("common", filterConfig.getInitParameter("common"));
        roleCommandChecker.addToMap("out", filterConfig.getInitParameter("out"));
        log.debug("initialization finished");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("starts");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (roleCommandChecker.getAccess(req)) {
            log.debug("finished");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.trace("failed");
            String errorMessage = "You do not have permission to access the requested resource";
            req.setAttribute("errorMessage", errorMessage);
            log.trace(errorMessage);
            req.getRequestDispatcher(Path.PAGE_ERROR).forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        log.debug("destroy starts");
        log.debug("destroy finished");
    }

    //todo занести сюда RoleCommandChecker?


}
