package sun.util.calendar;

import java.util.TimeZone;

/* loaded from: rt.jar:sun/util/calendar/AbstractCalendar.class */
public abstract class AbstractCalendar extends CalendarSystem {
    static final int SECOND_IN_MILLIS = 1000;
    static final int MINUTE_IN_MILLIS = 60000;
    static final int HOUR_IN_MILLIS = 3600000;
    static final int DAY_IN_MILLIS = 86400000;
    static final int EPOCH_OFFSET = 719163;
    private Era[] eras;

    protected abstract boolean isLeapYear(CalendarDate calendarDate);

    protected abstract long getFixedDate(CalendarDate calendarDate);

    protected abstract void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j2);

    protected AbstractCalendar() {
    }

    @Override // sun.util.calendar.CalendarSystem
    public Era getEra(String str) {
        if (this.eras != null) {
            for (int i2 = 0; i2 < this.eras.length; i2++) {
                if (this.eras[i2].equals(str)) {
                    return this.eras[i2];
                }
            }
            return null;
        }
        return null;
    }

    @Override // sun.util.calendar.CalendarSystem
    public Era[] getEras() {
        Era[] eraArr = null;
        if (this.eras != null) {
            eraArr = new Era[this.eras.length];
            System.arraycopy(this.eras, 0, eraArr, 0, this.eras.length);
        }
        return eraArr;
    }

    @Override // sun.util.calendar.CalendarSystem
    public void setEra(CalendarDate calendarDate, String str) {
        if (this.eras == null) {
            return;
        }
        for (int i2 = 0; i2 < this.eras.length; i2++) {
            Era era = this.eras[i2];
            if (era != null && era.getName().equals(str)) {
                calendarDate.setEra(era);
                return;
            }
        }
        throw new IllegalArgumentException("unknown era name: " + str);
    }

    protected void setEras(Era[] eraArr) {
        this.eras = eraArr;
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), newCalendarDate());
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate getCalendarDate(long j2) {
        return getCalendarDate(j2, newCalendarDate());
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate getCalendarDate(long j2, TimeZone timeZone) {
        return getCalendarDate(j2, newCalendarDate(timeZone));
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate getCalendarDate(long j2, CalendarDate calendarDate) {
        int i2 = 0;
        int offset = 0;
        int i3 = 0;
        long j3 = 0;
        TimeZone zone = calendarDate.getZone();
        if (zone != null) {
            int[] iArr = new int[2];
            if (zone instanceof ZoneInfo) {
                offset = ((ZoneInfo) zone).getOffsets(j2, iArr);
            } else {
                offset = zone.getOffset(j2);
                iArr[0] = zone.getRawOffset();
                iArr[1] = offset - iArr[0];
            }
            j3 = offset / DAY_IN_MILLIS;
            i2 = offset % DAY_IN_MILLIS;
            i3 = iArr[1];
        }
        calendarDate.setZoneOffset(offset);
        calendarDate.setDaylightSaving(i3);
        long j4 = j3 + (j2 / 86400000);
        int i4 = i2 + ((int) (j2 % 86400000));
        if (i4 >= DAY_IN_MILLIS) {
            i4 -= DAY_IN_MILLIS;
            j4++;
        } else {
            while (i4 < 0) {
                i4 += DAY_IN_MILLIS;
                j4--;
            }
        }
        getCalendarDateFromFixedDate(calendarDate, j4 + 719163);
        setTimeOfDay(calendarDate, i4);
        calendarDate.setLeapYear(isLeapYear(calendarDate));
        calendarDate.setNormalized(true);
        return calendarDate;
    }

    @Override // sun.util.calendar.CalendarSystem
    public long getTime(CalendarDate calendarDate) {
        long fixedDate = ((getFixedDate(calendarDate) - 719163) * 86400000) + getTimeOfDay(calendarDate);
        int offset = 0;
        TimeZone zone = calendarDate.getZone();
        if (zone != null) {
            if (calendarDate.isNormalized()) {
                return fixedDate - calendarDate.getZoneOffset();
            }
            int[] iArr = new int[2];
            if (calendarDate.isStandardTime()) {
                if (zone instanceof ZoneInfo) {
                    ((ZoneInfo) zone).getOffsetsByStandard(fixedDate, iArr);
                    offset = iArr[0];
                } else {
                    offset = zone.getOffset(fixedDate - zone.getRawOffset());
                }
            } else if (zone instanceof ZoneInfo) {
                offset = ((ZoneInfo) zone).getOffsetsByWall(fixedDate, iArr);
            } else {
                offset = zone.getOffset(fixedDate - zone.getRawOffset());
            }
        }
        long j2 = fixedDate - offset;
        getCalendarDate(j2, calendarDate);
        return j2;
    }

    protected long getTimeOfDay(CalendarDate calendarDate) {
        long timeOfDay = calendarDate.getTimeOfDay();
        if (timeOfDay != Long.MIN_VALUE) {
            return timeOfDay;
        }
        long timeOfDayValue = getTimeOfDayValue(calendarDate);
        calendarDate.setTimeOfDay(timeOfDayValue);
        return timeOfDayValue;
    }

    public long getTimeOfDayValue(CalendarDate calendarDate) {
        return (((((calendarDate.getHours() * 60) + calendarDate.getMinutes()) * 60) + calendarDate.getSeconds()) * 1000) + calendarDate.getMillis();
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate setTimeOfDay(CalendarDate calendarDate, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        boolean zIsNormalized = calendarDate.isNormalized();
        int i3 = i2 / HOUR_IN_MILLIS;
        int i4 = i2 % HOUR_IN_MILLIS;
        int i5 = i4 / MINUTE_IN_MILLIS;
        int i6 = i4 % MINUTE_IN_MILLIS;
        calendarDate.setHours(i3);
        calendarDate.setMinutes(i5);
        calendarDate.setSeconds(i6 / 1000);
        calendarDate.setMillis(i6 % 1000);
        calendarDate.setTimeOfDay(i2);
        if (i3 < 24 && zIsNormalized) {
            calendarDate.setNormalized(zIsNormalized);
        }
        return calendarDate;
    }

    @Override // sun.util.calendar.CalendarSystem
    public int getWeekLength() {
        return 7;
    }

    @Override // sun.util.calendar.CalendarSystem
    public CalendarDate getNthDayOfWeek(int i2, int i3, CalendarDate calendarDate) {
        long dayOfWeekDateAfter;
        CalendarDate calendarDate2 = (CalendarDate) calendarDate.clone();
        normalize(calendarDate2);
        long fixedDate = getFixedDate(calendarDate2);
        if (i2 > 0) {
            dayOfWeekDateAfter = (7 * i2) + getDayOfWeekDateBefore(fixedDate, i3);
        } else {
            dayOfWeekDateAfter = (7 * i2) + getDayOfWeekDateAfter(fixedDate, i3);
        }
        getCalendarDateFromFixedDate(calendarDate2, dayOfWeekDateAfter);
        return calendarDate2;
    }

    static long getDayOfWeekDateBefore(long j2, int i2) {
        return getDayOfWeekDateOnOrBefore(j2 - 1, i2);
    }

    static long getDayOfWeekDateAfter(long j2, int i2) {
        return getDayOfWeekDateOnOrBefore(j2 + 7, i2);
    }

    public static long getDayOfWeekDateOnOrBefore(long j2, int i2) {
        long j3 = j2 - (i2 - 1);
        if (j3 >= 0) {
            return j2 - (j3 % 7);
        }
        return j2 - CalendarUtils.mod(j3, 7L);
    }

    public boolean validateTime(CalendarDate calendarDate) {
        int minutes;
        int seconds;
        int millis;
        int hours = calendarDate.getHours();
        if (hours < 0 || hours >= 24 || (minutes = calendarDate.getMinutes()) < 0 || minutes >= 60 || (seconds = calendarDate.getSeconds()) < 0 || seconds >= 60 || (millis = calendarDate.getMillis()) < 0 || millis >= 1000) {
            return false;
        }
        return true;
    }

    int normalizeTime(CalendarDate calendarDate) {
        long timeOfDay = getTimeOfDay(calendarDate);
        long jFloorDivide = 0;
        if (timeOfDay >= 86400000) {
            jFloorDivide = timeOfDay / 86400000;
            timeOfDay %= 86400000;
        } else if (timeOfDay < 0) {
            jFloorDivide = CalendarUtils.floorDivide(timeOfDay, 86400000L);
            if (jFloorDivide != 0) {
                timeOfDay -= 86400000 * jFloorDivide;
            }
        }
        if (jFloorDivide != 0) {
            calendarDate.setTimeOfDay(timeOfDay);
        }
        calendarDate.setMillis((int) (timeOfDay % 1000));
        long j2 = timeOfDay / 1000;
        calendarDate.setSeconds((int) (j2 % 60));
        long j3 = j2 / 60;
        calendarDate.setMinutes((int) (j3 % 60));
        calendarDate.setHours((int) (j3 / 60));
        return (int) jFloorDivide;
    }
}
