package org.apache.commons.net.ntp;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javafx.fxml.FXMLLoader;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ntp/TimeStamp.class */
public class TimeStamp implements Serializable, Comparable<TimeStamp> {
    private static final long serialVersionUID = 8139806907588338737L;
    protected static final long msb0baseTime = 2085978496000L;
    protected static final long msb1baseTime = -2208988800000L;
    public static final String NTP_DATE_FORMAT = "EEE, MMM dd yyyy HH:mm:ss.SSS";
    private final long ntpTime;
    private DateFormat simpleFormatter;
    private DateFormat utcFormatter;

    public TimeStamp(long ntpTime) {
        this.ntpTime = ntpTime;
    }

    public TimeStamp(String hexStamp) throws NumberFormatException {
        this.ntpTime = decodeNtpHexString(hexStamp);
    }

    public TimeStamp(Date d2) {
        this.ntpTime = d2 == null ? 0L : toNtpTime(d2.getTime());
    }

    public long ntpValue() {
        return this.ntpTime;
    }

    public long getSeconds() {
        return (this.ntpTime >>> 32) & 4294967295L;
    }

    public long getFraction() {
        return this.ntpTime & 4294967295L;
    }

    public long getTime() {
        return getTime(this.ntpTime);
    }

    public Date getDate() {
        long time = getTime(this.ntpTime);
        return new Date(time);
    }

    public static long getTime(long ntpTimeValue) {
        long seconds = (ntpTimeValue >>> 32) & 4294967295L;
        long fraction = Math.round((1000.0d * (ntpTimeValue & 4294967295L)) / 4.294967296E9d);
        long msb = seconds & 2147483648L;
        if (msb == 0) {
            return msb0baseTime + (seconds * 1000) + fraction;
        }
        return msb1baseTime + (seconds * 1000) + fraction;
    }

    public static TimeStamp getNtpTime(long date) {
        return new TimeStamp(toNtpTime(date));
    }

    public static TimeStamp getCurrentTime() {
        return getNtpTime(System.currentTimeMillis());
    }

    protected static long decodeNtpHexString(String hexString) throws NumberFormatException {
        if (hexString == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        int ind = hexString.indexOf(46);
        if (ind == -1) {
            if (hexString.length() == 0) {
                return 0L;
            }
            return Long.parseLong(hexString, 16) << 32;
        }
        return (Long.parseLong(hexString.substring(0, ind), 16) << 32) | Long.parseLong(hexString.substring(ind + 1), 16);
    }

    public static TimeStamp parseNtpString(String s2) throws NumberFormatException {
        return new TimeStamp(decodeNtpHexString(s2));
    }

    protected static long toNtpTime(long t2) {
        long baseTime;
        boolean useBase1 = t2 < msb0baseTime;
        if (useBase1) {
            baseTime = t2 - msb1baseTime;
        } else {
            baseTime = t2 - msb0baseTime;
        }
        long seconds = baseTime / 1000;
        long fraction = ((baseTime % 1000) * EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH) / 1000;
        if (useBase1) {
            seconds |= 2147483648L;
        }
        long time = (seconds << 32) | fraction;
        return time;
    }

    public int hashCode() {
        return (int) (this.ntpTime ^ (this.ntpTime >>> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof TimeStamp) && this.ntpTime == ((TimeStamp) obj).ntpValue();
    }

    public String toString() {
        return toString(this.ntpTime);
    }

    private static void appendHexString(StringBuilder buf, long l2) {
        String s2 = Long.toHexString(l2);
        for (int i2 = s2.length(); i2 < 8; i2++) {
            buf.append('0');
        }
        buf.append(s2);
    }

    public static String toString(long ntpTime) {
        StringBuilder buf = new StringBuilder();
        appendHexString(buf, (ntpTime >>> 32) & 4294967295L);
        buf.append('.');
        appendHexString(buf, ntpTime & 4294967295L);
        return buf.toString();
    }

    public String toDateString() {
        if (this.simpleFormatter == null) {
            this.simpleFormatter = new SimpleDateFormat(NTP_DATE_FORMAT, Locale.US);
            this.simpleFormatter.setTimeZone(TimeZone.getDefault());
        }
        Date ntpDate = getDate();
        return this.simpleFormatter.format(ntpDate);
    }

    public String toUTCString() {
        if (this.utcFormatter == null) {
            this.utcFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", Locale.US);
            this.utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        Date ntpDate = getDate();
        return this.utcFormatter.format(ntpDate);
    }

    @Override // java.lang.Comparable
    public int compareTo(TimeStamp anotherTimeStamp) {
        long thisVal = this.ntpTime;
        long anotherVal = anotherTimeStamp.ntpTime;
        if (thisVal < anotherVal) {
            return -1;
        }
        return thisVal == anotherVal ? 0 : 1;
    }
}
