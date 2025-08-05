package jdk.jfr.consumer;

import java.time.DateTimeException;
import java.time.ZoneOffset;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.consumer.ChunkHeader;

/* loaded from: jfr.jar:jdk/jfr/consumer/TimeConverter.class */
final class TimeConverter {
    private final long startTicks;
    private final long startNanos;
    private final double divisor;
    private final ZoneOffset zoneOffet;

    TimeConverter(ChunkHeader chunkHeader, int i2) {
        this.startTicks = chunkHeader.getStartTicks();
        this.startNanos = chunkHeader.getStartNanos();
        this.divisor = chunkHeader.getTicksPerSecond() / 1.0E9d;
        this.zoneOffet = zoneOfSet(i2);
    }

    private ZoneOffset zoneOfSet(int i2) {
        try {
            return ZoneOffset.ofTotalSeconds(i2 / 1000);
        } catch (DateTimeException e2) {
            Logger.log(LogTag.JFR_SYSTEM_PARSER, LogLevel.INFO, "Could not create ZoneOffset from raw offset " + i2);
            return ZoneOffset.UTC;
        }
    }

    public long convertTimestamp(long j2) {
        return this.startNanos + ((long) ((j2 - this.startTicks) / this.divisor));
    }

    public long convertTimespan(long j2) {
        return (long) (j2 / this.divisor);
    }

    public ZoneOffset getZoneOffset() {
        return this.zoneOffet;
    }
}
