package com.moksem.moksembank.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class represents all jsp-pages and commands in app
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
    //pages
    public static final String PAGE_LOGIN = "/WEB-INF/jsp/loginClient.jsp";
    public static final String PAGE_LOGIN_ADMIN = "/WEB-INF/jsp/loginAdmin.jsp";
    public static final String PAGE_USER = "/WEB-INF/jsp/user/user.jsp";
    public static final String PAGE_PAYMENTS = "/WEB-INF/jsp/user/payments.jsp";
    public static final String PAGE_REFILL = "/WEB-INF/jsp/user/refill.jsp";
    public static final String PAGE_TRANSFER = "/WEB-INF/jsp/user/transfer.jsp";
    public static final String PAGE_REGISTRATION = "/WEB-INF/jsp/registration.jsp";
    public static final String PAGE_ADMIN = "/WEB-INF/jsp/admin/admin.jsp";
    public static final String PAGE_CLIENT = "/WEB-INF/jsp/admin/client.jsp";
    public static final String PAGE_ERROR = "/WEB-INF/jsp/error.jsp";
    public static final String PAGE_PROFILE = "WEB-INF/jsp/common/profile.jsp";

    //commands
    public static final String COMMAND_CLIENT_DATA = "/controller?action=client_data";
    public static final String COMMAND_ADMIN_MAIN = "/controller?action=admin_main";
    public static final String COMMAND_MAIN = "/controller?action=main";
    public static final String COMMAND_ACCOUNT = "/controller?action=account";
    public static final String COMMAND_PAYMENTS = "/controller?action=payments";
    public static final String COMMAND_USERS = "/controller?action=users";
    public static final String COMMAND_TRANSFER = "/controller?action=transfer";
    public static final String COMMAND_CLIENT = "/controller?action=client_info";
    public static final String COMMAND_CLIENT_LOGIN_PAGE = "/controller?action=client_login_page";
    public static final String COMMAND_ADMIN_LOGIN_PAGE = "/controller?action=admin_login_page";
    public static final String COMMAND_PROFILE = "/controller?action=profile";
    public static final String COMMAND_REDIRECT = "redirect";

    //i18n
    public static final String LOCALE_NAME_UA = "ua";
    public static final String LOCALE_NAME_EN = "en";
}
