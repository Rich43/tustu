package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Locale;
import sun.java2d.marlin.MarlinConst;
import sun.util.calendar.AbstractCalendar;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Era;
import sun.util.calendar.Gregorian;
import sun.util.calendar.JulianCalendar;
import sun.util.calendar.ZoneInfo;

/* loaded from: rt.jar:java/util/GregorianCalendar.class */
public class GregorianCalendar extends Calendar {
    public static final int BC = 0;
    static final int BCE = 0;
    public static final int AD = 1;
    static final int CE = 1;
    private static final int EPOCH_OFFSET = 719163;
    private static final int EPOCH_YEAR = 1970;
    static final int[] MONTH_LENGTH;
    static final int[] LEAP_MONTH_LENGTH;
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_HOUR = 3600000;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_WEEK = 604800000;
    static final int[] MIN_VALUES;
    static final int[] LEAST_MAX_VALUES;
    static final int[] MAX_VALUES;
    static final long serialVersionUID = -8125100834729963327L;
    private static final Gregorian gcal;
    private static JulianCalendar jcal;
    private static Era[] jeras;
    static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;
    private long gregorianCutover;
    private transient long gregorianCutoverDate;
    private transient int gregorianCutoverYear;
    private transient int gregorianCutoverYearJulian;
    private transient BaseCalendar.Date gdate;
    private transient BaseCalendar.Date cdate;
    private transient BaseCalendar calsys;
    private transient int[] zoneOffsets;
    private transient int[] originalFields;
    private transient long cachedFixedDate;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GregorianCalendar.class.desiredAssertionStatus();
        MONTH_LENGTH = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        LEAP_MONTH_LENGTH = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        MIN_VALUES = new int[]{0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0};
        LEAST_MAX_VALUES = new int[]{1, 292269054, 11, 52, 4, 28, 365, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000};
        MAX_VALUES = new int[]{1, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000};
        gcal = CalendarSystem.getGregorianCalendar();
    }

    public GregorianCalendar() {
        this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
        setZoneShared(true);
    }

    public GregorianCalendar(TimeZone timeZone) {
        this(timeZone, Locale.getDefault(Locale.Category.FORMAT));
    }

    public GregorianCalendar(Locale locale) {
        this(TimeZone.getDefaultRef(), locale);
        setZoneShared(true);
    }

    public GregorianCalendar(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736L;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(timeZone);
        setTimeInMillis(System.currentTimeMillis());
    }

    public GregorianCalendar(int i2, int i3, int i4) {
        this(i2, i3, i4, 0, 0, 0, 0);
    }

    public GregorianCalendar(int i2, int i3, int i4, int i5, int i6) {
        this(i2, i3, i4, i5, i6, 0, 0);
    }

    public GregorianCalendar(int i2, int i3, int i4, int i5, int i6, int i7) {
        this(i2, i3, i4, i5, i6, i7, 0);
    }

    GregorianCalendar(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736L;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(getZone());
        set(1, i2);
        set(2, i3);
        set(5, i4);
        if (i5 >= 12 && i5 <= 23) {
            internalSet(9, 1);
            internalSet(10, i5 - 12);
        } else {
            internalSet(10, i5);
        }
        setFieldsComputed(1536);
        set(11, i5);
        set(12, i6);
        set(13, i7);
        internalSet(14, i8);
    }

    GregorianCalendar(TimeZone timeZone, Locale locale, boolean z2) {
        super(timeZone, locale);
        this.gregorianCutover = DEFAULT_GREGORIAN_CUTOVER;
        this.gregorianCutoverDate = 577736L;
        this.gregorianCutoverYear = 1582;
        this.gregorianCutoverYearJulian = 1582;
        this.cachedFixedDate = Long.MIN_VALUE;
        this.gdate = gcal.newCalendarDate(getZone());
    }

    public void setGregorianChange(Date date) {
        long time = date.getTime();
        if (time == this.gregorianCutover) {
            return;
        }
        complete();
        setGregorianChange(time);
    }

    private void setGregorianChange(long j2) {
        this.gregorianCutover = j2;
        this.gregorianCutoverDate = CalendarUtils.floorDivide(j2, 86400000L) + 719163;
        if (j2 == Long.MAX_VALUE) {
            this.gregorianCutoverDate++;
        }
        this.gregorianCutoverYear = getGregorianCutoverDate().getYear();
        BaseCalendar julianCalendarSystem = getJulianCalendarSystem();
        BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem.newCalendarDate(TimeZone.NO_TIMEZONE);
        julianCalendarSystem.getCalendarDateFromFixedDate(date, this.gregorianCutoverDate - 1);
        this.gregorianCutoverYearJulian = date.getNormalizedYear();
        if (this.time < this.gregorianCutover) {
            setUnnormalized();
        }
    }

    public final Date getGregorianChange() {
        return new Date(this.gregorianCutover);
    }

    public boolean isLeapYear(int i2) {
        boolean z2;
        if ((i2 & 3) != 0) {
            return false;
        }
        if (i2 > this.gregorianCutoverYear) {
            return i2 % 100 != 0 || i2 % 400 == 0;
        }
        if (i2 < this.gregorianCutoverYearJulian) {
            return true;
        }
        if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
            z2 = getCalendarDate(this.gregorianCutoverDate).getMonth() < 3;
        } else {
            z2 = i2 == this.gregorianCutoverYear;
        }
        return (z2 && i2 % 100 == 0 && i2 % 400 != 0) ? false : true;
    }

    @Override // java.util.Calendar
    public String getCalendarType() {
        return "gregory";
    }

    @Override // java.util.Calendar
    public boolean equals(Object obj) {
        return (obj instanceof GregorianCalendar) && super.equals(obj) && this.gregorianCutover == ((GregorianCalendar) obj).gregorianCutover;
    }

    @Override // java.util.Calendar
    public int hashCode() {
        return super.hashCode() ^ ((int) this.gregorianCutoverDate);
    }

    @Override // java.util.Calendar
    public void add(int i2, int i3) {
        int i4;
        if (i3 == 0) {
            return;
        }
        if (i2 < 0 || i2 >= 15) {
            throw new IllegalArgumentException();
        }
        complete();
        if (i2 == 1) {
            int iInternalGet = internalGet(1);
            if (internalGetEra() == 1) {
                int i5 = iInternalGet + i3;
                if (i5 > 0) {
                    set(1, i5);
                } else {
                    set(1, 1 - i5);
                    set(0, 0);
                }
            } else {
                int i6 = iInternalGet - i3;
                if (i6 > 0) {
                    set(1, i6);
                } else {
                    set(1, 1 - i6);
                    set(0, 1);
                }
            }
            pinDayOfMonth();
            return;
        }
        if (i2 == 2) {
            int iInternalGet2 = internalGet(2) + i3;
            int iInternalGet3 = internalGet(1);
            if (iInternalGet2 >= 0) {
                i4 = iInternalGet2 / 12;
            } else {
                i4 = ((iInternalGet2 + 1) / 12) - 1;
            }
            if (i4 != 0) {
                if (internalGetEra() == 1) {
                    int i7 = iInternalGet3 + i4;
                    if (i7 > 0) {
                        set(1, i7);
                    } else {
                        set(1, 1 - i7);
                        set(0, 0);
                    }
                } else {
                    int i8 = iInternalGet3 - i4;
                    if (i8 > 0) {
                        set(1, i8);
                    } else {
                        set(1, 1 - i8);
                        set(0, 1);
                    }
                }
            }
            if (iInternalGet2 >= 0) {
                set(2, iInternalGet2 % 12);
            } else {
                int i9 = iInternalGet2 % 12;
                if (i9 < 0) {
                    i9 += 12;
                }
                set(2, 0 + i9);
            }
            pinDayOfMonth();
            return;
        }
        if (i2 == 0) {
            int iInternalGet4 = internalGet(0) + i3;
            if (iInternalGet4 < 0) {
                iInternalGet4 = 0;
            }
            if (iInternalGet4 > 1) {
                iInternalGet4 = 1;
            }
            set(0, iInternalGet4);
            return;
        }
        long j2 = i3;
        long j3 = 0;
        switch (i2) {
            case 3:
            case 4:
            case 8:
                j2 *= 7;
                break;
            case 9:
                j2 = i3 / 2;
                j3 = 12 * (i3 % 2);
                break;
            case 10:
            case 11:
                j2 *= 3600000;
                break;
            case 12:
                j2 *= 60000;
                break;
            case 13:
                j2 *= 1000;
                break;
        }
        if (i2 >= 10) {
            setTimeInMillis(this.time + j2);
            return;
        }
        long currentFixedDate = getCurrentFixedDate();
        long jInternalGet = ((((((j3 + internalGet(11)) * 60) + internalGet(12)) * 60) + internalGet(13)) * 1000) + internalGet(14);
        if (jInternalGet >= 86400000) {
            currentFixedDate++;
            jInternalGet -= 86400000;
        } else if (jInternalGet < 0) {
            currentFixedDate--;
            jInternalGet += 86400000;
        }
        long j4 = currentFixedDate + j2;
        int iInternalGet5 = internalGet(15) + internalGet(16);
        setTimeInMillis((((j4 - 719163) * 86400000) + jInternalGet) - iInternalGet5);
        int iInternalGet6 = iInternalGet5 - (internalGet(15) + internalGet(16));
        if (iInternalGet6 != 0) {
            setTimeInMillis(this.time + iInternalGet6);
            if (getCurrentFixedDate() != j4) {
                setTimeInMillis(this.time - iInternalGet6);
            }
        }
    }

    @Override // java.util.Calendar
    public void roll(int i2, boolean z2) {
        roll(i2, z2 ? 1 : -1);
    }

    @Override // java.util.Calendar
    public void roll(int i2, int i3) {
        int iInternalGet;
        long jInternalGet;
        int monthLength;
        int dayOfMonth;
        BaseCalendar julianCalendarSystem;
        if (i3 == 0) {
            return;
        }
        if (i2 < 0 || i2 >= 15) {
            throw new IllegalArgumentException();
        }
        complete();
        int minimum = getMinimum(i2);
        int maximum = getMaximum(i2);
        switch (i2) {
            case 2:
                if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                    int iInternalGet2 = (internalGet(2) + i3) % 12;
                    if (iInternalGet2 < 0) {
                        iInternalGet2 += 12;
                    }
                    set(2, iInternalGet2);
                    int iMonthLength = monthLength(iInternalGet2);
                    if (internalGet(5) > iMonthLength) {
                        set(5, iMonthLength);
                        return;
                    }
                    return;
                }
                int actualMaximum = getActualMaximum(2) + 1;
                int iInternalGet3 = (internalGet(2) + i3) % actualMaximum;
                if (iInternalGet3 < 0) {
                    iInternalGet3 += actualMaximum;
                }
                set(2, iInternalGet3);
                int actualMaximum2 = getActualMaximum(5);
                if (internalGet(5) > actualMaximum2) {
                    set(5, actualMaximum2);
                    return;
                }
                return;
            case 3:
                int normalizedYear = this.cdate.getNormalizedYear();
                int actualMaximum3 = getActualMaximum(3);
                set(7, internalGet(7));
                int iInternalGet4 = internalGet(3);
                int i4 = iInternalGet4 + i3;
                if (!isCutoverYear(normalizedYear)) {
                    int weekYear = getWeekYear();
                    if (weekYear == normalizedYear) {
                        if (i4 > minimum && i4 < actualMaximum3) {
                            set(3, i4);
                            return;
                        }
                        long currentFixedDate = getCurrentFixedDate();
                        if (this.calsys.getYearFromFixedDate(currentFixedDate - (7 * (iInternalGet4 - minimum))) != normalizedYear) {
                            minimum++;
                        }
                        if (this.calsys.getYearFromFixedDate(currentFixedDate + (7 * (actualMaximum3 - internalGet(3)))) != normalizedYear) {
                            actualMaximum3--;
                        }
                    } else if (weekYear > normalizedYear) {
                        if (i3 < 0) {
                            i3++;
                        }
                        iInternalGet4 = actualMaximum3;
                    } else {
                        if (i3 > 0) {
                            i3 -= iInternalGet4 - actualMaximum3;
                        }
                        iInternalGet4 = minimum;
                    }
                    set(i2, getRolledValue(iInternalGet4, i3, minimum, actualMaximum3));
                    return;
                }
                long currentFixedDate2 = getCurrentFixedDate();
                if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
                    julianCalendarSystem = getCutoverCalendarSystem();
                } else if (normalizedYear == this.gregorianCutoverYear) {
                    julianCalendarSystem = gcal;
                } else {
                    julianCalendarSystem = getJulianCalendarSystem();
                }
                long j2 = currentFixedDate2 - (7 * (iInternalGet4 - minimum));
                if (julianCalendarSystem.getYearFromFixedDate(j2) != normalizedYear) {
                    minimum++;
                }
                long j3 = currentFixedDate2 + (7 * (actualMaximum3 - iInternalGet4));
                if ((j3 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem()).getYearFromFixedDate(j3) != normalizedYear) {
                    actualMaximum3--;
                }
                BaseCalendar.Date calendarDate = getCalendarDate(j2 + ((getRolledValue(iInternalGet4, i3, minimum, actualMaximum3) - 1) * 7));
                set(2, calendarDate.getMonth() - 1);
                set(5, calendarDate.getDayOfMonth());
                return;
            case 4:
                boolean zIsCutoverYear = isCutoverYear(this.cdate.getNormalizedYear());
                int iInternalGet5 = internalGet(7) - getFirstDayOfWeek();
                if (iInternalGet5 < 0) {
                    iInternalGet5 += 7;
                }
                long currentFixedDate3 = getCurrentFixedDate();
                if (zIsCutoverYear) {
                    jInternalGet = getFixedDateMonth1(this.cdate, currentFixedDate3);
                    monthLength = actualMonthLength();
                } else {
                    jInternalGet = (currentFixedDate3 - internalGet(5)) + 1;
                    monthLength = this.calsys.getMonthLength(this.cdate);
                }
                long dayOfWeekDateOnOrBefore = BaseCalendar.getDayOfWeekDateOnOrBefore(jInternalGet + 6, getFirstDayOfWeek());
                if (((int) (dayOfWeekDateOnOrBefore - jInternalGet)) >= getMinimalDaysInFirstWeek()) {
                    dayOfWeekDateOnOrBefore -= 7;
                }
                long rolledValue = dayOfWeekDateOnOrBefore + ((getRolledValue(internalGet(i2), i3, 1, getActualMaximum(i2)) - 1) * 7) + iInternalGet5;
                if (rolledValue < jInternalGet) {
                    rolledValue = jInternalGet;
                } else if (rolledValue >= jInternalGet + monthLength) {
                    rolledValue = (jInternalGet + monthLength) - 1;
                }
                if (zIsCutoverYear) {
                    dayOfMonth = getCalendarDate(rolledValue).getDayOfMonth();
                } else {
                    dayOfMonth = ((int) (rolledValue - jInternalGet)) + 1;
                }
                set(5, dayOfMonth);
                return;
            case 5:
                if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                    maximum = this.calsys.getMonthLength(this.cdate);
                    break;
                } else {
                    BaseCalendar.Date calendarDate2 = getCalendarDate(getFixedDateMonth1(this.cdate, getCurrentFixedDate()) + getRolledValue((int) (r0 - r0), i3, 0, actualMonthLength() - 1));
                    if (!$assertionsDisabled && calendarDate2.getMonth() - 1 != internalGet(2)) {
                        throw new AssertionError();
                    }
                    set(5, calendarDate2.getDayOfMonth());
                    return;
                }
            case 6:
                maximum = getActualMaximum(i2);
                if (isCutoverYear(this.cdate.getNormalizedYear())) {
                    BaseCalendar.Date calendarDate3 = getCalendarDate((((getCurrentFixedDate() - internalGet(6)) + 1) + getRolledValue(((int) (r0 - r0)) + 1, i3, minimum, maximum)) - 1);
                    set(2, calendarDate3.getMonth() - 1);
                    set(5, calendarDate3.getDayOfMonth());
                    return;
                }
                break;
            case 7:
                if (!isCutoverYear(this.cdate.getNormalizedYear()) && (iInternalGet = internalGet(3)) > 1 && iInternalGet < 52) {
                    set(3, iInternalGet);
                    maximum = 7;
                    break;
                } else {
                    int i5 = i3 % 7;
                    if (i5 == 0) {
                        return;
                    }
                    long currentFixedDate4 = getCurrentFixedDate();
                    long dayOfWeekDateOnOrBefore2 = BaseCalendar.getDayOfWeekDateOnOrBefore(currentFixedDate4, getFirstDayOfWeek());
                    long j4 = currentFixedDate4 + i5;
                    if (j4 < dayOfWeekDateOnOrBefore2) {
                        j4 += 7;
                    } else if (j4 >= dayOfWeekDateOnOrBefore2 + 7) {
                        j4 -= 7;
                    }
                    BaseCalendar.Date calendarDate4 = getCalendarDate(j4);
                    set(0, calendarDate4.getNormalizedYear() <= 0 ? 0 : 1);
                    set(calendarDate4.getYear(), calendarDate4.getMonth() - 1, calendarDate4.getDayOfMonth());
                    return;
                }
                break;
            case 8:
                minimum = 1;
                if (!isCutoverYear(this.cdate.getNormalizedYear())) {
                    int iInternalGet6 = internalGet(5);
                    int monthLength2 = this.calsys.getMonthLength(this.cdate);
                    maximum = monthLength2 / 7;
                    if ((iInternalGet6 - 1) % 7 < monthLength2 % 7) {
                        maximum++;
                    }
                    set(7, internalGet(7));
                    break;
                } else {
                    long currentFixedDate5 = getCurrentFixedDate();
                    long fixedDateMonth1 = getFixedDateMonth1(this.cdate, currentFixedDate5);
                    int iActualMonthLength = actualMonthLength();
                    int i6 = iActualMonthLength % 7;
                    int i7 = iActualMonthLength / 7;
                    int i8 = ((int) (currentFixedDate5 - fixedDateMonth1)) % 7;
                    if (i8 < i6) {
                        i7++;
                    }
                    long rolledValue2 = fixedDateMonth1 + ((getRolledValue(internalGet(i2), i3, 1, i7) - 1) * 7) + i8;
                    AbstractCalendar julianCalendarSystem2 = rolledValue2 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
                    BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem2.newCalendarDate(TimeZone.NO_TIMEZONE);
                    julianCalendarSystem2.getCalendarDateFromFixedDate(date, rolledValue2);
                    set(5, date.getDayOfMonth());
                    return;
                }
            case 10:
            case 11:
                int rolledValue3 = getRolledValue(internalGet(i2), i3, minimum, maximum);
                int i9 = rolledValue3;
                if (i2 == 10 && internalGet(9) == 1) {
                    i9 += 12;
                }
                CalendarDate calendarDate5 = this.calsys.getCalendarDate(this.time, getZone());
                calendarDate5.setHours(i9);
                this.time = this.calsys.getTime(calendarDate5);
                if (internalGet(11) == calendarDate5.getHours()) {
                    int rolledValue4 = getRolledValue(rolledValue3, i3 > 0 ? 1 : -1, minimum, maximum);
                    if (i2 == 10 && internalGet(9) == 1) {
                        rolledValue4 += 12;
                    }
                    calendarDate5.setHours(rolledValue4);
                    this.time = this.calsys.getTime(calendarDate5);
                }
                int hours = calendarDate5.getHours();
                internalSet(11, hours);
                internalSet(9, hours / 12);
                internalSet(10, hours % 12);
                int zoneOffset = calendarDate5.getZoneOffset();
                int daylightSaving = calendarDate5.getDaylightSaving();
                internalSet(15, zoneOffset - daylightSaving);
                internalSet(16, daylightSaving);
                return;
        }
        set(i2, getRolledValue(internalGet(i2), i3, minimum, maximum));
    }

    @Override // java.util.Calendar
    public int getMinimum(int i2) {
        return MIN_VALUES[i2];
    }

    @Override // java.util.Calendar
    public int getMaximum(int i2) {
        switch (i2) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                if (this.gregorianCutoverYear <= 200) {
                    GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
                    gregorianCalendar.setLenient(true);
                    gregorianCalendar.setTimeInMillis(this.gregorianCutover);
                    int actualMaximum = gregorianCalendar.getActualMaximum(i2);
                    gregorianCalendar.setTimeInMillis(this.gregorianCutover - 1);
                    return Math.max(MAX_VALUES[i2], Math.max(actualMaximum, gregorianCalendar.getActualMaximum(i2)));
                }
                break;
        }
        return MAX_VALUES[i2];
    }

    @Override // java.util.Calendar
    public int getGreatestMinimum(int i2) {
        if (i2 == 5) {
            return Math.max(MIN_VALUES[i2], getCalendarDate(getFixedDateMonth1(getGregorianCutoverDate(), this.gregorianCutoverDate)).getDayOfMonth());
        }
        return MIN_VALUES[i2];
    }

    @Override // java.util.Calendar
    public int getLeastMaximum(int i2) {
        switch (i2) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
                GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
                gregorianCalendar.setLenient(true);
                gregorianCalendar.setTimeInMillis(this.gregorianCutover);
                int actualMaximum = gregorianCalendar.getActualMaximum(i2);
                gregorianCalendar.setTimeInMillis(this.gregorianCutover - 1);
                return Math.min(LEAST_MAX_VALUES[i2], Math.min(actualMaximum, gregorianCalendar.getActualMaximum(i2)));
            case 7:
            default:
                return LEAST_MAX_VALUES[i2];
        }
    }

    @Override // java.util.Calendar
    public int getActualMinimum(int i2) {
        if (i2 == 5) {
            GregorianCalendar normalizedCalendar = getNormalizedCalendar();
            int normalizedYear = normalizedCalendar.cdate.getNormalizedYear();
            if (normalizedYear == this.gregorianCutoverYear || normalizedYear == this.gregorianCutoverYearJulian) {
                return getCalendarDate(getFixedDateMonth1(normalizedCalendar.cdate, normalizedCalendar.calsys.getFixedDate(normalizedCalendar.cdate))).getDayOfMonth();
            }
        }
        return getMinimum(i2);
    }

    @Override // java.util.Calendar
    public int getActualMaximum(int i2) {
        int year;
        int iActualMonthLength;
        int dayOfWeek;
        long fixedDate;
        long fixedDate2;
        if ((130689 & (1 << i2)) != 0) {
            return getMaximum(i2);
        }
        GregorianCalendar normalizedCalendar = getNormalizedCalendar();
        BaseCalendar.Date date = normalizedCalendar.cdate;
        BaseCalendar baseCalendar = normalizedCalendar.calsys;
        int normalizedYear = date.getNormalizedYear();
        switch (i2) {
            case 1:
                if (normalizedCalendar == this) {
                    normalizedCalendar = (GregorianCalendar) clone();
                }
                long yearOffsetInMillis = normalizedCalendar.getYearOffsetInMillis();
                if (normalizedCalendar.internalGetEra() == 1) {
                    normalizedCalendar.setTimeInMillis(Long.MAX_VALUE);
                    year = normalizedCalendar.get(1);
                    if (yearOffsetInMillis > normalizedCalendar.getYearOffsetInMillis()) {
                        year--;
                        break;
                    }
                } else {
                    CalendarSystem julianCalendarSystem = normalizedCalendar.getTimeInMillis() >= this.gregorianCutover ? gcal : getJulianCalendarSystem();
                    CalendarDate calendarDate = julianCalendarSystem.getCalendarDate(Long.MIN_VALUE, getZone());
                    long dayOfYear = ((((((((baseCalendar.getDayOfYear(calendarDate) - 1) * 24) + calendarDate.getHours()) * 60) + calendarDate.getMinutes()) * 60) + calendarDate.getSeconds()) * 1000) + calendarDate.getMillis();
                    year = calendarDate.getYear();
                    if (year <= 0) {
                        if (!$assertionsDisabled && julianCalendarSystem != gcal) {
                            throw new AssertionError();
                        }
                        year = 1 - year;
                    }
                    if (yearOffsetInMillis < dayOfYear) {
                        year--;
                        break;
                    }
                }
                break;
            case 2:
                if (!normalizedCalendar.isCutoverYear(normalizedYear)) {
                    year = 11;
                    break;
                } else {
                    do {
                        normalizedYear++;
                        fixedDate2 = gcal.getFixedDate(normalizedYear, 1, 1, null);
                    } while (fixedDate2 < this.gregorianCutoverDate);
                    BaseCalendar.Date date2 = (BaseCalendar.Date) date.clone();
                    baseCalendar.getCalendarDateFromFixedDate(date2, fixedDate2 - 1);
                    year = date2.getMonth() - 1;
                    break;
                }
            case 3:
                if (!normalizedCalendar.isCutoverYear(normalizedYear)) {
                    CalendarDate calendarDateNewCalendarDate = baseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
                    calendarDateNewCalendarDate.setDate(date.getYear(), 1, 1);
                    int dayOfWeek2 = baseCalendar.getDayOfWeek(calendarDateNewCalendarDate) - getFirstDayOfWeek();
                    if (dayOfWeek2 < 0) {
                        dayOfWeek2 += 7;
                    }
                    year = 52;
                    int minimalDaysInFirstWeek = (dayOfWeek2 + getMinimalDaysInFirstWeek()) - 1;
                    if (minimalDaysInFirstWeek == 6 || (date.isLeapYear() && (minimalDaysInFirstWeek == 5 || minimalDaysInFirstWeek == 12))) {
                        year = 52 + 1;
                        break;
                    }
                } else {
                    if (normalizedCalendar == this) {
                        normalizedCalendar = (GregorianCalendar) normalizedCalendar.clone();
                    }
                    int actualMaximum = getActualMaximum(6);
                    normalizedCalendar.set(6, actualMaximum);
                    year = normalizedCalendar.get(3);
                    if (internalGet(1) != normalizedCalendar.getWeekYear()) {
                        normalizedCalendar.set(6, actualMaximum - 7);
                        year = normalizedCalendar.get(3);
                        break;
                    }
                }
                break;
            case 4:
                if (!normalizedCalendar.isCutoverYear(normalizedYear)) {
                    CalendarDate calendarDateNewCalendarDate2 = baseCalendar.newCalendarDate(null);
                    calendarDateNewCalendarDate2.setDate(date.getYear(), date.getMonth(), 1);
                    int dayOfWeek3 = baseCalendar.getDayOfWeek(calendarDateNewCalendarDate2);
                    int monthLength = baseCalendar.getMonthLength(calendarDateNewCalendarDate2);
                    int firstDayOfWeek = dayOfWeek3 - getFirstDayOfWeek();
                    if (firstDayOfWeek < 0) {
                        firstDayOfWeek += 7;
                    }
                    int i3 = 7 - firstDayOfWeek;
                    year = 3;
                    if (i3 >= getMinimalDaysInFirstWeek()) {
                        year = 3 + 1;
                    }
                    int i4 = monthLength - (i3 + 21);
                    if (i4 > 0) {
                        year++;
                        if (i4 > 7) {
                            year++;
                            break;
                        }
                    }
                } else {
                    if (normalizedCalendar == this) {
                        normalizedCalendar = (GregorianCalendar) normalizedCalendar.clone();
                    }
                    int iInternalGet = normalizedCalendar.internalGet(1);
                    int iInternalGet2 = normalizedCalendar.internalGet(2);
                    do {
                        year = normalizedCalendar.get(4);
                        normalizedCalendar.add(4, 1);
                        if (normalizedCalendar.get(1) != iInternalGet) {
                            break;
                        }
                    } while (normalizedCalendar.get(2) == iInternalGet2);
                }
                break;
            case 5:
                year = baseCalendar.getMonthLength(date);
                if (normalizedCalendar.isCutoverYear(normalizedYear) && date.getDayOfMonth() != year) {
                    long currentFixedDate = normalizedCalendar.getCurrentFixedDate();
                    if (currentFixedDate < this.gregorianCutoverDate) {
                        year = normalizedCalendar.getCalendarDate((normalizedCalendar.getFixedDateMonth1(normalizedCalendar.cdate, currentFixedDate) + normalizedCalendar.actualMonthLength()) - 1).getDayOfMonth();
                        break;
                    }
                }
                break;
            case 6:
                if (!normalizedCalendar.isCutoverYear(normalizedYear)) {
                    year = baseCalendar.getYearLength(date);
                    break;
                } else {
                    if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian) {
                        fixedDate = normalizedCalendar.getCutoverCalendarSystem().getFixedDate(normalizedYear, 1, 1, null);
                    } else if (normalizedYear == this.gregorianCutoverYearJulian) {
                        fixedDate = baseCalendar.getFixedDate(normalizedYear, 1, 1, null);
                    } else {
                        fixedDate = this.gregorianCutoverDate;
                    }
                    long fixedDate3 = gcal.getFixedDate(normalizedYear + 1, 1, 1, null);
                    if (fixedDate3 < this.gregorianCutoverDate) {
                        fixedDate3 = this.gregorianCutoverDate;
                    }
                    if (!$assertionsDisabled && fixedDate > baseCalendar.getFixedDate(date.getNormalizedYear(), date.getMonth(), date.getDayOfMonth(), date)) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && fixedDate3 < baseCalendar.getFixedDate(date.getNormalizedYear(), date.getMonth(), date.getDayOfMonth(), date)) {
                        throw new AssertionError();
                    }
                    year = (int) (fixedDate3 - fixedDate);
                    break;
                }
                break;
            case 7:
            default:
                throw new ArrayIndexOutOfBoundsException(i2);
            case 8:
                int dayOfWeek4 = date.getDayOfWeek();
                if (!normalizedCalendar.isCutoverYear(normalizedYear)) {
                    BaseCalendar.Date date3 = (BaseCalendar.Date) date.clone();
                    iActualMonthLength = baseCalendar.getMonthLength(date3);
                    date3.setDayOfMonth(1);
                    baseCalendar.normalize(date3);
                    dayOfWeek = date3.getDayOfWeek();
                } else {
                    if (normalizedCalendar == this) {
                        normalizedCalendar = (GregorianCalendar) clone();
                    }
                    iActualMonthLength = normalizedCalendar.actualMonthLength();
                    normalizedCalendar.set(5, normalizedCalendar.getActualMinimum(5));
                    dayOfWeek = normalizedCalendar.get(7);
                }
                int i5 = dayOfWeek4 - dayOfWeek;
                if (i5 < 0) {
                    i5 += 7;
                }
                year = ((iActualMonthLength - i5) + 6) / 7;
                break;
        }
        return year;
    }

    private long getYearOffsetInMillis() {
        return (((((((((internalGet(6) - 1) * 24) + internalGet(11)) * 60) + internalGet(12)) * 60) + internalGet(13)) * 1000) + internalGet(14)) - (internalGet(15) + internalGet(16));
    }

    @Override // java.util.Calendar
    public Object clone() {
        GregorianCalendar gregorianCalendar = (GregorianCalendar) super.clone();
        gregorianCalendar.gdate = (BaseCalendar.Date) this.gdate.clone();
        if (this.cdate != null) {
            if (this.cdate != this.gdate) {
                gregorianCalendar.cdate = (BaseCalendar.Date) this.cdate.clone();
            } else {
                gregorianCalendar.cdate = gregorianCalendar.gdate;
            }
        }
        gregorianCalendar.originalFields = null;
        gregorianCalendar.zoneOffsets = null;
        return gregorianCalendar;
    }

    @Override // java.util.Calendar
    public TimeZone getTimeZone() {
        TimeZone timeZone = super.getTimeZone();
        this.gdate.setZone(timeZone);
        if (this.cdate != null && this.cdate != this.gdate) {
            this.cdate.setZone(timeZone);
        }
        return timeZone;
    }

    @Override // java.util.Calendar
    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        this.gdate.setZone(timeZone);
        if (this.cdate != null && this.cdate != this.gdate) {
            this.cdate.setZone(timeZone);
        }
    }

    @Override // java.util.Calendar
    public final boolean isWeekDateSupported() {
        return true;
    }

    @Override // java.util.Calendar
    public int getWeekYear() {
        int i2 = get(1);
        if (internalGetEra() == 0) {
            i2 = 1 - i2;
        }
        if (i2 > this.gregorianCutoverYear + 1) {
            int iInternalGet = internalGet(3);
            if (internalGet(2) == 0) {
                if (iInternalGet >= 52) {
                    i2--;
                }
            } else if (iInternalGet == 1) {
                i2++;
            }
            return i2;
        }
        int iInternalGet2 = internalGet(6);
        int actualMaximum = getActualMaximum(6);
        int minimalDaysInFirstWeek = getMinimalDaysInFirstWeek();
        if (iInternalGet2 > minimalDaysInFirstWeek && iInternalGet2 < actualMaximum - 6) {
            return i2;
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
        gregorianCalendar.setLenient(true);
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.set(6, 1);
        gregorianCalendar.complete();
        int firstDayOfWeek = getFirstDayOfWeek() - gregorianCalendar.get(7);
        if (firstDayOfWeek != 0) {
            if (firstDayOfWeek < 0) {
                firstDayOfWeek += 7;
            }
            gregorianCalendar.add(6, firstDayOfWeek);
        }
        int i3 = gregorianCalendar.get(6);
        if (iInternalGet2 < i3) {
            if (i3 <= minimalDaysInFirstWeek) {
                i2--;
            }
        } else {
            gregorianCalendar.set(1, i2 + 1);
            gregorianCalendar.set(6, 1);
            gregorianCalendar.complete();
            int firstDayOfWeek2 = getFirstDayOfWeek() - gregorianCalendar.get(7);
            if (firstDayOfWeek2 != 0) {
                if (firstDayOfWeek2 < 0) {
                    firstDayOfWeek2 += 7;
                }
                gregorianCalendar.add(6, firstDayOfWeek2);
            }
            int i4 = gregorianCalendar.get(6) - 1;
            if (i4 == 0) {
                i4 = 7;
            }
            if (i4 >= minimalDaysInFirstWeek && (actualMaximum - iInternalGet2) + 1 <= 7 - i4) {
                i2++;
            }
        }
        return i2;
    }

    @Override // java.util.Calendar
    public void setWeekDate(int i2, int i3, int i4) {
        if (i4 < 1 || i4 > 7) {
            throw new IllegalArgumentException("invalid dayOfWeek: " + i4);
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) clone();
        gregorianCalendar.setLenient(true);
        int i5 = gregorianCalendar.get(0);
        gregorianCalendar.clear();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.set(0, i5);
        gregorianCalendar.set(1, i2);
        gregorianCalendar.set(3, 1);
        gregorianCalendar.set(7, getFirstDayOfWeek());
        int firstDayOfWeek = i4 - getFirstDayOfWeek();
        if (firstDayOfWeek < 0) {
            firstDayOfWeek += 7;
        }
        int i6 = firstDayOfWeek + (7 * (i3 - 1));
        if (i6 != 0) {
            gregorianCalendar.add(6, i6);
        } else {
            gregorianCalendar.complete();
        }
        if (!isLenient() && (gregorianCalendar.getWeekYear() != i2 || gregorianCalendar.internalGet(3) != i3 || gregorianCalendar.internalGet(7) != i4)) {
            throw new IllegalArgumentException();
        }
        set(0, gregorianCalendar.internalGet(0));
        set(1, gregorianCalendar.internalGet(1));
        set(2, gregorianCalendar.internalGet(2));
        set(5, gregorianCalendar.internalGet(5));
        internalSet(3, i3);
        complete();
    }

    @Override // java.util.Calendar
    public int getWeeksInWeekYear() {
        GregorianCalendar normalizedCalendar = getNormalizedCalendar();
        int weekYear = normalizedCalendar.getWeekYear();
        if (weekYear == normalizedCalendar.internalGet(1)) {
            return normalizedCalendar.getActualMaximum(3);
        }
        if (normalizedCalendar == this) {
            normalizedCalendar = (GregorianCalendar) normalizedCalendar.clone();
        }
        normalizedCalendar.setWeekDate(weekYear, 2, internalGet(7));
        return normalizedCalendar.getActualMaximum(3);
    }

    @Override // java.util.Calendar
    protected void computeFields() {
        int setStateFields;
        if (isPartiallyNormalized()) {
            setStateFields = getSetStateFields();
            int i2 = (setStateFields ^ (-1)) & 131071;
            if (i2 != 0 || this.calsys == null) {
                setStateFields |= computeFields(i2, setStateFields & MarlinConst.INITIAL_EDGES_CAPACITY);
                if (!$assertionsDisabled && setStateFields != 131071) {
                    throw new AssertionError();
                }
            }
        } else {
            setStateFields = 131071;
            computeFields(131071, 0);
        }
        setFieldsComputed(setStateFields);
    }

    private int computeFields(int i2, int i3) {
        int year;
        long fixedDate;
        int offset = 0;
        TimeZone zone = getZone();
        if (this.zoneOffsets == null) {
            this.zoneOffsets = new int[2];
        }
        if (i3 != 98304) {
            if (zone instanceof ZoneInfo) {
                offset = ((ZoneInfo) zone).getOffsets(this.time, this.zoneOffsets);
            } else {
                offset = zone.getOffset(this.time);
                this.zoneOffsets[0] = zone.getRawOffset();
                this.zoneOffsets[1] = offset - this.zoneOffsets[0];
            }
        }
        if (i3 != 0) {
            if (isFieldSet(i3, 15)) {
                this.zoneOffsets[0] = internalGet(15);
            }
            if (isFieldSet(i3, 16)) {
                this.zoneOffsets[1] = internalGet(16);
            }
            offset = this.zoneOffsets[0] + this.zoneOffsets[1];
        }
        long j2 = (offset / 86400000) + (this.time / 86400000);
        int i4 = (offset % 86400000) + ((int) (this.time % 86400000));
        if (i4 >= 86400000) {
            i4 = (int) (i4 - 86400000);
            j2++;
        } else {
            while (i4 < 0) {
                i4 = (int) (i4 + 86400000);
                j2--;
            }
        }
        long j3 = j2 + 719163;
        int i5 = 1;
        if (j3 >= this.gregorianCutoverDate) {
            if (!$assertionsDisabled && this.cachedFixedDate != Long.MIN_VALUE && !this.gdate.isNormalized()) {
                throw new AssertionError((Object) "cache control: not normalized");
            }
            if (!$assertionsDisabled && this.cachedFixedDate != Long.MIN_VALUE && gcal.getFixedDate(this.gdate.getNormalizedYear(), this.gdate.getMonth(), this.gdate.getDayOfMonth(), this.gdate) != this.cachedFixedDate) {
                throw new AssertionError((Object) ("cache control: inconsictency, cachedFixedDate=" + this.cachedFixedDate + ", computed=" + gcal.getFixedDate(this.gdate.getNormalizedYear(), this.gdate.getMonth(), this.gdate.getDayOfMonth(), this.gdate) + ", date=" + ((Object) this.gdate)));
            }
            if (j3 != this.cachedFixedDate) {
                gcal.getCalendarDateFromFixedDate(this.gdate, j3);
                this.cachedFixedDate = j3;
            }
            year = this.gdate.getYear();
            if (year <= 0) {
                year = 1 - year;
                i5 = 0;
            }
            this.calsys = gcal;
            this.cdate = this.gdate;
            if (!$assertionsDisabled && this.cdate.getDayOfWeek() <= 0) {
                throw new AssertionError((Object) ("dow=" + this.cdate.getDayOfWeek() + ", date=" + ((Object) this.cdate)));
            }
        } else {
            this.calsys = getJulianCalendarSystem();
            this.cdate = jcal.newCalendarDate(getZone());
            jcal.getCalendarDateFromFixedDate(this.cdate, j3);
            if (this.cdate.getEra() == jeras[0]) {
                i5 = 0;
            }
            year = this.cdate.getYear();
        }
        internalSet(0, i5);
        internalSet(1, year);
        int i6 = i2 | 3;
        int month = this.cdate.getMonth() - 1;
        int dayOfMonth = this.cdate.getDayOfMonth();
        if ((i2 & 164) != 0) {
            internalSet(2, month);
            internalSet(5, dayOfMonth);
            internalSet(7, this.cdate.getDayOfWeek());
            i6 |= 164;
        }
        if ((i2 & 32256) != 0) {
            if (i4 != 0) {
                int i7 = i4 / ONE_HOUR;
                internalSet(11, i7);
                internalSet(9, i7 / 12);
                internalSet(10, i7 % 12);
                int i8 = i4 % ONE_HOUR;
                internalSet(12, i8 / ONE_MINUTE);
                int i9 = i8 % ONE_MINUTE;
                internalSet(13, i9 / 1000);
                internalSet(14, i9 % 1000);
            } else {
                internalSet(11, 0);
                internalSet(9, 0);
                internalSet(10, 0);
                internalSet(12, 0);
                internalSet(13, 0);
                internalSet(14, 0);
            }
            i6 |= 32256;
        }
        if ((i2 & MarlinConst.INITIAL_EDGES_CAPACITY) != 0) {
            internalSet(15, this.zoneOffsets[0]);
            internalSet(16, this.zoneOffsets[1]);
            i6 |= MarlinConst.INITIAL_EDGES_CAPACITY;
        }
        if ((i2 & 344) != 0) {
            int normalizedYear = this.cdate.getNormalizedYear();
            long fixedDate2 = this.calsys.getFixedDate(normalizedYear, 1, 1, this.cdate);
            int i10 = ((int) (j3 - fixedDate2)) + 1;
            long fixedDateMonth1 = (j3 - dayOfMonth) + 1;
            int i11 = this.calsys == gcal ? this.gregorianCutoverYear : this.gregorianCutoverYearJulian;
            int i12 = dayOfMonth - 1;
            if (normalizedYear == i11) {
                if (this.gregorianCutoverYearJulian <= this.gregorianCutoverYear) {
                    fixedDate2 = getFixedDateJan1(this.cdate, j3);
                    if (j3 >= this.gregorianCutoverDate) {
                        fixedDateMonth1 = getFixedDateMonth1(this.cdate, j3);
                    }
                }
                int i13 = ((int) (j3 - fixedDate2)) + 1;
                int i14 = i10 - i13;
                i10 = i13;
                i12 = (int) (j3 - fixedDateMonth1);
            }
            internalSet(6, i10);
            internalSet(8, (i12 / 7) + 1);
            int weekNumber = getWeekNumber(fixedDate2, j3);
            if (weekNumber == 0) {
                long j4 = fixedDate2 - 1;
                long fixedDate3 = fixedDate2 - 365;
                if (normalizedYear > i11 + 1) {
                    if (CalendarUtils.isGregorianLeapYear(normalizedYear - 1)) {
                        fixedDate3--;
                    }
                } else if (normalizedYear <= this.gregorianCutoverYearJulian) {
                    if (CalendarUtils.isJulianLeapYear(normalizedYear - 1)) {
                        fixedDate3--;
                    }
                } else {
                    BaseCalendar baseCalendar = this.calsys;
                    int normalizedYear2 = getCalendarDate(j4).getNormalizedYear();
                    if (normalizedYear2 == this.gregorianCutoverYear) {
                        BaseCalendar cutoverCalendarSystem = getCutoverCalendarSystem();
                        if (cutoverCalendarSystem == jcal) {
                            fixedDate3 = cutoverCalendarSystem.getFixedDate(normalizedYear2, 1, 1, null);
                        } else {
                            fixedDate3 = this.gregorianCutoverDate;
                            Gregorian gregorian = gcal;
                        }
                    } else if (normalizedYear2 <= this.gregorianCutoverYearJulian) {
                        fixedDate3 = getJulianCalendarSystem().getFixedDate(normalizedYear2, 1, 1, null);
                    }
                }
                weekNumber = getWeekNumber(fixedDate3, j4);
            } else if (normalizedYear > this.gregorianCutoverYear || normalizedYear < this.gregorianCutoverYearJulian - 1) {
                if (weekNumber >= 52) {
                    long j5 = fixedDate2 + 365;
                    if (this.cdate.isLeapYear()) {
                        j5++;
                    }
                    long dayOfWeekDateOnOrBefore = BaseCalendar.getDayOfWeekDateOnOrBefore(j5 + 6, getFirstDayOfWeek());
                    if (((int) (dayOfWeekDateOnOrBefore - j5)) >= getMinimalDaysInFirstWeek() && j3 >= dayOfWeekDateOnOrBefore - 7) {
                        weekNumber = 1;
                    }
                }
            } else {
                BaseCalendar cutoverCalendarSystem2 = this.calsys;
                int i15 = normalizedYear + 1;
                if (i15 == this.gregorianCutoverYearJulian + 1 && i15 < this.gregorianCutoverYear) {
                    i15 = this.gregorianCutoverYear;
                }
                if (i15 == this.gregorianCutoverYear) {
                    cutoverCalendarSystem2 = getCutoverCalendarSystem();
                }
                if (i15 > this.gregorianCutoverYear || this.gregorianCutoverYearJulian == this.gregorianCutoverYear || i15 == this.gregorianCutoverYearJulian) {
                    fixedDate = cutoverCalendarSystem2.getFixedDate(i15, 1, 1, null);
                } else {
                    fixedDate = this.gregorianCutoverDate;
                    Gregorian gregorian2 = gcal;
                }
                long dayOfWeekDateOnOrBefore2 = BaseCalendar.getDayOfWeekDateOnOrBefore(fixedDate + 6, getFirstDayOfWeek());
                if (((int) (dayOfWeekDateOnOrBefore2 - fixedDate)) >= getMinimalDaysInFirstWeek() && j3 >= dayOfWeekDateOnOrBefore2 - 7) {
                    weekNumber = 1;
                }
            }
            internalSet(3, weekNumber);
            internalSet(4, getWeekNumber(fixedDateMonth1, j3));
            i6 |= 344;
        }
        return i6;
    }

    private int getWeekNumber(long j2, long j3) {
        long dayOfWeekDateOnOrBefore = Gregorian.getDayOfWeekDateOnOrBefore(j2 + 6, getFirstDayOfWeek());
        int i2 = (int) (dayOfWeekDateOnOrBefore - j2);
        if (!$assertionsDisabled && i2 > 7) {
            throw new AssertionError();
        }
        if (i2 >= getMinimalDaysInFirstWeek()) {
            dayOfWeekDateOnOrBefore -= 7;
        }
        int i3 = (int) (j3 - dayOfWeekDateOnOrBefore);
        if (i3 >= 0) {
            return (i3 / 7) + 1;
        }
        return CalendarUtils.floorDivide(i3, 7) + 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x020b  */
    @Override // java.util.Calendar
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void computeTime() {
        /*
            Method dump skipped, instructions count: 970
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.GregorianCalendar.computeTime():void");
    }

    private long getFixedDate(BaseCalendar baseCalendar, int i2, int i3) {
        int iInternalGet;
        int firstDayOfWeek;
        int iInternalGet2;
        int iInternalGet3 = 0;
        if (isFieldSet(i3, 2)) {
            iInternalGet3 = internalGet(2);
            if (iInternalGet3 > 11) {
                i2 += iInternalGet3 / 12;
                iInternalGet3 %= 12;
            } else if (iInternalGet3 < 0) {
                int[] iArr = new int[1];
                i2 += CalendarUtils.floorDivide(iInternalGet3, 12, iArr);
                iInternalGet3 = iArr[0];
            }
        }
        long fixedDate = baseCalendar.getFixedDate(i2, iInternalGet3 + 1, 1, baseCalendar == gcal ? this.gdate : null);
        if (isFieldSet(i3, 2)) {
            if (isFieldSet(i3, 5)) {
                if (isSet(5)) {
                    fixedDate = (fixedDate + internalGet(5)) - 1;
                }
            } else if (isFieldSet(i3, 4)) {
                long dayOfWeekDateOnOrBefore = BaseCalendar.getDayOfWeekDateOnOrBefore(fixedDate + 6, getFirstDayOfWeek());
                if (dayOfWeekDateOnOrBefore - fixedDate >= getMinimalDaysInFirstWeek()) {
                    dayOfWeekDateOnOrBefore -= 7;
                }
                if (isFieldSet(i3, 7)) {
                    dayOfWeekDateOnOrBefore = BaseCalendar.getDayOfWeekDateOnOrBefore(dayOfWeekDateOnOrBefore + 6, internalGet(7));
                }
                fixedDate = dayOfWeekDateOnOrBefore + (7 * (internalGet(4) - 1));
            } else {
                if (isFieldSet(i3, 7)) {
                    firstDayOfWeek = internalGet(7);
                } else {
                    firstDayOfWeek = getFirstDayOfWeek();
                }
                if (isFieldSet(i3, 8)) {
                    iInternalGet2 = internalGet(8);
                } else {
                    iInternalGet2 = 1;
                }
                if (iInternalGet2 >= 0) {
                    fixedDate = BaseCalendar.getDayOfWeekDateOnOrBefore((fixedDate + (7 * iInternalGet2)) - 1, firstDayOfWeek);
                } else {
                    fixedDate = BaseCalendar.getDayOfWeekDateOnOrBefore((fixedDate + (monthLength(iInternalGet3, i2) + (7 * (iInternalGet2 + 1)))) - 1, firstDayOfWeek);
                }
            }
        } else {
            if (i2 == this.gregorianCutoverYear && baseCalendar == gcal && fixedDate < this.gregorianCutoverDate && this.gregorianCutoverYear != this.gregorianCutoverYearJulian) {
                fixedDate = this.gregorianCutoverDate;
            }
            if (isFieldSet(i3, 6)) {
                fixedDate = (fixedDate + internalGet(6)) - 1;
            } else {
                long dayOfWeekDateOnOrBefore2 = BaseCalendar.getDayOfWeekDateOnOrBefore(fixedDate + 6, getFirstDayOfWeek());
                if (dayOfWeekDateOnOrBefore2 - fixedDate >= getMinimalDaysInFirstWeek()) {
                    dayOfWeekDateOnOrBefore2 -= 7;
                }
                if (isFieldSet(i3, 7) && (iInternalGet = internalGet(7)) != getFirstDayOfWeek()) {
                    dayOfWeekDateOnOrBefore2 = BaseCalendar.getDayOfWeekDateOnOrBefore(dayOfWeekDateOnOrBefore2 + 6, iInternalGet);
                }
                fixedDate = dayOfWeekDateOnOrBefore2 + (7 * (internalGet(3) - 1));
            }
        }
        return fixedDate;
    }

    private GregorianCalendar getNormalizedCalendar() {
        GregorianCalendar gregorianCalendar;
        if (isFullyNormalized()) {
            gregorianCalendar = this;
        } else {
            gregorianCalendar = (GregorianCalendar) clone();
            gregorianCalendar.setLenient(true);
            gregorianCalendar.complete();
        }
        return gregorianCalendar;
    }

    private static synchronized BaseCalendar getJulianCalendarSystem() {
        if (jcal == null) {
            jcal = (JulianCalendar) CalendarSystem.forName("julian");
            jeras = jcal.getEras();
        }
        return jcal;
    }

    private BaseCalendar getCutoverCalendarSystem() {
        if (this.gregorianCutoverYearJulian < this.gregorianCutoverYear) {
            return gcal;
        }
        return getJulianCalendarSystem();
    }

    private boolean isCutoverYear(int i2) {
        return i2 == (this.calsys == gcal ? this.gregorianCutoverYear : this.gregorianCutoverYearJulian);
    }

    private long getFixedDateJan1(BaseCalendar.Date date, long j2) {
        if (!$assertionsDisabled && date.getNormalizedYear() != this.gregorianCutoverYear && date.getNormalizedYear() != this.gregorianCutoverYearJulian) {
            throw new AssertionError();
        }
        if (this.gregorianCutoverYear != this.gregorianCutoverYearJulian && j2 >= this.gregorianCutoverDate) {
            return this.gregorianCutoverDate;
        }
        return getJulianCalendarSystem().getFixedDate(date.getNormalizedYear(), 1, 1, null);
    }

    private long getFixedDateMonth1(BaseCalendar.Date date, long j2) {
        long dayOfMonth;
        if (!$assertionsDisabled && date.getNormalizedYear() != this.gregorianCutoverYear && date.getNormalizedYear() != this.gregorianCutoverYearJulian) {
            throw new AssertionError();
        }
        BaseCalendar.Date gregorianCutoverDate = getGregorianCutoverDate();
        if (gregorianCutoverDate.getMonth() == 1 && gregorianCutoverDate.getDayOfMonth() == 1) {
            return (j2 - date.getDayOfMonth()) + 1;
        }
        if (date.getMonth() == gregorianCutoverDate.getMonth()) {
            BaseCalendar.Date lastJulianDate = getLastJulianDate();
            if (this.gregorianCutoverYear == this.gregorianCutoverYearJulian && gregorianCutoverDate.getMonth() == lastJulianDate.getMonth()) {
                dayOfMonth = jcal.getFixedDate(date.getNormalizedYear(), date.getMonth(), 1, null);
            } else {
                dayOfMonth = this.gregorianCutoverDate;
            }
        } else {
            dayOfMonth = (j2 - date.getDayOfMonth()) + 1;
        }
        return dayOfMonth;
    }

    private BaseCalendar.Date getCalendarDate(long j2) {
        AbstractCalendar julianCalendarSystem = j2 >= this.gregorianCutoverDate ? gcal : getJulianCalendarSystem();
        BaseCalendar.Date date = (BaseCalendar.Date) julianCalendarSystem.newCalendarDate(TimeZone.NO_TIMEZONE);
        julianCalendarSystem.getCalendarDateFromFixedDate(date, j2);
        return date;
    }

    private BaseCalendar.Date getGregorianCutoverDate() {
        return getCalendarDate(this.gregorianCutoverDate);
    }

    private BaseCalendar.Date getLastJulianDate() {
        return getCalendarDate(this.gregorianCutoverDate - 1);
    }

    private int monthLength(int i2, int i3) {
        return isLeapYear(i3) ? LEAP_MONTH_LENGTH[i2] : MONTH_LENGTH[i2];
    }

    private int monthLength(int i2) {
        int iInternalGet = internalGet(1);
        if (internalGetEra() == 0) {
            iInternalGet = 1 - iInternalGet;
        }
        return monthLength(i2, iInternalGet);
    }

    private int actualMonthLength() {
        int normalizedYear = this.cdate.getNormalizedYear();
        if (normalizedYear != this.gregorianCutoverYear && normalizedYear != this.gregorianCutoverYearJulian) {
            return this.calsys.getMonthLength(this.cdate);
        }
        BaseCalendar.Date dateNewCalendarDate = (BaseCalendar.Date) this.cdate.clone();
        long fixedDateMonth1 = getFixedDateMonth1(dateNewCalendarDate, this.calsys.getFixedDate(dateNewCalendarDate));
        long monthLength = fixedDateMonth1 + this.calsys.getMonthLength(dateNewCalendarDate);
        if (monthLength < this.gregorianCutoverDate) {
            return (int) (monthLength - fixedDateMonth1);
        }
        if (this.cdate != this.gdate) {
            dateNewCalendarDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        }
        gcal.getCalendarDateFromFixedDate(dateNewCalendarDate, monthLength);
        return (int) (getFixedDateMonth1(dateNewCalendarDate, monthLength) - fixedDateMonth1);
    }

    private int yearLength(int i2) {
        return isLeapYear(i2) ? 366 : 365;
    }

    private int yearLength() {
        int iInternalGet = internalGet(1);
        if (internalGetEra() == 0) {
            iInternalGet = 1 - iInternalGet;
        }
        return yearLength(iInternalGet);
    }

    private void pinDayOfMonth() {
        int iMonthLength;
        int iInternalGet = internalGet(1);
        if (iInternalGet > this.gregorianCutoverYear || iInternalGet < this.gregorianCutoverYearJulian) {
            iMonthLength = monthLength(internalGet(2));
        } else {
            iMonthLength = getNormalizedCalendar().getActualMaximum(5);
        }
        if (internalGet(5) > iMonthLength) {
            set(5, iMonthLength);
        }
    }

    private long getCurrentFixedDate() {
        return this.calsys == gcal ? this.cachedFixedDate : this.calsys.getFixedDate(this.cdate);
    }

    private static int getRolledValue(int i2, int i3, int i4, int i5) {
        if (!$assertionsDisabled && (i2 < i4 || i2 > i5)) {
            throw new AssertionError();
        }
        int i6 = (i5 - i4) + 1;
        int i7 = i2 + (i3 % i6);
        if (i7 > i5) {
            i7 -= i6;
        } else if (i7 < i4) {
            i7 += i6;
        }
        if ($assertionsDisabled || (i7 >= i4 && i7 <= i5)) {
            return i7;
        }
        throw new AssertionError();
    }

    private int internalGetEra() {
        if (isSet(0)) {
            return internalGet(0);
        }
        return 1;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.gdate == null) {
            this.gdate = gcal.newCalendarDate(getZone());
            this.cachedFixedDate = Long.MIN_VALUE;
        }
        setGregorianChange(this.gregorianCutover);
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(getTimeInMillis()), getTimeZone().toZoneId());
    }

    public static GregorianCalendar from(ZonedDateTime zonedDateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone(zonedDateTime.getZone()));
        gregorianCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
        gregorianCalendar.setFirstDayOfWeek(2);
        gregorianCalendar.setMinimalDaysInFirstWeek(4);
        try {
            gregorianCalendar.setTimeInMillis(Math.addExact(Math.multiplyExact(zonedDateTime.toEpochSecond(), 1000L), zonedDateTime.get(ChronoField.MILLI_OF_SECOND)));
            return gregorianCalendar;
        } catch (ArithmeticException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
}
