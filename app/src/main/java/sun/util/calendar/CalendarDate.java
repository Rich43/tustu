package sun.util.calendar;

import java.util.Locale;
import java.util.TimeZone;

/* loaded from: rt.jar:sun/util/calendar/CalendarDate.class */
public abstract class CalendarDate implements Cloneable {
    public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;
    public static final long TIME_UNDEFINED = Long.MIN_VALUE;
    private Era era;
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private boolean leapYear;
    private int hours;
    private int minutes;
    private int seconds;
    private int millis;
    private long fraction;
    private boolean normalized;
    private TimeZone zoneinfo;
    private int zoneOffset;
    private int daylightSaving;
    private boolean forceStandardTime;
    private Locale locale;

    /*  JADX ERROR: Failed to decode insn: 0x000B: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    public long getTimeOfDay() {
        /*
            r6 = this;
            r0 = r6
            boolean r0 = r0.isNormalized()
            if (r0 != 0) goto L10
            r0 = r6
            r1 = -9223372036854775808
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.fraction = r1
            return r-1
            r0 = r6
            long r0 = r0.fraction
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.calendar.CalendarDate.getTimeOfDay():long");
    }

    protected CalendarDate() {
        this(TimeZone.getDefault());
    }

    protected CalendarDate(TimeZone timeZone) {
        this.dayOfWeek = Integer.MIN_VALUE;
        this.zoneinfo = timeZone;
    }

    public Era getEra() {
        return this.era;
    }

    public CalendarDate setEra(Era era) {
        if (this.era == era) {
            return this;
        }
        this.era = era;
        this.normalized = false;
        return this;
    }

    public int getYear() {
        return this.year;
    }

    public CalendarDate setYear(int i2) {
        if (this.year != i2) {
            this.year = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addYear(int i2) {
        if (i2 != 0) {
            this.year += i2;
            this.normalized = false;
        }
        return this;
    }

    public boolean isLeapYear() {
        return this.leapYear;
    }

    void setLeapYear(boolean z2) {
        this.leapYear = z2;
    }

    public int getMonth() {
        return this.month;
    }

    public CalendarDate setMonth(int i2) {
        if (this.month != i2) {
            this.month = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMonth(int i2) {
        if (i2 != 0) {
            this.month += i2;
            this.normalized = false;
        }
        return this;
    }

    public int getDayOfMonth() {
        return this.dayOfMonth;
    }

    public CalendarDate setDayOfMonth(int i2) {
        if (this.dayOfMonth != i2) {
            this.dayOfMonth = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addDayOfMonth(int i2) {
        if (i2 != 0) {
            this.dayOfMonth += i2;
            this.normalized = false;
        }
        return this;
    }

    public int getDayOfWeek() {
        if (!isNormalized()) {
            this.dayOfWeek = Integer.MIN_VALUE;
        }
        return this.dayOfWeek;
    }

    public int getHours() {
        return this.hours;
    }

    public CalendarDate setHours(int i2) {
        if (this.hours != i2) {
            this.hours = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addHours(int i2) {
        if (i2 != 0) {
            this.hours += i2;
            this.normalized = false;
        }
        return this;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public CalendarDate setMinutes(int i2) {
        if (this.minutes != i2) {
            this.minutes = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMinutes(int i2) {
        if (i2 != 0) {
            this.minutes += i2;
            this.normalized = false;
        }
        return this;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public CalendarDate setSeconds(int i2) {
        if (this.seconds != i2) {
            this.seconds = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addSeconds(int i2) {
        if (i2 != 0) {
            this.seconds += i2;
            this.normalized = false;
        }
        return this;
    }

    public int getMillis() {
        return this.millis;
    }

    public CalendarDate setMillis(int i2) {
        if (this.millis != i2) {
            this.millis = i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate addMillis(int i2) {
        if (i2 != 0) {
            this.millis += i2;
            this.normalized = false;
        }
        return this;
    }

    public CalendarDate setDate(int i2, int i3, int i4) {
        setYear(i2);
        setMonth(i3);
        setDayOfMonth(i4);
        return this;
    }

    public CalendarDate addDate(int i2, int i3, int i4) {
        addYear(i2);
        addMonth(i3);
        addDayOfMonth(i4);
        return this;
    }

    public CalendarDate setTimeOfDay(int i2, int i3, int i4, int i5) {
        setHours(i2);
        setMinutes(i3);
        setSeconds(i4);
        setMillis(i5);
        return this;
    }

    public CalendarDate addTimeOfDay(int i2, int i3, int i4, int i5) {
        addHours(i2);
        addMinutes(i3);
        addSeconds(i4);
        addMillis(i5);
        return this;
    }

    protected void setTimeOfDay(long j2) {
        this.fraction = j2;
    }

    public boolean isNormalized() {
        return this.normalized;
    }

    public boolean isStandardTime() {
        return this.forceStandardTime;
    }

    public void setStandardTime(boolean z2) {
        this.forceStandardTime = z2;
    }

    public boolean isDaylightTime() {
        return (isStandardTime() || this.daylightSaving == 0) ? false : true;
    }

    protected void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getZone() {
        return this.zoneinfo;
    }

    public CalendarDate setZone(TimeZone timeZone) {
        this.zoneinfo = timeZone;
        return this;
    }

    public boolean isSameDate(CalendarDate calendarDate) {
        return getDayOfWeek() == calendarDate.getDayOfWeek() && getMonth() == calendarDate.getMonth() && getYear() == calendarDate.getYear() && getEra() == calendarDate.getEra();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CalendarDate)) {
            return false;
        }
        CalendarDate calendarDate = (CalendarDate) obj;
        if (isNormalized() != calendarDate.isNormalized()) {
            return false;
        }
        boolean z2 = this.zoneinfo != null;
        if (z2 != (calendarDate.zoneinfo != null)) {
            return false;
        }
        return (!z2 || this.zoneinfo.equals(calendarDate.zoneinfo)) && getEra() == calendarDate.getEra() && this.year == calendarDate.year && this.month == calendarDate.month && this.dayOfMonth == calendarDate.dayOfMonth && this.hours == calendarDate.hours && this.minutes == calendarDate.minutes && this.seconds == calendarDate.seconds && this.millis == calendarDate.millis && this.zoneOffset == calendarDate.zoneOffset;
    }

    public int hashCode() {
        long j2 = (((((((((((((this.year - 1970) * 12) + (this.month - 1)) * 30) + this.dayOfMonth) * 24) + this.hours) * 60) + this.minutes) * 60) + this.seconds) * 1000) + this.millis) - this.zoneOffset;
        int i2 = isNormalized() ? 1 : 0;
        int iHashCode = 0;
        Era era = getEra();
        if (era != null) {
            iHashCode = era.hashCode();
        }
        return (((((int) j2) * ((int) (j2 >> 32))) ^ iHashCode) ^ i2) ^ (this.zoneinfo != null ? this.zoneinfo.hashCode() : 0);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public String toString() {
        int i2;
        char c2;
        StringBuilder sb = new StringBuilder();
        CalendarUtils.sprintf0d(sb, this.year, 4).append('-');
        CalendarUtils.sprintf0d(sb, this.month, 2).append('-');
        CalendarUtils.sprintf0d(sb, this.dayOfMonth, 2).append('T');
        CalendarUtils.sprintf0d(sb, this.hours, 2).append(':');
        CalendarUtils.sprintf0d(sb, this.minutes, 2).append(':');
        CalendarUtils.sprintf0d(sb, this.seconds, 2).append('.');
        CalendarUtils.sprintf0d(sb, this.millis, 3);
        if (this.zoneOffset == 0) {
            sb.append('Z');
        } else if (this.zoneOffset != Integer.MIN_VALUE) {
            if (this.zoneOffset > 0) {
                i2 = this.zoneOffset;
                c2 = '+';
            } else {
                i2 = -this.zoneOffset;
                c2 = '-';
            }
            int i3 = i2 / 60000;
            sb.append(c2);
            CalendarUtils.sprintf0d(sb, i3 / 60, 2);
            CalendarUtils.sprintf0d(sb, i3 % 60, 2);
        } else {
            sb.append(" local time");
        }
        return sb.toString();
    }

    protected void setDayOfWeek(int i2) {
        this.dayOfWeek = i2;
    }

    protected void setNormalized(boolean z2) {
        this.normalized = z2;
    }

    public int getZoneOffset() {
        return this.zoneOffset;
    }

    protected void setZoneOffset(int i2) {
        this.zoneOffset = i2;
    }

    public int getDaylightSaving() {
        return this.daylightSaving;
    }

    protected void setDaylightSaving(int i2) {
        this.daylightSaving = i2;
    }
}
