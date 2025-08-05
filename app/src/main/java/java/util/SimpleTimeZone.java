package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Gregorian;

/* loaded from: rt.jar:java/util/SimpleTimeZone.class */
public class SimpleTimeZone extends TimeZone {
    private int startMonth;
    private int startDay;
    private int startDayOfWeek;
    private int startTime;
    private int startTimeMode;
    private int endMonth;
    private int endDay;
    private int endDayOfWeek;
    private int endTime;
    private int endTimeMode;
    private int startYear;
    private int rawOffset;
    private boolean useDaylight;
    private static final int millisPerHour = 3600000;
    private static final int millisPerDay = 86400000;
    private final byte[] monthLength;
    private int startMode;
    private int endMode;
    private int dstSavings;
    private transient long cacheYear;
    private transient long cacheStart;
    private transient long cacheEnd;
    private static final int DOM_MODE = 1;
    private static final int DOW_IN_MONTH_MODE = 2;
    private static final int DOW_GE_DOM_MODE = 3;
    private static final int DOW_LE_DOM_MODE = 4;
    public static final int WALL_TIME = 0;
    public static final int STANDARD_TIME = 1;
    public static final int UTC_TIME = 2;
    static final long serialVersionUID = -403250971215465050L;
    static final int currentSerialVersion = 2;
    private int serialVersionOnStream;
    private static final int MAX_RULE_NUM = 6;
    private static final byte[] staticMonthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final byte[] staticLeapMonthLength = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final Gregorian gcal = CalendarSystem.getGregorianCalendar();

    public SimpleTimeZone(int i2, String str) {
        this.useDaylight = false;
        this.monthLength = staticMonthLength;
        this.serialVersionOnStream = 2;
        this.rawOffset = i2;
        setID(str);
        this.dstSavings = millisPerHour;
    }

    public SimpleTimeZone(int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        this(i2, str, i3, i4, i5, i6, 0, i7, i8, i9, i10, 0, millisPerHour);
    }

    public SimpleTimeZone(int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        this(i2, str, i3, i4, i5, i6, 0, i7, i8, i9, i10, 0, i11);
    }

    public SimpleTimeZone(int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13) {
        this.useDaylight = false;
        this.monthLength = staticMonthLength;
        this.serialVersionOnStream = 2;
        setID(str);
        this.rawOffset = i2;
        this.startMonth = i3;
        this.startDay = i4;
        this.startDayOfWeek = i5;
        this.startTime = i6;
        this.startTimeMode = i7;
        this.endMonth = i8;
        this.endDay = i9;
        this.endDayOfWeek = i10;
        this.endTime = i11;
        this.endTimeMode = i12;
        this.dstSavings = i13;
        decodeRules();
        if (i13 <= 0) {
            throw new IllegalArgumentException("Illegal daylight saving value: " + i13);
        }
    }

    public void setStartYear(int i2) {
        this.startYear = i2;
        invalidateCache();
    }

    public void setStartRule(int i2, int i3, int i4, int i5) {
        this.startMonth = i2;
        this.startDay = i3;
        this.startDayOfWeek = i4;
        this.startTime = i5;
        this.startTimeMode = 0;
        decodeStartRule();
        invalidateCache();
    }

    public void setStartRule(int i2, int i3, int i4) {
        setStartRule(i2, i3, 0, i4);
    }

    public void setStartRule(int i2, int i3, int i4, int i5, boolean z2) {
        if (z2) {
            setStartRule(i2, i3, -i4, i5);
        } else {
            setStartRule(i2, -i3, -i4, i5);
        }
    }

    public void setEndRule(int i2, int i3, int i4, int i5) {
        this.endMonth = i2;
        this.endDay = i3;
        this.endDayOfWeek = i4;
        this.endTime = i5;
        this.endTimeMode = 0;
        decodeEndRule();
        invalidateCache();
    }

    public void setEndRule(int i2, int i3, int i4) {
        setEndRule(i2, i3, 0, i4);
    }

    public void setEndRule(int i2, int i3, int i4, int i5, boolean z2) {
        if (z2) {
            setEndRule(i2, i3, -i4, i5);
        } else {
            setEndRule(i2, -i3, -i4, i5);
        }
    }

    @Override // java.util.TimeZone
    public int getOffset(long j2) {
        return getOffsets(j2, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.TimeZone
    int getOffsets(long j2, int[] iArr) {
        BaseCalendar baseCalendar;
        int offset = this.rawOffset;
        if (this.useDaylight) {
            baseCalendar = this;
            synchronized (this) {
                try {
                    if (this.cacheStart != 0 && j2 >= this.cacheStart && j2 < this.cacheEnd) {
                        offset += this.dstSavings;
                    } else {
                        baseCalendar = j2 >= -12219292800000L ? gcal : (BaseCalendar) CalendarSystem.forName("julian");
                        BaseCalendar.Date date = (BaseCalendar.Date) baseCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
                        baseCalendar.getCalendarDate(j2 + this.rawOffset, date);
                        int normalizedYear = date.getNormalizedYear();
                        if (normalizedYear >= this.startYear) {
                            date.setTimeOfDay(0, 0, 0, 0);
                            offset = getOffset(baseCalendar, date, normalizedYear, j2);
                        }
                    }
                } finally {
                }
            }
        }
        if (iArr != null) {
            iArr[0] = this.rawOffset;
            iArr[1] = baseCalendar - this.rawOffset;
        }
        return baseCalendar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v44, types: [sun.util.calendar.BaseCalendar] */
    @Override // java.util.TimeZone
    public int getOffset(int i2, int i3, int i4, int i5, int i6, int i7) {
        if (i2 != 1 && i2 != 0) {
            throw new IllegalArgumentException("Illegal era " + i2);
        }
        int iMod = i3;
        if (i2 == 0) {
            iMod = 1 - iMod;
        }
        if (iMod >= 292278994) {
            iMod = 2800 + (iMod % 2800);
        } else if (iMod <= -292269054) {
            iMod = (int) CalendarUtils.mod(iMod, 28L);
        }
        int i8 = i4 + 1;
        Gregorian gregorian = gcal;
        BaseCalendar.Date date = (BaseCalendar.Date) gregorian.newCalendarDate(TimeZone.NO_TIMEZONE);
        date.setDate(iMod, i8, i5);
        long time = gregorian.getTime(date) + (i7 - this.rawOffset);
        if (time < -12219292800000L) {
            gregorian = (BaseCalendar) CalendarSystem.forName("julian");
            date = (BaseCalendar.Date) gregorian.newCalendarDate(TimeZone.NO_TIMEZONE);
            date.setNormalizedDate(iMod, i8, i5);
            time = (gregorian.getTime(date) + i7) - this.rawOffset;
        }
        if (date.getNormalizedYear() != iMod || date.getMonth() != i8 || date.getDayOfMonth() != i5 || i6 < 1 || i6 > 7 || i7 < 0 || i7 >= millisPerDay) {
            throw new IllegalArgumentException();
        }
        if (!this.useDaylight || i3 < this.startYear || i2 != 1) {
            return this.rawOffset;
        }
        return getOffset(gregorian, date, iMod, time);
    }

    private int getOffset(BaseCalendar baseCalendar, BaseCalendar.Date date, int i2, long j2) {
        synchronized (this) {
            if (this.cacheStart != 0) {
                if (j2 >= this.cacheStart && j2 < this.cacheEnd) {
                    return this.rawOffset + this.dstSavings;
                }
                if (i2 == this.cacheYear) {
                    return this.rawOffset;
                }
            }
            long start = getStart(baseCalendar, date, i2);
            long end = getEnd(baseCalendar, date, i2);
            int i3 = this.rawOffset;
            if (start <= end) {
                if (j2 >= start && j2 < end) {
                    i3 += this.dstSavings;
                }
                synchronized (this) {
                    this.cacheYear = i2;
                    this.cacheStart = start;
                    this.cacheEnd = end;
                }
            } else {
                if (j2 < end) {
                    start = getStart(baseCalendar, date, i2 - 1);
                    if (j2 >= start) {
                        i3 += this.dstSavings;
                    }
                } else if (j2 >= start) {
                    end = getEnd(baseCalendar, date, i2 + 1);
                    if (j2 < end) {
                        i3 += this.dstSavings;
                    }
                }
                if (start <= end) {
                    synchronized (this) {
                        this.cacheYear = this.startYear - 1;
                        this.cacheStart = start;
                        this.cacheEnd = end;
                    }
                }
            }
            return i3;
        }
    }

    private long getStart(BaseCalendar baseCalendar, BaseCalendar.Date date, int i2) {
        int i3 = this.startTime;
        if (this.startTimeMode != 2) {
            i3 -= this.rawOffset;
        }
        return getTransition(baseCalendar, date, this.startMode, i2, this.startMonth, this.startDay, this.startDayOfWeek, i3);
    }

    private long getEnd(BaseCalendar baseCalendar, BaseCalendar.Date date, int i2) {
        int i3 = this.endTime;
        if (this.endTimeMode != 2) {
            i3 -= this.rawOffset;
        }
        if (this.endTimeMode == 0) {
            i3 -= this.dstSavings;
        }
        return getTransition(baseCalendar, date, this.endMode, i2, this.endMonth, this.endDay, this.endDayOfWeek, i3);
    }

    private long getTransition(BaseCalendar baseCalendar, BaseCalendar.Date date, int i2, int i3, int i4, int i5, int i6, int i7) {
        date.setNormalizedYear(i3);
        date.setMonth(i4 + 1);
        switch (i2) {
            case 1:
                date.setDayOfMonth(i5);
                break;
            case 2:
                date.setDayOfMonth(1);
                if (i5 < 0) {
                    date.setDayOfMonth(baseCalendar.getMonthLength(date));
                }
                date = (BaseCalendar.Date) baseCalendar.getNthDayOfWeek(i5, i6, date);
                break;
            case 3:
                date.setDayOfMonth(i5);
                date = (BaseCalendar.Date) baseCalendar.getNthDayOfWeek(1, i6, date);
                break;
            case 4:
                date.setDayOfMonth(i5);
                date = (BaseCalendar.Date) baseCalendar.getNthDayOfWeek(-1, i6, date);
                break;
        }
        return baseCalendar.getTime(date) + i7;
    }

    @Override // java.util.TimeZone
    public int getRawOffset() {
        return this.rawOffset;
    }

    @Override // java.util.TimeZone
    public void setRawOffset(int i2) {
        this.rawOffset = i2;
    }

    public void setDSTSavings(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Illegal daylight saving value: " + i2);
        }
        this.dstSavings = i2;
    }

    @Override // java.util.TimeZone
    public int getDSTSavings() {
        if (this.useDaylight) {
            return this.dstSavings;
        }
        return 0;
    }

    @Override // java.util.TimeZone
    public boolean useDaylightTime() {
        return this.useDaylight;
    }

    @Override // java.util.TimeZone
    public boolean observesDaylightTime() {
        return useDaylightTime();
    }

    @Override // java.util.TimeZone
    public boolean inDaylightTime(Date date) {
        return getOffset(date.getTime()) != this.rawOffset;
    }

    @Override // java.util.TimeZone
    public Object clone() {
        return super.clone();
    }

    public synchronized int hashCode() {
        return (((((((this.startMonth ^ this.startDay) ^ this.startDayOfWeek) ^ this.startTime) ^ this.endMonth) ^ this.endDay) ^ this.endDayOfWeek) ^ this.endTime) ^ this.rawOffset;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SimpleTimeZone)) {
            return false;
        }
        SimpleTimeZone simpleTimeZone = (SimpleTimeZone) obj;
        return getID().equals(simpleTimeZone.getID()) && hasSameRules(simpleTimeZone);
    }

    @Override // java.util.TimeZone
    public boolean hasSameRules(TimeZone timeZone) {
        if (this == timeZone) {
            return true;
        }
        if (!(timeZone instanceof SimpleTimeZone)) {
            return false;
        }
        SimpleTimeZone simpleTimeZone = (SimpleTimeZone) timeZone;
        return this.rawOffset == simpleTimeZone.rawOffset && this.useDaylight == simpleTimeZone.useDaylight && (!this.useDaylight || (this.dstSavings == simpleTimeZone.dstSavings && this.startMode == simpleTimeZone.startMode && this.startMonth == simpleTimeZone.startMonth && this.startDay == simpleTimeZone.startDay && this.startDayOfWeek == simpleTimeZone.startDayOfWeek && this.startTime == simpleTimeZone.startTime && this.startTimeMode == simpleTimeZone.startTimeMode && this.endMode == simpleTimeZone.endMode && this.endMonth == simpleTimeZone.endMonth && this.endDay == simpleTimeZone.endDay && this.endDayOfWeek == simpleTimeZone.endDayOfWeek && this.endTime == simpleTimeZone.endTime && this.endTimeMode == simpleTimeZone.endTimeMode && this.startYear == simpleTimeZone.startYear));
    }

    public String toString() {
        return getClass().getName() + "[id=" + getID() + ",offset=" + this.rawOffset + ",dstSavings=" + this.dstSavings + ",useDaylight=" + this.useDaylight + ",startYear=" + this.startYear + ",startMode=" + this.startMode + ",startMonth=" + this.startMonth + ",startDay=" + this.startDay + ",startDayOfWeek=" + this.startDayOfWeek + ",startTime=" + this.startTime + ",startTimeMode=" + this.startTimeMode + ",endMode=" + this.endMode + ",endMonth=" + this.endMonth + ",endDay=" + this.endDay + ",endDayOfWeek=" + this.endDayOfWeek + ",endTime=" + this.endTime + ",endTimeMode=" + this.endTimeMode + ']';
    }

    private synchronized void invalidateCache() {
        this.cacheYear = this.startYear - 1;
        this.cacheEnd = 0L;
        this.cacheStart = 0L;
    }

    private void decodeRules() {
        decodeStartRule();
        decodeEndRule();
    }

    private void decodeStartRule() {
        this.useDaylight = (this.startDay == 0 || this.endDay == 0) ? false : true;
        if (this.startDay != 0) {
            if (this.startMonth < 0 || this.startMonth > 11) {
                throw new IllegalArgumentException("Illegal start month " + this.startMonth);
            }
            if (this.startTime < 0 || this.startTime > millisPerDay) {
                throw new IllegalArgumentException("Illegal start time " + this.startTime);
            }
            if (this.startDayOfWeek == 0) {
                this.startMode = 1;
            } else {
                if (this.startDayOfWeek > 0) {
                    this.startMode = 2;
                } else {
                    this.startDayOfWeek = -this.startDayOfWeek;
                    if (this.startDay > 0) {
                        this.startMode = 3;
                    } else {
                        this.startDay = -this.startDay;
                        this.startMode = 4;
                    }
                }
                if (this.startDayOfWeek > 7) {
                    throw new IllegalArgumentException("Illegal start day of week " + this.startDayOfWeek);
                }
            }
            if (this.startMode == 2) {
                if (this.startDay < -5 || this.startDay > 5) {
                    throw new IllegalArgumentException("Illegal start day of week in month " + this.startDay);
                }
            } else if (this.startDay < 1 || this.startDay > staticMonthLength[this.startMonth]) {
                throw new IllegalArgumentException("Illegal start day " + this.startDay);
            }
        }
    }

    private void decodeEndRule() {
        this.useDaylight = (this.startDay == 0 || this.endDay == 0) ? false : true;
        if (this.endDay != 0) {
            if (this.endMonth < 0 || this.endMonth > 11) {
                throw new IllegalArgumentException("Illegal end month " + this.endMonth);
            }
            if (this.endTime < 0 || this.endTime > millisPerDay) {
                throw new IllegalArgumentException("Illegal end time " + this.endTime);
            }
            if (this.endDayOfWeek == 0) {
                this.endMode = 1;
            } else {
                if (this.endDayOfWeek > 0) {
                    this.endMode = 2;
                } else {
                    this.endDayOfWeek = -this.endDayOfWeek;
                    if (this.endDay > 0) {
                        this.endMode = 3;
                    } else {
                        this.endDay = -this.endDay;
                        this.endMode = 4;
                    }
                }
                if (this.endDayOfWeek > 7) {
                    throw new IllegalArgumentException("Illegal end day of week " + this.endDayOfWeek);
                }
            }
            if (this.endMode == 2) {
                if (this.endDay < -5 || this.endDay > 5) {
                    throw new IllegalArgumentException("Illegal end day of week in month " + this.endDay);
                }
            } else if (this.endDay < 1 || this.endDay > staticMonthLength[this.endMonth]) {
                throw new IllegalArgumentException("Illegal end day " + this.endDay);
            }
        }
    }

    private void makeRulesCompatible() {
        switch (this.startMode) {
            case 1:
                this.startDay = 1 + (this.startDay / 7);
                this.startDayOfWeek = 1;
                break;
            case 3:
                if (this.startDay != 1) {
                    this.startDay = 1 + (this.startDay / 7);
                    break;
                }
                break;
            case 4:
                if (this.startDay >= 30) {
                    this.startDay = -1;
                    break;
                } else {
                    this.startDay = 1 + (this.startDay / 7);
                    break;
                }
        }
        switch (this.endMode) {
            case 1:
                this.endDay = 1 + (this.endDay / 7);
                this.endDayOfWeek = 1;
                break;
            case 3:
                if (this.endDay != 1) {
                    this.endDay = 1 + (this.endDay / 7);
                    break;
                }
                break;
            case 4:
                if (this.endDay >= 30) {
                    this.endDay = -1;
                    break;
                } else {
                    this.endDay = 1 + (this.endDay / 7);
                    break;
                }
        }
        switch (this.startTimeMode) {
            case 2:
                this.startTime += this.rawOffset;
                break;
        }
        while (this.startTime < 0) {
            this.startTime += millisPerDay;
            this.startDayOfWeek = 1 + ((this.startDayOfWeek + 5) % 7);
        }
        while (this.startTime >= millisPerDay) {
            this.startTime -= millisPerDay;
            this.startDayOfWeek = 1 + (this.startDayOfWeek % 7);
        }
        switch (this.endTimeMode) {
            case 1:
                this.endTime += this.dstSavings;
                break;
            case 2:
                this.endTime += this.rawOffset + this.dstSavings;
                break;
        }
        while (this.endTime < 0) {
            this.endTime += millisPerDay;
            this.endDayOfWeek = 1 + ((this.endDayOfWeek + 5) % 7);
        }
        while (this.endTime >= millisPerDay) {
            this.endTime -= millisPerDay;
            this.endDayOfWeek = 1 + (this.endDayOfWeek % 7);
        }
    }

    private byte[] packRules() {
        return new byte[]{(byte) this.startDay, (byte) this.startDayOfWeek, (byte) this.endDay, (byte) this.endDayOfWeek, (byte) this.startTimeMode, (byte) this.endTimeMode};
    }

    private void unpackRules(byte[] bArr) {
        this.startDay = bArr[0];
        this.startDayOfWeek = bArr[1];
        this.endDay = bArr[2];
        this.endDayOfWeek = bArr[3];
        if (bArr.length >= 6) {
            this.startTimeMode = bArr[4];
            this.endTimeMode = bArr[5];
        }
    }

    private int[] packTimes() {
        return new int[]{this.startTime, this.endTime};
    }

    private void unpackTimes(int[] iArr) {
        this.startTime = iArr[0];
        this.endTime = iArr[1];
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        byte[] bArrPackRules = packRules();
        int[] iArrPackTimes = packTimes();
        makeRulesCompatible();
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(bArrPackRules.length);
        objectOutputStream.write(bArrPackRules);
        objectOutputStream.writeObject(iArrPackTimes);
        unpackRules(bArrPackRules);
        unpackTimes(iArrPackTimes);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            if (this.startDayOfWeek == 0) {
                this.startDayOfWeek = 1;
            }
            if (this.endDayOfWeek == 0) {
                this.endDayOfWeek = 1;
            }
            this.endMode = 2;
            this.startMode = 2;
            this.dstSavings = millisPerHour;
        } else {
            int i2 = objectInputStream.readInt();
            if (i2 <= 6) {
                byte[] bArr = new byte[i2];
                objectInputStream.readFully(bArr);
                unpackRules(bArr);
            } else {
                throw new InvalidObjectException("Too many rules: " + i2);
            }
        }
        if (this.serialVersionOnStream >= 2) {
            unpackTimes((int[]) objectInputStream.readObject());
        }
        this.serialVersionOnStream = 2;
    }
}
