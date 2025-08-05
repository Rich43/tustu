package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.security.krb5.internal.Krb5;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/Duration.class */
public final class Duration implements TemporalAmount, Comparable<Duration>, Serializable {
    private static final long serialVersionUID = 3078945930695997490L;
    private final long seconds;
    private final int nanos;
    public static final Duration ZERO = new Duration(0, 0);
    private static final BigInteger BI_NANOS_PER_SECOND = BigInteger.valueOf(NativeMediaPlayer.ONE_SECOND);
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);

    public static Duration ofDays(long j2) {
        return create(Math.multiplyExact(j2, 86400L), 0);
    }

    public static Duration ofHours(long j2) {
        return create(Math.multiplyExact(j2, 3600L), 0);
    }

    public static Duration ofMinutes(long j2) {
        return create(Math.multiplyExact(j2, 60L), 0);
    }

    public static Duration ofSeconds(long j2) {
        return create(j2, 0);
    }

    public static Duration ofSeconds(long j2, long j3) {
        return create(Math.addExact(j2, Math.floorDiv(j3, NativeMediaPlayer.ONE_SECOND)), (int) Math.floorMod(j3, NativeMediaPlayer.ONE_SECOND));
    }

    public static Duration ofMillis(long j2) {
        long j3 = j2 / 1000;
        int i2 = (int) (j2 % 1000);
        if (i2 < 0) {
            i2 += 1000;
            j3--;
        }
        return create(j3, i2 * 1000000);
    }

    public static Duration ofNanos(long j2) {
        long j3 = j2 / NativeMediaPlayer.ONE_SECOND;
        int i2 = (int) (j2 % NativeMediaPlayer.ONE_SECOND);
        if (i2 < 0) {
            i2 = (int) (i2 + NativeMediaPlayer.ONE_SECOND);
            j3--;
        }
        return create(j3, i2);
    }

    public static Duration of(long j2, TemporalUnit temporalUnit) {
        return ZERO.plus(j2, temporalUnit);
    }

    public static Duration from(TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, Constants.ATTRNAME_AMOUNT);
        Duration durationPlus = ZERO;
        for (TemporalUnit temporalUnit : temporalAmount.getUnits()) {
            durationPlus = durationPlus.plus(temporalAmount.get(temporalUnit), temporalUnit);
        }
        return durationPlus;
    }

    public static Duration parse(CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        Matcher matcher = PATTERN.matcher(charSequence);
        if (matcher.matches() && !"T".equals(matcher.group(3))) {
            boolean zEquals = LanguageTag.SEP.equals(matcher.group(1));
            String strGroup = matcher.group(2);
            String strGroup2 = matcher.group(4);
            String strGroup3 = matcher.group(5);
            String strGroup4 = matcher.group(6);
            String strGroup5 = matcher.group(7);
            if (strGroup != null || strGroup2 != null || strGroup3 != null || strGroup4 != null) {
                long number = parseNumber(charSequence, strGroup, Krb5.DEFAULT_MAXIMUM_TICKET_LIFETIME, "days");
                long number2 = parseNumber(charSequence, strGroup2, 3600, "hours");
                long number3 = parseNumber(charSequence, strGroup3, 60, "minutes");
                long number4 = parseNumber(charSequence, strGroup4, 1, "seconds");
                try {
                    return create(zEquals, number, number2, number3, number4, parseFraction(charSequence, strGroup5, number4 < 0 ? -1 : 1));
                } catch (ArithmeticException e2) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: overflow", charSequence, 0).initCause(e2));
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Duration", charSequence, 0);
    }

    private static long parseNumber(CharSequence charSequence, String str, int i2, String str2) {
        if (str == null) {
            return 0L;
        }
        try {
            return Math.multiplyExact(Long.parseLong(str), i2);
        } catch (ArithmeticException | NumberFormatException e2) {
            throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: " + str2, charSequence, 0).initCause(e2));
        }
    }

    private static int parseFraction(CharSequence charSequence, String str, int i2) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt((str + "000000000").substring(0, 9)) * i2;
        } catch (ArithmeticException | NumberFormatException e2) {
            throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: fraction", charSequence, 0).initCause(e2));
        }
    }

    private static Duration create(boolean z2, long j2, long j3, long j4, long j5, int i2) {
        long jAddExact = Math.addExact(j2, Math.addExact(j3, Math.addExact(j4, j5)));
        if (z2) {
            return ofSeconds(jAddExact, i2).negated();
        }
        return ofSeconds(jAddExact, i2);
    }

    public static Duration between(Temporal temporal, Temporal temporal2) {
        long j2;
        try {
            return ofNanos(temporal.until(temporal2, ChronoUnit.NANOS));
        } catch (ArithmeticException | DateTimeException e2) {
            long jUntil = temporal.until(temporal2, ChronoUnit.SECONDS);
            try {
                j2 = temporal2.getLong(ChronoField.NANO_OF_SECOND) - temporal.getLong(ChronoField.NANO_OF_SECOND);
                if (jUntil > 0 && j2 < 0) {
                    jUntil++;
                } else if (jUntil < 0 && j2 > 0) {
                    jUntil--;
                }
            } catch (DateTimeException e3) {
                j2 = 0;
            }
            return ofSeconds(jUntil, j2);
        }
    }

    private static Duration create(long j2, int i2) {
        if ((j2 | i2) == 0) {
            return ZERO;
        }
        return new Duration(j2, i2);
    }

    private Duration(long j2, int i2) {
        this.seconds = j2;
        this.nanos = i2;
    }

    @Override // java.time.temporal.TemporalAmount
    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.SECONDS) {
            return this.seconds;
        }
        if (temporalUnit == ChronoUnit.NANOS) {
            return this.nanos;
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
    }

    @Override // java.time.temporal.TemporalAmount
    public List<TemporalUnit> getUnits() {
        return DurationUnits.UNITS;
    }

    /* loaded from: rt.jar:java/time/Duration$DurationUnits.class */
    private static class DurationUnits {
        static final List<TemporalUnit> UNITS = Collections.unmodifiableList(Arrays.asList(ChronoUnit.SECONDS, ChronoUnit.NANOS));

        private DurationUnits() {
        }
    }

    public boolean isZero() {
        return (this.seconds | ((long) this.nanos)) == 0;
    }

    public boolean isNegative() {
        return this.seconds < 0;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public int getNano() {
        return this.nanos;
    }

    public Duration withSeconds(long j2) {
        return create(j2, this.nanos);
    }

    public Duration withNanos(int i2) {
        ChronoField.NANO_OF_SECOND.checkValidIntValue(i2);
        return create(this.seconds, i2);
    }

    public Duration plus(Duration duration) {
        return plus(duration.getSeconds(), duration.getNano());
    }

    public Duration plus(long j2, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporalUnit, "unit");
        if (temporalUnit == ChronoUnit.DAYS) {
            return plus(Math.multiplyExact(j2, 86400L), 0L);
        }
        if (temporalUnit.isDurationEstimated()) {
            throw new UnsupportedTemporalTypeException("Unit must not have an estimated duration");
        }
        if (j2 == 0) {
            return this;
        }
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return plusNanos(j2);
                case MICROS:
                    return plusSeconds((j2 / NativeMediaPlayer.ONE_SECOND) * 1000).plusNanos((j2 % NativeMediaPlayer.ONE_SECOND) * 1000);
                case MILLIS:
                    return plusMillis(j2);
                case SECONDS:
                    return plusSeconds(j2);
                default:
                    return plusSeconds(Math.multiplyExact(temporalUnit.getDuration().seconds, j2));
            }
        }
        return plusSeconds(temporalUnit.getDuration().multipliedBy(j2).getSeconds()).plusNanos(r0.getNano());
    }

    public Duration plusDays(long j2) {
        return plus(Math.multiplyExact(j2, 86400L), 0L);
    }

    public Duration plusHours(long j2) {
        return plus(Math.multiplyExact(j2, 3600L), 0L);
    }

    public Duration plusMinutes(long j2) {
        return plus(Math.multiplyExact(j2, 60L), 0L);
    }

    public Duration plusSeconds(long j2) {
        return plus(j2, 0L);
    }

    public Duration plusMillis(long j2) {
        return plus(j2 / 1000, (j2 % 1000) * 1000000);
    }

    public Duration plusNanos(long j2) {
        return plus(0L, j2);
    }

    private Duration plus(long j2, long j3) {
        if ((j2 | j3) == 0) {
            return this;
        }
        return ofSeconds(Math.addExact(Math.addExact(this.seconds, j2), j3 / NativeMediaPlayer.ONE_SECOND), this.nanos + (j3 % NativeMediaPlayer.ONE_SECOND));
    }

    public Duration minus(Duration duration) {
        long seconds = duration.getSeconds();
        int nano = duration.getNano();
        if (seconds == Long.MIN_VALUE) {
            return plus(Long.MAX_VALUE, -nano).plus(1L, 0L);
        }
        return plus(-seconds, -nano);
    }

    public Duration minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public Duration minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1L) : plusDays(-j2);
    }

    public Duration minusHours(long j2) {
        return j2 == Long.MIN_VALUE ? plusHours(Long.MAX_VALUE).plusHours(1L) : plusHours(-j2);
    }

    public Duration minusMinutes(long j2) {
        return j2 == Long.MIN_VALUE ? plusMinutes(Long.MAX_VALUE).plusMinutes(1L) : plusMinutes(-j2);
    }

    public Duration minusSeconds(long j2) {
        return j2 == Long.MIN_VALUE ? plusSeconds(Long.MAX_VALUE).plusSeconds(1L) : plusSeconds(-j2);
    }

    public Duration minusMillis(long j2) {
        return j2 == Long.MIN_VALUE ? plusMillis(Long.MAX_VALUE).plusMillis(1L) : plusMillis(-j2);
    }

    public Duration minusNanos(long j2) {
        return j2 == Long.MIN_VALUE ? plusNanos(Long.MAX_VALUE).plusNanos(1L) : plusNanos(-j2);
    }

    public Duration multipliedBy(long j2) {
        if (j2 == 0) {
            return ZERO;
        }
        if (j2 == 1) {
            return this;
        }
        return create(toSeconds().multiply(BigDecimal.valueOf(j2)));
    }

    public Duration dividedBy(long j2) {
        if (j2 == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        if (j2 == 1) {
            return this;
        }
        return create(toSeconds().divide(BigDecimal.valueOf(j2), RoundingMode.DOWN));
    }

    private BigDecimal toSeconds() {
        return BigDecimal.valueOf(this.seconds).add(BigDecimal.valueOf(this.nanos, 9));
    }

    private static Duration create(BigDecimal bigDecimal) {
        BigInteger bigIntegerExact = bigDecimal.movePointRight(9).toBigIntegerExact();
        BigInteger[] bigIntegerArrDivideAndRemainder = bigIntegerExact.divideAndRemainder(BI_NANOS_PER_SECOND);
        if (bigIntegerArrDivideAndRemainder[0].bitLength() > 63) {
            throw new ArithmeticException("Exceeds capacity of Duration: " + ((Object) bigIntegerExact));
        }
        return ofSeconds(bigIntegerArrDivideAndRemainder[0].longValue(), bigIntegerArrDivideAndRemainder[1].intValue());
    }

    public Duration negated() {
        return multipliedBy(-1L);
    }

    public Duration abs() {
        return isNegative() ? negated() : this;
    }

    @Override // java.time.temporal.TemporalAmount
    public Temporal addTo(Temporal temporal) {
        if (this.seconds != 0) {
            temporal = temporal.plus(this.seconds, ChronoUnit.SECONDS);
        }
        if (this.nanos != 0) {
            temporal = temporal.plus(this.nanos, ChronoUnit.NANOS);
        }
        return temporal;
    }

    @Override // java.time.temporal.TemporalAmount
    public Temporal subtractFrom(Temporal temporal) {
        if (this.seconds != 0) {
            temporal = temporal.minus(this.seconds, ChronoUnit.SECONDS);
        }
        if (this.nanos != 0) {
            temporal = temporal.minus(this.nanos, ChronoUnit.NANOS);
        }
        return temporal;
    }

    public long toDays() {
        return this.seconds / 86400;
    }

    public long toHours() {
        return this.seconds / 3600;
    }

    public long toMinutes() {
        return this.seconds / 60;
    }

    public long toMillis() {
        return Math.addExact(Math.multiplyExact(this.seconds, 1000L), this.nanos / 1000000);
    }

    public long toNanos() {
        return Math.addExact(Math.multiplyExact(this.seconds, NativeMediaPlayer.ONE_SECOND), this.nanos);
    }

    @Override // java.lang.Comparable
    public int compareTo(Duration duration) {
        int iCompare = Long.compare(this.seconds, duration.seconds);
        if (iCompare != 0) {
            return iCompare;
        }
        return this.nanos - duration.nanos;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Duration) {
            Duration duration = (Duration) obj;
            return this.seconds == duration.seconds && this.nanos == duration.nanos;
        }
        return false;
    }

    public int hashCode() {
        return ((int) (this.seconds ^ (this.seconds >>> 32))) + (51 * this.nanos);
    }

    public String toString() {
        if (this == ZERO) {
            return "PT0S";
        }
        long j2 = this.seconds / 3600;
        int i2 = (int) ((this.seconds % 3600) / 60);
        int i3 = (int) (this.seconds % 60);
        StringBuilder sb = new StringBuilder(24);
        sb.append("PT");
        if (j2 != 0) {
            sb.append(j2).append('H');
        }
        if (i2 != 0) {
            sb.append(i2).append('M');
        }
        if (i3 == 0 && this.nanos == 0 && sb.length() > 2) {
            return sb.toString();
        }
        if (i3 < 0 && this.nanos > 0) {
            if (i3 == -1) {
                sb.append("-0");
            } else {
                sb.append(i3 + 1);
            }
        } else {
            sb.append(i3);
        }
        if (this.nanos > 0) {
            int length = sb.length();
            if (i3 < 0) {
                sb.append(2000000000 - this.nanos);
            } else {
                sb.append(this.nanos + NativeMediaPlayer.ONE_SECOND);
            }
            while (sb.charAt(sb.length() - 1) == '0') {
                sb.setLength(sb.length() - 1);
            }
            sb.setCharAt(length, '.');
        }
        sb.append('S');
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }

    static Duration readExternal(DataInput dataInput) throws IOException {
        return ofSeconds(dataInput.readLong(), dataInput.readInt());
    }
}
