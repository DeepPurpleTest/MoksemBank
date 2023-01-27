package com.moksem.moksembank.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MyCommand {
    String execute(HttpServletRequest req, HttpServletResponse resp);
}
