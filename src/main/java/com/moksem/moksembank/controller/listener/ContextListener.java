package com.moksem.moksembank.controller.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context Listener
 */
@Log4j2
public class ContextListener implements ServletContextListener {

    /**
     * Init method
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Servlet context initialization starts");
        initFactory();
        log.debug("Servlet context initialization starts");
    }


    /**
     * Initializes CommandFactory
     */
    private void initFactory() {
        log.debug("CommandFactory initialization starts");

        // initialize commands container
        // load class to JVM
        try {
            Class.forName("com.moksem.moksembank.controller.command.CommandFactory");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        log.debug("CommandFactory initialization finished");
    }

    /**
     * Destroy method
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("Servlet context destruction starts");
        // do nothing
        log.debug("Servlet context destruction starts");
    }
}
