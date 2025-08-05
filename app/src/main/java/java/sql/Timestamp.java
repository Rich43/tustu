package java.sql;

import java.time.Instant;
import java.time.LocalDateTime;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/sql/Timestamp.class */
public class Timestamp extends java.util.Date {
    private int nanos;
    static final long serialVersionUID = 2745179027874758501L;
    private static final int MILLIS_PER_SECOND = 1000;

    @Deprecated
    public Timestamp(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        super(i2, i3, i4, i5, i6, i7);
        if (i8 > 999999999 || i8 < 0) {
            throw new IllegalArgumentException("nanos > 999999999 or < 0");
        }
        this.nanos = i8;
    }

    public Timestamp(long j2) {
        super((j2 / 1000) * 1000);
        this.nanos = (int) ((j2 % 1000) * 1000000);
        if (this.nanos < 0) {
            this.nanos = 1000000000 + this.nanos;
            super.setTime(((j2 / 1000) - 1) * 1000);
        }
    }

    @Override // java.util.Date
    public void setTime(long j2) {
        super.setTime((j2 / 1000) * 1000);
        this.nanos = (int) ((j2 % 1000) * 1000000);
        if (this.nanos < 0) {
            this.nanos = 1000000000 + this.nanos;
            super.setTime(((j2 / 1000) - 1) * 1000);
        }
    }

    @Override // java.util.Date
    public long getTime() {
        return super.getTime() + (this.nanos / 1000000);
    }

    public static Timestamp valueOf(String str) throws NumberFormatException {
        int i2;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        if (str == null) {
            throw new IllegalArgumentException("null string");
        }
        String strTrim = str.trim();
        int iIndexOf = strTrim.indexOf(32);
        if (iIndexOf > 0) {
            String strSubstring = strTrim.substring(0, iIndexOf);
            String strSubstring2 = strTrim.substring(iIndexOf + 1);
            int iIndexOf2 = strSubstring.indexOf(45);
            int iIndexOf3 = strSubstring.indexOf(45, iIndexOf2 + 1);
            if (strSubstring2 == null) {
                throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
            }
            int iIndexOf4 = strSubstring2.indexOf(58);
            int iIndexOf5 = strSubstring2.indexOf(58, iIndexOf4 + 1);
            int iIndexOf6 = strSubstring2.indexOf(46, iIndexOf5 + 1);
            boolean z2 = false;
            if (iIndexOf2 > 0 && iIndexOf3 > 0 && iIndexOf3 < strSubstring.length() - 1) {
                String strSubstring3 = strSubstring.substring(0, iIndexOf2);
                String strSubstring4 = strSubstring.substring(iIndexOf2 + 1, iIndexOf3);
                String strSubstring5 = strSubstring.substring(iIndexOf3 + 1);
                if (strSubstring3.length() == 4 && strSubstring4.length() >= 1 && strSubstring4.length() <= 2 && strSubstring5.length() >= 1 && strSubstring5.length() <= 2) {
                    i3 = Integer.parseInt(strSubstring3);
                    i4 = Integer.parseInt(strSubstring4);
                    i5 = Integer.parseInt(strSubstring5);
                    if (i4 >= 1 && i4 <= 12 && i5 >= 1 && i5 <= 31) {
                        z2 = true;
                    }
                }
            }
            if (!z2) {
                throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
            }
            if ((iIndexOf4 > 0) & (iIndexOf5 > 0) & (iIndexOf5 < strSubstring2.length() - 1)) {
                int i7 = Integer.parseInt(strSubstring2.substring(0, iIndexOf4));
                int i8 = Integer.parseInt(strSubstring2.substring(iIndexOf4 + 1, iIndexOf5));
                if ((iIndexOf6 > 0) & (iIndexOf6 < strSubstring2.length() - 1)) {
                    i2 = Integer.parseInt(strSubstring2.substring(iIndexOf5 + 1, iIndexOf6));
                    String strSubstring6 = strSubstring2.substring(iIndexOf6 + 1);
                    if (strSubstring6.length() > 9) {
                        throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
                    }
                    if (!Character.isDigit(strSubstring6.charAt(0))) {
                        throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
                    }
                    i6 = Integer.parseInt(strSubstring6 + "000000000".substring(0, 9 - strSubstring6.length()));
                } else {
                    if (iIndexOf6 > 0) {
                        throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
                    }
                    i2 = Integer.parseInt(strSubstring2.substring(iIndexOf5 + 1));
                }
                return new Timestamp(i3 - 1900, i4 - 1, i5, i7, i8, i2, i6);
            }
            throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
        }
        throw new IllegalArgumentException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
    }

    @Override // java.util.Date
    public String toString() {
        String str;
        String string;
        String string2;
        String string3;
        String string4;
        String string5;
        String str2;
        int year = super.getYear() + 1900;
        int month = super.getMonth() + 1;
        int date = super.getDate();
        int hours = super.getHours();
        int minutes = super.getMinutes();
        int seconds = super.getSeconds();
        if (year < 1000) {
            String str3 = "" + year;
            str = "0000".substring(0, 4 - str3.length()) + str3;
        } else {
            str = "" + year;
        }
        if (month < 10) {
            string = "0" + month;
        } else {
            string = Integer.toString(month);
        }
        if (date < 10) {
            string2 = "0" + date;
        } else {
            string2 = Integer.toString(date);
        }
        if (hours < 10) {
            string3 = "0" + hours;
        } else {
            string3 = Integer.toString(hours);
        }
        if (minutes < 10) {
            string4 = "0" + minutes;
        } else {
            string4 = Integer.toString(minutes);
        }
        if (seconds < 10) {
            string5 = "0" + seconds;
        } else {
            string5 = Integer.toString(seconds);
        }
        if (this.nanos == 0) {
            str2 = "0";
        } else {
            String string6 = Integer.toString(this.nanos);
            String str4 = "000000000".substring(0, 9 - string6.length()) + string6;
            char[] cArr = new char[str4.length()];
            str4.getChars(0, str4.length(), cArr, 0);
            int i2 = 8;
            while (cArr[i2] == '0') {
                i2--;
            }
            str2 = new String(cArr, 0, i2 + 1);
        }
        StringBuffer stringBuffer = new StringBuffer(20 + str2.length());
        stringBuffer.append(str);
        stringBuffer.append(LanguageTag.SEP);
        stringBuffer.append(string);
        stringBuffer.append(LanguageTag.SEP);
        stringBuffer.append(string2);
        stringBuffer.append(" ");
        stringBuffer.append(string3);
        stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
        stringBuffer.append(string4);
        stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
        stringBuffer.append(string5);
        stringBuffer.append(".");
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    public int getNanos() {
        return this.nanos;
    }

    public void setNanos(int i2) {
        if (i2 > 999999999 || i2 < 0) {
            throw new IllegalArgumentException("nanos > 999999999 or < 0");
        }
        this.nanos = i2;
    }

    public boolean equals(Timestamp timestamp) {
        if (super.equals((Object) timestamp) && this.nanos == timestamp.nanos) {
            return true;
        }
        return false;
    }

    @Override // java.util.Date
    public boolean equals(Object obj) {
        if (obj instanceof Timestamp) {
            return equals((Timestamp) obj);
        }
        return false;
    }

    public boolean before(Timestamp timestamp) {
        return compareTo(timestamp) < 0;
    }

    public boolean after(Timestamp timestamp) {
        return compareTo(timestamp) > 0;
    }

    public int compareTo(Timestamp timestamp) {
        long time = getTime();
        long time2 = timestamp.getTime();
        int i2 = time < time2 ? -1 : time == time2 ? 0 : 1;
        if (i2 == 0) {
            if (this.nanos > timestamp.nanos) {
                return 1;
            }
            if (this.nanos < timestamp.nanos) {
                return -1;
            }
        }
        return i2;
    }

    @Override // java.util.Date, java.lang.Comparable
    public int compareTo(java.util.Date date) {
        if (date instanceof Timestamp) {
            return compareTo((Timestamp) date);
        }
        return compareTo(new Timestamp(date.getTime()));
    }

    @Override // java.util.Date
    public int hashCode() {
        return super.hashCode();
    }

    public static Timestamp valueOf(LocalDateTime localDateTime) {
        return new Timestamp(localDateTime.getYear() - 1900, localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano());
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(getYear() + 1900, getMonth() + 1, getDate(), getHours(), getMinutes(), getSeconds(), getNanos());
    }

    public static Timestamp from(Instant instant) {
        try {
            Timestamp timestamp = new Timestamp(instant.getEpochSecond() * 1000);
            timestamp.nanos = instant.getNano();
            return timestamp;
        } catch (ArithmeticException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    @Override // java.util.Date
    public Instant toInstant() {
        return Instant.ofEpochSecond(super.getTime() / 1000, this.nanos);
    }
}
