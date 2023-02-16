package com.moksem.moksembank.util.tag;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tag extends TagSupport {
    LocalDateTime dateTime;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            LocalDate localDate = dateTime.toLocalDate();
            LocalTime localTime = dateTime.toLocalTime();

            int day = localDate.getDayOfMonth();
            List<String> month = Arrays.stream(localDate.getMonth().toString().split("")).toList();
            String currentMonth = month.stream()
                    .map(element -> month.indexOf(element) != 0 ? element.toLowerCase() : element.toUpperCase())
                    .collect(Collectors.joining(""));
            int year = localDate.getYear();

            int hour = localTime.getHour();
            int minute = localTime.getMinute();

            out.println(day + "-"
                    + currentMonth + "-"
                    + year + " "
                    + hour + ":"
                    + (minute < 10 ?
                    "0" + minute : minute));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
