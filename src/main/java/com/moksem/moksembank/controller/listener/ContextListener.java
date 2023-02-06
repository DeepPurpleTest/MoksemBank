package com.moksem.moksembank.controller.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Log4j2
public class ContextListener implements ServletContextListener {
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
