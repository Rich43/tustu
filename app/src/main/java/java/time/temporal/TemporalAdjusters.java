package java.time.temporal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.UnaryOperator;

/* loaded from: rt.jar:java/time/temporal/TemporalAdjusters.class */
public final class TemporalAdjusters {
    private TemporalAdjusters() {
    }

    public static TemporalAdjuster ofDateAdjuster(UnaryOperator<LocalDate> unaryOperator) {
        Objects.requireNonNull(unaryOperator, "dateBasedAdjuster");
        return temporal -> {
            return temporal.with((LocalDate) unaryOperator.apply(LocalDate.from((TemporalAccessor) temporal)));
        };
    }

    public static TemporalAdjuster firstDayOfMonth() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_MONTH, 1L);
        };
    }

    public static TemporalAdjuster lastDayOfMonth() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_MONTH, temporal.range(ChronoField.DAY_OF_MONTH).getMaximum());
        };
    }

    public static TemporalAdjuster firstDayOfNextMonth() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_MONTH, 1L).plus(1L, ChronoUnit.MONTHS);
        };
    }

    public static TemporalAdjuster firstDayOfYear() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_YEAR, 1L);
        };
    }

    public static TemporalAdjuster lastDayOfYear() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_YEAR, temporal.range(ChronoField.DAY_OF_YEAR).getMaximum());
        };
    }

    public static TemporalAdjuster firstDayOfNextYear() {
        return temporal -> {
            return temporal.with(ChronoField.DAY_OF_YEAR, 1L).plus(1L, ChronoUnit.YEARS);
        };
    }

    public static TemporalAdjuster firstInMonth(DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(1, dayOfWeek);
    }

    public static TemporalAdjuster lastInMonth(DayOfWeek dayOfWeek) {
        return dayOfWeekInMonth(-1, dayOfWeek);
    }

    public static TemporalAdjuster dayOfWeekInMonth(int i2, DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        int value = dayOfWeek.getValue();
        if (i2 >= 0) {
            return temporal -> {
                return temporal.with(ChronoField.DAY_OF_MONTH, 1L).plus((int) ((((value - r0.get(ChronoField.DAY_OF_WEEK)) + 7) % 7) + ((i2 - 1) * 7)), ChronoUnit.DAYS);
            };
        }
        return temporal2 -> {
            Temporal temporalWith = temporal2.with(ChronoField.DAY_OF_MONTH, temporal2.range(ChronoField.DAY_OF_MONTH).getMaximum());
            int i3 = value - temporalWith.get(ChronoField.DAY_OF_WEEK);
            return temporalWith.plus((int) ((i3 == 0 ? 0 : i3 > 0 ? i3 - 7 : i3) - (((-i2) - 1) * 7)), ChronoUnit.DAYS);
        };
    }

    public static TemporalAdjuster next(DayOfWeek dayOfWeek) {
        int value = dayOfWeek.getValue();
        return temporal -> {
            return temporal.plus(temporal.get(ChronoField.DAY_OF_WEEK) - value >= 0 ? 7 - r0 : -r0, ChronoUnit.DAYS);
        };
    }

    public static TemporalAdjuster nextOrSame(DayOfWeek dayOfWeek) {
        int value = dayOfWeek.getValue();
        return temporal -> {
            int i2 = temporal.get(ChronoField.DAY_OF_WEEK);
            if (i2 == value) {
                return temporal;
            }
            return temporal.plus(i2 - value >= 0 ? 7 - r0 : -r0, ChronoUnit.DAYS);
        };
    }

    public static TemporalAdjuster previous(DayOfWeek dayOfWeek) {
        int value = dayOfWeek.getValue();
        return temporal -> {
            return temporal.minus(value - temporal.get(ChronoField.DAY_OF_WEEK) >= 0 ? 7 - r0 : -r0, ChronoUnit.DAYS);
        };
    }

    public static TemporalAdjuster previousOrSame(DayOfWeek dayOfWeek) {
        int value = dayOfWeek.getValue();
        return temporal -> {
            int i2 = temporal.get(ChronoField.DAY_OF_WEEK);
            if (i2 == value) {
                return temporal;
            }
            return temporal.minus(value - i2 >= 0 ? 7 - r0 : -r0, ChronoUnit.DAYS);
        };
    }
}
