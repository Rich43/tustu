package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import sun.java2d.marlin.MarlinConst;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Era;
import sun.util.calendar.Gregorian;
import sun.util.calendar.LocalGregorianCalendar;
import sun.util.calendar.ZoneInfo;
import sun.util.locale.provider.CalendarDataUtility;

/* loaded from: rt.jar:java/util/JapaneseImperialCalendar.class */
class JapaneseImperialCalendar extends Calendar {
    public static final int BEFORE_MEIJI = 0;
    public static final int MEIJI = 1;
    public static final int TAISHO = 2;
    public static final int SHOWA = 3;
    public static final int HEISEI = 4;
    private static final int REIWA = 5;
    private static final int EPOCH_OFFSET = 719163;
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_HOUR = 3600000;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_WEEK = 604800000;
    private static final LocalGregorianCalendar jcal;
    private static final Gregorian gcal;
    private static final Era BEFORE_MEIJI_ERA;
    private static final Era[] eras;
    private static final long[] sinceFixedDates;
    private static final int currentEra;
    static final int[] MIN_VALUES;
    static final int[] LEAST_MAX_VALUES;
    static final int[] MAX_VALUES;
    private static final long serialVersionUID = -3364572813905467929L;
    private transient LocalGregorianCalendar.Date jdate;
    private transient int[] zoneOffsets;
    private transient int[] originalFields;
    private transient long cachedFixedDate;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JapaneseImperialCalendar.class.desiredAssertionStatus();
        jcal = (LocalGregorianCalendar) CalendarSystem.forName("japanese");
        gcal = CalendarSystem.getGregorianCalendar();
        BEFORE_MEIJI_ERA = new Era("BeforeMeiji", "BM", Long.MIN_VALUE, false);
        MIN_VALUES = new int[]{0, -292275055, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0};
        LEAST_MAX_VALUES = new int[]{0, 0, 0, 0, 4, 28, 0, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000};
        MAX_VALUES = new int[]{0, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000};
        Era[] eras2 = jcal.getEras();
        int length = eras2.length + 1;
        eras = new Era[length];
        sinceFixedDates = new long[length];
        int i2 = 0;
        sinceFixedDates[0] = gcal.getFixedDate(BEFORE_MEIJI_ERA.getSinceDate());
        int i3 = 0 + 1;
        eras[0] = BEFORE_MEIJI_ERA;
        for (Era era : eras2) {
            if (era.getSince(TimeZone.NO_TIMEZONE) < System.currentTimeMillis()) {
                i2 = i3;
            }
            sinceFixedDates[i3] = gcal.getFixedDate(era.getSinceDate());
            int i4 = i3;
            i3++;
            eras[i4] = era;
        }
        currentEra = i2;
        int[] iArr = LEAST_MAX_VALUES;
        int[] iArr2 = MAX_VALUES;
        int length2 = eras.length - 1;
        iArr2[0] = length2;
        iArr[0] = length2;
        int iMin = Integer.MAX_VALUE;
        int iMin2 = Integer.MAX_VALUE;
        Gregorian.Date dateNewCalendarDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        for (int i5 = 1; i5 < eras.length; i5++) {
            long j2 = sinceFixedDates[i5];
            CalendarDate sinceDate = eras[i5].getSinceDate();
            dateNewCalendarDate.setDate(sinceDate.getYear(), 1, 1);
            long fixedDate = gcal.getFixedDate(dateNewCalendarDate);
            if (j2 != fixedDate) {
                iMin2 = Math.min(((int) (j2 - fixedDate)) + 1, iMin2);
            }
            dateNewCalendarDate.setDate(sinceDate.getYear(), 12, 31);
            long fixedDate2 = gcal.getFixedDate(dateNewCalendarDate);
            if (j2 != fixedDate2) {
                iMin2 = Math.min(((int) (fixedDate2 - j2)) + 1, iMin2);
            }
            LocalGregorianCalendar.Date calendarDate = getCalendarDate(j2 - 1);
            int year = calendarDate.getYear();
            if (calendarDate.getMonth() != 1 || calendarDate.getDayOfMonth() != 1) {
                year--;
            }
            iMin = Math.min(year, iMin);
        }
        LEAST_MAX_VALUES[1] = iMin;
        LEAST_MAX_VALUES[6] = iMin2;
    }

    JapaneseImperialCalendar(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
        this.cachedFixedDate = Long.MIN_VALUE;
        this.jdate = jcal.newCalendarDate(timeZone);
        setTimeInMillis(System.currentTimeMillis());
    }

    JapaneseImperialCalendar(TimeZone timeZone, Locale locale, boolean z2) {
        super(timeZone, locale);
        this.cachedFixedDate = Long.MIN_VALUE;
        this.jdate = jcal.newCalendarDate(timeZone);
    }

    @Override // java.util.Calendar
    public String getCalendarType() {
        return "japanese";
    }

    @Override // java.util.Calendar
    public boolean equals(Object obj) {
        return (obj instanceof JapaneseImperialCalendar) && super.equals(obj);
    }

    @Override // java.util.Calendar
    public int hashCode() {
        return super.hashCode() ^ this.jdate.hashCode();
    }

    @Override // java.util.Calendar
    public void add(int i2, int i3) {
        if (i3 == 0) {
            return;
        }
        if (i2 < 0 || i2 >= 15) {
            throw new IllegalArgumentException();
        }
        complete();
        if (i2 == 1) {
            LocalGregorianCalendar.Date date = (LocalGregorianCalendar.Date) this.jdate.clone();
            date.addYear(i3);
            pinDayOfMonth(date);
            set(0, getEraIndex(date));
            set(1, date.getYear());
            set(2, date.getMonth() - 1);
            set(5, date.getDayOfMonth());
            return;
        }
        if (i2 == 2) {
            LocalGregorianCalendar.Date date2 = (LocalGregorianCalendar.Date) this.jdate.clone();
            date2.addMonth(i3);
            pinDayOfMonth(date2);
            set(0, getEraIndex(date2));
            set(1, date2.getYear());
            set(2, date2.getMonth() - 1);
            set(5, date2.getDayOfMonth());
            return;
        }
        if (i2 == 0) {
            int iInternalGet = internalGet(0) + i3;
            if (iInternalGet < 0) {
                iInternalGet = 0;
            } else if (iInternalGet > eras.length - 1) {
                iInternalGet = eras.length - 1;
            }
            set(0, iInternalGet);
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
        long j4 = this.cachedFixedDate;
        long jInternalGet = ((((((j3 + internalGet(11)) * 60) + internalGet(12)) * 60) + internalGet(13)) * 1000) + internalGet(14);
        if (jInternalGet >= 86400000) {
            j4++;
            jInternalGet -= 86400000;
        } else if (jInternalGet < 0) {
            j4--;
            jInternalGet += 86400000;
        }
        long j5 = j4 + j2;
        int iInternalGet2 = internalGet(15) + internalGet(16);
        setTimeInMillis((((j5 - 719163) * 86400000) + jInternalGet) - iInternalGet2);
        int iInternalGet3 = iInternalGet2 - (internalGet(15) + internalGet(16));
        if (iInternalGet3 != 0) {
            setTimeInMillis(this.time + iInternalGet3);
            if (this.cachedFixedDate != j5) {
                setTimeInMillis(this.time - iInternalGet3);
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
            case 1:
                minimum = getActualMinimum(i2);
                maximum = getActualMaximum(i2);
                break;
            case 2:
                if (!isTransitionYear(this.jdate.getNormalizedYear())) {
                    int year = this.jdate.getYear();
                    if (year == getMaximum(1)) {
                        LocalGregorianCalendar.Date calendarDate = jcal.getCalendarDate(this.time, getZone());
                        LocalGregorianCalendar.Date calendarDate2 = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                        int month = calendarDate2.getMonth() - 1;
                        int rolledValue = getRolledValue(internalGet(i2), i3, minimum, month);
                        if (rolledValue == month) {
                            calendarDate.addYear(-400);
                            calendarDate.setMonth(rolledValue + 1);
                            if (calendarDate.getDayOfMonth() > calendarDate2.getDayOfMonth()) {
                                calendarDate.setDayOfMonth(calendarDate2.getDayOfMonth());
                                jcal.normalize(calendarDate);
                            }
                            if (calendarDate.getDayOfMonth() == calendarDate2.getDayOfMonth() && calendarDate.getTimeOfDay() > calendarDate2.getTimeOfDay()) {
                                calendarDate.setMonth(rolledValue + 1);
                                calendarDate.setDayOfMonth(calendarDate2.getDayOfMonth() - 1);
                                jcal.normalize(calendarDate);
                                rolledValue = calendarDate.getMonth() - 1;
                            }
                            set(5, calendarDate.getDayOfMonth());
                        }
                        set(2, rolledValue);
                        return;
                    }
                    if (year == getMinimum(1)) {
                        LocalGregorianCalendar.Date calendarDate3 = jcal.getCalendarDate(this.time, getZone());
                        LocalGregorianCalendar.Date calendarDate4 = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
                        int month2 = calendarDate4.getMonth() - 1;
                        int rolledValue2 = getRolledValue(internalGet(i2), i3, month2, maximum);
                        if (rolledValue2 == month2) {
                            calendarDate3.addYear(400);
                            calendarDate3.setMonth(rolledValue2 + 1);
                            if (calendarDate3.getDayOfMonth() < calendarDate4.getDayOfMonth()) {
                                calendarDate3.setDayOfMonth(calendarDate4.getDayOfMonth());
                                jcal.normalize(calendarDate3);
                            }
                            if (calendarDate3.getDayOfMonth() == calendarDate4.getDayOfMonth() && calendarDate3.getTimeOfDay() < calendarDate4.getTimeOfDay()) {
                                calendarDate3.setMonth(rolledValue2 + 1);
                                calendarDate3.setDayOfMonth(calendarDate4.getDayOfMonth() + 1);
                                jcal.normalize(calendarDate3);
                                rolledValue2 = calendarDate3.getMonth() - 1;
                            }
                            set(5, calendarDate3.getDayOfMonth());
                        }
                        set(2, rolledValue2);
                        return;
                    }
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
                int eraIndex = getEraIndex(this.jdate);
                CalendarDate sinceDate = null;
                if (this.jdate.getYear() == 1) {
                    sinceDate = eras[eraIndex].getSinceDate();
                    minimum = sinceDate.getMonth() - 1;
                } else if (eraIndex < eras.length - 1) {
                    sinceDate = eras[eraIndex + 1].getSinceDate();
                    if (sinceDate.getYear() == this.jdate.getNormalizedYear()) {
                        maximum = sinceDate.getMonth() - 1;
                        if (sinceDate.getDayOfMonth() == 1) {
                            maximum--;
                        }
                    }
                }
                if (minimum == maximum) {
                    return;
                }
                int rolledValue3 = getRolledValue(internalGet(i2), i3, minimum, maximum);
                set(2, rolledValue3);
                if (rolledValue3 == minimum) {
                    if ((sinceDate.getMonth() != 1 || sinceDate.getDayOfMonth() != 1) && this.jdate.getDayOfMonth() < sinceDate.getDayOfMonth()) {
                        set(5, sinceDate.getDayOfMonth());
                        return;
                    }
                    return;
                }
                if (rolledValue3 == maximum && sinceDate.getMonth() - 1 == rolledValue3 && this.jdate.getDayOfMonth() >= (dayOfMonth = sinceDate.getDayOfMonth())) {
                    set(5, dayOfMonth - 1);
                    return;
                }
                return;
            case 3:
                int normalizedYear = this.jdate.getNormalizedYear();
                maximum = getActualMaximum(3);
                set(7, internalGet(7));
                int iInternalGet3 = internalGet(3) + i3;
                if (!isTransitionYear(this.jdate.getNormalizedYear())) {
                    int year2 = this.jdate.getYear();
                    if (year2 == getMaximum(1)) {
                        maximum = getActualMaximum(3);
                    } else if (year2 == getMinimum(1)) {
                        minimum = getActualMinimum(3);
                        maximum = getActualMaximum(3);
                        if (iInternalGet3 > minimum && iInternalGet3 < maximum) {
                            set(3, iInternalGet3);
                            return;
                        }
                    }
                    if (iInternalGet3 > minimum && iInternalGet3 < maximum) {
                        set(3, iInternalGet3);
                        return;
                    }
                    long j2 = this.cachedFixedDate;
                    long j3 = j2 - (7 * (r0 - minimum));
                    if (year2 != getMinimum(1)) {
                        if (gcal.getYearFromFixedDate(j3) != normalizedYear) {
                            minimum++;
                        }
                    } else if (j3 < jcal.getFixedDate(jcal.getCalendarDate(Long.MIN_VALUE, getZone()))) {
                        minimum++;
                    }
                    if (gcal.getYearFromFixedDate(j2 + (7 * (maximum - internalGet(3)))) != normalizedYear) {
                        maximum--;
                        break;
                    }
                } else {
                    long j4 = this.cachedFixedDate;
                    long j5 = j4 - (7 * (r0 - minimum));
                    LocalGregorianCalendar.Date calendarDate5 = getCalendarDate(j5);
                    if (calendarDate5.getEra() != this.jdate.getEra() || calendarDate5.getYear() != this.jdate.getYear()) {
                        minimum++;
                    }
                    jcal.getCalendarDateFromFixedDate(calendarDate5, j4 + (7 * (maximum - r0)));
                    if (calendarDate5.getEra() != this.jdate.getEra() || calendarDate5.getYear() != this.jdate.getYear()) {
                        maximum--;
                    }
                    LocalGregorianCalendar.Date calendarDate6 = getCalendarDate(j5 + ((getRolledValue(r0, i3, minimum, maximum) - 1) * 7));
                    set(2, calendarDate6.getMonth() - 1);
                    set(5, calendarDate6.getDayOfMonth());
                    return;
                }
                break;
            case 4:
                boolean zIsTransitionYear = isTransitionYear(this.jdate.getNormalizedYear());
                int iInternalGet4 = internalGet(7) - getFirstDayOfWeek();
                if (iInternalGet4 < 0) {
                    iInternalGet4 += 7;
                }
                long j6 = this.cachedFixedDate;
                if (zIsTransitionYear) {
                    jInternalGet = getFixedDateMonth1(this.jdate, j6);
                    monthLength = actualMonthLength();
                } else {
                    jInternalGet = (j6 - internalGet(5)) + 1;
                    monthLength = jcal.getMonthLength(this.jdate);
                }
                long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(jInternalGet + 6, getFirstDayOfWeek());
                if (((int) (dayOfWeekDateOnOrBefore - jInternalGet)) >= getMinimalDaysInFirstWeek()) {
                    dayOfWeekDateOnOrBefore -= 7;
                }
                long rolledValue4 = dayOfWeekDateOnOrBefore + ((getRolledValue(internalGet(i2), i3, 1, getActualMaximum(i2)) - 1) * 7) + iInternalGet4;
                if (rolledValue4 < jInternalGet) {
                    rolledValue4 = jInternalGet;
                } else if (rolledValue4 >= jInternalGet + monthLength) {
                    rolledValue4 = (jInternalGet + monthLength) - 1;
                }
                set(5, getCalendarDate(rolledValue4).getDayOfMonth());
                return;
            case 5:
                if (!isTransitionYear(this.jdate.getNormalizedYear())) {
                    maximum = jcal.getMonthLength(this.jdate);
                    break;
                } else {
                    LocalGregorianCalendar.Date calendarDate7 = getCalendarDate(getFixedDateMonth1(this.jdate, this.cachedFixedDate) + getRolledValue((int) (this.cachedFixedDate - r0), i3, 0, actualMonthLength() - 1));
                    if (!$assertionsDisabled && (getEraIndex(calendarDate7) != internalGetEra() || calendarDate7.getYear() != internalGet(1) || calendarDate7.getMonth() - 1 != internalGet(2))) {
                        throw new AssertionError();
                    }
                    set(5, calendarDate7.getDayOfMonth());
                    return;
                }
            case 6:
                maximum = getActualMaximum(i2);
                if (isTransitionYear(this.jdate.getNormalizedYear())) {
                    LocalGregorianCalendar.Date calendarDate8 = getCalendarDate((this.cachedFixedDate - internalGet(6)) + getRolledValue(internalGet(6), i3, minimum, maximum));
                    if (!$assertionsDisabled && (getEraIndex(calendarDate8) != internalGetEra() || calendarDate8.getYear() != internalGet(1))) {
                        throw new AssertionError();
                    }
                    set(2, calendarDate8.getMonth() - 1);
                    set(5, calendarDate8.getDayOfMonth());
                    return;
                }
                break;
            case 7:
                int normalizedYear2 = this.jdate.getNormalizedYear();
                if (!isTransitionYear(normalizedYear2) && !isTransitionYear(normalizedYear2 - 1) && (iInternalGet = internalGet(3)) > 1 && iInternalGet < 52) {
                    set(3, internalGet(3));
                    maximum = 7;
                    break;
                } else {
                    int i4 = i3 % 7;
                    if (i4 == 0) {
                        return;
                    }
                    long j7 = this.cachedFixedDate;
                    long dayOfWeekDateOnOrBefore2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(j7, getFirstDayOfWeek());
                    long j8 = j7 + i4;
                    if (j8 < dayOfWeekDateOnOrBefore2) {
                        j8 += 7;
                    } else if (j8 >= dayOfWeekDateOnOrBefore2 + 7) {
                        j8 -= 7;
                    }
                    LocalGregorianCalendar.Date calendarDate9 = getCalendarDate(j8);
                    set(0, getEraIndex(calendarDate9));
                    set(calendarDate9.getYear(), calendarDate9.getMonth() - 1, calendarDate9.getDayOfMonth());
                    return;
                }
                break;
            case 8:
                minimum = 1;
                if (!isTransitionYear(this.jdate.getNormalizedYear())) {
                    int iInternalGet5 = internalGet(5);
                    int monthLength2 = jcal.getMonthLength(this.jdate);
                    maximum = monthLength2 / 7;
                    if ((iInternalGet5 - 1) % 7 < monthLength2 % 7) {
                        maximum++;
                    }
                    set(7, internalGet(7));
                    break;
                } else {
                    long j9 = this.cachedFixedDate;
                    long fixedDateMonth1 = getFixedDateMonth1(this.jdate, j9);
                    int iActualMonthLength = actualMonthLength();
                    int i5 = iActualMonthLength % 7;
                    int i6 = iActualMonthLength / 7;
                    int i7 = ((int) (j9 - fixedDateMonth1)) % 7;
                    if (i7 < i5) {
                        i6++;
                    }
                    set(5, getCalendarDate(fixedDateMonth1 + ((getRolledValue(internalGet(i2), i3, 1, i6) - 1) * 7) + i7).getDayOfMonth());
                    return;
                }
            case 10:
            case 11:
                int i8 = maximum + 1;
                int iInternalGet6 = (internalGet(i2) + i3) % i8;
                if (iInternalGet6 < 0) {
                    iInternalGet6 += i8;
                }
                this.time += ONE_HOUR * (iInternalGet6 - r0);
                LocalGregorianCalendar.Date calendarDate10 = jcal.getCalendarDate(this.time, getZone());
                if (internalGet(5) != calendarDate10.getDayOfMonth()) {
                    calendarDate10.setEra(this.jdate.getEra());
                    calendarDate10.setDate(internalGet(1), internalGet(2) + 1, internalGet(5));
                    if (i2 == 10) {
                        if (!$assertionsDisabled && internalGet(9) != 1) {
                            throw new AssertionError();
                        }
                        calendarDate10.addHours(12);
                    }
                    this.time = jcal.getTime(calendarDate10);
                }
                int hours = calendarDate10.getHours();
                internalSet(i2, hours % i8);
                if (i2 == 10) {
                    internalSet(11, hours);
                } else {
                    internalSet(9, hours / 12);
                    internalSet(10, hours % 12);
                }
                int zoneOffset = calendarDate10.getZoneOffset();
                int daylightSaving = calendarDate10.getDaylightSaving();
                internalSet(15, zoneOffset - daylightSaving);
                internalSet(16, daylightSaving);
                return;
        }
        set(i2, getRolledValue(internalGet(i2), i3, minimum, maximum));
    }

    @Override // java.util.Calendar
    public String getDisplayName(int i2, int i3, Locale locale) {
        if (!checkDisplayNameParams(i2, i3, 1, 4, locale, 647)) {
            return null;
        }
        int i4 = get(i2);
        if (i2 == 1 && (getBaseStyle(i3) != 2 || i4 != 1 || get(0) == 0)) {
            return null;
        }
        String strRetrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(getCalendarType(), i2, i4, i3, locale);
        if ((strRetrieveFieldValueName == null || strRetrieveFieldValueName.isEmpty()) && i2 == 0 && i4 < eras.length) {
            Era era = eras[i4];
            strRetrieveFieldValueName = i3 == 1 ? era.getAbbreviation() : era.getName();
        }
        return strRetrieveFieldValueName;
    }

    @Override // java.util.Calendar
    public Map<String, Integer> getDisplayNames(int i2, int i3, Locale locale) {
        if (!checkDisplayNameParams(i2, i3, 0, 4, locale, 647)) {
            return null;
        }
        Map<String, Integer> mapRetrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(getCalendarType(), i2, i3, locale);
        if (mapRetrieveFieldValueNames != null && i2 == 0) {
            int size = mapRetrieveFieldValueNames.size();
            if (i3 == 0) {
                HashSet hashSet = new HashSet();
                Iterator<String> it = mapRetrieveFieldValueNames.keySet().iterator();
                while (it.hasNext()) {
                    hashSet.add(mapRetrieveFieldValueNames.get(it.next()));
                }
                size = hashSet.size();
            }
            if (size < eras.length) {
                int baseStyle = getBaseStyle(i3);
                for (int i4 = size; i4 < eras.length; i4++) {
                    Era era = eras[i4];
                    if (baseStyle == 0 || baseStyle == 1 || baseStyle == 4) {
                        mapRetrieveFieldValueNames.put(era.getAbbreviation(), Integer.valueOf(i4));
                    }
                    if (baseStyle == 0 || baseStyle == 2) {
                        mapRetrieveFieldValueNames.put(era.getName(), Integer.valueOf(i4));
                    }
                }
            }
        }
        return mapRetrieveFieldValueNames;
    }

    @Override // java.util.Calendar
    public int getMinimum(int i2) {
        return MIN_VALUES[i2];
    }

    @Override // java.util.Calendar
    public int getMaximum(int i2) {
        switch (i2) {
            case 1:
                return Math.max(LEAST_MAX_VALUES[1], jcal.getCalendarDate(Long.MAX_VALUE, getZone()).getYear());
            default:
                return MAX_VALUES[i2];
        }
    }

    @Override // java.util.Calendar
    public int getGreatestMinimum(int i2) {
        if (i2 == 1) {
            return 1;
        }
        return MIN_VALUES[i2];
    }

    @Override // java.util.Calendar
    public int getLeastMaximum(int i2) {
        switch (i2) {
            case 1:
                return Math.min(LEAST_MAX_VALUES[1], getMaximum(1));
            default:
                return LEAST_MAX_VALUES[i2];
        }
    }

    @Override // java.util.Calendar
    public int getActualMinimum(int i2) {
        if (!isFieldSet(14, i2)) {
            return getMinimum(i2);
        }
        int month = 0;
        LocalGregorianCalendar.Date calendarDate = jcal.getCalendarDate(getNormalizedCalendar().getTimeInMillis(), getZone());
        int eraIndex = getEraIndex(calendarDate);
        switch (i2) {
            case 1:
                if (eraIndex > 0) {
                    month = 1;
                    CalendarDate calendarDate2 = jcal.getCalendarDate(eras[eraIndex].getSince(getZone()), getZone());
                    calendarDate.setYear(calendarDate2.getYear());
                    jcal.normalize(calendarDate);
                    if (!$assertionsDisabled && calendarDate.isLeapYear() != calendarDate2.isLeapYear()) {
                        throw new AssertionError();
                    }
                    if (getYearOffsetInMillis(calendarDate) < getYearOffsetInMillis(calendarDate2)) {
                        month = 1 + 1;
                        break;
                    }
                } else {
                    month = getMinimum(i2);
                    CalendarDate calendarDate3 = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
                    int year = calendarDate3.getYear();
                    if (year > 400) {
                        year -= 400;
                    }
                    calendarDate.setYear(year);
                    jcal.normalize(calendarDate);
                    if (getYearOffsetInMillis(calendarDate) < getYearOffsetInMillis(calendarDate3)) {
                        month++;
                        break;
                    }
                }
                break;
            case 2:
                if (eraIndex > 1 && calendarDate.getYear() == 1) {
                    LocalGregorianCalendar.Date calendarDate4 = jcal.getCalendarDate(eras[eraIndex].getSince(getZone()), getZone());
                    month = calendarDate4.getMonth() - 1;
                    if (calendarDate.getDayOfMonth() < calendarDate4.getDayOfMonth()) {
                        month++;
                        break;
                    }
                }
                break;
            case 3:
                month = 1;
                LocalGregorianCalendar.Date calendarDate5 = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
                calendarDate5.addYear(400);
                jcal.normalize(calendarDate5);
                calendarDate.setEra(calendarDate5.getEra());
                calendarDate.setYear(calendarDate5.getYear());
                jcal.normalize(calendarDate);
                long fixedDate = jcal.getFixedDate(calendarDate5);
                long fixedDate2 = jcal.getFixedDate(calendarDate) - (7 * (getWeekNumber(fixedDate, r0) - 1));
                if (fixedDate2 < fixedDate || (fixedDate2 == fixedDate && calendarDate.getTimeOfDay() < calendarDate5.getTimeOfDay())) {
                    month = 1 + 1;
                    break;
                }
                break;
        }
        return month;
    }

    @Override // java.util.Calendar
    public int getActualMaximum(int i2) {
        LocalGregorianCalendar.Date calendarDate;
        int year;
        if ((130689 & (1 << i2)) != 0) {
            return getMaximum(i2);
        }
        JapaneseImperialCalendar normalizedCalendar = getNormalizedCalendar();
        LocalGregorianCalendar.Date date = normalizedCalendar.jdate;
        date.getNormalizedYear();
        switch (i2) {
            case 1:
                LocalGregorianCalendar.Date calendarDate2 = jcal.getCalendarDate(normalizedCalendar.getTimeInMillis(), getZone());
                int eraIndex = getEraIndex(date);
                if (eraIndex == eras.length - 1) {
                    calendarDate = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                    year = calendarDate.getYear();
                    if (year > 400) {
                        calendarDate2.setYear(year - 400);
                    }
                } else {
                    calendarDate = jcal.getCalendarDate(eras[eraIndex + 1].getSince(getZone()) - 1, getZone());
                    year = calendarDate.getYear();
                    calendarDate2.setYear(year);
                }
                jcal.normalize(calendarDate2);
                if (getYearOffsetInMillis(calendarDate2) > getYearOffsetInMillis(calendarDate)) {
                    year--;
                    break;
                }
                break;
            case 2:
                year = 11;
                if (isTransitionYear(date.getNormalizedYear())) {
                    int eraIndex2 = getEraIndex(date);
                    if (date.getYear() != 1) {
                        eraIndex2++;
                        if (!$assertionsDisabled && eraIndex2 >= eras.length) {
                            throw new AssertionError();
                        }
                    }
                    long j2 = sinceFixedDates[eraIndex2];
                    if (normalizedCalendar.cachedFixedDate < j2) {
                        LocalGregorianCalendar.Date date2 = (LocalGregorianCalendar.Date) date.clone();
                        jcal.getCalendarDateFromFixedDate(date2, j2 - 1);
                        year = date2.getMonth() - 1;
                        break;
                    }
                } else {
                    LocalGregorianCalendar.Date calendarDate3 = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                    if (date.getEra() == calendarDate3.getEra() && date.getYear() == calendarDate3.getYear()) {
                        year = calendarDate3.getMonth() - 1;
                        break;
                    }
                }
                break;
            case 3:
                if (!isTransitionYear(date.getNormalizedYear())) {
                    LocalGregorianCalendar.Date calendarDate4 = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                    if (date.getEra() != calendarDate4.getEra() || date.getYear() != calendarDate4.getYear()) {
                        if (date.getEra() == null && date.getYear() == getMinimum(1)) {
                            LocalGregorianCalendar.Date calendarDate5 = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
                            calendarDate5.addYear(400);
                            jcal.normalize(calendarDate5);
                            calendarDate4.setEra(calendarDate5.getEra());
                            calendarDate4.setDate(calendarDate5.getYear() + 1, 1, 1);
                            jcal.normalize(calendarDate4);
                            long fixedDate = jcal.getFixedDate(calendarDate5);
                            long fixedDate2 = jcal.getFixedDate(calendarDate4);
                            long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(fixedDate2 + 6, getFirstDayOfWeek());
                            if (((int) (dayOfWeekDateOnOrBefore - fixedDate2)) >= getMinimalDaysInFirstWeek()) {
                                dayOfWeekDateOnOrBefore -= 7;
                            }
                            year = getWeekNumber(fixedDate, dayOfWeekDateOnOrBefore);
                            break;
                        } else {
                            Gregorian.Date dateNewCalendarDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
                            dateNewCalendarDate.setDate(date.getNormalizedYear(), 1, 1);
                            int dayOfWeek = gcal.getDayOfWeek(dateNewCalendarDate) - getFirstDayOfWeek();
                            if (dayOfWeek < 0) {
                                dayOfWeek += 7;
                            }
                            year = 52;
                            int minimalDaysInFirstWeek = (dayOfWeek + getMinimalDaysInFirstWeek()) - 1;
                            if (minimalDaysInFirstWeek == 6 || (date.isLeapYear() && (minimalDaysInFirstWeek == 5 || minimalDaysInFirstWeek == 12))) {
                                year = 52 + 1;
                                break;
                            }
                        }
                    } else {
                        long fixedDate3 = jcal.getFixedDate(calendarDate4);
                        year = getWeekNumber(getFixedDateJan1(calendarDate4, fixedDate3), fixedDate3);
                        break;
                    }
                } else {
                    if (normalizedCalendar == this) {
                        normalizedCalendar = (JapaneseImperialCalendar) normalizedCalendar.clone();
                    }
                    int actualMaximum = getActualMaximum(6);
                    normalizedCalendar.set(6, actualMaximum);
                    year = normalizedCalendar.get(3);
                    if (year == 1 && actualMaximum > 7) {
                        normalizedCalendar.add(3, -1);
                        year = normalizedCalendar.get(3);
                        break;
                    }
                }
                break;
            case 4:
                LocalGregorianCalendar.Date calendarDate6 = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                if (date.getEra() != calendarDate6.getEra() || date.getYear() != calendarDate6.getYear()) {
                    Gregorian.Date dateNewCalendarDate2 = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
                    dateNewCalendarDate2.setDate(date.getNormalizedYear(), date.getMonth(), 1);
                    int dayOfWeek2 = gcal.getDayOfWeek(dateNewCalendarDate2);
                    int iActualMonthLength = actualMonthLength();
                    int firstDayOfWeek = dayOfWeek2 - getFirstDayOfWeek();
                    if (firstDayOfWeek < 0) {
                        firstDayOfWeek += 7;
                    }
                    int i3 = 7 - firstDayOfWeek;
                    year = 3;
                    if (i3 >= getMinimalDaysInFirstWeek()) {
                        year = 3 + 1;
                    }
                    int i4 = iActualMonthLength - (i3 + 21);
                    if (i4 > 0) {
                        year++;
                        if (i4 > 7) {
                            year++;
                            break;
                        }
                    }
                } else {
                    long fixedDate4 = jcal.getFixedDate(calendarDate6);
                    year = getWeekNumber((fixedDate4 - calendarDate6.getDayOfMonth()) + 1, fixedDate4);
                    break;
                }
                break;
            case 5:
                year = jcal.getMonthLength(date);
                break;
            case 6:
                if (isTransitionYear(date.getNormalizedYear())) {
                    int eraIndex3 = getEraIndex(date);
                    if (date.getYear() != 1) {
                        eraIndex3++;
                        if (!$assertionsDisabled && eraIndex3 >= eras.length) {
                            throw new AssertionError();
                        }
                    }
                    long j3 = sinceFixedDates[eraIndex3];
                    long j4 = normalizedCalendar.cachedFixedDate;
                    Gregorian.Date dateNewCalendarDate3 = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
                    dateNewCalendarDate3.setDate(date.getNormalizedYear(), 1, 1);
                    if (j4 < j3) {
                        year = (int) (j3 - gcal.getFixedDate(dateNewCalendarDate3));
                        break;
                    } else {
                        dateNewCalendarDate3.addYear(1);
                        year = (int) (gcal.getFixedDate(dateNewCalendarDate3) - j3);
                        break;
                    }
                } else {
                    LocalGregorianCalendar.Date calendarDate7 = jcal.getCalendarDate(Long.MAX_VALUE, getZone());
                    if (date.getEra() != calendarDate7.getEra() || date.getYear() != calendarDate7.getYear()) {
                        if (date.getYear() == getMinimum(1)) {
                            LocalGregorianCalendar.Date calendarDate8 = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
                            long fixedDate5 = jcal.getFixedDate(calendarDate8);
                            calendarDate8.addYear(1);
                            calendarDate8.setMonth(1).setDayOfMonth(1);
                            jcal.normalize(calendarDate8);
                            year = (int) (jcal.getFixedDate(calendarDate8) - fixedDate5);
                            break;
                        } else {
                            year = jcal.getYearLength(date);
                            break;
                        }
                    } else {
                        long fixedDate6 = jcal.getFixedDate(calendarDate7);
                        year = ((int) (fixedDate6 - getFixedDateJan1(calendarDate7, fixedDate6))) + 1;
                        break;
                    }
                }
                break;
            case 7:
            default:
                throw new ArrayIndexOutOfBoundsException(i2);
            case 8:
                int dayOfWeek3 = date.getDayOfWeek();
                BaseCalendar.Date date3 = (BaseCalendar.Date) date.clone();
                int monthLength = jcal.getMonthLength(date3);
                date3.setDayOfMonth(1);
                jcal.normalize(date3);
                int dayOfWeek4 = dayOfWeek3 - date3.getDayOfWeek();
                if (dayOfWeek4 < 0) {
                    dayOfWeek4 += 7;
                }
                year = ((monthLength - dayOfWeek4) + 6) / 7;
                break;
        }
        return year;
    }

    private long getYearOffsetInMillis(CalendarDate calendarDate) {
        return (((jcal.getDayOfYear(calendarDate) - 1) * 86400000) + calendarDate.getTimeOfDay()) - calendarDate.getZoneOffset();
    }

    @Override // java.util.Calendar
    public Object clone() {
        JapaneseImperialCalendar japaneseImperialCalendar = (JapaneseImperialCalendar) super.clone();
        japaneseImperialCalendar.jdate = (LocalGregorianCalendar.Date) this.jdate.clone();
        japaneseImperialCalendar.originalFields = null;
        japaneseImperialCalendar.zoneOffsets = null;
        return japaneseImperialCalendar;
    }

    @Override // java.util.Calendar
    public TimeZone getTimeZone() {
        TimeZone timeZone = super.getTimeZone();
        this.jdate.setZone(timeZone);
        return timeZone;
    }

    @Override // java.util.Calendar
    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        this.jdate.setZone(timeZone);
    }

    @Override // java.util.Calendar
    protected void computeFields() {
        int setStateFields;
        if (isPartiallyNormalized()) {
            setStateFields = getSetStateFields();
            int i2 = (setStateFields ^ (-1)) & 131071;
            if (i2 != 0 || this.cachedFixedDate == Long.MIN_VALUE) {
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
        int dayOfYear;
        long fixedDate;
        long fixedDate2;
        long fixedDate3;
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
        if (j3 != this.cachedFixedDate || j3 < 0) {
            jcal.getCalendarDateFromFixedDate(this.jdate, j3);
            this.cachedFixedDate = j3;
        }
        int eraIndex = getEraIndex(this.jdate);
        int year = this.jdate.getYear();
        internalSet(0, eraIndex);
        internalSet(1, year);
        int i5 = i2 | 3;
        int month = this.jdate.getMonth() - 1;
        int dayOfMonth = this.jdate.getDayOfMonth();
        if ((i2 & 164) != 0) {
            internalSet(2, month);
            internalSet(5, dayOfMonth);
            internalSet(7, this.jdate.getDayOfWeek());
            i5 |= 164;
        }
        if ((i2 & 32256) != 0) {
            if (i4 != 0) {
                int i6 = i4 / ONE_HOUR;
                internalSet(11, i6);
                internalSet(9, i6 / 12);
                internalSet(10, i6 % 12);
                int i7 = i4 % ONE_HOUR;
                internalSet(12, i7 / ONE_MINUTE);
                int i8 = i7 % ONE_MINUTE;
                internalSet(13, i8 / 1000);
                internalSet(14, i8 % 1000);
            } else {
                internalSet(11, 0);
                internalSet(9, 0);
                internalSet(10, 0);
                internalSet(12, 0);
                internalSet(13, 0);
                internalSet(14, 0);
            }
            i5 |= 32256;
        }
        if ((i2 & MarlinConst.INITIAL_EDGES_CAPACITY) != 0) {
            internalSet(15, this.zoneOffsets[0]);
            internalSet(16, this.zoneOffsets[1]);
            i5 |= MarlinConst.INITIAL_EDGES_CAPACITY;
        }
        if ((i2 & 344) != 0) {
            int normalizedYear = this.jdate.getNormalizedYear();
            boolean zIsTransitionYear = isTransitionYear(this.jdate.getNormalizedYear());
            if (zIsTransitionYear) {
                fixedDate = getFixedDateJan1(this.jdate, j3);
                dayOfYear = ((int) (j3 - fixedDate)) + 1;
            } else if (normalizedYear == MIN_VALUES[1]) {
                fixedDate = jcal.getFixedDate(jcal.getCalendarDate(Long.MIN_VALUE, getZone()));
                dayOfYear = ((int) (j3 - fixedDate)) + 1;
            } else {
                dayOfYear = (int) jcal.getDayOfYear(this.jdate);
                fixedDate = (j3 - dayOfYear) + 1;
            }
            long fixedDateMonth1 = zIsTransitionYear ? getFixedDateMonth1(this.jdate, j3) : (j3 - dayOfMonth) + 1;
            internalSet(6, dayOfYear);
            internalSet(8, ((dayOfMonth - 1) / 7) + 1);
            int weekNumber = getWeekNumber(fixedDate, j3);
            if (weekNumber == 0) {
                long j4 = fixedDate - 1;
                LocalGregorianCalendar.Date calendarDate = getCalendarDate(j4);
                if (!zIsTransitionYear && !isTransitionYear(calendarDate.getNormalizedYear())) {
                    fixedDate3 = fixedDate - 365;
                    if (calendarDate.isLeapYear()) {
                        fixedDate3--;
                    }
                } else if (zIsTransitionYear) {
                    if (this.jdate.getYear() == 1) {
                        if (eraIndex > 5) {
                            CalendarDate sinceDate = eras[eraIndex - 1].getSinceDate();
                            if (normalizedYear == sinceDate.getYear()) {
                                calendarDate.setMonth(sinceDate.getMonth()).setDayOfMonth(sinceDate.getDayOfMonth());
                            }
                        } else {
                            calendarDate.setMonth(1).setDayOfMonth(1);
                        }
                        jcal.normalize(calendarDate);
                        fixedDate3 = jcal.getFixedDate(calendarDate);
                    } else {
                        fixedDate3 = fixedDate - 365;
                        if (calendarDate.isLeapYear()) {
                            fixedDate3--;
                        }
                    }
                } else {
                    CalendarDate sinceDate2 = eras[getEraIndex(this.jdate)].getSinceDate();
                    calendarDate.setMonth(sinceDate2.getMonth()).setDayOfMonth(sinceDate2.getDayOfMonth());
                    jcal.normalize(calendarDate);
                    fixedDate3 = jcal.getFixedDate(calendarDate);
                }
                weekNumber = getWeekNumber(fixedDate3, j4);
            } else if (!zIsTransitionYear) {
                if (weekNumber >= 52) {
                    long j5 = fixedDate + 365;
                    if (this.jdate.isLeapYear()) {
                        j5++;
                    }
                    long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(j5 + 6, getFirstDayOfWeek());
                    if (((int) (dayOfWeekDateOnOrBefore - j5)) >= getMinimalDaysInFirstWeek() && j3 >= dayOfWeekDateOnOrBefore - 7) {
                        weekNumber = 1;
                    }
                }
            } else {
                LocalGregorianCalendar.Date date = (LocalGregorianCalendar.Date) this.jdate.clone();
                if (this.jdate.getYear() == 1) {
                    date.addYear(1);
                    date.setMonth(1).setDayOfMonth(1);
                    fixedDate2 = jcal.getFixedDate(date);
                } else {
                    int eraIndex2 = getEraIndex(date) + 1;
                    CalendarDate sinceDate3 = eras[eraIndex2].getSinceDate();
                    date.setEra(eras[eraIndex2]);
                    date.setDate(1, sinceDate3.getMonth(), sinceDate3.getDayOfMonth());
                    jcal.normalize(date);
                    fixedDate2 = jcal.getFixedDate(date);
                }
                long dayOfWeekDateOnOrBefore2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(fixedDate2 + 6, getFirstDayOfWeek());
                if (((int) (dayOfWeekDateOnOrBefore2 - fixedDate2)) >= getMinimalDaysInFirstWeek() && j3 >= dayOfWeekDateOnOrBefore2 - 7) {
                    weekNumber = 1;
                }
            }
            internalSet(3, weekNumber);
            internalSet(4, getWeekNumber(fixedDateMonth1, j3));
            i5 |= 344;
        }
        return i5;
    }

    private int getWeekNumber(long j2, long j3) {
        long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(j2 + 6, getFirstDayOfWeek());
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

    @Override // java.util.Calendar
    protected void computeTime() {
        int iInternalGet;
        int iInternalGet2;
        long jInternalGet;
        if (!isLenient()) {
            if (this.originalFields == null) {
                this.originalFields = new int[17];
            }
            for (int i2 = 0; i2 < 17; i2++) {
                int iInternalGet3 = internalGet(i2);
                if (isExternallySet(i2) && (iInternalGet3 < getMinimum(i2) || iInternalGet3 > getMaximum(i2))) {
                    throw new IllegalArgumentException(getFieldName(i2));
                }
                this.originalFields[i2] = iInternalGet3;
            }
        }
        int iSelectFields = selectFields();
        if (isSet(0)) {
            iInternalGet = internalGet(0);
            iInternalGet2 = isSet(1) ? internalGet(1) : 1;
        } else if (isSet(1)) {
            iInternalGet = currentEra;
            iInternalGet2 = internalGet(1);
        } else {
            iInternalGet = 3;
            iInternalGet2 = 45;
        }
        if (isFieldSet(iSelectFields, 11)) {
            jInternalGet = 0 + internalGet(11);
        } else {
            jInternalGet = 0 + internalGet(10);
            if (isFieldSet(iSelectFields, 9)) {
                jInternalGet += 12 * internalGet(9);
            }
        }
        long jInternalGet2 = (((((jInternalGet * 60) + internalGet(12)) * 60) + internalGet(13)) * 1000) + internalGet(14);
        long j2 = jInternalGet2 / 86400000;
        long j3 = jInternalGet2 % 86400000;
        while (j3 < 0) {
            j3 += 86400000;
            j2--;
        }
        long fixedDate = (((j2 + getFixedDate(iInternalGet, iInternalGet2, iSelectFields)) - 719163) * 86400000) + j3;
        TimeZone zone = getZone();
        if (this.zoneOffsets == null) {
            this.zoneOffsets = new int[2];
        }
        int i3 = iSelectFields & MarlinConst.INITIAL_EDGES_CAPACITY;
        if (i3 != 98304) {
            if (zone instanceof ZoneInfo) {
                ((ZoneInfo) zone).getOffsetsByWall(fixedDate, this.zoneOffsets);
            } else {
                zone.getOffsets(fixedDate - zone.getRawOffset(), this.zoneOffsets);
            }
        }
        if (i3 != 0) {
            if (isFieldSet(i3, 15)) {
                this.zoneOffsets[0] = internalGet(15);
            }
            if (isFieldSet(i3, 16)) {
                this.zoneOffsets[1] = internalGet(16);
            }
        }
        this.time = fixedDate - (this.zoneOffsets[0] + this.zoneOffsets[1]);
        int iComputeFields = computeFields(iSelectFields | getSetStateFields(), i3);
        if (!isLenient()) {
            for (int i4 = 0; i4 < 17; i4++) {
                if (isExternallySet(i4) && this.originalFields[i4] != internalGet(i4)) {
                    int iInternalGet4 = internalGet(i4);
                    System.arraycopy(this.originalFields, 0, this.fields, 0, this.fields.length);
                    throw new IllegalArgumentException(getFieldName(i4) + "=" + iInternalGet4 + ", expected " + this.originalFields[i4]);
                }
            }
        }
        setFieldsNormalized(iComputeFields);
    }

    private long getFixedDate(int i2, int i3, int i4) {
        int iInternalGet;
        int firstDayOfWeek;
        int iInternalGet2;
        int month = 0;
        int dayOfMonth = 1;
        if (isFieldSet(i4, 2)) {
            month = internalGet(2);
            if (month > 11) {
                i3 += month / 12;
                month %= 12;
            } else if (month < 0) {
                int[] iArr = new int[1];
                i3 += CalendarUtils.floorDivide(month, 12, iArr);
                month = iArr[0];
            }
        } else if (i3 == 1 && i2 != 0) {
            CalendarDate sinceDate = eras[i2].getSinceDate();
            month = sinceDate.getMonth() - 1;
            dayOfMonth = sinceDate.getDayOfMonth();
        }
        if (i3 == MIN_VALUES[1]) {
            LocalGregorianCalendar.Date calendarDate = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
            int month2 = calendarDate.getMonth() - 1;
            if (month < month2) {
                month = month2;
            }
            if (month == month2) {
                dayOfMonth = calendarDate.getDayOfMonth();
            }
        }
        LocalGregorianCalendar.Date dateNewCalendarDate = jcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        dateNewCalendarDate.setEra(i2 > 0 ? eras[i2] : null);
        dateNewCalendarDate.setDate(i3, month + 1, dayOfMonth);
        jcal.normalize(dateNewCalendarDate);
        long fixedDate = jcal.getFixedDate(dateNewCalendarDate);
        if (isFieldSet(i4, 2)) {
            if (isFieldSet(i4, 5)) {
                if (isSet(5)) {
                    fixedDate = (fixedDate + internalGet(5)) - dayOfMonth;
                }
            } else if (isFieldSet(i4, 4)) {
                long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(fixedDate + 6, getFirstDayOfWeek());
                if (dayOfWeekDateOnOrBefore - fixedDate >= getMinimalDaysInFirstWeek()) {
                    dayOfWeekDateOnOrBefore -= 7;
                }
                if (isFieldSet(i4, 7)) {
                    dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(dayOfWeekDateOnOrBefore + 6, internalGet(7));
                }
                fixedDate = dayOfWeekDateOnOrBefore + (7 * (internalGet(4) - 1));
            } else {
                if (isFieldSet(i4, 7)) {
                    firstDayOfWeek = internalGet(7);
                } else {
                    firstDayOfWeek = getFirstDayOfWeek();
                }
                if (isFieldSet(i4, 8)) {
                    iInternalGet2 = internalGet(8);
                } else {
                    iInternalGet2 = 1;
                }
                if (iInternalGet2 >= 0) {
                    fixedDate = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore((fixedDate + (7 * iInternalGet2)) - 1, firstDayOfWeek);
                } else {
                    fixedDate = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore((fixedDate + (monthLength(month, i3) + (7 * (iInternalGet2 + 1)))) - 1, firstDayOfWeek);
                }
            }
        } else if (isFieldSet(i4, 6)) {
            if (isTransitionYear(dateNewCalendarDate.getNormalizedYear())) {
                fixedDate = getFixedDateJan1(dateNewCalendarDate, fixedDate);
            }
            fixedDate = (fixedDate + internalGet(6)) - 1;
        } else {
            long dayOfWeekDateOnOrBefore2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(fixedDate + 6, getFirstDayOfWeek());
            if (dayOfWeekDateOnOrBefore2 - fixedDate >= getMinimalDaysInFirstWeek()) {
                dayOfWeekDateOnOrBefore2 -= 7;
            }
            if (isFieldSet(i4, 7) && (iInternalGet = internalGet(7)) != getFirstDayOfWeek()) {
                dayOfWeekDateOnOrBefore2 = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(dayOfWeekDateOnOrBefore2 + 6, iInternalGet);
            }
            fixedDate = dayOfWeekDateOnOrBefore2 + (7 * (internalGet(3) - 1));
        }
        return fixedDate;
    }

    private long getFixedDateJan1(LocalGregorianCalendar.Date date, long j2) {
        date.getEra();
        if (date.getEra() != null && date.getYear() == 1) {
            for (int eraIndex = getEraIndex(date); eraIndex > 0; eraIndex--) {
                long fixedDate = gcal.getFixedDate(eras[eraIndex].getSinceDate());
                if (fixedDate <= j2) {
                    return fixedDate;
                }
            }
        }
        Gregorian.Date dateNewCalendarDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        dateNewCalendarDate.setDate(date.getNormalizedYear(), 1, 1);
        return gcal.getFixedDate(dateNewCalendarDate);
    }

    private long getFixedDateMonth1(LocalGregorianCalendar.Date date, long j2) {
        int transitionEraIndex = getTransitionEraIndex(date);
        if (transitionEraIndex != -1) {
            long j3 = sinceFixedDates[transitionEraIndex];
            if (j3 <= j2) {
                return j3;
            }
        }
        return (j2 - date.getDayOfMonth()) + 1;
    }

    private static LocalGregorianCalendar.Date getCalendarDate(long j2) {
        LocalGregorianCalendar.Date dateNewCalendarDate = jcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        jcal.getCalendarDateFromFixedDate(dateNewCalendarDate, j2);
        return dateNewCalendarDate;
    }

    private int monthLength(int i2, int i3) {
        return CalendarUtils.isGregorianLeapYear(i3) ? GregorianCalendar.LEAP_MONTH_LENGTH[i2] : GregorianCalendar.MONTH_LENGTH[i2];
    }

    private int monthLength(int i2) {
        if ($assertionsDisabled || this.jdate.isNormalized()) {
            return this.jdate.isLeapYear() ? GregorianCalendar.LEAP_MONTH_LENGTH[i2] : GregorianCalendar.MONTH_LENGTH[i2];
        }
        throw new AssertionError();
    }

    private int actualMonthLength() {
        int monthLength = jcal.getMonthLength(this.jdate);
        int transitionEraIndex = getTransitionEraIndex(this.jdate);
        if (transitionEraIndex != -1) {
            long j2 = sinceFixedDates[transitionEraIndex];
            CalendarDate sinceDate = eras[transitionEraIndex].getSinceDate();
            if (j2 <= this.cachedFixedDate) {
                monthLength -= sinceDate.getDayOfMonth() - 1;
            } else {
                monthLength = sinceDate.getDayOfMonth() - 1;
            }
        }
        return monthLength;
    }

    private static int getTransitionEraIndex(LocalGregorianCalendar.Date date) {
        int eraIndex = getEraIndex(date);
        CalendarDate sinceDate = eras[eraIndex].getSinceDate();
        if (sinceDate.getYear() == date.getNormalizedYear() && sinceDate.getMonth() == date.getMonth()) {
            return eraIndex;
        }
        if (eraIndex < eras.length - 1) {
            int i2 = eraIndex + 1;
            CalendarDate sinceDate2 = eras[i2].getSinceDate();
            if (sinceDate2.getYear() == date.getNormalizedYear() && sinceDate2.getMonth() == date.getMonth()) {
                return i2;
            }
            return -1;
        }
        return -1;
    }

    private boolean isTransitionYear(int i2) {
        for (int length = eras.length - 1; length > 0; length--) {
            int year = eras[length].getSinceDate().getYear();
            if (i2 == year) {
                return true;
            }
            if (i2 > year) {
                return false;
            }
        }
        return false;
    }

    private static int getEraIndex(LocalGregorianCalendar.Date date) {
        Era era = date.getEra();
        for (int length = eras.length - 1; length > 0; length--) {
            if (eras[length] == era) {
                return length;
            }
        }
        return 0;
    }

    private JapaneseImperialCalendar getNormalizedCalendar() {
        JapaneseImperialCalendar japaneseImperialCalendar;
        if (isFullyNormalized()) {
            japaneseImperialCalendar = this;
        } else {
            japaneseImperialCalendar = (JapaneseImperialCalendar) clone();
            japaneseImperialCalendar.setLenient(true);
            japaneseImperialCalendar.complete();
        }
        return japaneseImperialCalendar;
    }

    private void pinDayOfMonth(LocalGregorianCalendar.Date date) {
        int year = date.getYear();
        int dayOfMonth = date.getDayOfMonth();
        if (year != getMinimum(1)) {
            date.setDayOfMonth(1);
            jcal.normalize(date);
            int monthLength = jcal.getMonthLength(date);
            if (dayOfMonth > monthLength) {
                date.setDayOfMonth(monthLength);
            } else {
                date.setDayOfMonth(dayOfMonth);
            }
            jcal.normalize(date);
            return;
        }
        LocalGregorianCalendar.Date calendarDate = jcal.getCalendarDate(Long.MIN_VALUE, getZone());
        LocalGregorianCalendar.Date calendarDate2 = jcal.getCalendarDate(this.time, getZone());
        long timeOfDay = calendarDate2.getTimeOfDay();
        calendarDate2.addYear(400);
        calendarDate2.setMonth(date.getMonth());
        calendarDate2.setDayOfMonth(1);
        jcal.normalize(calendarDate2);
        int monthLength2 = jcal.getMonthLength(calendarDate2);
        if (dayOfMonth > monthLength2) {
            calendarDate2.setDayOfMonth(monthLength2);
        } else if (dayOfMonth < calendarDate.getDayOfMonth()) {
            calendarDate2.setDayOfMonth(calendarDate.getDayOfMonth());
        } else {
            calendarDate2.setDayOfMonth(dayOfMonth);
        }
        if (calendarDate2.getDayOfMonth() == calendarDate.getDayOfMonth() && timeOfDay < calendarDate.getTimeOfDay()) {
            calendarDate2.setDayOfMonth(Math.min(dayOfMonth + 1, monthLength2));
        }
        date.setDate(year, calendarDate2.getMonth(), calendarDate2.getDayOfMonth());
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
        return isSet(0) ? internalGet(0) : currentEra;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.jdate == null) {
            this.jdate = jcal.newCalendarDate(getZone());
            this.cachedFixedDate = Long.MIN_VALUE;
        }
    }
}
