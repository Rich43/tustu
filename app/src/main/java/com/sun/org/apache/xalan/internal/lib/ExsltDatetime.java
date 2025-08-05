package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltDatetime.class */
public class ExsltDatetime {
    static final String dt = "yyyy-MM-dd'T'HH:mm:ss";

    /* renamed from: d, reason: collision with root package name */
    static final String f12000d = "yyyy-MM-dd";
    static final String gym = "yyyy-MM";
    static final String gy = "yyyy";
    static final String gmd = "--MM-dd";
    static final String gm = "--MM--";
    static final String gd = "---dd";

    /* renamed from: t, reason: collision with root package name */
    static final String f12001t = "HH:mm:ss";
    static final String EMPTY_STR = "";

    public static String dateTime() {
        Calendar cal = Calendar.getInstance();
        Date datetime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dt);
        StringBuffer buff = new StringBuffer(dateFormat.format(datetime));
        int offset = cal.get(15) + cal.get(16);
        if (offset == 0) {
            buff.append(Constants.HASIDCALL_INDEX_SIG);
        } else {
            int hrs = offset / 3600000;
            int min = offset % 3600000;
            char posneg = hrs < 0 ? '-' : '+';
            buff.append(posneg).append(formatDigits(hrs)).append(':').append(formatDigits(min));
        }
        return buff.toString();
    }

    private static String formatDigits(int q2) {
        String dd = String.valueOf(Math.abs(q2));
        return dd.length() == 1 ? '0' + dd : dd;
    }

    public static String date(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String leader = edz[0];
        String datetime = edz[1];
        String zone = edz[2];
        if (datetime == null || zone == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d};
        Date date = testFormats(datetime, formatsIn);
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(f12000d);
        dateFormat.setLenient(false);
        String dateOut = dateFormat.format(date);
        if (dateOut.length() == 0) {
            return "";
        }
        return leader + dateOut + zone;
    }

    public static String date() {
        String datetime = dateTime().toString();
        String date = datetime.substring(0, datetime.indexOf("T"));
        String zone = datetime.substring(getZoneStart(datetime));
        return date + zone;
    }

    public static String time(String timeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(timeIn);
        String time = edz[1];
        String zone = edz[2];
        if (time == null || zone == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d, f12001t};
        Date date = testFormats(time, formatsIn);
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(f12001t);
        String out = dateFormat.format(date);
        return out + zone;
    }

    public static String time() {
        String datetime = dateTime().toString();
        String time = datetime.substring(datetime.indexOf("T") + 1);
        return time;
    }

    public static double year(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        boolean ad2 = edz[0].length() == 0;
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d, gym, gy};
        double yr = getNumber(datetime, formats, 1);
        if (ad2 || yr == Double.NaN) {
            return yr;
        }
        return -yr;
    }

    public static double year() {
        Calendar cal = Calendar.getInstance();
        return cal.get(1);
    }

    public static double monthInYear(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d, gym, gm, gmd};
        return getNumber(datetime, formats, 2) + 1.0d;
    }

    public static double monthInYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(2) + 1;
    }

    public static double weekInYear(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d};
        return getNumber(datetime, formats, 3);
    }

    public static double weekInYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(3);
    }

    public static double dayInYear(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d};
        return getNumber(datetime, formats, 6);
    }

    public static double dayInYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(6);
    }

    public static double dayInMonth(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        String[] formats = {dt, f12000d, gmd, gd};
        double day = getNumber(datetime, formats, 5);
        return day;
    }

    public static double dayInMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(5);
    }

    public static double dayOfWeekInMonth(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d};
        return getNumber(datetime, formats, 8);
    }

    public static double dayOfWeekInMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(8);
    }

    public static double dayInWeek(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12000d};
        return getNumber(datetime, formats, 7);
    }

    public static double dayInWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(7);
    }

    public static double hourInDay(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12001t};
        return getNumber(datetime, formats, 11);
    }

    public static double hourInDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(11);
    }

    public static double minuteInHour(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12001t};
        return getNumber(datetime, formats, 12);
    }

    public static double minuteInHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(12);
    }

    public static double secondInMinute(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return Double.NaN;
        }
        String[] formats = {dt, f12001t};
        return getNumber(datetime, formats, 13);
    }

    public static double secondInMinute() {
        Calendar cal = Calendar.getInstance();
        return cal.get(13);
    }

    public static XObject leapYear(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return new XNumber(Double.NaN);
        }
        String[] formats = {dt, f12000d, gym, gy};
        double dbl = getNumber(datetime, formats, 1);
        if (dbl == Double.NaN) {
            return new XNumber(Double.NaN);
        }
        int yr = (int) dbl;
        return new XBoolean(yr % 400 == 0 || (yr % 100 != 0 && yr % 4 == 0));
    }

    public static boolean leapYear() {
        Calendar cal = Calendar.getInstance();
        int yr = cal.get(1);
        return yr % 400 == 0 || (yr % 100 != 0 && yr % 4 == 0);
    }

    public static String monthName(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d, gym, gm};
        return getNameOrAbbrev(datetimeIn, formatsIn, "MMMM");
    }

    public static String monthName() {
        Calendar.getInstance();
        return getNameOrAbbrev("MMMM");
    }

    public static String monthAbbreviation(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d, gym, gm};
        return getNameOrAbbrev(datetimeIn, formatsIn, "MMM");
    }

    public static String monthAbbreviation() {
        return getNameOrAbbrev("MMM");
    }

    public static String dayName(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d};
        return getNameOrAbbrev(datetimeIn, formatsIn, "EEEE");
    }

    public static String dayName() {
        return getNameOrAbbrev("EEEE");
    }

    public static String dayAbbreviation(String datetimeIn) throws ParseException {
        String[] edz = getEraDatetimeZone(datetimeIn);
        String datetime = edz[1];
        if (datetime == null) {
            return "";
        }
        String[] formatsIn = {dt, f12000d};
        return getNameOrAbbrev(datetimeIn, formatsIn, "EEE");
    }

    public static String dayAbbreviation() {
        return getNameOrAbbrev("EEE");
    }

    private static String[] getEraDatetimeZone(String in) {
        String leader = "";
        String datetime = in;
        String zone = "";
        if (in.charAt(0) == '-' && !in.startsWith("--")) {
            leader = LanguageTag.SEP;
            datetime = in.substring(1);
        }
        int z2 = getZoneStart(datetime);
        if (z2 > 0) {
            zone = datetime.substring(z2);
            datetime = datetime.substring(0, z2);
        } else if (z2 == -2) {
            zone = null;
        }
        return new String[]{leader, datetime, zone};
    }

    private static int getZoneStart(String datetime) {
        if (datetime.indexOf(Constants.HASIDCALL_INDEX_SIG) == datetime.length() - 1) {
            return datetime.length() - 1;
        }
        if (datetime.length() >= 6 && datetime.charAt(datetime.length() - 3) == ':') {
            if (datetime.charAt(datetime.length() - 6) == '+' || datetime.charAt(datetime.length() - 6) == '-') {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    dateFormat.setLenient(false);
                    dateFormat.parse(datetime.substring(datetime.length() - 5));
                    return datetime.length() - 6;
                } catch (ParseException pe) {
                    System.out.println("ParseException " + pe.getErrorOffset());
                    return -2;
                }
            }
            return -1;
        }
        return -1;
    }

    private static Date testFormats(String in, String[] formats) throws ParseException {
        for (String str : formats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(str);
                dateFormat.setLenient(false);
                return dateFormat.parse(in);
            } catch (ParseException e2) {
            }
        }
        return null;
    }

    private static double getNumber(String in, String[] formats, int calField) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        Date date = testFormats(in, formats);
        if (date == null) {
            return Double.NaN;
        }
        cal.setTime(date);
        return cal.get(calField);
    }

    private static String getNameOrAbbrev(String in, String[] formatsIn, String formatOut) throws ParseException {
        for (String str : formatsIn) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(str, Locale.ENGLISH);
                dateFormat.setLenient(false);
                Date dt2 = dateFormat.parse(in);
                dateFormat.applyPattern(formatOut);
                return dateFormat.format(dt2);
            } catch (ParseException e2) {
            }
        }
        return "";
    }

    private static String getNameOrAbbrev(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(cal.getTime());
    }

    public static String formatDate(String dateTime, String pattern) {
        TimeZone timeZone;
        String zone;
        if (dateTime.endsWith(Constants.HASIDCALL_INDEX_SIG) || dateTime.endsWith("z")) {
            timeZone = TimeZone.getTimeZone("GMT");
            dateTime = dateTime.substring(0, dateTime.length() - 1) + "GMT";
            zone = "z";
        } else if (dateTime.length() >= 6 && dateTime.charAt(dateTime.length() - 3) == ':' && (dateTime.charAt(dateTime.length() - 6) == '+' || dateTime.charAt(dateTime.length() - 6) == '-')) {
            String offset = dateTime.substring(dateTime.length() - 6);
            if ("+00:00".equals(offset) || "-00:00".equals(offset)) {
                timeZone = TimeZone.getTimeZone("GMT");
            } else {
                timeZone = TimeZone.getTimeZone("GMT" + offset);
            }
            zone = "z";
            dateTime = dateTime.substring(0, dateTime.length() - 6) + "GMT" + offset;
        } else {
            timeZone = TimeZone.getDefault();
            zone = "";
        }
        String[] formats = {dt + zone, f12000d, gym, gy};
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat(f12001t + zone);
            inFormat.setLenient(false);
            Date d2 = inFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat(strip("GyMdDEFwW", pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d2);
        } catch (ParseException e2) {
            for (String str : formats) {
                try {
                    SimpleDateFormat inFormat2 = new SimpleDateFormat(str);
                    inFormat2.setLenient(false);
                    Date d3 = inFormat2.parse(dateTime);
                    SimpleDateFormat outFormat2 = new SimpleDateFormat(pattern);
                    outFormat2.setTimeZone(timeZone);
                    return outFormat2.format(d3);
                } catch (ParseException e3) {
                }
            }
            try {
                SimpleDateFormat inFormat3 = new SimpleDateFormat(gmd);
                inFormat3.setLenient(false);
                Date d4 = inFormat3.parse(dateTime);
                SimpleDateFormat outFormat3 = new SimpleDateFormat(strip("Gy", pattern));
                outFormat3.setTimeZone(timeZone);
                return outFormat3.format(d4);
            } catch (ParseException e4) {
                try {
                    SimpleDateFormat inFormat4 = new SimpleDateFormat(gm);
                    inFormat4.setLenient(false);
                    Date d5 = inFormat4.parse(dateTime);
                    SimpleDateFormat outFormat4 = new SimpleDateFormat(strip("Gy", pattern));
                    outFormat4.setTimeZone(timeZone);
                    return outFormat4.format(d5);
                } catch (ParseException e5) {
                    try {
                        SimpleDateFormat inFormat5 = new SimpleDateFormat(gd);
                        inFormat5.setLenient(false);
                        Date d6 = inFormat5.parse(dateTime);
                        SimpleDateFormat outFormat5 = new SimpleDateFormat(strip("GyM", pattern));
                        outFormat5.setTimeZone(timeZone);
                        return outFormat5.format(d6);
                    } catch (ParseException e6) {
                        return "";
                    }
                }
            }
        }
    }

    private static String strip(String symbols, String pattern) {
        int i2 = 0;
        StringBuffer result = new StringBuffer(pattern.length());
        while (i2 < pattern.length()) {
            char ch = pattern.charAt(i2);
            if (ch == '\'') {
                int endQuote = pattern.indexOf(39, i2 + 1);
                if (endQuote == -1) {
                    endQuote = pattern.length();
                }
                result.append(pattern.substring(i2, endQuote));
                int i3 = endQuote;
                int i4 = endQuote + 1;
                i2 = i3;
            } else if (symbols.indexOf(ch) > -1) {
                i2++;
            } else {
                result.append(ch);
                i2++;
            }
        }
        return result.toString();
    }
}
