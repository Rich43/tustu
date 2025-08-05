package sun.util.calendar;

import java.sql.Types;
import java.util.TimeZone;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: rt.jar:sun/util/calendar/BaseCalendar.class */
public abstract class BaseCalendar extends AbstractCalendar {
    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    private static final int BASE_YEAR = 1970;
    private static final int[] FIXED_DATES;
    static final int[] DAYS_IN_MONTH;
    static final int[] ACCUMULATED_DAYS_IN_MONTH;
    static final int[] ACCUMULATED_DAYS_IN_MONTH_LEAP;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BaseCalendar.class.desiredAssertionStatus();
        FIXED_DATES = new int[]{719163, 719528, 719893, 720259, 720624, 720989, 721354, 721720, 722085, 722450, 722815, 723181, 723546, 723911, 724276, 724642, 725007, 725372, 725737, 726103, 726468, 726833, 727198, 727564, 727929, 728294, 728659, 729025, 729390, 729755, 730120, 730486, 730851, 731216, 731581, 731947, 732312, 732677, 733042, 733408, 733773, 734138, 734503, 734869, 735234, 735599, 735964, 736330, 736695, 737060, 737425, 737791, 738156, 738521, 738886, 739252, 739617, 739982, 740347, 740713, 741078, 741443, 741808, 742174, 742539, 742904, 743269, 743635, 744000, 744365};
        DAYS_IN_MONTH = new int[]{31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        ACCUMULATED_DAYS_IN_MONTH = new int[]{-30, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, FTPReply.SECURITY_MECHANISM_IS_OK};
        ACCUMULATED_DAYS_IN_MONTH_LEAP = new int[]{-30, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
    }

    /* loaded from: rt.jar:sun/util/calendar/BaseCalendar$Date.class */
    public static abstract class Date extends CalendarDate {
        int cachedYear;
        long cachedFixedDateJan1;
        long cachedFixedDateNextJan1;

        public abstract int getNormalizedYear();

        public abstract void setNormalizedYear(int i2);

        protected Date() {
            this.cachedYear = Types.BLOB;
            this.cachedFixedDateJan1 = 731581L;
            this.cachedFixedDateNextJan1 = this.cachedFixedDateJan1 + 366;
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
            this.cachedYear = Types.BLOB;
            this.cachedFixedDateJan1 = 731581L;
            this.cachedFixedDateNextJan1 = this.cachedFixedDateJan1 + 366;
        }

        public Date setNormalizedDate(int i2, int i3, int i4) {
            setNormalizedYear(i2);
            setMonth(i3).setDayOfMonth(i4);
            return this;
        }

        protected final boolean hit(int i2) {
            return i2 == this.cachedYear;
        }

        protected final boolean hit(long j2) {
            return j2 >= this.cachedFixedDateJan1 && j2 < this.cachedFixedDateNextJan1;
        }

        protected int getCachedYear() {
            return this.cachedYear;
        }

        protected long getCachedJan1() {
            return this.cachedFixedDateJan1;
        }

        protected void setCache(int i2, long j2, int i3) {
            this.cachedYear = i2;
            this.cachedFixedDateJan1 = j2;
            this.cachedFixedDateNextJan1 = j2 + i3;
        }
    }

    @Override // sun.util.calendar.CalendarSystem
    public boolean validate(CalendarDate calendarDate) {
        int dayOfMonth;
        Date date = (Date) calendarDate;
        if (date.isNormalized()) {
            return true;
        }
        int month = date.getMonth();
        if (month < 1 || month > 12 || (dayOfMonth = date.getDayOfMonth()) <= 0 || dayOfMonth > getMonthLength(date.getNormalizedYear(), month)) {
            return false;
        }
        int dayOfWeek = date.getDayOfWeek();
        if ((dayOfWeek != Integer.MIN_VALUE && dayOfWeek != getDayOfWeek(date)) || !validateTime(calendarDate)) {
            return false;
        }
        date.setNormalized(true);
        return true;
    }

    @Override // sun.util.calendar.CalendarSystem
    public boolean normalize(CalendarDate calendarDate) {
        if (calendarDate.isNormalized()) {
            return true;
        }
        Date date = (Date) calendarDate;
        if (date.getZone() != null) {
            getTime(calendarDate);
            return true;
        }
        int iNormalizeTime = normalizeTime(date);
        normalizeMonth(date);
        long dayOfMonth = date.getDayOfMonth() + iNormalizeTime;
        int month = date.getMonth();
        int normalizedYear = date.getNormalizedYear();
        int monthLength = getMonthLength(normalizedYear, month);
        if (dayOfMonth > 0 && dayOfMonth <= monthLength) {
            date.setDayOfWeek(getDayOfWeek(date));
        } else if (dayOfMonth <= 0 && dayOfMonth > -28) {
            int i2 = month - 1;
            date.setDayOfMonth((int) (dayOfMonth + getMonthLength(normalizedYear, i2)));
            if (i2 == 0) {
                i2 = 12;
                date.setNormalizedYear(normalizedYear - 1);
            }
            date.setMonth(i2);
        } else if (dayOfMonth > monthLength && dayOfMonth < monthLength + 28) {
            int i3 = month + 1;
            date.setDayOfMonth((int) (dayOfMonth - monthLength));
            if (i3 > 12) {
                date.setNormalizedYear(normalizedYear + 1);
                i3 = 1;
            }
            date.setMonth(i3);
        } else {
            getCalendarDateFromFixedDate(date, (dayOfMonth + getFixedDate(normalizedYear, month, 1, date)) - 1);
        }
        calendarDate.setLeapYear(isLeapYear(date.getNormalizedYear()));
        calendarDate.setZoneOffset(0);
        calendarDate.setDaylightSaving(0);
        date.setNormalized(true);
        return true;
    }

    void normalizeMonth(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        int normalizedYear = date.getNormalizedYear();
        long month = date.getMonth();
        if (month <= 0) {
            long j2 = 1 - month;
            date.setNormalizedYear(normalizedYear - ((int) ((j2 / 12) + 1)));
            date.setMonth((int) (13 - (j2 % 12)));
            return;
        }
        if (month > 12) {
            date.setNormalizedYear(normalizedYear + ((int) ((month - 1) / 12)));
            date.setMonth((int) (((month - 1) % 12) + 1));
        }
    }

    @Override // sun.util.calendar.CalendarSystem
    public int getYearLength(CalendarDate calendarDate) {
        return isLeapYear(((Date) calendarDate).getNormalizedYear()) ? 366 : 365;
    }

    @Override // sun.util.calendar.CalendarSystem
    public int getYearLengthInMonths(CalendarDate calendarDate) {
        return 12;
    }

    @Override // sun.util.calendar.CalendarSystem
    public int getMonthLength(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        int month = date.getMonth();
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Illegal month value: " + month);
        }
        return getMonthLength(date.getNormalizedYear(), month);
    }

    private int getMonthLength(int i2, int i3) {
        int i4 = DAYS_IN_MONTH[i3];
        if (i3 == 2 && isLeapYear(i2)) {
            i4++;
        }
        return i4;
    }

    public long getDayOfYear(CalendarDate calendarDate) {
        return getDayOfYear(((Date) calendarDate).getNormalizedYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth());
    }

    final long getDayOfYear(int i2, int i3, int i4) {
        return i4 + (isLeapYear(i2) ? ACCUMULATED_DAYS_IN_MONTH_LEAP[i3] : ACCUMULATED_DAYS_IN_MONTH[i3]);
    }

    @Override // sun.util.calendar.AbstractCalendar
    public long getFixedDate(CalendarDate calendarDate) {
        if (!calendarDate.isNormalized()) {
            normalizeMonth(calendarDate);
        }
        return getFixedDate(((Date) calendarDate).getNormalizedYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth(), (Date) calendarDate);
    }

    public long getFixedDate(int i2, int i3, int i4, Date date) {
        long jFloorDivide;
        boolean z2 = i3 == 1 && i4 == 1;
        if (date != null && date.hit(i2)) {
            if (z2) {
                return date.getCachedJan1();
            }
            return (date.getCachedJan1() + getDayOfYear(i2, i3, i4)) - 1;
        }
        int i5 = i2 - BASE_YEAR;
        if (i5 >= 0 && i5 < FIXED_DATES.length) {
            long j2 = FIXED_DATES[i5];
            if (date != null) {
                date.setCache(i2, j2, isLeapYear(i2) ? 366 : 365);
            }
            return z2 ? j2 : (j2 + getDayOfYear(i2, i3, i4)) - 1;
        }
        long j3 = i2 - 1;
        long j4 = i4;
        if (j3 >= 0) {
            jFloorDivide = j4 + (((365 * j3) + (j3 / 4)) - (j3 / 100)) + (j3 / 400) + (((367 * i3) - 362) / 12);
        } else {
            jFloorDivide = j4 + (((365 * j3) + CalendarUtils.floorDivide(j3, 4L)) - CalendarUtils.floorDivide(j3, 100L)) + CalendarUtils.floorDivide(j3, 400L) + CalendarUtils.floorDivide((367 * i3) - 362, 12);
        }
        if (i3 > 2) {
            jFloorDivide -= isLeapYear(i2) ? 1L : 2L;
        }
        if (date != null && z2) {
            date.setCache(i2, jFloorDivide, isLeapYear(i2) ? 366 : 365);
        }
        return jFloorDivide;
    }

    @Override // sun.util.calendar.AbstractCalendar
    public void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j2) {
        int gregorianYearFromFixedDate;
        long fixedDate;
        boolean zIsLeapYear;
        int iFloorDivide;
        Date date = (Date) calendarDate;
        if (date.hit(j2)) {
            gregorianYearFromFixedDate = date.getCachedYear();
            fixedDate = date.getCachedJan1();
            zIsLeapYear = isLeapYear(gregorianYearFromFixedDate);
        } else {
            gregorianYearFromFixedDate = getGregorianYearFromFixedDate(j2);
            fixedDate = getFixedDate(gregorianYearFromFixedDate, 1, 1, null);
            zIsLeapYear = isLeapYear(gregorianYearFromFixedDate);
            date.setCache(gregorianYearFromFixedDate, fixedDate, zIsLeapYear ? 366 : 365);
        }
        int i2 = (int) (j2 - fixedDate);
        long j3 = fixedDate + 31 + 28;
        if (zIsLeapYear) {
            j3++;
        }
        if (j2 >= j3) {
            i2 += zIsLeapYear ? 1 : 2;
        }
        int i3 = (12 * i2) + 373;
        if (i3 > 0) {
            iFloorDivide = i3 / 367;
        } else {
            iFloorDivide = CalendarUtils.floorDivide(i3, 367);
        }
        long j4 = fixedDate + ACCUMULATED_DAYS_IN_MONTH[iFloorDivide];
        if (zIsLeapYear && iFloorDivide >= 3) {
            j4++;
        }
        int i4 = ((int) (j2 - j4)) + 1;
        int dayOfWeekFromFixedDate = getDayOfWeekFromFixedDate(j2);
        if (!$assertionsDisabled && dayOfWeekFromFixedDate <= 0) {
            throw new AssertionError((Object) ("negative day of week " + dayOfWeekFromFixedDate));
        }
        date.setNormalizedYear(gregorianYearFromFixedDate);
        date.setMonth(iFloorDivide);
        date.setDayOfMonth(i4);
        date.setDayOfWeek(dayOfWeekFromFixedDate);
        date.setLeapYear(zIsLeapYear);
        date.setNormalized(true);
    }

    public int getDayOfWeek(CalendarDate calendarDate) {
        return getDayOfWeekFromFixedDate(getFixedDate(calendarDate));
    }

    public static final int getDayOfWeekFromFixedDate(long j2) {
        if (j2 >= 0) {
            return ((int) (j2 % 7)) + 1;
        }
        return ((int) CalendarUtils.mod(j2, 7L)) + 1;
    }

    public int getYearFromFixedDate(long j2) {
        return getGregorianYearFromFixedDate(j2);
    }

    final int getGregorianYearFromFixedDate(long j2) {
        int iFloorDivide;
        int iFloorDivide2;
        int iFloorDivide3;
        int iFloorDivide4;
        if (j2 > 0) {
            long j3 = j2 - 1;
            iFloorDivide = (int) (j3 / 146097);
            int i2 = (int) (j3 % 146097);
            iFloorDivide2 = i2 / 36524;
            int i3 = i2 % 36524;
            iFloorDivide3 = i3 / 1461;
            int i4 = i3 % 1461;
            iFloorDivide4 = i4 / 365;
            int i5 = (i4 % 365) + 1;
        } else {
            long j4 = j2 - 1;
            iFloorDivide = (int) CalendarUtils.floorDivide(j4, 146097L);
            int iMod = (int) CalendarUtils.mod(j4, 146097L);
            iFloorDivide2 = CalendarUtils.floorDivide(iMod, 36524);
            int iMod2 = CalendarUtils.mod(iMod, 36524);
            iFloorDivide3 = CalendarUtils.floorDivide(iMod2, 1461);
            int iMod3 = CalendarUtils.mod(iMod2, 1461);
            iFloorDivide4 = CalendarUtils.floorDivide(iMod3, 365);
            int iMod4 = CalendarUtils.mod(iMod3, 365) + 1;
        }
        int i6 = (400 * iFloorDivide) + (100 * iFloorDivide2) + (4 * iFloorDivide3) + iFloorDivide4;
        if (iFloorDivide2 != 4 && iFloorDivide4 != 4) {
            i6++;
        }
        return i6;
    }

    @Override // sun.util.calendar.AbstractCalendar
    protected boolean isLeapYear(CalendarDate calendarDate) {
        return isLeapYear(((Date) calendarDate).getNormalizedYear());
    }

    boolean isLeapYear(int i2) {
        return CalendarUtils.isGregorianLeapYear(i2);
    }
}
