/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.SQLException;
import java.util.*;
import java.text.*;

/**
 *
 * @author Stephen
 */
public class DateUtil {

    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOUR_MILLIS * 24;
    public final static long YEAR_MILLIS = DAY_MILLIS * 365;

    public static DateFormat OUT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static DateFormat OUT_TIME_FORMAT = new SimpleDateFormat("H:mm:ss");
    public static DateFormat OUT_DATETIME_FORMAT = new SimpleDateFormat("d/M/yyyy H:mm:ss");
    public static DateFormat OUT_TIMESTAMP_FORMAT = new SimpleDateFormat("d/M/yy H:mm:ss.SSS");

    public static DateFormat IN_DATE_FORMAT = new SimpleDateFormat("d/M/yy");
    public static DateFormat IN_TIME_FORMAT = new SimpleDateFormat("H:mm:ss");
    public static DateFormat IN_DATETIME_FORMAT = new SimpleDateFormat("d/M/yy H:mm:ss");
    public static DateFormat IN_TIMESTAMP_FORMAT = new SimpleDateFormat("d/M/yy H:mm:ss.SSS");

    public static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddkkmmss");

    public static Calendar calendar = new GregorianCalendar();

    static {
        IN_DATE_FORMAT.setLenient(false);
        IN_TIME_FORMAT.setLenient(false);
        IN_DATETIME_FORMAT.setLenient(false);
    }

    /**
     * Create a new DateTime. To the last second. This will not create any
     * extra-millis-seconds, which may cause bugs when writing to stores such as
     * databases that round milli-seconds up and down.
     */
    public static java.util.Date newDateTime() {
        return new java.util.Date((System.currentTimeMillis() / SECOND_MILLIS) * SECOND_MILLIS);
    }

    /**
     * Create a new Date. To the last day.
     */
    public static java.sql.Date newDate() {
        return new java.sql.Date((System.currentTimeMillis() / DAY_MILLIS) * DAY_MILLIS);
    }

    /**
     * Create a new Time, with no date component.
     */
    public static java.sql.Time newTime() {
        return new java.sql.Time(System.currentTimeMillis() % DAY_MILLIS);
    }

    /**
     * Create a new Timestamp.
     */
    public static java.sql.Timestamp newTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * Get the seconds difference
     */
    public static int secondsDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / SECOND_MILLIS) - (earlierDate.getTime() / SECOND_MILLIS));
    }

    /**
     * Get the minutes difference
     */
    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / MINUTE_MILLIS) - (earlierDate.getTime() / MINUTE_MILLIS));
    }

    /**
     * Get the hours difference
     */
    public static int hoursDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / HOUR_MILLIS) - (earlierDate.getTime() / HOUR_MILLIS));
    }

    /**
     * Get the days difference
     */
    public static int daysDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / DAY_MILLIS) - (earlierDate.getTime() / DAY_MILLIS));
    }

    /**
     * Roll the java.util.Time forward or backward.
     *
     * @param startDate - The start date
     * @period Calendar.YEAR etc
     * @param amount - Negative to rollbackwards.
     */
    public static java.sql.Time rollTime(java.util.Date startDate, int period, int amount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(period, amount);
        return new java.sql.Time(gc.getTime().getTime());
    }

    /**
     * Roll the java.util.Date forward or backward.
     *
     * @param startDate - The start date
     * @period Calendar.YEAR etc
     * @param amount - Negative to rollbackwards.
     */
    public static java.util.Date rollDateTime(java.util.Date startDate, int period, int amount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(period, amount);
        return new java.util.Date(gc.getTime().getTime());
    }

    /**
     * Roll the java.sql.Date forward or backward.
     *
     * @param startDate - The start date
     * @period Calendar.YEAR etc
     * @param amount - Negative to rollbackwards.
     */
    public static java.sql.Date rollDate(java.util.Date startDate, int period, int amount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startDate);
        gc.add(period, amount);
        return new java.sql.Date(gc.getTime().getTime());
    }

    /**
     * Roll the years forward or backward.
     *
     * @param startDate - The start date
     * @param years - Negative to rollbackwards.
     */
    public static java.sql.Date rollYears(java.util.Date startDate, int years) {
        return rollDate(startDate, Calendar.YEAR, years);
    }

    /**
     * Roll the days forward or backward.
     *
     * @param startDate - The start date
     * @param months - Negative to rollbackwards.
     */
    public static java.sql.Date rollMonths(java.util.Date startDate, int months) {
        return rollDate(startDate, Calendar.MONTH, months);
    }

    /**
     * Roll the days forward or backward.
     *
     * @param startDate - The start date
     * @param days - Negative to rollbackwards.
     */
    public static java.sql.Date rollDays(java.util.Date startDate, int days) {
        return rollDate(startDate, Calendar.DATE, days);
    }

    /**
     * Checks the day, month and year are equal.
     */
    public static boolean dateEquals(java.util.Date d1, java.util.Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        return d1.getDate() == d2.getDate()
                && d1.getMonth() == d2.getMonth()
                && d1.getYear() == d2.getYear();
    }

    /**
     * Checks the hour, minute and second are equal.
     */
    public static boolean timeEquals(java.util.Date d1, java.util.Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        return d1.getHours() == d2.getHours()
                && d1.getMinutes() == d2.getMinutes()
                && d1.getSeconds() == d2.getSeconds();
    }

    /**
     * Checks the second, hour, month, day, month and year are equal.
     */
    public static boolean dateTimeEquals(java.util.Date d1, java.util.Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        return d1.getDate() == d2.getDate()
                && d1.getMonth() == d2.getMonth()
                && d1.getYear() == d2.getYear()
                && d1.getHours() == d2.getHours()
                && d1.getMinutes() == d2.getMinutes()
                && d1.getSeconds() == d2.getSeconds();
    }

    /**
     * Convert an Object of type Classs to an Object.
     */
    public static Object toObject(Class clazz, Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        if (clazz == null) {
            return value;
        }

        if (java.sql.Date.class.isAssignableFrom(clazz)) {
            return toDate(value);
        }
        if (java.sql.Time.class.isAssignableFrom(clazz)) {
            return toTime(value);
        }
        if (java.sql.Timestamp.class.isAssignableFrom(clazz)) {
            return toTimestamp(value);
        }
        if (java.util.Date.class.isAssignableFrom(clazz)) {
            return toDateTime(value);
        }

        return value;
    }

    /**
     * Convert an Object to a DateTime, without an Exception
     */
    public static java.util.Date getDateTime(Object value) {
        try {
            return toDateTime(value);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * Convert an Object to a DateTime.
     */
    public static java.util.Date toDateTime(Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.util.Date) {
            return (java.util.Date) value;
        }
        if (value instanceof String) {
            if ("".equals((String) value)) {
                return null;
            }
            return IN_DATETIME_FORMAT.parse((String) value);
        }

        return IN_DATETIME_FORMAT.parse(value.toString());
    }

    /**
     * Convert an Object to a Date, without an Exception
     */
    public static java.sql.Date getDate(Object value) {
        try {
            return toDate(value);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * Convert an Object to a Date.
     */
    public static java.sql.Date toDate(Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof String) {
            if ("".equals((String) value)) {
                return null;
            }
            return new java.sql.Date(IN_DATE_FORMAT.parse((String) value).getTime());
        }

        return new java.sql.Date(IN_DATE_FORMAT.parse(value.toString()).getTime());
    }

    /**
     * Convert an Object to a Time, without an Exception
     */
    public static java.sql.Time getTime(Object value) {
        try {
            return toTime(value);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * Convert an Object to a Time.
     */
    public static java.sql.Time toTime(Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Time) {
            return (java.sql.Time) value;
        }
        if (value instanceof String) {
            if ("".equals((String) value)) {
                return null;
            }
            return new java.sql.Time(IN_TIME_FORMAT.parse((String) value).getTime());
        }

        return new java.sql.Time(IN_TIME_FORMAT.parse(value.toString()).getTime());
    }

    /**
     * Convert an Object to a Timestamp, without an Exception
     */
    public static java.sql.Timestamp getTimestamp(Object value) {
        try {
            return toTimestamp(value);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * Convert an Object to a Timestamp.
     */
    public static java.sql.Timestamp toTimestamp(Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }
        if (value instanceof String) {
            if ("".equals((String) value)) {
                return null;
            }
            return new java.sql.Timestamp(IN_TIMESTAMP_FORMAT.parse((String) value).getTime());
        }

        return new java.sql.Timestamp(IN_TIMESTAMP_FORMAT.parse(value.toString()).getTime());
    }

    /**
     * Tells you if the date part of a datetime is in a certain time range.
     */
    public static boolean isTimeInRange(java.sql.Time start, java.sql.Time end, java.util.Date d) {
        d = new java.sql.Time(d.getHours(), d.getMinutes(), d.getSeconds());

        if (start == null || end == null) {
            return false;
        }

        if (start.before(end) && (!(d.after(start) && d.before(end)))) {
            return false;
        }

        if (end.before(start) && (!(d.after(end) || d.before(start)))) {
            return false;
        }
        return true;
    }

    public static int getYear(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDate(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static int getHour(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR);
    }

    public static int getMinute(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSeconds(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static int getMillisecond(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * Convert an Object to a String using Dates
     */
    public static String toString(Object date) {
        if (date == null) {
            return null;
        }

        if (java.sql.Timestamp.class.isAssignableFrom(date.getClass())) {
            return OUT_TIMESTAMP_FORMAT.format(date);
        }
        if (java.sql.Time.class.isAssignableFrom(date.getClass())) {
            return OUT_TIME_FORMAT.format(date);
        }
        if (java.sql.Date.class.isAssignableFrom(date.getClass())) {
            return OUT_DATE_FORMAT.format(date);
        }
        if (java.util.Date.class.isAssignableFrom(date.getClass())) {
            return OUT_DATETIME_FORMAT.format(date);
        }

        throw new IllegalArgumentException("Unsupported type " + date.getClass());
    }

    public static String readDate(String date) {
        // this function reads out any date with format yyyy-MM-dd
        String[] months = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String dateText = "";
        String superscript = "ᵗʰ";
        try {
            String[] dates = date.split("-");
            String year = dates[0].trim();
            String month = dates[1].trim();
            String day = dates[2].trim();
            int mth = Integer.parseInt(month);
            int dy = Integer.parseInt(day);

            if (dy != 13 && day.charAt(day.length() - 1) == '3') {
                superscript = "ʳᵈ";
            } else if (dy != 11 && day.charAt(day.length() - 1) == '1') {
                superscript = "st";
            } else if (dy != 12 && day.charAt(day.length() - 1) == '2') {
                superscript = "nd";
            }

            dateText = dy + superscript + " " + months[mth - 1] + " " + year;
        } catch (Exception e) {
            dateText = "N/A";
        }
        return dateText;
    }

    public static String calculateTimeDifference(java.sql.Timestamp oldTime, java.sql.Timestamp currentTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        
        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String difference = "";
        if (diffMinutes >= 60) {
            difference = diffHours + " hour";
            if (diffHours > 1) {
                difference = difference + "s";
            }
        } else if (diffSeconds >= 60) {
            difference = diffMinutes + " minute";
            if (diffMinutes > 1) {
                difference = difference + "s";
            }
        } else {
            difference = diffSeconds + " second";
            if (diffSeconds > 1) {
                difference = difference + "s";
            }
        }
        return difference;
    }

    public static long calculateTimeDifferenceReturnMilliseconds(java.sql.Timestamp oldTime, java.sql.Timestamp currentTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        return diff;
    }

    public static String ReadMilliseconds(long milli) {
        long diffSeconds = milli / 1000;
        long diffMinutes = milli / (60 * 1000);
        long diffHours = milli / (60 * 60 * 1000);
        long diffDays = milli / (24 * 60 * 60 * 1000);

        String difference = "";
        if (diffMinutes >= 60) {
            difference = diffHours + " hour";
            if (diffHours > 1) {
                difference = difference + "s";
            }
        } else if (diffSeconds >= 60) {
            difference = diffMinutes + " minute";
            if (diffMinutes > 1) {
                difference = difference + "s";
            }
        } else {
            difference = diffSeconds + " second";
            if (diffSeconds > 1) {
                difference = difference + "s";
            }
        }
        return difference;
    }

    public static String readTime(String time) throws ClassNotFoundException, SQLException {
        String realTime = "";
        String suffix = "A.M";
        String nums[] = time.split(":");
        if (Integer.parseInt(nums[0]) > 12) {
            nums[0] = "" + (Integer.parseInt(nums[0]) - 12);
            suffix = "P.M";
        } else if (Integer.parseInt(nums[0]) == 12) {
            suffix = "P.M";
        }
        realTime = "" + nums[0] + ":" + nums[1] + " " + suffix;
        return realTime;
    }

    public static String ConvertMinutesToHours(int minutes) throws ClassNotFoundException, SQLException {
        String realTime = "N/A";
        try {
            int hours = minutes / 60;
            int left_minutes = minutes % 60;
            String lm = "";
            if (left_minutes > 0) {
                lm = left_minutes + " minutes";
            }
            String hrs = "";
            if(hours > 0) hrs = hours + " hours ";
            realTime = hrs + "" + lm;
        } catch (Exception e) {
            return "0";
        }
        if(realTime.trim().equals("")) realTime = "0";
        return realTime;
    }

    public static long calculateTimeDifferenceInHours(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffHours = diff / (60 * 60 * 1000);

        return diffHours;
    }

    public static long convertTimeToHours(java.sql.Timestamp Time) {
        long milliseconds1 = Time.getTime();
        long timeInHours = milliseconds1 / (60 * 60 * 1000);

        return timeInHours;
    }

}
