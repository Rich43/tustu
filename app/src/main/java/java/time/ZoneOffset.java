package java.time;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.zone.ZoneRules;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/ZoneOffset.class */
public final class ZoneOffset extends ZoneId implements TemporalAccessor, TemporalAdjuster, Comparable<ZoneOffset>, Serializable {
    private static final long serialVersionUID = 2357656521762053153L;
    private final int totalSeconds;
    private final transient String id;
    private static final ConcurrentMap<Integer, ZoneOffset> SECONDS_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    private static final ConcurrentMap<String, ZoneOffset> ID_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    public static final ZoneOffset UTC = ofTotalSeconds(0);
    public static final ZoneOffset MIN = ofTotalSeconds(-64800);
    private static final int MAX_SECONDS = 64800;
    public static final ZoneOffset MAX = ofTotalSeconds(MAX_SECONDS);

    /* JADX WARN: Removed duplicated region for block: B:18:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0133  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.time.ZoneOffset of(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.ZoneOffset.of(java.lang.String):java.time.ZoneOffset");
    }

    private static int parseNumber(CharSequence charSequence, int i2, boolean z2) {
        if (z2 && charSequence.charAt(i2 - 1) != ':') {
            throw new DateTimeException("Invalid ID for ZoneOffset, colon not found when expected: " + ((Object) charSequence));
        }
        char cCharAt = charSequence.charAt(i2);
        char cCharAt2 = charSequence.charAt(i2 + 1);
        if (cCharAt < '0' || cCharAt > '9' || cCharAt2 < '0' || cCharAt2 > '9') {
            throw new DateTimeException("Invalid ID for ZoneOffset, non numeric characters found: " + ((Object) charSequence));
        }
        return ((cCharAt - '0') * 10) + (cCharAt2 - '0');
    }

    public static ZoneOffset ofHours(int i2) {
        return ofHoursMinutesSeconds(i2, 0, 0);
    }

    public static ZoneOffset ofHoursMinutes(int i2, int i3) {
        return ofHoursMinutesSeconds(i2, i3, 0);
    }

    public static ZoneOffset ofHoursMinutesSeconds(int i2, int i3, int i4) {
        validate(i2, i3, i4);
        return ofTotalSeconds(totalSeconds(i2, i3, i4));
    }

    public static ZoneOffset from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        ZoneOffset zoneOffset = (ZoneOffset) temporalAccessor.query(TemporalQueries.offset());
        if (zoneOffset == null) {
            throw new DateTimeException("Unable to obtain ZoneOffset from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName());
        }
        return zoneOffset;
    }

    private static void validate(int i2, int i3, int i4) {
        if (i2 < -18 || i2 > 18) {
            throw new DateTimeException("Zone offset hours not in valid range: value " + i2 + " is not in the range -18 to 18");
        }
        if (i2 > 0) {
            if (i3 < 0 || i4 < 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be positive because hours is positive");
            }
        } else if (i2 < 0) {
            if (i3 > 0 || i4 > 0) {
                throw new DateTimeException("Zone offset minutes and seconds must be negative because hours is negative");
            }
        } else if ((i3 > 0 && i4 < 0) || (i3 < 0 && i4 > 0)) {
            throw new DateTimeException("Zone offset minutes and seconds must have the same sign");
        }
        if (i3 < -59 || i3 > 59) {
            throw new DateTimeException("Zone offset minutes not in valid range: value " + i3 + " is not in the range -59 to 59");
        }
        if (i4 < -59 || i4 > 59) {
            throw new DateTimeException("Zone offset seconds not in valid range: value " + i4 + " is not in the range -59 to 59");
        }
        if (Math.abs(i2) == 18 && (i3 | i4) != 0) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        }
    }

    private static int totalSeconds(int i2, int i3, int i4) {
        return (i2 * 3600) + (i3 * 60) + i4;
    }

    public static ZoneOffset ofTotalSeconds(int i2) {
        if (i2 < -64800 || i2 > MAX_SECONDS) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        }
        if (i2 % 900 == 0) {
            Integer numValueOf = Integer.valueOf(i2);
            ZoneOffset zoneOffset = SECONDS_CACHE.get(numValueOf);
            if (zoneOffset == null) {
                SECONDS_CACHE.putIfAbsent(numValueOf, new ZoneOffset(i2));
                zoneOffset = SECONDS_CACHE.get(numValueOf);
                ID_CACHE.putIfAbsent(zoneOffset.getId(), zoneOffset);
            }
            return zoneOffset;
        }
        return new ZoneOffset(i2);
    }

    private ZoneOffset(int i2) {
        this.totalSeconds = i2;
        this.id = buildId(i2);
    }

    private static String buildId(int i2) {
        if (i2 == 0) {
            return Constants.HASIDCALL_INDEX_SIG;
        }
        int iAbs = Math.abs(i2);
        StringBuilder sb = new StringBuilder();
        int i3 = iAbs / 3600;
        int i4 = (iAbs / 60) % 60;
        sb.append(i2 < 0 ? LanguageTag.SEP : Marker.ANY_NON_NULL_MARKER).append(i3 < 10 ? "0" : "").append(i3).append(i4 < 10 ? ":0" : CallSiteDescriptor.TOKEN_DELIMITER).append(i4);
        int i5 = iAbs % 60;
        if (i5 != 0) {
            sb.append(i5 < 10 ? ":0" : CallSiteDescriptor.TOKEN_DELIMITER).append(i5);
        }
        return sb.toString();
    }

    public int getTotalSeconds() {
        return this.totalSeconds;
    }

    @Override // java.time.ZoneId
    public String getId() {
        return this.id;
    }

    @Override // java.time.ZoneId
    public ZoneRules getRules() {
        return ZoneRules.of(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return temporalField instanceof ChronoField ? temporalField == ChronoField.OFFSET_SECONDS : temporalField != null && temporalField.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        return super.range(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return temporalField.getFrom(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return this;
        }
        return (R) super.query(temporalQuery);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.OFFSET_SECONDS, this.totalSeconds);
    }

    @Override // java.lang.Comparable
    public int compareTo(ZoneOffset zoneOffset) {
        return zoneOffset.totalSeconds - this.totalSeconds;
    }

    @Override // java.time.ZoneId
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneOffset) && this.totalSeconds == ((ZoneOffset) obj).totalSeconds;
    }

    @Override // java.time.ZoneId
    public int hashCode() {
        return this.totalSeconds;
    }

    @Override // java.time.ZoneId
    public String toString() {
        return this.id;
    }

    private Object writeReplace() {
        return new Ser((byte) 8, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    @Override // java.time.ZoneId
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(8);
        writeExternal(dataOutput);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        int i2 = this.totalSeconds;
        int i3 = i2 % 900 == 0 ? i2 / 900 : 127;
        dataOutput.writeByte(i3);
        if (i3 == 127) {
            dataOutput.writeInt(i2);
        }
    }

    static ZoneOffset readExternal(DataInput dataInput) throws IOException {
        byte b2 = dataInput.readByte();
        return b2 == Byte.MAX_VALUE ? ofTotalSeconds(dataInput.readInt()) : ofTotalSeconds(b2 * 900);
    }
}
