package com.moksem.moksembank.controller.filters;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import java.io.IOException;
/**
 * Encoding filter.
 */
@Log4j2
public class EncodingFilter implements Filter {
    private String encoding;

    /**
     * Init method
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Initialization starts");
        encoding = filterConfig.getInitParameter("encoding");
        log.trace("Encoding from web.xml ->" + encoding);
        log.debug("Initialization finished");
    }

    /**
     * Main method
     */
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

    /**
     * Destroy method
     */
    @Override
    public void destroy() {
        log.debug("Destruction starts");
        // do nothing
        log.debug("Destruction finished");
    }
}
