package com.helpu.classclue.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
    private static final SimpleDateFormat READABLE_TIME_FORMAT = new SimpleDateFormat("h:mm a", Locale.getDefault());

    // Format a date for display
    public static String formatDateForDisplay(String dateStr) {
        try {
            Date date = DATE_FORMAT.parse(dateStr);
            if (date == null) return dateStr;

            Calendar today = Calendar.getInstance();
            Calendar eventDate = Calendar.getInstance();
            eventDate.setTime(date);

            if (isSameDay(today, eventDate)) {
                return "Today";
            } else if (isNextDay(today, eventDate)) {
                return "Tomorrow";
            } else {
                return READABLE_DATE_FORMAT.format(date);
            }
        } catch (ParseException e) {
            return dateStr;
        }
    }

    // Format time for display
    public static String formatTimeForDisplay(String timeStr) {
        try {
            Date time = TIME_FORMAT.parse(timeStr);
            if (time == null) return timeStr;

            return READABLE_TIME_FORMAT.format(time);
        } catch (ParseException e) {
            return timeStr;
        }
    }

    // Format date and time for display (e.g., "Today, 11:30 AM - 12:30 PM")
    public static String formatDateTimeForDisplay(String dateStr, String startTimeStr, String endTimeStr) {
        String formattedDate = formatDateForDisplay(dateStr);
        String formattedStartTime = formatTimeForDisplay(startTimeStr);

        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            String formattedEndTime = formatTimeForDisplay(endTimeStr);
            return formattedDate + ", " + formattedStartTime + " - " + formattedEndTime;
        } else {
            return formattedDate + ", " + formattedStartTime;
        }
    }

    // Check if two calendars represent the same day
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // Check if cal2 is the day after cal1
    private static boolean isNextDay(Calendar cal1, Calendar cal2) {
        Calendar nextDay = (Calendar) cal1.clone();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);
        return isSameDay(nextDay, cal2);
    }
}