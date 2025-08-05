package com.sun.webkit.network;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: jfxrt.jar:com/sun/webkit/network/DateParser.class */
final class DateParser {
    private static final Logger logger = Logger.getLogger(DateParser.class.getName());
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("[\\x09\\x20-\\x2F\\x3B-\\x40\\x5B-\\x60\\x7B-\\x7E]+");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(?:[^\\d].*)*");
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})(?:[^\\d].*)*");
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})(?:[^\\d].*)*");
    private static final Map<String, Integer> MONTH_MAP;

    static {
        Map<String, Integer> map = new HashMap<>(12);
        map.put("jan", 0);
        map.put("feb", 1);
        map.put("mar", 2);
        map.put("apr", 3);
        map.put("may", 4);
        map.put("jun", 5);
        map.put("jul", 6);
        map.put("aug", 7);
        map.put("sep", 8);
        map.put("oct", 9);
        map.put("nov", 10);
        map.put("dec", 11);
        MONTH_MAP = Collections.unmodifiableMap(map);
    }

    private DateParser() {
        throw new AssertionError();
    }

    static long parse(String date) throws ParseException {
        Integer yearTmp;
        Integer monthTmp;
        Integer dayOfMonthTmp;
        Time timeTmp;
        logger.log(Level.FINEST, "date: [{0}]", date);
        Time time = null;
        Integer dayOfMonth = null;
        Integer month = null;
        Integer year = null;
        String[] tokens = DELIMITER_PATTERN.split(date, 0);
        for (String token : tokens) {
            if (token.length() != 0) {
                if (time == null && (timeTmp = parseTime(token)) != null) {
                    time = timeTmp;
                } else if (dayOfMonth == null && (dayOfMonthTmp = parseDayOfMonth(token)) != null) {
                    dayOfMonth = dayOfMonthTmp;
                } else if (month == null && (monthTmp = parseMonth(token)) != null) {
                    month = monthTmp;
                } else if (year == null && (yearTmp = parseYear(token)) != null) {
                    year = yearTmp;
                }
            }
        }
        if (year != null) {
            if (year.intValue() >= 70 && year.intValue() <= 99) {
                year = Integer.valueOf(year.intValue() + 1900);
            } else if (year.intValue() >= 0 && year.intValue() <= 69) {
                year = Integer.valueOf(year.intValue() + 2000);
            }
        }
        if (time == null || dayOfMonth == null || month == null || year == null || dayOfMonth.intValue() < 1 || dayOfMonth.intValue() > 31 || year.intValue() < 1601 || time.hour > 23 || time.minute > 59 || time.second > 59) {
            throw new ParseException("Error parsing date", 0);
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setLenient(false);
        calendar.clear();
        calendar.set(year.intValue(), month.intValue(), dayOfMonth.intValue(), time.hour, time.minute, time.second);
        try {
            long result = calendar.getTimeInMillis();
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "result: [{0}]", new Date(result).toString());
            }
            return result;
        } catch (Exception ex) {
            ParseException pe = new ParseException("Error parsing date", 0);
            pe.initCause(ex);
            throw pe;
        }
    }

    private static Time parseTime(String token) {
        Matcher matcher = TIME_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new Time(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
        }
        return null;
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/DateParser$Time.class */
    private static final class Time {
        private final int hour;
        private final int minute;
        private final int second;

        private Time(int hour, int minute, int second) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
    }

    private static Integer parseDayOfMonth(String token) {
        Matcher matcher = DAY_OF_MONTH_PATTERN.matcher(token);
        if (matcher.matches()) {
            return Integer.valueOf(Integer.parseInt(matcher.group(1)));
        }
        return null;
    }

    private static Integer parseMonth(String token) {
        if (token.length() >= 3) {
            return MONTH_MAP.get(token.substring(0, 3).toLowerCase());
        }
        return null;
    }

    private static Integer parseYear(String token) {
        Matcher matcher = YEAR_PATTERN.matcher(token);
        if (matcher.matches()) {
            return Integer.valueOf(Integer.parseInt(matcher.group(1)));
        }
        return null;
    }
}
