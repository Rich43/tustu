package java.time.temporal;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/temporal/ValueRange.class */
public final class ValueRange implements Serializable {
    private static final long serialVersionUID = -7317881728594519368L;
    private final long minSmallest;
    private final long minLargest;
    private final long maxSmallest;
    private final long maxLargest;

    public static ValueRange of(long j2, long j3) {
        if (j2 > j3) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value");
        }
        return new ValueRange(j2, j2, j3, j3);
    }

    public static ValueRange of(long j2, long j3, long j4) {
        return of(j2, j2, j3, j4);
    }

    public static ValueRange of(long j2, long j3, long j4, long j5) {
        if (j2 > j3) {
            throw new IllegalArgumentException("Smallest minimum value must be less than largest minimum value");
        }
        if (j4 > j5) {
            throw new IllegalArgumentException("Smallest maximum value must be less than largest maximum value");
        }
        if (j3 > j5) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value");
        }
        return new ValueRange(j2, j3, j4, j5);
    }

    private ValueRange(long j2, long j3, long j4, long j5) {
        this.minSmallest = j2;
        this.minLargest = j3;
        this.maxSmallest = j4;
        this.maxLargest = j5;
    }

    public boolean isFixed() {
        return this.minSmallest == this.minLargest && this.maxSmallest == this.maxLargest;
    }

    public long getMinimum() {
        return this.minSmallest;
    }

    public long getLargestMinimum() {
        return this.minLargest;
    }

    public long getSmallestMaximum() {
        return this.maxSmallest;
    }

    public long getMaximum() {
        return this.maxLargest;
    }

    public boolean isIntValue() {
        return getMinimum() >= -2147483648L && getMaximum() <= 2147483647L;
    }

    public boolean isValidValue(long j2) {
        return j2 >= getMinimum() && j2 <= getMaximum();
    }

    public boolean isValidIntValue(long j2) {
        return isIntValue() && isValidValue(j2);
    }

    public long checkValidValue(long j2, TemporalField temporalField) {
        if (!isValidValue(j2)) {
            throw new DateTimeException(genInvalidFieldMessage(temporalField, j2));
        }
        return j2;
    }

    public int checkValidIntValue(long j2, TemporalField temporalField) {
        if (!isValidIntValue(j2)) {
            throw new DateTimeException(genInvalidFieldMessage(temporalField, j2));
        }
        return (int) j2;
    }

    private String genInvalidFieldMessage(TemporalField temporalField, long j2) {
        if (temporalField != null) {
            return "Invalid value for " + ((Object) temporalField) + " (valid values " + ((Object) this) + "): " + j2;
        }
        return "Invalid value (valid values " + ((Object) this) + "): " + j2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.minSmallest > this.minLargest) {
            throw new InvalidObjectException("Smallest minimum value must be less than largest minimum value");
        }
        if (this.maxSmallest > this.maxLargest) {
            throw new InvalidObjectException("Smallest maximum value must be less than largest maximum value");
        }
        if (this.minLargest > this.maxLargest) {
            throw new InvalidObjectException("Minimum value must be less than maximum value");
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ValueRange) {
            ValueRange valueRange = (ValueRange) obj;
            return this.minSmallest == valueRange.minSmallest && this.minLargest == valueRange.minLargest && this.maxSmallest == valueRange.maxSmallest && this.maxLargest == valueRange.maxLargest;
        }
        return false;
    }

    public int hashCode() {
        long j2 = this.minSmallest + (this.minLargest << 16) + (this.minLargest >> 48) + (this.maxSmallest << 32) + (this.maxSmallest >> 32) + (this.maxLargest << 48) + (this.maxLargest >> 16);
        return (int) (j2 ^ (j2 >>> 32));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.minSmallest);
        if (this.minSmallest != this.minLargest) {
            sb.append('/').append(this.minLargest);
        }
        sb.append(" - ").append(this.maxSmallest);
        if (this.maxSmallest != this.maxLargest) {
            sb.append('/').append(this.maxLargest);
        }
        return sb.toString();
    }
}
