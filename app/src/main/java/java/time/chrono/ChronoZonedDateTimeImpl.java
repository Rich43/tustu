package java.time.chrono;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.List;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/ChronoZonedDateTimeImpl.class */
final class ChronoZonedDateTimeImpl<D extends ChronoLocalDate> implements ChronoZonedDateTime<D>, Serializable {
    private static final long serialVersionUID = -5261813987200935591L;
    private final transient ChronoLocalDateTimeImpl<D> dateTime;
    private final transient ZoneOffset offset;
    private final transient ZoneId zone;

    static <R extends ChronoLocalDate> ChronoZonedDateTime<R> ofBest(ChronoLocalDateTimeImpl<R> chronoLocalDateTimeImpl, ZoneId zoneId, ZoneOffset zoneOffset) {
        ZoneOffset offsetAfter;
        Objects.requireNonNull(chronoLocalDateTimeImpl, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, (ZoneOffset) zoneId, zoneId);
        }
        ZoneRules rules = zoneId.getRules();
        LocalDateTime localDateTimeFrom = LocalDateTime.from((TemporalAccessor) chronoLocalDateTimeImpl);
        List<ZoneOffset> validOffsets = rules.getValidOffsets(localDateTimeFrom);
        if (validOffsets.size() == 1) {
            offsetAfter = validOffsets.get(0);
        } else if (validOffsets.size() == 0) {
            ZoneOffsetTransition transition = rules.getTransition(localDateTimeFrom);
            chronoLocalDateTimeImpl = chronoLocalDateTimeImpl.plusSeconds(transition.getDuration().getSeconds());
            offsetAfter = transition.getOffsetAfter();
        } else if (zoneOffset != null && validOffsets.contains(zoneOffset)) {
            offsetAfter = zoneOffset;
        } else {
            offsetAfter = validOffsets.get(0);
        }
        Objects.requireNonNull(offsetAfter, "offset");
        return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, offsetAfter, zoneId);
    }

    static ChronoZonedDateTimeImpl<?> ofInstant(Chronology chronology, Instant instant, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(instant);
        Objects.requireNonNull(offset, "offset");
        return new ChronoZonedDateTimeImpl<>((ChronoLocalDateTimeImpl) chronology.localDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset)), offset, zoneId);
    }

    private ChronoZonedDateTimeImpl<D> create(Instant instant, ZoneId zoneId) {
        return (ChronoZonedDateTimeImpl<D>) ofInstant(getChronology(), instant, zoneId);
    }

    static <R extends ChronoLocalDate> ChronoZonedDateTimeImpl<R> ensureValid(Chronology chronology, Temporal temporal) {
        ChronoZonedDateTimeImpl<R> chronoZonedDateTimeImpl = (ChronoZonedDateTimeImpl) temporal;
        if (!chronology.equals(chronoZonedDateTimeImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, required: " + chronology.getId() + ", actual: " + chronoZonedDateTimeImpl.getChronology().getId());
        }
        return chronoZonedDateTimeImpl;
    }

    private ChronoZonedDateTimeImpl(ChronoLocalDateTimeImpl<D> chronoLocalDateTimeImpl, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.dateTime = (ChronoLocalDateTimeImpl) Objects.requireNonNull(chronoLocalDateTimeImpl, SchemaSymbols.ATTVAL_DATETIME);
        this.offset = (ZoneOffset) Objects.requireNonNull(zoneOffset, "offset");
        this.zone = (ZoneId) Objects.requireNonNull(zoneId, "zone");
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ZoneOffset getOffset() {
        return this.offset;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ChronoZonedDateTime<D> withEarlierOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from((TemporalAccessor) this));
        if (transition != null && transition.isOverlap()) {
            ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ChronoZonedDateTime<D> withLaterOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from((TemporalAccessor) this));
        if (transition != null) {
            ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(getOffset())) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ChronoLocalDateTime<D> toLocalDateTime() {
        return this.dateTime;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ZoneId getZone() {
        return this.zone;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId) {
        return ofBest(this.dateTime, zoneId, this.offset);
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toInstant(this.offset), zoneId);
    }

    @Override // java.time.chrono.ChronoZonedDateTime, java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return (temporalField instanceof ChronoField) || (temporalField != null && temporalField.isSupportedBy(this));
    }

    @Override // java.time.chrono.ChronoZonedDateTime, java.time.temporal.Temporal
    public ChronoZonedDateTime<D> with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            switch (chronoField) {
                case INSTANT_SECONDS:
                    return plus(j2 - toEpochSecond(), (TemporalUnit) ChronoUnit.SECONDS);
                case OFFSET_SECONDS:
                    return create(this.dateTime.toInstant(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j2))), this.zone);
                default:
                    return ofBest(this.dateTime.with(temporalField, j2), this.zone, this.offset);
            }
        }
        return ensureValid(getChronology(), temporalField.adjustInto(this, j2));
    }

    @Override // java.time.chrono.ChronoZonedDateTime, java.time.temporal.Temporal
    public ChronoZonedDateTime<D> plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with((TemporalAdjuster) this.dateTime.plus(j2, temporalUnit));
        }
        return ensureValid(getChronology(), temporalUnit.addTo(this, j2));
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        ChronoZonedDateTime<? extends ChronoLocalDate> chronoZonedDateTimeZonedDateTime = getChronology().zonedDateTime(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return this.dateTime.until(chronoZonedDateTimeZonedDateTime.withZoneSameInstant(this.offset).toLocalDateTime(), temporalUnit);
        }
        Objects.requireNonNull(temporalUnit, "unit");
        return temporalUnit.between(this, chronoZonedDateTimeZonedDateTime);
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.dateTime);
        objectOutput.writeObject(this.offset);
        objectOutput.writeObject(this.zone);
    }

    static ChronoZonedDateTime<?> readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        ChronoLocalDateTime chronoLocalDateTime = (ChronoLocalDateTime) objectInput.readObject();
        ZoneOffset zoneOffset = (ZoneOffset) objectInput.readObject();
        return chronoLocalDateTime.atZone(zoneOffset).withZoneSameLocal((ZoneId) objectInput.readObject());
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ChronoZonedDateTime) && compareTo((ChronoZonedDateTime<?>) obj) == 0;
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public int hashCode() {
        return (toLocalDateTime().hashCode() ^ getOffset().hashCode()) ^ Integer.rotateLeft(getZone().hashCode(), 3);
    }

    @Override // java.time.chrono.ChronoZonedDateTime
    public String toString() {
        String str = toLocalDateTime().toString() + getOffset().toString();
        if (getOffset() != getZone()) {
            str = str + '[' + getZone().toString() + ']';
        }
        return str;
    }
}
