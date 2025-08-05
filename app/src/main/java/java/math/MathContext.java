package java.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/* loaded from: rt.jar:java/math/MathContext.class */
public final class MathContext implements Serializable {
    private static final int DEFAULT_DIGITS = 9;
    private static final int MIN_DIGITS = 0;
    private static final long serialVersionUID = 5579720004786848255L;
    final int precision;
    final RoundingMode roundingMode;
    private static final RoundingMode DEFAULT_ROUNDINGMODE = RoundingMode.HALF_UP;
    public static final MathContext UNLIMITED = new MathContext(0, RoundingMode.HALF_UP);
    public static final MathContext DECIMAL32 = new MathContext(7, RoundingMode.HALF_EVEN);
    public static final MathContext DECIMAL64 = new MathContext(16, RoundingMode.HALF_EVEN);
    public static final MathContext DECIMAL128 = new MathContext(34, RoundingMode.HALF_EVEN);

    public MathContext(int i2) {
        this(i2, DEFAULT_ROUNDINGMODE);
    }

    public MathContext(int i2, RoundingMode roundingMode) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Digits < 0");
        }
        if (roundingMode == null) {
            throw new NullPointerException("null RoundingMode");
        }
        this.precision = i2;
        this.roundingMode = roundingMode;
    }

    public MathContext(String str) {
        if (str == null) {
            throw new NullPointerException("null String");
        }
        try {
            if (!str.startsWith("precision=")) {
                throw new RuntimeException();
            }
            int iIndexOf = str.indexOf(32);
            int i2 = Integer.parseInt(str.substring(10, iIndexOf));
            if (!str.startsWith("roundingMode=", iIndexOf + 1)) {
                throw new RuntimeException();
            }
            this.roundingMode = RoundingMode.valueOf(str.substring(iIndexOf + 1 + 13, str.length()));
            if (i2 < 0) {
                throw new IllegalArgumentException("Digits < 0");
            }
            this.precision = i2;
        } catch (RuntimeException e2) {
            throw new IllegalArgumentException("bad string format");
        }
    }

    public int getPrecision() {
        return this.precision;
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MathContext)) {
            return false;
        }
        MathContext mathContext = (MathContext) obj;
        return mathContext.precision == this.precision && mathContext.roundingMode == this.roundingMode;
    }

    public int hashCode() {
        return this.precision + (this.roundingMode.hashCode() * 59);
    }

    public String toString() {
        return "precision=" + this.precision + " roundingMode=" + this.roundingMode.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.precision < 0) {
            throw new StreamCorruptedException("MathContext: invalid digits in stream");
        }
        if (this.roundingMode == null) {
            throw new StreamCorruptedException("MathContext: null roundingMode in stream");
        }
    }
}
