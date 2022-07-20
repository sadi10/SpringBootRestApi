package com.synesisit.gpvas.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public final static String ISO = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYYMMDDD = "yyyyMMDD";

    public static Date parse(String dateString, String format) throws ParseException {
        DateFormat dateformat = new SimpleDateFormat(format);

        return dateformat.parse(dateString);
    }

    public static String format(Date date, String format) throws ParseException {
        DateFormat dateformat = new SimpleDateFormat(format);

        return dateformat.format(date);
    }
}

