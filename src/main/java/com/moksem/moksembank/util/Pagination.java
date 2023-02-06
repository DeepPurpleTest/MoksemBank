package com.moksem.moksembank.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.IntStream;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pagination {
    public static final int RECORDS_PER_PAGE = 5;
    public static final int PAGES = 3;

    public static void paginate(HttpServletRequest req, int maxPage) {
        HttpSession session = req.getSession();
        String page = (String) session.getAttribute("page");
        int pageValue = getPage(page);
        List<Integer> maxPages = getPages(maxPage);
        List<Integer> currentPages;

//        System.out.println("PAGEVALUE = " + pageValue);
        if (maxPages.size() < PAGES + 2)
            currentPages = maxPages;
        else
            currentPages = createCurrentPages(pageValue, maxPages);
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(currentPages);
        System.out.println("PAGINATE CURRENT PAGE IS: " + pageValue);
        req.setAttribute("currentPages", currentPages);
        req.setAttribute("pages", maxPages);
        req.setAttribute("page", pageValue + 1);
    }

    private static List<Integer> createCurrentPages(int pageValue, List<Integer> maxPages) {
        List<Integer> currentPages;
        int start;
        int end;
        if (pageValue < PAGES) {
            start = 1;
            end = start + PAGES;
            currentPages = maxPages.subList(start, end);
        } else if (pageValue > maxPages.size() - 1 - PAGES) {
            start = maxPages.size() - 1;
            end = start - PAGES;
            currentPages = maxPages.subList(end, start);
//            System.out.println("PAGEVALUE > maxPages.size() - 2");
        } else {
            start = pageValue - PAGES / 2;
            end = pageValue + PAGES / 2;
            currentPages = maxPages.subList(start, end + 1);
//            System.out.println("ELSE");
        }
        return currentPages;
    }

    public static List<Integer> getPages(int maxPage) {
        System.out.println((int) Math.ceil(RECORDS_PER_PAGE * 1.0 / maxPage));
        return IntStream.range(1, (int) Math.ceil(maxPage * 1.0 / RECORDS_PER_PAGE) + 1)
                .boxed()
                .toList();
    }

    public static int getPage(String page) {
        if (page == null || !page.matches("^\\d+"))
            page = "0";
        return Integer.parseInt(page);
    }

//    public static void main(String[] args) {
//        String page = "198";
//        paginate(page, 1003);
//    }
}
