package sun.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import sun.util.locale.provider.CalendarDataUtility;

/* loaded from: rt.jar:sun/util/BuddhistCalendar.class */
public class BuddhistCalendar extends GregorianCalendar {
    private static final long serialVersionUID = -8527488697350388578L;
    private static final int BUDDHIST_YEAR_OFFSET = 543;
    private transient int yearOffset;

    public BuddhistCalendar() {
        this.yearOffset = 543;
    }

    public BuddhistCalendar(TimeZone timeZone) {
        super(timeZone);
        this.yearOffset = 543;
    }

    public BuddhistCalendar(Locale locale) {
        super(locale);
        this.yearOffset = 543;
    }

    public BuddhistCalendar(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
        this.yearOffset = 543;
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public String getCalendarType() {
        return "buddhist";
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public boolean equals(Object obj) {
        return (obj instanceof BuddhistCalendar) && super.equals(obj);
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public int hashCode() {
        return super.hashCode() ^ 543;
    }

    @Override // java.util.Calendar
    public int get(int i2) {
        if (i2 == 1) {
            return super.get(i2) + this.yearOffset;
        }
        return super.get(i2);
    }

    @Override // java.util.Calendar
    public void set(int i2, int i3) {
        if (i2 == 1) {
            super.set(i2, i3 - this.yearOffset);
        } else {
            super.set(i2, i3);
        }
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public void add(int i2, int i3) {
        int i4 = this.yearOffset;
        this.yearOffset = 0;
        try {
            super.add(i2, i3);
            this.yearOffset = i4;
        } catch (Throwable th) {
            this.yearOffset = i4;
            throw th;
        }
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public void roll(int i2, int i3) {
        int i4 = this.yearOffset;
        this.yearOffset = 0;
        try {
            super.roll(i2, i3);
            this.yearOffset = i4;
        } catch (Throwable th) {
            this.yearOffset = i4;
            throw th;
        }
    }

    @Override // java.util.Calendar
    public String getDisplayName(int i2, int i3, Locale locale) {
        if (i2 != 0) {
            return super.getDisplayName(i2, i3, locale);
        }
        return CalendarDataUtility.retrieveFieldValueName("buddhist", i2, get(i2), i3, locale);
    }

    @Override // java.util.Calendar
    public Map<String, Integer> getDisplayNames(int i2, int i3, Locale locale) {
        if (i2 != 0) {
            return super.getDisplayNames(i2, i3, locale);
        }
        return CalendarDataUtility.retrieveFieldValueNames("buddhist", i2, i3, locale);
    }

    @Override // java.util.GregorianCalendar, java.util.Calendar
    public int getActualMaximum(int i2) {
        int i3 = this.yearOffset;
        this.yearOffset = 0;
        try {
            int actualMaximum = super.getActualMaximum(i2);
            this.yearOffset = i3;
            return actualMaximum;
        } catch (Throwable th) {
            this.yearOffset = i3;
            throw th;
        }
    }

    @Override // java.util.Calendar
    public String toString() {
        int i2;
        String string = super.toString();
        if (!isSet(1)) {
            return string;
        }
        int iIndexOf = string.indexOf("YEAR=");
        if (iIndexOf == -1) {
            return string;
        }
        int length = iIndexOf + "YEAR=".length();
        StringBuilder sb = new StringBuilder(string.substring(0, length));
        do {
            i2 = length;
            length++;
        } while (Character.isDigit(string.charAt(i2)));
        sb.append(internalGet(1) + 543).append(string.substring(length - 1));
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.yearOffset = 543;
    }
}
