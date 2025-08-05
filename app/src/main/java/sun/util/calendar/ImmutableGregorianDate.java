package sun.util.calendar;

import java.util.Locale;
import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

/* loaded from: rt.jar:sun/util/calendar/ImmutableGregorianDate.class */
class ImmutableGregorianDate extends BaseCalendar.Date {
    private final BaseCalendar.Date date;

    ImmutableGregorianDate(BaseCalendar.Date date) {
        if (date == null) {
            throw new NullPointerException();
        }
        this.date = date;
    }

    @Override // sun.util.calendar.CalendarDate
    public Era getEra() {
        return this.date.getEra();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setEra(Era era) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getYear() {
        return this.date.getYear();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setYear(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addYear(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean isLeapYear() {
        return this.date.isLeapYear();
    }

    @Override // sun.util.calendar.CalendarDate
    void setLeapYear(boolean z2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public int getMonth() {
        return this.date.getMonth();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setMonth(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addMonth(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setDayOfMonth(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addDayOfMonth(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getDayOfWeek() {
        return this.date.getDayOfWeek();
    }

    @Override // sun.util.calendar.CalendarDate
    public int getHours() {
        return this.date.getHours();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setHours(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addHours(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getMinutes() {
        return this.date.getMinutes();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setMinutes(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addMinutes(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getSeconds() {
        return this.date.getSeconds();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setSeconds(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addSeconds(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public int getMillis() {
        return this.date.getMillis();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setMillis(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addMillis(int i2) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public long getTimeOfDay() {
        return this.date.getTimeOfDay();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setDate(int i2, int i3, int i4) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addDate(int i2, int i3, int i4) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setTimeOfDay(int i2, int i3, int i4, int i5) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate addTimeOfDay(int i2, int i3, int i4, int i5) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setTimeOfDay(long j2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean isNormalized() {
        return this.date.isNormalized();
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean isStandardTime() {
        return this.date.isStandardTime();
    }

    @Override // sun.util.calendar.CalendarDate
    public void setStandardTime(boolean z2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean isDaylightTime() {
        return this.date.isDaylightTime();
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setLocale(Locale locale) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public TimeZone getZone() {
        return this.date.getZone();
    }

    @Override // sun.util.calendar.CalendarDate
    public CalendarDate setZone(TimeZone timeZone) {
        unsupported();
        return this;
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean isSameDate(CalendarDate calendarDate) {
        return calendarDate.isSameDate(calendarDate);
    }

    @Override // sun.util.calendar.CalendarDate
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImmutableGregorianDate)) {
            return false;
        }
        return this.date.equals(((ImmutableGregorianDate) obj).date);
    }

    @Override // sun.util.calendar.CalendarDate
    public int hashCode() {
        return this.date.hashCode();
    }

    @Override // sun.util.calendar.CalendarDate
    public Object clone() {
        return super.clone();
    }

    @Override // sun.util.calendar.CalendarDate
    public String toString() {
        return this.date.toString();
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setDayOfWeek(int i2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setNormalized(boolean z2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public int getZoneOffset() {
        return this.date.getZoneOffset();
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setZoneOffset(int i2) {
        unsupported();
    }

    @Override // sun.util.calendar.CalendarDate
    public int getDaylightSaving() {
        return this.date.getDaylightSaving();
    }

    @Override // sun.util.calendar.CalendarDate
    protected void setDaylightSaving(int i2) {
        unsupported();
    }

    @Override // sun.util.calendar.BaseCalendar.Date
    public int getNormalizedYear() {
        return this.date.getNormalizedYear();
    }

    @Override // sun.util.calendar.BaseCalendar.Date
    public void setNormalizedYear(int i2) {
        unsupported();
    }

    private void unsupported() {
        throw new UnsupportedOperationException();
    }
}
