package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.time.Instant;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.ZoneInfo;

/* loaded from: rt.jar:java/util/Date.class */
public class Date implements Serializable, Cloneable, Comparable<Date> {
    private static BaseCalendar jcal;
    private transient long fastTime;
    private transient BaseCalendar.Date cdate;
    private static int defaultCenturyStart;
    private static final long serialVersionUID = 7523967970034938905L;
    private static final BaseCalendar gcal = CalendarSystem.getGregorianCalendar();
    private static final String[] wtb = {"am", "pm", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", "gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"};
    private static final int[] ttb = {14, 1, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 10000, 10000, 10000, 10300, 10240, 10360, 10300, 10420, 10360, 10480, 10420};

    public Date() {
        this(System.currentTimeMillis());
    }

    public Date(long j2) {
        this.fastTime = j2;
    }

    @Deprecated
    public Date(int i2, int i3, int i4) {
        this(i2, i3, i4, 0, 0, 0);
    }

    @Deprecated
    public Date(int i2, int i3, int i4, int i5, int i6) {
        this(i2, i3, i4, i5, i6, 0);
    }

    @Deprecated
    public Date(int i2, int i3, int i4, int i5, int i6, int i7) {
        int iFloorDivide = i2 + 1900;
        if (i3 >= 12) {
            iFloorDivide += i3 / 12;
            i3 %= 12;
        } else if (i3 < 0) {
            iFloorDivide += CalendarUtils.floorDivide(i3, 12);
            i3 = CalendarUtils.mod(i3, 12);
        }
        this.cdate = (BaseCalendar.Date) getCalendarSystem(iFloorDivide).newCalendarDate(TimeZone.getDefaultRef());
        this.cdate.setNormalizedDate(iFloorDivide, i3 + 1, i4).setTimeOfDay(i5, i6, i7, 0);
        getTimeImpl();
        this.cdate = null;
    }

    @Deprecated
    public Date(String str) {
        this(parse(str));
    }

    public Object clone() {
        Date date = null;
        try {
            date = (Date) super.clone();
            if (this.cdate != null) {
                date.cdate = (BaseCalendar.Date) this.cdate.clone();
            }
        } catch (CloneNotSupportedException e2) {
        }
        return date;
    }

    @Deprecated
    public static long UTC(int i2, int i3, int i4, int i5, int i6, int i7) {
        int iFloorDivide = i2 + 1900;
        if (i3 >= 12) {
            iFloorDivide += i3 / 12;
            i3 %= 12;
        } else if (i3 < 0) {
            iFloorDivide += CalendarUtils.floorDivide(i3, 12);
            i3 = CalendarUtils.mod(i3, 12);
        }
        BaseCalendar.Date date = (BaseCalendar.Date) getCalendarSystem(iFloorDivide).newCalendarDate(null);
        date.setNormalizedDate(iFloorDivide, i3 + 1, i4).setTimeOfDay(i5, i6, i7, 0);
        Date date2 = new Date(0L);
        date2.normalize(date);
        return date2.fastTime;
    }

    /* JADX WARN: Code restructure failed: missing block: B:176:0x0303, code lost:
    
        if (r24 >= 0) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0309, code lost:
    
        r21 = 0;
     */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long parse(java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 1008
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Date.parse(java.lang.String):long");
    }

    @Deprecated
    public int getYear() {
        return normalize().getYear() - 1900;
    }

    @Deprecated
    public void setYear(int i2) {
        getCalendarDate().setNormalizedYear(i2 + 1900);
    }

    @Deprecated
    public int getMonth() {
        return normalize().getMonth() - 1;
    }

    @Deprecated
    public void setMonth(int i2) {
        int iFloorDivide = 0;
        if (i2 >= 12) {
            iFloorDivide = i2 / 12;
            i2 %= 12;
        } else if (i2 < 0) {
            iFloorDivide = CalendarUtils.floorDivide(i2, 12);
            i2 = CalendarUtils.mod(i2, 12);
        }
        BaseCalendar.Date calendarDate = getCalendarDate();
        if (iFloorDivide != 0) {
            calendarDate.setNormalizedYear(calendarDate.getNormalizedYear() + iFloorDivide);
        }
        calendarDate.setMonth(i2 + 1);
    }

    @Deprecated
    public int getDate() {
        return normalize().getDayOfMonth();
    }

    @Deprecated
    public void setDate(int i2) {
        getCalendarDate().setDayOfMonth(i2);
    }

    @Deprecated
    public int getDay() {
        return normalize().getDayOfWeek() - 1;
    }

    @Deprecated
    public int getHours() {
        return normalize().getHours();
    }

    @Deprecated
    public void setHours(int i2) {
        getCalendarDate().setHours(i2);
    }

    @Deprecated
    public int getMinutes() {
        return normalize().getMinutes();
    }

    @Deprecated
    public void setMinutes(int i2) {
        getCalendarDate().setMinutes(i2);
    }

    @Deprecated
    public int getSeconds() {
        return normalize().getSeconds();
    }

    @Deprecated
    public void setSeconds(int i2) {
        getCalendarDate().setSeconds(i2);
    }

    public long getTime() {
        return getTimeImpl();
    }

    private final long getTimeImpl() {
        if (this.cdate != null && !this.cdate.isNormalized()) {
            normalize();
        }
        return this.fastTime;
    }

    public void setTime(long j2) {
        this.fastTime = j2;
        this.cdate = null;
    }

    public boolean before(Date date) {
        return getMillisOf(this) < getMillisOf(date);
    }

    public boolean after(Date date) {
        return getMillisOf(this) > getMillisOf(date);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Date) && getTime() == ((Date) obj).getTime();
    }

    static final long getMillisOf(Date date) {
        if (date.cdate == null || date.cdate.isNormalized()) {
            return date.fastTime;
        }
        return gcal.getTime((BaseCalendar.Date) date.cdate.clone());
    }

    @Override // java.lang.Comparable
    public int compareTo(Date date) {
        long millisOf = getMillisOf(this);
        long millisOf2 = getMillisOf(date);
        if (millisOf < millisOf2) {
            return -1;
        }
        return millisOf == millisOf2 ? 0 : 1;
    }

    public int hashCode() {
        long time = getTime();
        return ((int) time) ^ ((int) (time >> 32));
    }

    public String toString() {
        BaseCalendar.Date dateNormalize = normalize();
        StringBuilder sb = new StringBuilder(28);
        int dayOfWeek = dateNormalize.getDayOfWeek();
        if (dayOfWeek == 1) {
            dayOfWeek = 8;
        }
        convertToAbbr(sb, wtb[dayOfWeek]).append(' ');
        convertToAbbr(sb, wtb[(dateNormalize.getMonth() - 1) + 2 + 7]).append(' ');
        CalendarUtils.sprintf0d(sb, dateNormalize.getDayOfMonth(), 2).append(' ');
        CalendarUtils.sprintf0d(sb, dateNormalize.getHours(), 2).append(':');
        CalendarUtils.sprintf0d(sb, dateNormalize.getMinutes(), 2).append(':');
        CalendarUtils.sprintf0d(sb, dateNormalize.getSeconds(), 2).append(' ');
        TimeZone zone = dateNormalize.getZone();
        if (zone != null) {
            sb.append(zone.getDisplayName(dateNormalize.isDaylightTime(), 0, Locale.US));
        } else {
            sb.append("GMT");
        }
        sb.append(' ').append(dateNormalize.getYear());
        return sb.toString();
    }

    private static final StringBuilder convertToAbbr(StringBuilder sb, String str) {
        sb.append(Character.toUpperCase(str.charAt(0)));
        sb.append(str.charAt(1)).append(str.charAt(2));
        return sb;
    }

    @Deprecated
    public String toLocaleString() {
        return DateFormat.getDateTimeInstance().format(this);
    }

    @Deprecated
    public String toGMTString() {
        BaseCalendar.Date date = (BaseCalendar.Date) getCalendarSystem(getTime()).getCalendarDate(getTime(), (TimeZone) null);
        StringBuilder sb = new StringBuilder(32);
        CalendarUtils.sprintf0d(sb, date.getDayOfMonth(), 1).append(' ');
        convertToAbbr(sb, wtb[(date.getMonth() - 1) + 2 + 7]).append(' ');
        sb.append(date.getYear()).append(' ');
        CalendarUtils.sprintf0d(sb, date.getHours(), 2).append(':');
        CalendarUtils.sprintf0d(sb, date.getMinutes(), 2).append(':');
        CalendarUtils.sprintf0d(sb, date.getSeconds(), 2);
        sb.append(" GMT");
        return sb.toString();
    }

    @Deprecated
    public int getTimezoneOffset() {
        int zoneOffset;
        if (this.cdate == null) {
            TimeZone defaultRef = TimeZone.getDefaultRef();
            if (defaultRef instanceof ZoneInfo) {
                zoneOffset = ((ZoneInfo) defaultRef).getOffsets(this.fastTime, null);
            } else {
                zoneOffset = defaultRef.getOffset(this.fastTime);
            }
        } else {
            normalize();
            zoneOffset = this.cdate.getZoneOffset();
        }
        return (-zoneOffset) / 60000;
    }

    private final BaseCalendar.Date getCalendarDate() {
        if (this.cdate == null) {
            this.cdate = (BaseCalendar.Date) getCalendarSystem(this.fastTime).getCalendarDate(this.fastTime, TimeZone.getDefaultRef());
        }
        return this.cdate;
    }

    private final BaseCalendar.Date normalize() {
        if (this.cdate == null) {
            this.cdate = (BaseCalendar.Date) getCalendarSystem(this.fastTime).getCalendarDate(this.fastTime, TimeZone.getDefaultRef());
            return this.cdate;
        }
        if (!this.cdate.isNormalized()) {
            this.cdate = normalize(this.cdate);
        }
        TimeZone defaultRef = TimeZone.getDefaultRef();
        if (defaultRef != this.cdate.getZone()) {
            this.cdate.setZone(defaultRef);
            getCalendarSystem(this.cdate).getCalendarDate(this.fastTime, this.cdate);
        }
        return this.cdate;
    }

    private final BaseCalendar.Date normalize(BaseCalendar.Date date) {
        int normalizedYear = date.getNormalizedYear();
        int month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        int millis = date.getMillis();
        TimeZone zone = date.getZone();
        if (normalizedYear == 1582 || normalizedYear > 280000000 || normalizedYear < -280000000) {
            if (zone == null) {
                zone = TimeZone.getTimeZone("GMT");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(zone);
            gregorianCalendar.clear();
            gregorianCalendar.set(14, millis);
            gregorianCalendar.set(normalizedYear, month - 1, dayOfMonth, hours, minutes, seconds);
            this.fastTime = gregorianCalendar.getTimeInMillis();
            return (BaseCalendar.Date) getCalendarSystem(this.fastTime).getCalendarDate(this.fastTime, zone);
        }
        BaseCalendar calendarSystem = getCalendarSystem(normalizedYear);
        if (calendarSystem != getCalendarSystem(date)) {
            date = (BaseCalendar.Date) calendarSystem.newCalendarDate(zone);
            date.setNormalizedDate(normalizedYear, month, dayOfMonth).setTimeOfDay(hours, minutes, seconds, millis);
        }
        this.fastTime = calendarSystem.getTime(date);
        BaseCalendar calendarSystem2 = getCalendarSystem(this.fastTime);
        if (calendarSystem2 != calendarSystem) {
            date = (BaseCalendar.Date) calendarSystem2.newCalendarDate(zone);
            date.setNormalizedDate(normalizedYear, month, dayOfMonth).setTimeOfDay(hours, minutes, seconds, millis);
            this.fastTime = calendarSystem2.getTime(date);
        }
        return date;
    }

    private static final BaseCalendar getCalendarSystem(int i2) {
        if (i2 >= 1582) {
            return gcal;
        }
        return getJulianCalendar();
    }

    private static final BaseCalendar getCalendarSystem(long j2) {
        if (j2 >= 0 || j2 >= (-12219292800000L) - TimeZone.getDefaultRef().getOffset(j2)) {
            return gcal;
        }
        return getJulianCalendar();
    }

    private static final BaseCalendar getCalendarSystem(BaseCalendar.Date date) {
        if (jcal == null) {
            return gcal;
        }
        if (date.getEra() != null) {
            return jcal;
        }
        return gcal;
    }

    private static final synchronized BaseCalendar getJulianCalendar() {
        if (jcal == null) {
            jcal = (BaseCalendar) CalendarSystem.forName("julian");
        }
        return jcal;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeLong(getTimeImpl());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.fastTime = objectInputStream.readLong();
    }

    public static Date from(Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public Instant toInstant() {
        return Instant.ofEpochMilli(getTime());
    }
}
