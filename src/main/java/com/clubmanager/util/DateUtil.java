package com.clubmanager.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class cho việc format ngày tháng
 */
public class DateUtil {
    
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    private static final DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern(SQL_DATETIME_FORMAT);
    
    /**
     * Format LocalDateTime sang string dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(dateTimeFormatter);
    }
    
    /**
     * Format LocalDateTime sang string dd/MM/yyyy
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(dateFormatter);
    }
    
    /**
     * Parse string sang LocalDateTime từ format HTML datetime-local
     */
    public static LocalDateTime parseHtmlDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) return null;
        // HTML datetime-local format: yyyy-MM-ddTHH:mm
        return LocalDateTime.parse(dateTimeStr);
    }
    
    /**
     * Format LocalDateTime cho input HTML datetime-local
     */
    public static String formatForHtmlInput(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    /**
     * Format Date sang string
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
