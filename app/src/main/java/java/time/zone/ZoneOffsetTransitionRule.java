package java.time.zone;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:java/time/zone/ZoneOffsetTransitionRule.class */
public final class ZoneOffsetTransitionRule implements Serializable {
    private static final long serialVersionUID = 6889046316657758795L;
    private final Month month;
    private final byte dom;
    private final DayOfWeek dow;
    private final LocalTime time;
    private final boolean timeEndOfDay;
    private final TimeDefinition timeDefinition;
    private final ZoneOffset standardOffset;
    private final ZoneOffset offsetBefore;
    private final ZoneOffset offsetAfter;

    public static ZoneOffsetTransitionRule of(Month month, int i2, DayOfWeek dayOfWeek, LocalTime localTime, boolean z2, TimeDefinition timeDefinition, ZoneOffset zoneOffset, ZoneOffset zoneOffset2, ZoneOffset zoneOffset3) {
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(localTime, SchemaSymbols.ATTVAL_TIME);
        Objects.requireNonNull(timeDefinition, "timeDefnition");
        Objects.requireNonNull(zoneOffset, "standardOffset");
        Objects.requireNonNull(zoneOffset2, "offsetBefore");
        Objects.requireNonNull(zoneOffset3, "offsetAfter");
        if (i2 < -28 || i2 > 31 || i2 == 0) {
            throw new IllegalArgumentException("Day of month indicator must be between -28 and 31 inclusive excluding zero");
        }
        if (z2 && !localTime.equals(LocalTime.MIDNIGHT)) {
            throw new IllegalArgumentException("Time must be midnight when end of day flag is true");
        }
        return new ZoneOffsetTransitionRule(month, i2, dayOfWeek, localTime, z2, timeDefinition, zoneOffset, zoneOffset2, zoneOffset3);
    }

    ZoneOffsetTransitionRule(Month month, int i2, DayOfWeek dayOfWeek, LocalTime localTime, boolean z2, TimeDefinition timeDefinition, ZoneOffset zoneOffset, ZoneOffset zoneOffset2, ZoneOffset zoneOffset3) {
        this.month = month;
        this.dom = (byte) i2;
        this.dow = dayOfWeek;
        this.time = localTime;
        this.timeEndOfDay = z2;
        this.timeDefinition = timeDefinition;
        this.standardOffset = zoneOffset;
        this.offsetBefore = zoneOffset2;
        this.offsetAfter = zoneOffset3;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        int secondOfDay = this.timeEndOfDay ? Krb5.DEFAULT_MAXIMUM_TICKET_LIFETIME : this.time.toSecondOfDay();
        int totalSeconds = this.standardOffset.getTotalSeconds();
        int totalSeconds2 = this.offsetBefore.getTotalSeconds() - totalSeconds;
        int totalSeconds3 = this.offsetAfter.getTotalSeconds() - totalSeconds;
        int hour = secondOfDay % 3600 == 0 ? this.timeEndOfDay ? 24 : this.time.getHour() : 31;
        int i2 = totalSeconds % 900 == 0 ? (totalSeconds / 900) + 128 : 255;
        int i3 = (totalSeconds2 == 0 || totalSeconds2 == 1800 || totalSeconds2 == 3600) ? totalSeconds2 / 1800 : 3;
        int i4 = (totalSeconds3 == 0 || totalSeconds3 == 1800 || totalSeconds3 == 3600) ? totalSeconds3 / 1800 : 3;
        dataOutput.writeInt((this.month.getValue() << 28) + ((this.dom + 32) << 22) + ((this.dow == null ? 0 : this.dow.getValue()) << 19) + (hour << 14) + (this.timeDefinition.ordinal() << 12) + (i2 << 4) + (i3 << 2) + i4);
        if (hour == 31) {
            dataOutput.writeInt(secondOfDay);
        }
        if (i2 == 255) {
            dataOutput.writeInt(totalSeconds);
        }
        if (i3 == 3) {
            dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
        }
        if (i4 == 3) {
            dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
        }
    }

    static ZoneOffsetTransitionRule readExternal(DataInput dataInput) throws IOException {
        int i2 = dataInput.readInt();
        Month monthOf = Month.of(i2 >>> 28);
        int i3 = ((i2 & 264241152) >>> 22) - 32;
        int i4 = (i2 & 3670016) >>> 19;
        DayOfWeek dayOfWeekOf = i4 == 0 ? null : DayOfWeek.of(i4);
        int i5 = (i2 & 507904) >>> 14;
        TimeDefinition timeDefinition = TimeDefinition.values()[(i2 & StackType.NULL_CHECK_START) >>> 12];
        int i6 = (i2 & 4080) >>> 4;
        int i7 = (i2 & 12) >>> 2;
        int i8 = i2 & 3;
        LocalTime localTimeOfSecondOfDay = i5 == 31 ? LocalTime.ofSecondOfDay(dataInput.readInt()) : LocalTime.of(i5 % 24, 0);
        ZoneOffset zoneOffsetOfTotalSeconds = i6 == 255 ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds((i6 - 128) * 900);
        return of(monthOf, i3, dayOfWeekOf, localTimeOfSecondOfDay, i5 == 24, timeDefinition, zoneOffsetOfTotalSeconds, i7 == 3 ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(zoneOffsetOfTotalSeconds.getTotalSeconds() + (i7 * 1800)), i8 == 3 ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(zoneOffsetOfTotalSeconds.getTotalSeconds() + (i8 * 1800)));
    }

    public Month getMonth() {
        return this.month;
    }

    public int getDayOfMonthIndicator() {
        return this.dom;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dow;
    }

    public LocalTime getLocalTime() {
        return this.time;
    }

    public boolean isMidnightEndOfDay() {
        return this.timeEndOfDay;
    }

    public TimeDefinition getTimeDefinition() {
        return this.timeDefinition;
    }

    public ZoneOffset getStandardOffset() {
        return this.standardOffset;
    }

    public ZoneOffset getOffsetBefore() {
        return this.offsetBefore;
    }

    public ZoneOffset getOffsetAfter() {
        return this.offsetAfter;
    }

    public ZoneOffsetTransition createTransition(int i2) {
        LocalDate localDateOf;
        if (this.dom < 0) {
            localDateOf = LocalDate.of(i2, this.month, this.month.length(IsoChronology.INSTANCE.isLeapYear(i2)) + 1 + this.dom);
            if (this.dow != null) {
                localDateOf = localDateOf.with(TemporalAdjusters.previousOrSame(this.dow));
            }
        } else {
            localDateOf = LocalDate.of(i2, this.month, this.dom);
            if (this.dow != null) {
                localDateOf = localDateOf.with(TemporalAdjusters.nextOrSame(this.dow));
            }
        }
        if (this.timeEndOfDay) {
            localDateOf = localDateOf.plusDays(1L);
        }
        return new ZoneOffsetTransition(this.timeDefinition.createDateTime(LocalDateTime.of(localDateOf, this.time), this.standardOffset, this.offsetBefore), this.offsetBefore, this.offsetAfter);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ZoneOffsetTransitionRule) {
            ZoneOffsetTransitionRule zoneOffsetTransitionRule = (ZoneOffsetTransitionRule) obj;
            return this.month == zoneOffsetTransitionRule.month && this.dom == zoneOffsetTransitionRule.dom && this.dow == zoneOffsetTransitionRule.dow && this.timeDefinition == zoneOffsetTransitionRule.timeDefinition && this.time.equals(zoneOffsetTransitionRule.time) && this.timeEndOfDay == zoneOffsetTransitionRule.timeEndOfDay && this.standardOffset.equals(zoneOffsetTransitionRule.standardOffset) && this.offsetBefore.equals(zoneOffsetTransitionRule.offsetBefore) && this.offsetAfter.equals(zoneOffsetTransitionRule.offsetAfter);
        }
        return false;
    }

    public int hashCode() {
        return ((((((((this.time.toSecondOfDay() + (this.timeEndOfDay ? 1 : 0)) << 15) + (this.month.ordinal() << 11)) + ((this.dom + 32) << 5)) + ((this.dow == null ? 7 : this.dow.ordinal()) << 2)) + this.timeDefinition.ordinal()) ^ this.standardOffset.hashCode()) ^ this.offsetBefore.hashCode()) ^ this.offsetAfter.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransitionRule[").append(this.offsetBefore.compareTo(this.offsetAfter) > 0 ? "Gap " : "Overlap ").append((Object) this.offsetBefore).append(" to ").append((Object) this.offsetAfter).append(", ");
        if (this.dow != null) {
            if (this.dom == -1) {
                sb.append(this.dow.name()).append(" on or before last day of ").append(this.month.name());
            } else if (this.dom < 0) {
                sb.append(this.dow.name()).append(" on or before last day minus ").append((-this.dom) - 1).append(" of ").append(this.month.name());
            } else {
                sb.append(this.dow.name()).append(" on or after ").append(this.month.name()).append(' ').append((int) this.dom);
            }
        } else {
            sb.append(this.month.name()).append(' ').append((int) this.dom);
        }
        sb.append(" at ").append(this.timeEndOfDay ? "24:00" : this.time.toString()).append(" ").append((Object) this.timeDefinition).append(", standard offset ").append((Object) this.standardOffset).append(']');
        return sb.toString();
    }

    /* loaded from: rt.jar:java/time/zone/ZoneOffsetTransitionRule$TimeDefinition.class */
    public enum TimeDefinition {
        UTC,
        WALL,
        STANDARD;

        public LocalDateTime createDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
            switch (this) {
                case UTC:
                    return localDateTime.plusSeconds(zoneOffset2.getTotalSeconds() - ZoneOffset.UTC.getTotalSeconds());
                case STANDARD:
                    return localDateTime.plusSeconds(zoneOffset2.getTotalSeconds() - zoneOffset.getTotalSeconds());
                default:
                    return localDateTime;
            }
        }
    }
}
