package java.time.format;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* loaded from: rt.jar:java/time/format/Parsed.class */
final class Parsed implements TemporalAccessor {
    ZoneId zone;
    Chronology chrono;
    boolean leapSecond;
    private ResolverStyle resolverStyle;
    private ChronoLocalDate date;
    private LocalTime time;
    final Map<TemporalField, Long> fieldValues = new HashMap();
    Period excessDays = Period.ZERO;

    Parsed() {
    }

    Parsed copy() {
        Parsed parsed = new Parsed();
        parsed.fieldValues.putAll(this.fieldValues);
        parsed.zone = this.zone;
        parsed.chrono = this.chrono;
        parsed.leapSecond = this.leapSecond;
        return parsed;
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        if (!this.fieldValues.containsKey(temporalField)) {
            if (this.date == null || !this.date.isSupported(temporalField)) {
                if (this.time == null || !this.time.isSupported(temporalField)) {
                    return (temporalField == null || (temporalField instanceof ChronoField) || !temporalField.isSupportedBy(this)) ? false : true;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        Long l2 = this.fieldValues.get(temporalField);
        if (l2 != null) {
            return l2.longValue();
        }
        if (this.date != null && this.date.isSupported(temporalField)) {
            return this.date.getLong(temporalField);
        }
        if (this.time != null && this.time.isSupported(temporalField)) {
            return this.time.getLong(temporalField);
        }
        if (temporalField instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return temporalField.getFrom(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId()) {
            return (R) this.zone;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R) this.chrono;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            if (this.date != null) {
                return (R) LocalDate.from((TemporalAccessor) this.date);
            }
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R) this.time;
        }
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return temporalQuery.queryFrom(this);
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return null;
        }
        return temporalQuery.queryFrom(this);
    }

    TemporalAccessor resolve(ResolverStyle resolverStyle, Set<TemporalField> set) {
        if (set != null) {
            this.fieldValues.keySet().retainAll(set);
        }
        this.resolverStyle = resolverStyle;
        resolveFields();
        resolveTimeLenient();
        crossCheck();
        resolvePeriod();
        resolveFractional();
        resolveInstant();
        return this;
    }

    private void resolveFields() {
        resolveInstantFields();
        resolveDateFields();
        resolveTimeFields();
        if (this.fieldValues.size() > 0) {
            int i2 = 0;
            loop0: while (i2 < 50) {
                Iterator<Map.Entry<TemporalField, Long>> it = this.fieldValues.entrySet().iterator();
                while (it.hasNext()) {
                    TemporalField key = it.next().getKey();
                    Object objResolve = key.resolve(this.fieldValues, this, this.resolverStyle);
                    if (objResolve != null) {
                        if (objResolve instanceof ChronoZonedDateTime) {
                            ChronoZonedDateTime chronoZonedDateTime = (ChronoZonedDateTime) objResolve;
                            if (this.zone == null) {
                                this.zone = chronoZonedDateTime.getZone();
                            } else if (!this.zone.equals(chronoZonedDateTime.getZone())) {
                                throw new DateTimeException("ChronoZonedDateTime must use the effective parsed zone: " + ((Object) this.zone));
                            }
                            objResolve = chronoZonedDateTime.toLocalDateTime();
                        }
                        if (objResolve instanceof ChronoLocalDateTime) {
                            ChronoLocalDateTime chronoLocalDateTime = (ChronoLocalDateTime) objResolve;
                            updateCheckConflict(chronoLocalDateTime.toLocalTime(), Period.ZERO);
                            updateCheckConflict(chronoLocalDateTime.toLocalDate());
                            i2++;
                        } else if (objResolve instanceof ChronoLocalDate) {
                            updateCheckConflict((ChronoLocalDate) objResolve);
                            i2++;
                        } else if (objResolve instanceof LocalTime) {
                            updateCheckConflict((LocalTime) objResolve, Period.ZERO);
                            i2++;
                        } else {
                            throw new DateTimeException("Method resolve() can only return ChronoZonedDateTime, ChronoLocalDateTime, ChronoLocalDate or LocalTime");
                        }
                    } else if (!this.fieldValues.containsKey(key)) {
                        i2++;
                    }
                }
            }
            if (i2 == 50) {
                throw new DateTimeException("One of the parsed fields has an incorrectly implemented resolve method");
            }
            if (i2 > 0) {
                resolveInstantFields();
                resolveDateFields();
                resolveTimeFields();
            }
        }
    }

    private void updateCheckConflict(TemporalField temporalField, TemporalField temporalField2, Long l2) {
        Long lPut = this.fieldValues.put(temporalField2, l2);
        if (lPut != null && lPut.longValue() != l2.longValue()) {
            throw new DateTimeException("Conflict found: " + ((Object) temporalField2) + " " + ((Object) lPut) + " differs from " + ((Object) temporalField2) + " " + ((Object) l2) + " while resolving  " + ((Object) temporalField));
        }
    }

    private void resolveInstantFields() {
        if (this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS)) {
            if (this.zone != null) {
                resolveInstantFields0(this.zone);
                return;
            }
            Long l2 = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
            if (l2 != null) {
                resolveInstantFields0(ZoneOffset.ofTotalSeconds(l2.intValue()));
            }
        }
    }

    private void resolveInstantFields0(ZoneId zoneId) {
        updateCheckConflict(this.chrono.zonedDateTime(Instant.ofEpochSecond(this.fieldValues.remove(ChronoField.INSTANT_SECONDS).longValue()), zoneId).toLocalDate());
        updateCheckConflict(ChronoField.INSTANT_SECONDS, ChronoField.SECOND_OF_DAY, Long.valueOf(r0.toLocalTime().toSecondOfDay()));
    }

    private void resolveDateFields() {
        updateCheckConflict(this.chrono.resolveDate(this.fieldValues, this.resolverStyle));
    }

    private void updateCheckConflict(ChronoLocalDate chronoLocalDate) {
        if (this.date != null) {
            if (chronoLocalDate != null && !this.date.equals(chronoLocalDate)) {
                throw new DateTimeException("Conflict found: Fields resolved to two different dates: " + ((Object) this.date) + " " + ((Object) chronoLocalDate));
            }
        } else if (chronoLocalDate != null) {
            if (!this.chrono.equals(chronoLocalDate.getChronology())) {
                throw new DateTimeException("ChronoLocalDate must use the effective parsed chronology: " + ((Object) this.chrono));
            }
            this.date = chronoLocalDate;
        }
    }

    private void resolveTimeFields() {
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_DAY)) {
            long jLongValue = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_DAY).longValue();
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && jLongValue != 0)) {
                ChronoField.CLOCK_HOUR_OF_DAY.checkValidValue(jLongValue);
            }
            updateCheckConflict(ChronoField.CLOCK_HOUR_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(jLongValue == 24 ? 0L : jLongValue));
        }
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_AMPM)) {
            long jLongValue2 = this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_AMPM).longValue();
            if (this.resolverStyle == ResolverStyle.STRICT || (this.resolverStyle == ResolverStyle.SMART && jLongValue2 != 0)) {
                ChronoField.CLOCK_HOUR_OF_AMPM.checkValidValue(jLongValue2);
            }
            updateCheckConflict(ChronoField.CLOCK_HOUR_OF_AMPM, ChronoField.HOUR_OF_AMPM, Long.valueOf(jLongValue2 == 12 ? 0L : jLongValue2));
        }
        if (this.fieldValues.containsKey(ChronoField.AMPM_OF_DAY) && this.fieldValues.containsKey(ChronoField.HOUR_OF_AMPM)) {
            long jLongValue3 = this.fieldValues.remove(ChronoField.AMPM_OF_DAY).longValue();
            long jLongValue4 = this.fieldValues.remove(ChronoField.HOUR_OF_AMPM).longValue();
            if (this.resolverStyle == ResolverStyle.LENIENT) {
                updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(Math.addExact(Math.multiplyExact(jLongValue3, 12L), jLongValue4)));
            } else {
                ChronoField.AMPM_OF_DAY.checkValidValue(jLongValue3);
                ChronoField.HOUR_OF_AMPM.checkValidValue(jLongValue3);
                updateCheckConflict(ChronoField.AMPM_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf((jLongValue3 * 12) + jLongValue4));
            }
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_DAY)) {
            long jLongValue5 = this.fieldValues.remove(ChronoField.NANO_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_DAY.checkValidValue(jLongValue5);
            }
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(jLongValue5 / 3600000000000L));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf((jLongValue5 / 60000000000L) % 60));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.SECOND_OF_MINUTE, Long.valueOf((jLongValue5 / NativeMediaPlayer.ONE_SECOND) % 60));
            updateCheckConflict(ChronoField.NANO_OF_DAY, ChronoField.NANO_OF_SECOND, Long.valueOf(jLongValue5 % NativeMediaPlayer.ONE_SECOND));
        }
        if (this.fieldValues.containsKey(ChronoField.MICRO_OF_DAY)) {
            long jLongValue6 = this.fieldValues.remove(ChronoField.MICRO_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MICRO_OF_DAY.checkValidValue(jLongValue6);
            }
            updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.SECOND_OF_DAY, Long.valueOf(jLongValue6 / 1000000));
            updateCheckConflict(ChronoField.MICRO_OF_DAY, ChronoField.MICRO_OF_SECOND, Long.valueOf(jLongValue6 % 1000000));
        }
        if (this.fieldValues.containsKey(ChronoField.MILLI_OF_DAY)) {
            long jLongValue7 = this.fieldValues.remove(ChronoField.MILLI_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MILLI_OF_DAY.checkValidValue(jLongValue7);
            }
            updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.SECOND_OF_DAY, Long.valueOf(jLongValue7 / 1000));
            updateCheckConflict(ChronoField.MILLI_OF_DAY, ChronoField.MILLI_OF_SECOND, Long.valueOf(jLongValue7 % 1000));
        }
        if (this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY)) {
            long jLongValue8 = this.fieldValues.remove(ChronoField.SECOND_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.SECOND_OF_DAY.checkValidValue(jLongValue8);
            }
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(jLongValue8 / 3600));
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf((jLongValue8 / 60) % 60));
            updateCheckConflict(ChronoField.SECOND_OF_DAY, ChronoField.SECOND_OF_MINUTE, Long.valueOf(jLongValue8 % 60));
        }
        if (this.fieldValues.containsKey(ChronoField.MINUTE_OF_DAY)) {
            long jLongValue9 = this.fieldValues.remove(ChronoField.MINUTE_OF_DAY).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MINUTE_OF_DAY.checkValidValue(jLongValue9);
            }
            updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.HOUR_OF_DAY, Long.valueOf(jLongValue9 / 60));
            updateCheckConflict(ChronoField.MINUTE_OF_DAY, ChronoField.MINUTE_OF_HOUR, Long.valueOf(jLongValue9 % 60));
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            long jLongValue10 = this.fieldValues.get(ChronoField.NANO_OF_SECOND).longValue();
            if (this.resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_SECOND.checkValidValue(jLongValue10);
            }
            if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                long jLongValue11 = this.fieldValues.remove(ChronoField.MICRO_OF_SECOND).longValue();
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MICRO_OF_SECOND.checkValidValue(jLongValue11);
                }
                jLongValue10 = (jLongValue11 * 1000) + (jLongValue10 % 1000);
                updateCheckConflict(ChronoField.MICRO_OF_SECOND, ChronoField.NANO_OF_SECOND, Long.valueOf(jLongValue10));
            }
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                long jLongValue12 = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND).longValue();
                if (this.resolverStyle != ResolverStyle.LENIENT) {
                    ChronoField.MILLI_OF_SECOND.checkValidValue(jLongValue12);
                }
                updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.NANO_OF_SECOND, Long.valueOf((jLongValue12 * 1000000) + (jLongValue10 % 1000000)));
            }
        }
        if (this.fieldValues.containsKey(ChronoField.HOUR_OF_DAY) && this.fieldValues.containsKey(ChronoField.MINUTE_OF_HOUR) && this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE) && this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            resolveTime(this.fieldValues.remove(ChronoField.HOUR_OF_DAY).longValue(), this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR).longValue(), this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE).longValue(), this.fieldValues.remove(ChronoField.NANO_OF_SECOND).longValue());
        }
    }

    private void resolveTimeLenient() {
        if (this.time == null) {
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND)) {
                long jLongValue = this.fieldValues.remove(ChronoField.MILLI_OF_SECOND).longValue();
                if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                    long jLongValue2 = (jLongValue * 1000) + (this.fieldValues.get(ChronoField.MICRO_OF_SECOND).longValue() % 1000);
                    updateCheckConflict(ChronoField.MILLI_OF_SECOND, ChronoField.MICRO_OF_SECOND, Long.valueOf(jLongValue2));
                    this.fieldValues.remove(ChronoField.MICRO_OF_SECOND);
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(jLongValue2 * 1000));
                } else {
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(jLongValue * 1000000));
                }
            } else if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND)) {
                this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(this.fieldValues.remove(ChronoField.MICRO_OF_SECOND).longValue() * 1000));
            }
            Long l2 = this.fieldValues.get(ChronoField.HOUR_OF_DAY);
            if (l2 != null) {
                Long l3 = this.fieldValues.get(ChronoField.MINUTE_OF_HOUR);
                Long l4 = this.fieldValues.get(ChronoField.SECOND_OF_MINUTE);
                Long l5 = this.fieldValues.get(ChronoField.NANO_OF_SECOND);
                if (l3 == null && (l4 != null || l5 != null)) {
                    return;
                }
                if (l3 != null && l4 == null && l5 != null) {
                    return;
                }
                resolveTime(l2.longValue(), l3 != null ? l3.longValue() : 0L, l4 != null ? l4.longValue() : 0L, l5 != null ? l5.longValue() : 0L);
                this.fieldValues.remove(ChronoField.HOUR_OF_DAY);
                this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR);
                this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE);
                this.fieldValues.remove(ChronoField.NANO_OF_SECOND);
            }
        }
        if (this.resolverStyle != ResolverStyle.LENIENT && this.fieldValues.size() > 0) {
            for (Map.Entry<TemporalField, Long> entry : this.fieldValues.entrySet()) {
                TemporalField key = entry.getKey();
                if ((key instanceof ChronoField) && key.isTimeBased()) {
                    ((ChronoField) key).checkValidValue(entry.getValue().longValue());
                }
            }
        }
    }

    private void resolveTime(long j2, long j3, long j4, long j5) {
        if (this.resolverStyle == ResolverStyle.LENIENT) {
            long jAddExact = Math.addExact(Math.addExact(Math.addExact(Math.multiplyExact(j2, 3600000000000L), Math.multiplyExact(j3, 60000000000L)), Math.multiplyExact(j4, NativeMediaPlayer.ONE_SECOND)), j5);
            updateCheckConflict(LocalTime.ofNanoOfDay(Math.floorMod(jAddExact, 86400000000000L)), Period.ofDays((int) Math.floorDiv(jAddExact, 86400000000000L)));
            return;
        }
        int iCheckValidIntValue = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(j3);
        int iCheckValidIntValue2 = ChronoField.NANO_OF_SECOND.checkValidIntValue(j5);
        if (this.resolverStyle == ResolverStyle.SMART && j2 == 24 && iCheckValidIntValue == 0 && j4 == 0 && iCheckValidIntValue2 == 0) {
            updateCheckConflict(LocalTime.MIDNIGHT, Period.ofDays(1));
        } else {
            updateCheckConflict(LocalTime.of(ChronoField.HOUR_OF_DAY.checkValidIntValue(j2), iCheckValidIntValue, ChronoField.SECOND_OF_MINUTE.checkValidIntValue(j4), iCheckValidIntValue2), Period.ZERO);
        }
    }

    private void resolvePeriod() {
        if (this.date != null && this.time != null && !this.excessDays.isZero()) {
            this.date = this.date.plus((TemporalAmount) this.excessDays);
            this.excessDays = Period.ZERO;
        }
    }

    private void resolveFractional() {
        if (this.time == null) {
            if (this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS) || this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY) || this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE)) {
                if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
                    long jLongValue = this.fieldValues.get(ChronoField.NANO_OF_SECOND).longValue();
                    this.fieldValues.put(ChronoField.MICRO_OF_SECOND, Long.valueOf(jLongValue / 1000));
                    this.fieldValues.put(ChronoField.MILLI_OF_SECOND, Long.valueOf(jLongValue / 1000000));
                } else {
                    this.fieldValues.put(ChronoField.NANO_OF_SECOND, 0L);
                    this.fieldValues.put(ChronoField.MICRO_OF_SECOND, 0L);
                    this.fieldValues.put(ChronoField.MILLI_OF_SECOND, 0L);
                }
            }
        }
    }

    private void resolveInstant() {
        if (this.date != null && this.time != null) {
            if (this.zone != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(this.zone).getLong(ChronoField.INSTANT_SECONDS)));
                return;
            }
            Long l2 = this.fieldValues.get(ChronoField.OFFSET_SECONDS);
            if (l2 != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(ZoneOffset.ofTotalSeconds(l2.intValue())).getLong(ChronoField.INSTANT_SECONDS)));
            }
        }
    }

    private void updateCheckConflict(LocalTime localTime, Period period) {
        if (this.time != null) {
            if (!this.time.equals(localTime)) {
                throw new DateTimeException("Conflict found: Fields resolved to different times: " + ((Object) this.time) + " " + ((Object) localTime));
            }
            if (!this.excessDays.isZero() && !period.isZero() && !this.excessDays.equals(period)) {
                throw new DateTimeException("Conflict found: Fields resolved to different excess periods: " + ((Object) this.excessDays) + " " + ((Object) period));
            }
            this.excessDays = period;
            return;
        }
        this.time = localTime;
        this.excessDays = period;
    }

    private void crossCheck() {
        if (this.date != null) {
            crossCheck(this.date);
        }
        if (this.time != null) {
            crossCheck(this.time);
            if (this.date != null && this.fieldValues.size() > 0) {
                crossCheck(this.date.atTime(this.time));
            }
        }
    }

    private void crossCheck(TemporalAccessor temporalAccessor) {
        Iterator<Map.Entry<TemporalField, Long>> it = this.fieldValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<TemporalField, Long> next = it.next();
            TemporalField key = next.getKey();
            if (temporalAccessor.isSupported(key)) {
                try {
                    long j2 = temporalAccessor.getLong(key);
                    long jLongValue = next.getValue().longValue();
                    if (j2 != jLongValue) {
                        throw new DateTimeException("Conflict found: Field " + ((Object) key) + " " + j2 + " differs from " + ((Object) key) + " " + jLongValue + " derived from " + ((Object) temporalAccessor));
                    }
                    it.remove();
                } catch (RuntimeException e2) {
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append((Object) this.fieldValues).append(',').append((Object) this.chrono);
        if (this.zone != null) {
            sb.append(',').append((Object) this.zone);
        }
        if (this.date != null || this.time != null) {
            sb.append(" resolved to ");
            if (this.date != null) {
                sb.append((Object) this.date);
                if (this.time != null) {
                    sb.append('T').append((Object) this.time);
                }
            } else {
                sb.append((Object) this.time);
            }
        }
        return sb.toString();
    }
}
