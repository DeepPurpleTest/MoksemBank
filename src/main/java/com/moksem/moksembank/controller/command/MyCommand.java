package com.moksem.moksembank.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main interface for the Command pattern implementation
 */
public interface MyCommand {
    /**
     * Execution method for command
     */
    String execute(HttpServletRequest req, HttpServletResponse resp);
}
