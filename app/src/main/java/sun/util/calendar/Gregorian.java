package sun.util.calendar;

import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

/* loaded from: rt.jar:sun/util/calendar/Gregorian.class */
public class Gregorian extends BaseCalendar {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: rt.jar:sun/util/calendar/Gregorian$Date.class */
    public static class Date extends BaseCalendar.Date {
        protected Date() {
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public int getNormalizedYear() {
            return getYear();
        }

        @Override // sun.util.calendar.BaseCalendar.Date
        public void setNormalizedYear(int i2) {
            setYear(i2);
        }
    }

    Gregorian() {
    }

    @Override // sun.util.calendar.CalendarSystem
    public String getName() {
        return "gregorian";
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
}
