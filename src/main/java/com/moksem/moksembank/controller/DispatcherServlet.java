package com.moksem.moksembank.controller;

import com.moksem.moksembank.controller.command.CommandFactory;
import com.moksem.moksembank.controller.command.MyCommand;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main controller servlet
 */
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doRequest(req, resp);
    }

    public void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandFactory factory = CommandFactory.getFactory();
        MyCommand command = factory.getCommand(req);
        String page = command.execute(req, resp);
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        if (!page.equals("redirect"))
            dispatcher.forward(req, resp);
    }
}
