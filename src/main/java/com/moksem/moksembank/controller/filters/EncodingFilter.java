package com.moksem.moksembank.controller.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final Logger log = Logger.getLogger(EncodingFilter.class);
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Initialization starts");
        encoding = filterConfig.getInitParameter("encoding");
        log.trace("Encoding from web.xml ->" + encoding);
        log.debug("Initialization finished");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Filter starts");
        String requestEncoding = servletRequest.getCharacterEncoding();
        if (requestEncoding == null) {
            log.trace("Request encoding = null, set encoding ->" + encoding);
            servletRequest.setCharacterEncoding(encoding);
        }
        log.debug("Filter finished");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.debug("Destruction starts");

        log.debug("Destruction finished");
    }
}
