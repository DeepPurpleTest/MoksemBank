package com.moksem.moksembank.controller.listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(ContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("initialization starts");

        initFactory();

        log.debug("initialization finished");
    }

    private void initFactory(){
        log.debug("CommandFactory initialization starts");
        try {
            Class.forName("com.moksem.moksembank.controller.command.CommandFactory");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        log.debug("CommandFactory initialization finished");
    }
}
