package sun.util.calendar;

import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

/* loaded from: rt.jar:sun/util/calendar/JulianCalendar.class */
public class JulianCalendar extends BaseCalendar {
    private static final int BCE = 0;
    private static final int CE = 1;
    private static final Era[] eras;
    private static final int JULIAN_EPOCH = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JulianCalendar.class.desiredAssertionStatus();
        eras = new Era[]{new Era("BeforeCommonEra", "B.C.E.", Long.MIN_VALUE, false), new Era("CommonEra", "C.E.", -62135709175808L, true)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:sun/util/calendar/JulianCalendar$Date.class */
    public static class Date extends BaseCalendar.Date {
        protected Date() {
            setCache(1, -1L, 365);
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
            setCache(1, -1L, 365);
        }

        @Override // sun.util.calendar.CalendarDate
        public Date setEra(Era era) {
            if (era == null) {
                throw new NullPointerException();
            }
            if (era != JulianCalendar.eras[0] || era != JulianCalendar.eras[1]) {
                throw new IllegalArgumentException("unknown era: " + ((Object) era));
            }
            super.setEra(era);
            return this;
        }

        protected void setKnownEra(Era era) {
            super.setEra(era);
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public int getNormalizedYear() {
            if (getEra() == JulianCalendar.eras[0]) {
                return 1 - getYear();
            }
            return getYear();
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public void setNormalizedYear(int i2) {
            if (i2 <= 0) {
                setYear(1 - i2);
                setKnownEra(JulianCalendar.eras[0]);
            } else {
                setYear(i2);
                setKnownEra(JulianCalendar.eras[1]);
            }
        }

        @Override // sun.util.calendar.CalendarDate
        public String toString() {
            String abbreviation;
            String string = super.toString();
            String strSubstring = string.substring(string.indexOf(84));
            StringBuffer stringBuffer = new StringBuffer();
            Era era = getEra();
            if (era != null && (abbreviation = era.getAbbreviation()) != null) {
                stringBuffer.append(abbreviation).append(' ');
            }
            stringBuffer.append(getYear()).append('-');
            CalendarUtils.sprintf0d(stringBuffer, getMonth(), 2).append('-');
            CalendarUtils.sprintf0d(stringBuffer, getDayOfMonth(), 2);
            stringBuffer.append(strSubstring);
            return stringBuffer.toString();
        }
    }

    JulianCalendar() {
        setEras(eras);
    }

    @Override // sun.util.calendar.CalendarSystem
    public String getName() {
        return "julian";
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), (CalendarDate) newCalendarDate());
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2) {
        return getCalendarDate(j2, (CalendarDate) newCalendarDate());
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2, CalendarDate calendarDate) {
        return (Date) super.getCalendarDate(j2, calendarDate);
    }

    @Override // sun.util.calendar.AbstractCalendar, sun.util.calendar.CalendarSystem
    public Date getCalendarDate(long j2, TimeZone timeZone) {
        return getCalendarDate(j2, (CalendarDate) newCalendarDate(timeZone));
    }

    @Override // sun.util.calendar.CalendarSystem
    public Date newCalendarDate() {
        return new Date();
    }

    @Override // sun.util.calendar.CalendarSystem
    public Date newCalendarDate(TimeZone timeZone) {
        return new Date(timeZone);
    }

    @Override // sun.util.calendar.BaseCalendar
    public long getFixedDate(int i2, int i3, int i4, BaseCalendar.Date date) {
        long jFloorDivide;
        long jFloorDivide2;
        boolean z2 = i3 == 1 && i4 == 1;
        if (date != null && date.hit(i2)) {
            if (z2) {
                return date.getCachedJan1();
            }
            return (date.getCachedJan1() + getDayOfYear(i2, i3, i4)) - 1;
        }
        long j2 = i2;
        long j3 = (-2) + (365 * (j2 - 1)) + i4;
        if (j2 > 0) {
            jFloorDivide = j3 + ((j2 - 1) / 4);
        } else {
            jFloorDivide = j3 + CalendarUtils.floorDivide(j2 - 1, 4L);
        }
        if (i3 > 0) {
            jFloorDivide2 = jFloorDivide + (((367 * i3) - 362) / 12);
        } else {
            jFloorDivide2 = jFloorDivide + CalendarUtils.floorDivide((367 * i3) - 362, 12L);
        }
        if (i3 > 2) {
            jFloorDivide2 -= CalendarUtils.isJulianLeapYear(i2) ? 1L : 2L;
        }
        if (date != null && z2) {
            date.setCache(i2, jFloorDivide2, CalendarUtils.isJulianLeapYear(i2) ? 366 : 365);
        }
        return jFloorDivide2;
    }

    @Override // sun.util.calendar.BaseCalendar, sun.util.calendar.AbstractCalendar
    public void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j2) {
        int iFloorDivide;
        int iFloorDivide2;
        Date date = (Date) calendarDate;
        long j3 = (4 * (j2 - (-1))) + 1464;
        if (j3 >= 0) {
            iFloorDivide = (int) (j3 / 1461);
        } else {
            iFloorDivide = (int) CalendarUtils.floorDivide(j3, 1461L);
        }
        int fixedDate = (int) (j2 - getFixedDate(iFloorDivide, 1, 1, date));
        boolean zIsJulianLeapYear = CalendarUtils.isJulianLeapYear(iFloorDivide);
        if (j2 >= getFixedDate(iFloorDivide, 3, 1, date)) {
            fixedDate += zIsJulianLeapYear ? 1 : 2;
        }
        int i2 = (12 * fixedDate) + 373;
        if (i2 > 0) {
            iFloorDivide2 = i2 / 367;
        } else {
            iFloorDivide2 = CalendarUtils.floorDivide(i2, 367);
        }
        int fixedDate2 = ((int) (j2 - getFixedDate(iFloorDivide, iFloorDivide2, 1, date))) + 1;
        int dayOfWeekFromFixedDate = getDayOfWeekFromFixedDate(j2);
        if (!$assertionsDisabled && dayOfWeekFromFixedDate <= 0) {
            throw new AssertionError((Object) ("negative day of week " + dayOfWeekFromFixedDate));
        }
        date.setNormalizedYear(iFloorDivide);
        date.setMonth(iFloorDivide2);
        date.setDayOfMonth(fixedDate2);
        date.setDayOfWeek(dayOfWeekFromFixedDate);
        date.setLeapYear(zIsJulianLeapYear);
        date.setNormalized(true);
    }

    @Override // sun.util.calendar.BaseCalendar
    public int getYearFromFixedDate(long j2) {
        return (int) CalendarUtils.floorDivide((4 * (j2 - (-1))) + 1464, 1461L);
    }

    @Override // sun.util.calendar.BaseCalendar
    public int getDayOfWeek(CalendarDate calendarDate) {
        return getDayOfWeekFromFixedDate(getFixedDate(calendarDate));
    }

    @Override // sun.util.calendar.BaseCalendar
    boolean isLeapYear(int i2) {
        return CalendarUtils.isJulianLeapYear(i2);
    }
}
