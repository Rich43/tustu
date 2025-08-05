package java.nio.file.attribute;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/nio/file/attribute/FileTime.class */
public final class FileTime implements Comparable<FileTime> {
    private final TimeUnit unit;
    private final long value;
    private Instant instant;
    private String valueAsString;
    private static final long HOURS_PER_DAY = 24;
    private static final long MINUTES_PER_HOUR = 60;
    private static final long SECONDS_PER_MINUTE = 60;
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_DAY = 86400;
    private static final long MILLIS_PER_SECOND = 1000;
    private static final long MICROS_PER_SECOND = 1000000;
    private static final long NANOS_PER_SECOND = 1000000000;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_MICRO = 1000;
    private static final long MIN_SECOND = -31557014167219200L;
    private static final long MAX_SECOND = 31556889864403199L;
    private static final long DAYS_PER_10000_YEARS = 3652425;
    private static final long SECONDS_PER_10000_YEARS = 315569520000L;
    private static final long SECONDS_0000_TO_1970 = 62167219200L;

    private FileTime(long j2, TimeUnit timeUnit, Instant instant) {
        this.value = j2;
        this.unit = timeUnit;
        this.instant = instant;
    }

    public static FileTime from(long j2, TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "unit");
        return new FileTime(j2, timeUnit, null);
    }

    public static FileTime fromMillis(long j2) {
        return new FileTime(j2, TimeUnit.MILLISECONDS, null);
    }

    public static FileTime from(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return new FileTime(0L, null, instant);
    }

    public long to(TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "unit");
        if (this.unit != null) {
            return timeUnit.convert(this.value, this.unit);
        }
        long jConvert = timeUnit.convert(this.instant.getEpochSecond(), TimeUnit.SECONDS);
        if (jConvert == Long.MIN_VALUE || jConvert == Long.MAX_VALUE) {
            return jConvert;
        }
        long jConvert2 = timeUnit.convert(this.instant.getNano(), TimeUnit.NANOSECONDS);
        long j2 = jConvert + jConvert2;
        if (((jConvert ^ j2) & (jConvert2 ^ j2)) >= 0) {
            return j2;
        }
        if (jConvert < 0) {
            return Long.MIN_VALUE;
        }
        return Long.MAX_VALUE;
    }

    public long toMillis() {
        if (this.unit != null) {
            return this.unit.toMillis(this.value);
        }
        long epochSecond = this.instant.getEpochSecond();
        int nano = this.instant.getNano();
        long j2 = epochSecond * 1000;
        if (((Math.abs(epochSecond) | 1000) >>> 31) == 0 || j2 / 1000 == epochSecond) {
            return j2 + (nano / NANOS_PER_MILLI);
        }
        if (epochSecond < 0) {
            return Long.MIN_VALUE;
        }
        return Long.MAX_VALUE;
    }

    private static long scale(long j2, long j3, long j4) {
        if (j2 > j4) {
            return Long.MAX_VALUE;
        }
        if (j2 < (-j4)) {
            return Long.MIN_VALUE;
        }
        return j2 * j3;
    }

    public Instant toInstant() {
        long jFloorDiv;
        if (this.instant == null) {
            int iFloorMod = 0;
            switch (this.unit) {
                case DAYS:
                    jFloorDiv = scale(this.value, SECONDS_PER_DAY, 106751991167300L);
                    break;
                case HOURS:
                    jFloorDiv = scale(this.value, SECONDS_PER_HOUR, 2562047788015215L);
                    break;
                case MINUTES:
                    jFloorDiv = scale(this.value, 60L, 153722867280912930L);
                    break;
                case SECONDS:
                    jFloorDiv = this.value;
                    break;
                case MILLISECONDS:
                    jFloorDiv = Math.floorDiv(this.value, 1000L);
                    iFloorMod = ((int) Math.floorMod(this.value, 1000L)) * NANOS_PER_MILLI;
                    break;
                case MICROSECONDS:
                    jFloorDiv = Math.floorDiv(this.value, MICROS_PER_SECOND);
                    iFloorMod = ((int) Math.floorMod(this.value, MICROS_PER_SECOND)) * 1000;
                    break;
                case NANOSECONDS:
                    jFloorDiv = Math.floorDiv(this.value, 1000000000L);
                    iFloorMod = (int) Math.floorMod(this.value, 1000000000L);
                    break;
                default:
                    throw new AssertionError((Object) "Unit not handled");
            }
            if (jFloorDiv <= MIN_SECOND) {
                this.instant = Instant.MIN;
            } else if (jFloorDiv >= MAX_SECOND) {
                this.instant = Instant.MAX;
            } else {
                this.instant = Instant.ofEpochSecond(jFloorDiv, iFloorMod);
            }
        }
        return this.instant;
    }

    public boolean equals(Object obj) {
        return (obj instanceof FileTime) && compareTo((FileTime) obj) == 0;
    }

    public int hashCode() {
        return toInstant().hashCode();
    }

    private long toDays() {
        if (this.unit != null) {
            return this.unit.toDays(this.value);
        }
        return TimeUnit.SECONDS.toDays(toInstant().getEpochSecond());
    }

    private long toExcessNanos(long j2) {
        if (this.unit != null) {
            return this.unit.toNanos(this.value - this.unit.convert(j2, TimeUnit.DAYS));
        }
        return TimeUnit.SECONDS.toNanos(toInstant().getEpochSecond() - TimeUnit.DAYS.toSeconds(j2));
    }

    @Override // java.lang.Comparable
    public int compareTo(FileTime fileTime) {
        if (this.unit != null && this.unit == fileTime.unit) {
            return Long.compare(this.value, fileTime.value);
        }
        long epochSecond = toInstant().getEpochSecond();
        int iCompare = Long.compare(epochSecond, fileTime.toInstant().getEpochSecond());
        if (iCompare != 0) {
            return iCompare;
        }
        int iCompare2 = Long.compare(toInstant().getNano(), fileTime.toInstant().getNano());
        if (iCompare2 != 0) {
            return iCompare2;
        }
        if (epochSecond != MAX_SECOND && epochSecond != MIN_SECOND) {
            return 0;
        }
        long days = toDays();
        long days2 = fileTime.toDays();
        if (days == days2) {
            return Long.compare(toExcessNanos(days), fileTime.toExcessNanos(days2));
        }
        return Long.compare(days, days2);
    }

    private StringBuilder append(StringBuilder sb, int i2, int i3) {
        while (i2 > 0) {
            sb.append((char) ((i3 / i2) + 48));
            i3 %= i2;
            i2 /= 10;
        }
        return sb;
    }

    public String toString() {
        long epochSecond;
        LocalDateTime localDateTimeOfEpochSecond;
        int year;
        int i2;
        if (this.valueAsString == null) {
            int nano = 0;
            if (this.instant == null && this.unit.compareTo(TimeUnit.SECONDS) >= 0) {
                epochSecond = this.unit.toSeconds(this.value);
            } else {
                epochSecond = toInstant().getEpochSecond();
                nano = toInstant().getNano();
            }
            if (epochSecond >= -62167219200L) {
                long j2 = (epochSecond - SECONDS_PER_10000_YEARS) + SECONDS_0000_TO_1970;
                long jFloorDiv = Math.floorDiv(j2, SECONDS_PER_10000_YEARS) + 1;
                localDateTimeOfEpochSecond = LocalDateTime.ofEpochSecond(Math.floorMod(j2, SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, nano, ZoneOffset.UTC);
                year = localDateTimeOfEpochSecond.getYear() + (((int) jFloorDiv) * 10000);
            } else {
                long j3 = epochSecond + SECONDS_0000_TO_1970;
                long j4 = j3 / SECONDS_PER_10000_YEARS;
                localDateTimeOfEpochSecond = LocalDateTime.ofEpochSecond((j3 % SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, nano, ZoneOffset.UTC);
                year = localDateTimeOfEpochSecond.getYear() + (((int) j4) * 10000);
            }
            if (year <= 0) {
                year--;
            }
            int nano2 = localDateTimeOfEpochSecond.getNano();
            StringBuilder sb = new StringBuilder(64);
            sb.append(year < 0 ? LanguageTag.SEP : "");
            int iAbs = Math.abs(year);
            if (iAbs < 10000) {
                append(sb, 1000, Math.abs(iAbs));
            } else {
                sb.append(String.valueOf(iAbs));
            }
            sb.append('-');
            append(sb, 10, localDateTimeOfEpochSecond.getMonthValue());
            sb.append('-');
            append(sb, 10, localDateTimeOfEpochSecond.getDayOfMonth());
            sb.append('T');
            append(sb, 10, localDateTimeOfEpochSecond.getHour());
            sb.append(':');
            append(sb, 10, localDateTimeOfEpochSecond.getMinute());
            sb.append(':');
            append(sb, 10, localDateTimeOfEpochSecond.getSecond());
            if (nano2 != 0) {
                sb.append('.');
                int i3 = 100000000;
                while (true) {
                    i2 = i3;
                    if (nano2 % 10 != 0) {
                        break;
                    }
                    nano2 /= 10;
                    i3 = i2 / 10;
                }
                append(sb, i2, nano2);
            }
            sb.append('Z');
            this.valueAsString = sb.toString();
        }
        return this.valueAsString;
    }
}
