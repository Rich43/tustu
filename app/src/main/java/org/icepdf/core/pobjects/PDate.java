package org.icepdf.core.pobjects;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.pobjects.security.SecurityManager;
import sun.util.locale.LanguageTag;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PDate.class */
public class PDate {
    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("'D:'yyyyMMddHHmmss");
    private static final int OFFSET_YYYY = 4;
    private static final int OFFSET_MM = 2;
    private static final int OFFSET_DD = 2;
    private static final int OFFSET_HH = 2;
    private static final int OFFSET_mm = 2;
    private static final int OFFSET_SS = 2;
    private static final int OFFSET_0 = 1;
    private static final String DATE_PREFIX = "D:";
    private static String[] monthNames;
    private String year = "";
    private String month = "";
    private String day = "";
    private String hour = "";
    private String minute = "";
    private String second = "";
    private String timeZoneOffset = "";
    private String timeZoneHour = "";
    private String timeZoneMinute = "";
    private boolean notStandardFormat = false;

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        monthNames = new String[]{"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    public PDate(SecurityManager securityManager, String date) {
        if (date != null) {
            parseDate(date);
        }
    }

    public String getYear() {
        return this.year;
    }

    public String getMonth() {
        return this.month;
    }

    public String getDay() {
        return this.day;
    }

    public String getHour() {
        return this.hour;
    }

    public String getMinute() {
        return this.minute;
    }

    public String getSecond() {
        return this.second;
    }

    public String getTimeZoneHour() {
        return this.timeZoneHour;
    }

    public String getTimeZoneMinute() {
        return this.timeZoneMinute;
    }

    public boolean getTimeZoneOffset() {
        return !LanguageTag.SEP.equals(this.timeZoneOffset);
    }

    public String toString() {
        if (!this.notStandardFormat) {
            StringBuilder sb = new StringBuilder(40);
            if (getMonth(this.month).length() > 0) {
                sb.append(getMonth(this.month));
            }
            if (this.day.length() > 0) {
                sb.append(" ").append(this.day);
            }
            if (this.year.length() > 0) {
                sb.append(", ").append(this.year);
            }
            if (this.hour.length() > 0) {
                sb.append(" ").append(this.hour);
            }
            if (this.minute.length() > 0) {
                sb.append(CallSiteDescriptor.TOKEN_DELIMITER).append(this.minute);
            }
            if (this.second.length() > 0) {
                sb.append(CallSiteDescriptor.TOKEN_DELIMITER).append(this.second);
            }
            if (this.timeZoneOffset.length() > 0) {
                if (this.timeZoneOffset.equalsIgnoreCase(Constants.HASIDCALL_INDEX_SIG)) {
                    sb.append(" (UTC)");
                } else {
                    sb.append(" (UTC ").append(this.timeZoneOffset);
                    if (this.timeZoneHour.length() > 0) {
                        sb.append("").append(this.timeZoneHour);
                    }
                    if (this.timeZoneMinute.length() > 0) {
                        sb.append(CallSiteDescriptor.TOKEN_DELIMITER).append(this.timeZoneMinute);
                    }
                    sb.append(")");
                }
            }
            return sb.toString();
        }
        return this.day;
    }

    private void parseDate(String date) {
        if (date.indexOf(DATE_PREFIX) >= 0) {
            parseAdobeDate(date.substring(2));
            return;
        }
        if (date.indexOf("/") >= 0) {
            parseGhostScriptDate(date);
            return;
        }
        this.year = date;
        this.month = date;
        this.day = date;
        this.hour = date;
        this.minute = date;
        this.second = date;
        this.timeZoneOffset = date;
        this.timeZoneHour = date;
        this.timeZoneMinute = date;
        this.notStandardFormat = true;
    }

    private void parseGhostScriptDate(String date) {
        StringTokenizer dateTime = new StringTokenizer(date);
        StringTokenizer dateToken = new StringTokenizer(dateTime.nextToken(), "/");
        StringTokenizer timeToken = new StringTokenizer(dateTime.nextToken(), CallSiteDescriptor.TOKEN_DELIMITER);
        this.month = dateToken.nextToken();
        this.day = dateToken.nextToken();
        this.year = dateToken.nextToken();
        this.hour = timeToken.nextToken();
        this.minute = timeToken.nextToken();
        this.second = timeToken.nextToken();
    }

    private void parseAdobeDate(String date) {
        int totalOffset = 0;
        if (0 + 4 <= date.length()) {
            int currentOffset = 0 + 4;
            this.year = date.substring(0, currentOffset);
            totalOffset = 0 + currentOffset;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset2 = totalOffset + 2;
            this.month = date.substring(totalOffset, currentOffset2);
            totalOffset += 2;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset3 = totalOffset + 2;
            this.day = date.substring(totalOffset, currentOffset3);
            totalOffset += 2;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset4 = totalOffset + 2;
            this.hour = date.substring(totalOffset, currentOffset4);
            totalOffset += 2;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset5 = totalOffset + 2;
            this.minute = date.substring(totalOffset, currentOffset5);
            totalOffset += 2;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset6 = totalOffset + 2;
            this.second = date.substring(totalOffset, currentOffset6);
            totalOffset += 2;
        }
        if (totalOffset + 1 <= date.length()) {
            int currentOffset7 = totalOffset + 1;
            this.timeZoneOffset = date.substring(totalOffset, currentOffset7);
            totalOffset++;
        }
        if (totalOffset + 2 <= date.length()) {
            int currentOffset8 = totalOffset + 2;
            this.timeZoneHour = date.substring(totalOffset, currentOffset8);
            totalOffset += 2;
        }
        if (totalOffset + 4 <= date.length()) {
            this.timeZoneMinute = date.substring(totalOffset + 1, totalOffset + 3);
        }
    }

    private String getMonth(String month) {
        int monthIndex = 0;
        try {
            monthIndex = Integer.parseInt(month);
        } catch (NumberFormatException e2) {
        }
        return monthNames[monthIndex];
    }

    public static String formatDateTime(Date time, TimeZone tz) {
        Calendar cal = Calendar.getInstance(tz, Locale.ENGLISH);
        cal.setTime(time);
        int offset = cal.get(15) + cal.get(16);
        Date dt1 = new Date(time.getTime() + offset);
        StringBuffer sb = new StringBuffer();
        sb.append(DATE_FORMAT.format(dt1));
        int offset2 = offset / 60000;
        if (offset2 == 0) {
            sb.append('Z');
        } else {
            if (offset2 > 0) {
                sb.append('+');
            } else {
                sb.append('-');
            }
            int offsetHour = Math.abs(offset2 / 60);
            int offsetMinutes = Math.abs(offset2 % 60);
            if (offsetHour < 10) {
                sb.append('0');
            }
            sb.append(Integer.toString(offsetHour));
            sb.append('\'');
            if (offsetMinutes < 10) {
                sb.append('0');
            }
            sb.append(Integer.toString(offsetMinutes));
            sb.append('\'');
        }
        return sb.toString();
    }

    public static String formatDateTime(Date time) {
        return formatDateTime(time, TimeZone.getDefault());
    }

    public static PDate createDate(Date date) {
        return new PDate(null, formatDateTime(date));
    }
}
