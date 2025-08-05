package java.sql;

import java.time.Instant;
import java.time.LocalTime;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/sql/Time.class */
public class Time extends java.util.Date {
    static final long serialVersionUID = 8397324403548013681L;

    @Deprecated
    public Time(int i2, int i3, int i4) {
        super(70, 0, 1, i2, i3, i4);
    }

    public Time(long j2) {
        super(j2);
    }

    @Override // java.util.Date
    public void setTime(long j2) {
        super.setTime(j2);
    }

    public static Time valueOf(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        int iIndexOf = str.indexOf(58);
        int iIndexOf2 = str.indexOf(58, iIndexOf + 1);
        if (!((iIndexOf > 0) & (iIndexOf2 > 0) & (iIndexOf2 < str.length() - 1))) {
            throw new IllegalArgumentException();
        }
        return new Time(Integer.parseInt(str.substring(0, iIndexOf)), Integer.parseInt(str.substring(iIndexOf + 1, iIndexOf2)), Integer.parseInt(str.substring(iIndexOf2 + 1)));
    }

    @Override // java.util.Date
    public String toString() {
        String string;
        String string2;
        String string3;
        int hours = super.getHours();
        int minutes = super.getMinutes();
        int seconds = super.getSeconds();
        if (hours < 10) {
            string = "0" + hours;
        } else {
            string = Integer.toString(hours);
        }
        if (minutes < 10) {
            string2 = "0" + minutes;
        } else {
            string2 = Integer.toString(minutes);
        }
        if (seconds < 10) {
            string3 = "0" + seconds;
        } else {
            string3 = Integer.toString(seconds);
        }
        return string + CallSiteDescriptor.TOKEN_DELIMITER + string2 + CallSiteDescriptor.TOKEN_DELIMITER + string3;
    }

    @Override // java.util.Date
    @Deprecated
    public int getYear() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public int getMonth() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public int getDay() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public int getDate() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setYear(int i2) {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setMonth(int i2) {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setDate(int i2) {
        throw new IllegalArgumentException();
    }

    public static Time valueOf(LocalTime localTime) {
        return new Time(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
    }

    public LocalTime toLocalTime() {
        return LocalTime.of(getHours(), getMinutes(), getSeconds());
    }

    @Override // java.util.Date
    public Instant toInstant() {
        throw new UnsupportedOperationException();
    }
}
