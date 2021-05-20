package me.japanesestudy.app.wordremember.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by guyu on 2018/1/21.
 */

public class DateTool {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static Calendar calendar = Calendar.getInstance();

    public static String getDateString(long timemills) {
        Date date = new Date(timemills);
        return simpleDateFormat.format(date);
    }
    public static Date getDate(long timemills) {
        String dateString = getDateString(timemills);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getDateString(Date date) {
        return simpleDateFormat.format(date);
    }
    public static Date getYesterday() {
        Date date = getDate(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }
    public static Date getToday() {
        return getDate(System.currentTimeMillis());
    }
}
