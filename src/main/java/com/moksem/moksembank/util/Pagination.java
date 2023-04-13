package com.moksem.moksembank.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Pagination class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pagination {
    public static final int RECORDS_PER_PAGE = 5;
    public static final int PAGES = 3;

    /**
     * Set to session attributes for pagination
     *
     * @param req       object of {@link HttpServletRequest} from command
     * @param maxPage   max pages from database
     */
    public static void paginate(HttpServletRequest req, int maxPage) {
        HttpSession session = req.getSession();
        String page = (String) session.getAttribute("page");
        int pageValue = getPage(page);
        List<Integer> maxPages = getPages(maxPage);
        List<Integer> currentPages;
        // 10
        // 1 2 3 4 5 6 7 8 9 10
        // 1 2 3 4 10

//        System.out.println("PAGEVALUE = " + pageValue);
        if (maxPages.size() < PAGES + 2)
            currentPages = maxPages;
        else
            currentPages = createCurrentPages(pageValue, maxPages);
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(currentPages);
        List<Integer> pages;
        if (maxPages.size() > 1)
            pages = new ArrayList<>(List.of(maxPages.get(0), maxPages.get(maxPages.size() - 1)));
        else
            pages = maxPages;
        req.setAttribute("currentPages", currentPages);
        req.setAttribute("pages", pages);
        req.setAttribute("page", pageValue + 1);
    }

    /**
     *  Return list for pagination
     *
     * @param pageValue current page value
     * @param maxPages  max pages from database
     * @return          list of {@link Integer}
     */
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

    /**
     *  Return list for pagination
     *
     * @param maxPage   max pages from database
     * @return          list of {@link Integer}
     */
    public static List<Integer> getPages(int maxPage) {
//        System.out.println((int) Math.ceil(RECORDS_PER_PAGE * 1.0 / maxPage));
        return IntStream.range(1, (int) Math.ceil(maxPage * 1.0 / RECORDS_PER_PAGE) + 1)
                .boxed()
                .toList();
    }

    /**
     * Validate current page value
     *
     * @param currentPage   current page
     * @return              int value of page
     */
    public static int getPage(String currentPage) {
        if (currentPage == null || !currentPage.matches("^\\d+"))
            currentPage = "0";
        return Integer.parseInt(currentPage);
    }
}
