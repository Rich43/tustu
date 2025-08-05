package java.sql;

import java.time.Instant;
import java.time.LocalDate;

/* loaded from: rt.jar:java/sql/Date.class */
public class Date extends java.util.Date {
    static final long serialVersionUID = 1511598038487230103L;

    @Deprecated
    public Date(int i2, int i3, int i4) {
        super(i2, i3, i4);
    }

    public Date(long j2) {
        super(j2);
    }

    @Override // java.util.Date
    public void setTime(long j2) {
        super.setTime(j2);
    }

    public static Date valueOf(String str) throws NumberFormatException {
        Date date = null;
        if (str == null) {
            throw new IllegalArgumentException();
        }
        int iIndexOf = str.indexOf(45);
        int iIndexOf2 = str.indexOf(45, iIndexOf + 1);
        if (iIndexOf > 0 && iIndexOf2 > 0 && iIndexOf2 < str.length() - 1) {
            String strSubstring = str.substring(0, iIndexOf);
            String strSubstring2 = str.substring(iIndexOf + 1, iIndexOf2);
            String strSubstring3 = str.substring(iIndexOf2 + 1);
            if (strSubstring.length() == 4 && strSubstring2.length() >= 1 && strSubstring2.length() <= 2 && strSubstring3.length() >= 1 && strSubstring3.length() <= 2) {
                int i2 = Integer.parseInt(strSubstring);
                int i3 = Integer.parseInt(strSubstring2);
                int i4 = Integer.parseInt(strSubstring3);
                if (i3 >= 1 && i3 <= 12 && i4 >= 1 && i4 <= 31) {
                    date = new Date(i2 - 1900, i3 - 1, i4);
                }
            }
        }
        if (date == null) {
            throw new IllegalArgumentException();
        }
        return date;
    }

    @Override // java.util.Date
    public String toString() {
        int year = super.getYear() + 1900;
        int month = super.getMonth() + 1;
        int date = super.getDate();
        char[] charArray = "2000-00-00".toCharArray();
        charArray[0] = Character.forDigit(year / 1000, 10);
        charArray[1] = Character.forDigit((year / 100) % 10, 10);
        charArray[2] = Character.forDigit((year / 10) % 10, 10);
        charArray[3] = Character.forDigit(year % 10, 10);
        charArray[5] = Character.forDigit(month / 10, 10);
        charArray[6] = Character.forDigit(month % 10, 10);
        charArray[8] = Character.forDigit(date / 10, 10);
        charArray[9] = Character.forDigit(date % 10, 10);
        return new String(charArray);
    }

    @Override // java.util.Date
    @Deprecated
    public int getHours() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public int getMinutes() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public int getSeconds() {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setHours(int i2) {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setMinutes(int i2) {
        throw new IllegalArgumentException();
    }

    @Override // java.util.Date
    @Deprecated
    public void setSeconds(int i2) {
        throw new IllegalArgumentException();
    }

    public static Date valueOf(LocalDate localDate) {
        return new Date(localDate.getYear() - 1900, localDate.getMonthValue() - 1, localDate.getDayOfMonth());
    }

    public LocalDate toLocalDate() {
        return LocalDate.of(getYear() + 1900, getMonth() + 1, getDate());
    }

    @Override // java.util.Date
    public Instant toInstant() {
        throw new UnsupportedOperationException();
    }
}
