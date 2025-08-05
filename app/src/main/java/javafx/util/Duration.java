package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:javafx/util/Duration.class */
public class Duration implements Comparable<Duration>, Serializable {
    public static final Duration ZERO = new Duration(0.0d);
    public static final Duration ONE = new Duration(1.0d);
    public static final Duration INDEFINITE = new Duration(Double.POSITIVE_INFINITY);
    public static final Duration UNKNOWN = new Duration(Double.NaN);
    private final double millis;

    public static Duration valueOf(String time) throws NumberFormatException {
        int index = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= time.length()) {
                break;
            }
            char c2 = time.charAt(i2);
            if (Character.isDigit(c2) || c2 == '.' || c2 == '-') {
                i2++;
            } else {
                index = i2;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
        }
        double value = Double.parseDouble(time.substring(0, index));
        String suffix = time.substring(index);
        if ("ms".equals(suffix)) {
            return millis(value);
        }
        if (PdfOps.s_TOKEN.equals(suffix)) {
            return seconds(value);
        }
        if (PdfOps.m_TOKEN.equals(suffix)) {
            return minutes(value);
        }
        if (PdfOps.h_TOKEN.equals(suffix)) {
            return hours(value);
        }
        throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
    }

    public static Duration millis(double ms) {
        if (ms == 0.0d) {
            return ZERO;
        }
        if (ms == 1.0d) {
            return ONE;
        }
        if (ms == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(ms)) {
            return UNKNOWN;
        }
        return new Duration(ms);
    }

    public static Duration seconds(double s2) {
        if (s2 == 0.0d) {
            return ZERO;
        }
        if (s2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(s2)) {
            return UNKNOWN;
        }
        return new Duration(s2 * 1000.0d);
    }

    public static Duration minutes(double m2) {
        if (m2 == 0.0d) {
            return ZERO;
        }
        if (m2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(m2)) {
            return UNKNOWN;
        }
        return new Duration(m2 * 60000.0d);
    }

    public static Duration hours(double h2) {
        if (h2 == 0.0d) {
            return ZERO;
        }
        if (h2 == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        }
        if (Double.isNaN(h2)) {
            return UNKNOWN;
        }
        return new Duration(h2 * 3600000.0d);
    }

    public Duration(@NamedArg("millis") double millis) {
        this.millis = millis;
    }

    public double toMillis() {
        return this.millis;
    }

    public double toSeconds() {
        return this.millis / 1000.0d;
    }

    public double toMinutes() {
        return this.millis / 60000.0d;
    }

    public double toHours() {
        return this.millis / 3600000.0d;
    }

    public Duration add(Duration other) {
        return millis(this.millis + other.millis);
    }

    public Duration subtract(Duration other) {
        return millis(this.millis - other.millis);
    }

    @Deprecated
    public Duration multiply(Duration other) {
        return millis(this.millis * other.millis);
    }

    public Duration multiply(double n2) {
        return millis(this.millis * n2);
    }

    public Duration divide(double n2) {
        return millis(this.millis / n2);
    }

    @Deprecated
    public Duration divide(Duration other) {
        return millis(this.millis / other.millis);
    }

    public Duration negate() {
        return millis(-this.millis);
    }

    public boolean isIndefinite() {
        return this.millis == Double.POSITIVE_INFINITY;
    }

    public boolean isUnknown() {
        return Double.isNaN(this.millis);
    }

    public boolean lessThan(Duration other) {
        return this.millis < other.millis;
    }

    public boolean lessThanOrEqualTo(Duration other) {
        return this.millis <= other.millis;
    }

    public boolean greaterThan(Duration other) {
        return this.millis > other.millis;
    }

    public boolean greaterThanOrEqualTo(Duration other) {
        return this.millis >= other.millis;
    }

    public String toString() {
        return isIndefinite() ? "INDEFINITE" : isUnknown() ? "UNKNOWN" : this.millis + " ms";
    }

    @Override // java.lang.Comparable
    public int compareTo(Duration d2) {
        return Double.compare(this.millis, d2.millis);
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Duration) && this.millis == ((Duration) obj).millis);
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(this.millis);
        return (int) (bits ^ (bits >>> 32));
    }
}
