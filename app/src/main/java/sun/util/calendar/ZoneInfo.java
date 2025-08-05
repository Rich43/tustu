package sun.util.calendar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/* loaded from: rt.jar:sun/util/calendar/ZoneInfo.class */
public class ZoneInfo extends TimeZone {
    private static final int UTC_TIME = 0;
    private static final int STANDARD_TIME = 1;
    private static final int WALL_TIME = 2;
    private static final long OFFSET_MASK = 15;
    private static final long DST_MASK = 240;
    private static final int DST_NSHIFT = 4;
    private static final long ABBR_MASK = 3840;
    private static final int TRANSITION_NSHIFT = 12;
    private static final CalendarSystem gcal = CalendarSystem.getGregorianCalendar();
    private int rawOffset;
    private int rawOffsetDiff;
    private int checksum;
    private int dstSavings;
    private long[] transitions;
    private int[] offsets;
    private int[] simpleTimeZoneParams;
    private boolean willGMTOffsetChange;
    private transient boolean dirty;
    private static final long serialVersionUID = 2653134537216586139L;
    private transient SimpleTimeZone lastRule;

    public ZoneInfo() {
        this.rawOffsetDiff = 0;
        this.willGMTOffsetChange = false;
        this.dirty = false;
    }

    public ZoneInfo(String str, int i2) {
        this(str, i2, 0, 0, null, null, null, false);
    }

    ZoneInfo(String str, int i2, int i3, int i4, long[] jArr, int[] iArr, int[] iArr2, boolean z2) {
        this.rawOffsetDiff = 0;
        this.willGMTOffsetChange = false;
        this.dirty = false;
        setID(str);
        this.rawOffset = i2;
        this.dstSavings = i3;
        this.checksum = i4;
        this.transitions = jArr;
        this.offsets = iArr;
        this.simpleTimeZoneParams = iArr2;
        this.willGMTOffsetChange = z2;
    }

    @Override // java.util.TimeZone
    public int getOffset(long j2) {
        return getOffsets(j2, null, 0);
    }

    public int getOffsets(long j2, int[] iArr) {
        return getOffsets(j2, iArr, 0);
    }

    public int getOffsetsByStandard(long j2, int[] iArr) {
        return getOffsets(j2, iArr, 1);
    }

    public int getOffsetsByWall(long j2, int[] iArr) {
        return getOffsets(j2, iArr, 2);
    }

    private int getOffsets(long j2, int[] iArr, int i2) {
        if (this.transitions == null) {
            int lastRawOffset = getLastRawOffset();
            if (iArr != null) {
                iArr[0] = lastRawOffset;
                iArr[1] = 0;
            }
            return lastRawOffset;
        }
        long j3 = j2 - this.rawOffsetDiff;
        int transitionIndex = getTransitionIndex(j3, i2);
        if (transitionIndex < 0) {
            int lastRawOffset2 = getLastRawOffset();
            if (iArr != null) {
                iArr[0] = lastRawOffset2;
                iArr[1] = 0;
            }
            return lastRawOffset2;
        }
        if (transitionIndex < this.transitions.length) {
            long j4 = this.transitions[transitionIndex];
            int i3 = this.offsets[(int) (j4 & OFFSET_MASK)] + this.rawOffsetDiff;
            if (iArr != null) {
                int i4 = (int) ((j4 >>> 4) & OFFSET_MASK);
                int i5 = i4 == 0 ? 0 : this.offsets[i4];
                iArr[0] = i3 - i5;
                iArr[1] = i5;
            }
            return i3;
        }
        SimpleTimeZone lastRule = getLastRule();
        if (lastRule != null) {
            int rawOffset = lastRule.getRawOffset();
            long j5 = j3;
            if (i2 != 0) {
                j5 -= this.rawOffset;
            }
            int offset = lastRule.getOffset(j5) - this.rawOffset;
            if (offset > 0 && lastRule.getOffset(j5 - offset) == rawOffset && i2 == 2) {
                offset = 0;
            }
            if (iArr != null) {
                iArr[0] = rawOffset;
                iArr[1] = offset;
            }
            return rawOffset + offset;
        }
        int lastRawOffset3 = getLastRawOffset();
        if (iArr != null) {
            iArr[0] = lastRawOffset3;
            iArr[1] = 0;
        }
        return lastRawOffset3;
    }

    private int getTransitionIndex(long j2, int i2) {
        int i3;
        int i4 = 0;
        int length = this.transitions.length - 1;
        while (i4 <= length) {
            int i5 = (i4 + length) / 2;
            long j3 = this.transitions[i5];
            long j4 = j3 >> 12;
            if (i2 != 0) {
                j4 += this.offsets[(int) (j3 & OFFSET_MASK)];
            }
            if (i2 == 1 && (i3 = (int) ((j3 >>> 4) & OFFSET_MASK)) != 0) {
                j4 -= this.offsets[i3];
            }
            if (j4 < j2) {
                i4 = i5 + 1;
            } else if (j4 > j2) {
                length = i5 - 1;
            } else {
                return i5;
            }
        }
        if (i4 >= this.transitions.length) {
            return i4;
        }
        return i4 - 1;
    }

    @Override // java.util.TimeZone
    public int getOffset(int i2, int i3, int i4, int i5, int i6, int i7) {
        if (i7 < 0 || i7 >= 86400000) {
            throw new IllegalArgumentException();
        }
        if (i2 == 0) {
            i3 = 1 - i3;
        } else if (i2 != 1) {
            throw new IllegalArgumentException();
        }
        CalendarDate calendarDateNewCalendarDate = gcal.newCalendarDate(null);
        calendarDateNewCalendarDate.setDate(i3, i4 + 1, i5);
        if (!gcal.validate(calendarDateNewCalendarDate)) {
            throw new IllegalArgumentException();
        }
        if (i6 < 1 || i6 > 7) {
            throw new IllegalArgumentException();
        }
        if (this.transitions == null) {
            return getLastRawOffset();
        }
        return getOffsets((gcal.getTime(calendarDateNewCalendarDate) + i7) - this.rawOffset, null, 0);
    }

    @Override // java.util.TimeZone
    public synchronized void setRawOffset(int i2) {
        if (i2 == this.rawOffset + this.rawOffsetDiff) {
            return;
        }
        this.rawOffsetDiff = i2 - this.rawOffset;
        if (this.lastRule != null) {
            this.lastRule.setRawOffset(i2);
        }
        this.dirty = true;
    }

    @Override // java.util.TimeZone
    public int getRawOffset() {
        if (!this.willGMTOffsetChange) {
            return this.rawOffset + this.rawOffsetDiff;
        }
        int[] iArr = new int[2];
        getOffsets(System.currentTimeMillis(), iArr, 0);
        return iArr[0];
    }

    public boolean isDirty() {
        return this.dirty;
    }

    private int getLastRawOffset() {
        return this.rawOffset + this.rawOffsetDiff;
    }

    @Override // java.util.TimeZone
    public boolean useDaylightTime() {
        return this.simpleTimeZoneParams != null;
    }

    @Override // java.util.TimeZone
    public boolean observesDaylightTime() {
        int transitionIndex;
        if (this.simpleTimeZoneParams != null) {
            return true;
        }
        if (this.transitions == null || (transitionIndex = getTransitionIndex(System.currentTimeMillis() - this.rawOffsetDiff, 0)) < 0) {
            return false;
        }
        for (int i2 = transitionIndex; i2 < this.transitions.length; i2++) {
            if ((this.transitions[i2] & 240) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.TimeZone
    public boolean inDaylightTime(Date date) {
        int transitionIndex;
        if (date == null) {
            throw new NullPointerException();
        }
        if (this.transitions == null || (transitionIndex = getTransitionIndex(date.getTime() - this.rawOffsetDiff, 0)) < 0) {
            return false;
        }
        if (transitionIndex < this.transitions.length) {
            return (this.transitions[transitionIndex] & 240) != 0;
        }
        SimpleTimeZone lastRule = getLastRule();
        if (lastRule != null) {
            return lastRule.inDaylightTime(date);
        }
        return false;
    }

    @Override // java.util.TimeZone
    public int getDSTSavings() {
        return this.dstSavings;
    }

    public String toString() {
        return getClass().getName() + "[id=\"" + getID() + "\",offset=" + getLastRawOffset() + ",dstSavings=" + this.dstSavings + ",useDaylight=" + useDaylightTime() + ",transitions=" + (this.transitions != null ? this.transitions.length : 0) + ",lastRule=" + ((Object) (this.lastRule == null ? getLastRuleInstance() : this.lastRule)) + "]";
    }

    public static String[] getAvailableIDs() {
        return ZoneInfoFile.getZoneIds();
    }

    public static String[] getAvailableIDs(int i2) {
        return ZoneInfoFile.getZoneIds(i2);
    }

    public static TimeZone getTimeZone(String str) {
        return ZoneInfoFile.getZoneInfo(str);
    }

    private synchronized SimpleTimeZone getLastRule() {
        if (this.lastRule == null) {
            this.lastRule = getLastRuleInstance();
        }
        return this.lastRule;
    }

    public SimpleTimeZone getLastRuleInstance() {
        if (this.simpleTimeZoneParams == null) {
            return null;
        }
        if (this.simpleTimeZoneParams.length == 10) {
            return new SimpleTimeZone(getLastRawOffset(), getID(), this.simpleTimeZoneParams[0], this.simpleTimeZoneParams[1], this.simpleTimeZoneParams[2], this.simpleTimeZoneParams[3], this.simpleTimeZoneParams[4], this.simpleTimeZoneParams[5], this.simpleTimeZoneParams[6], this.simpleTimeZoneParams[7], this.simpleTimeZoneParams[8], this.simpleTimeZoneParams[9], this.dstSavings);
        }
        return new SimpleTimeZone(getLastRawOffset(), getID(), this.simpleTimeZoneParams[0], this.simpleTimeZoneParams[1], this.simpleTimeZoneParams[2], this.simpleTimeZoneParams[3], this.simpleTimeZoneParams[4], this.simpleTimeZoneParams[5], this.simpleTimeZoneParams[6], this.simpleTimeZoneParams[7], this.dstSavings);
    }

    @Override // java.util.TimeZone
    public Object clone() {
        ZoneInfo zoneInfo = (ZoneInfo) super.clone();
        zoneInfo.lastRule = null;
        return zoneInfo;
    }

    public int hashCode() {
        return getLastRawOffset() ^ this.checksum;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneInfo)) {
            return false;
        }
        ZoneInfo zoneInfo = (ZoneInfo) obj;
        return getID().equals(zoneInfo.getID()) && getLastRawOffset() == zoneInfo.getLastRawOffset() && this.checksum == zoneInfo.checksum;
    }

    @Override // java.util.TimeZone
    public boolean hasSameRules(TimeZone timeZone) {
        if (this == timeZone) {
            return true;
        }
        if (timeZone == null) {
            return false;
        }
        if (timeZone instanceof ZoneInfo) {
            return getLastRawOffset() == ((ZoneInfo) timeZone).getLastRawOffset() && this.checksum == ((ZoneInfo) timeZone).checksum;
        }
        if (getRawOffset() == timeZone.getRawOffset() && this.transitions == null && !useDaylightTime() && !timeZone.useDaylightTime()) {
            return true;
        }
        return false;
    }

    public static Map<String, String> getAliasTable() {
        return ZoneInfoFile.getAliasMap();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.dirty = true;
    }
}
