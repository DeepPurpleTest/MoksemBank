package com.moksem.moksembank.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Class for transfer parameters from request to sessin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionAttributes {

    public static void checkParameters(HttpServletRequest req, HttpSession session){
        if (req.getParameter("sort") != null) {
            SessionAttributes.clearSession(session);
            SessionAttributes.toSession(req, session);
        }
    }

    public static void toSession(HttpServletRequest req, HttpSession session){
        Enumeration<String> parameters = req.getParameterNames();
        Iterator<String> iterator = parameters.asIterator();
        while(iterator.hasNext()){
            String param = iterator.next();
            if(!param.equals("action"))
                session.setAttribute(param, req.getParameter(param));
        }
    }

    public static void clearSession(HttpSession session){
        String locale = "javax.servlet.jsp.jstl.fmt.locale.session";
        Enumeration<String> attributes = session.getAttributeNames();
        Iterator<String> iterator = attributes.asIterator();
        while(iterator.hasNext()){
            String attribute = iterator.next();
            if(!attribute.equals("user") && !attribute.equals("role") && !attribute.equals("id")
                    && !attribute.equals(locale))
                session.removeAttribute(attribute);
        }
    }

//    public static void getSessionAttributes(HttpSession session){
//        Enumeration<String> attributes = session.getAttributeNames();
//        Iterator<String> iterator = attributes.asIterator();
//        while(iterator.hasNext()){
//            String attribute = iterator.next();
//            System.out.println("ATTRIBUTE " + attribute);
//        }
//    }
}
