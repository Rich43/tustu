package org.apache.commons.math3.geometry.partitioning.utilities;

import java.util.Arrays;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/partitioning/utilities/OrderedTuple.class */
public class OrderedTuple implements Comparable<OrderedTuple> {
    private static final long SIGN_MASK = Long.MIN_VALUE;
    private static final long EXPONENT_MASK = 9218868437227405312L;
    private static final long MANTISSA_MASK = 4503599627370495L;
    private static final long IMPLICIT_ONE = 4503599627370496L;
    private double[] components;
    private int offset;
    private int lsb;
    private long[] encoding;
    private boolean posInf;
    private boolean negInf;
    private boolean nan;

    public OrderedTuple(double... components) {
        this.components = (double[]) components.clone();
        int msb = Integer.MIN_VALUE;
        this.lsb = Integer.MAX_VALUE;
        this.posInf = false;
        this.negInf = false;
        this.nan = false;
        for (int i2 = 0; i2 < components.length; i2++) {
            if (Double.isInfinite(components[i2])) {
                if (components[i2] < 0.0d) {
                    this.negInf = true;
                } else {
                    this.posInf = true;
                }
            } else if (Double.isNaN(components[i2])) {
                this.nan = true;
            } else {
                long b2 = Double.doubleToLongBits(components[i2]);
                long m2 = mantissa(b2);
                if (m2 != 0) {
                    int e2 = exponent(b2);
                    msb = FastMath.max(msb, e2 + computeMSB(m2));
                    this.lsb = FastMath.min(this.lsb, e2 + computeLSB(m2));
                }
            }
        }
        if (this.posInf && this.negInf) {
            this.posInf = false;
            this.negInf = false;
            this.nan = true;
        }
        if (this.lsb <= msb) {
            encode(msb + 16);
        } else {
            this.encoding = new long[]{0};
        }
    }

    private void encode(int minOffset) {
        this.offset = minOffset + 31;
        this.offset -= this.offset % 32;
        if (this.encoding != null && this.encoding.length == 1 && this.encoding[0] == 0) {
            return;
        }
        int neededBits = (this.offset + 1) - this.lsb;
        int neededLongs = (neededBits + 62) / 63;
        this.encoding = new long[this.components.length * neededLongs];
        int eIndex = 0;
        int shift = 62;
        long word = 0;
        int k2 = this.offset;
        while (eIndex < this.encoding.length) {
            for (int vIndex = 0; vIndex < this.components.length; vIndex++) {
                if (getBit(vIndex, k2) != 0) {
                    word |= 1 << shift;
                }
                int i2 = shift;
                shift--;
                if (i2 == 0) {
                    int i3 = eIndex;
                    eIndex++;
                    this.encoding[i3] = word;
                    word = 0;
                    shift = 62;
                }
            }
            k2--;
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(OrderedTuple ot) {
        if (this.components.length == ot.components.length) {
            if (this.nan) {
                return 1;
            }
            if (ot.nan || this.negInf || ot.posInf) {
                return -1;
            }
            if (this.posInf || ot.negInf) {
                return 1;
            }
            if (this.offset < ot.offset) {
                encode(ot.offset);
            } else if (this.offset > ot.offset) {
                ot.encode(this.offset);
            }
            int limit = FastMath.min(this.encoding.length, ot.encoding.length);
            for (int i2 = 0; i2 < limit; i2++) {
                if (this.encoding[i2] < ot.encoding[i2]) {
                    return -1;
                }
                if (this.encoding[i2] > ot.encoding[i2]) {
                    return 1;
                }
            }
            if (this.encoding.length < ot.encoding.length) {
                return -1;
            }
            if (this.encoding.length > ot.encoding.length) {
                return 1;
            }
            return 0;
        }
        return this.components.length - ot.components.length;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof OrderedTuple) && compareTo((OrderedTuple) other) == 0;
    }

    public int hashCode() {
        int hash = Arrays.hashCode(this.components);
        return (((((((((hash * 37) + this.offset) * 37) + this.lsb) * 37) + (this.posInf ? 97 : 71)) * 37) + (this.negInf ? 97 : 71)) * 37) + (this.nan ? 97 : 71);
    }

    public double[] getComponents() {
        return (double[]) this.components.clone();
    }

    private static long sign(long bits) {
        return bits & Long.MIN_VALUE;
    }

    private static int exponent(long bits) {
        return ((int) ((bits & 9218868437227405312L) >> 52)) - 1075;
    }

    private static long mantissa(long bits) {
        return (bits & 9218868437227405312L) == 0 ? (bits & 4503599627370495L) << 1 : IMPLICIT_ONE | (bits & 4503599627370495L);
    }

    private static int computeMSB(long l2) {
        long ll = l2;
        long mask = 4294967295L;
        int scale = 32;
        int msb = 0;
        while (scale != 0) {
            if ((ll & mask) != ll) {
                msb |= scale;
                ll >>= scale;
            }
            scale >>= 1;
            mask >>= scale;
        }
        return msb;
    }

    private static int computeLSB(long l2) {
        long ll = l2;
        long mask = -4294967296L;
        int scale = 32;
        int lsb = 0;
        while (scale != 0) {
            if ((ll & mask) == ll) {
                lsb |= scale;
                ll >>= scale;
            }
            scale >>= 1;
            mask >>= scale;
        }
        return lsb;
    }

    private int getBit(int i2, int k2) {
        long bits = Double.doubleToLongBits(this.components[i2]);
        int e2 = exponent(bits);
        if (k2 < e2 || k2 > this.offset) {
            return 0;
        }
        if (k2 == this.offset) {
            return sign(bits) == 0 ? 1 : 0;
        }
        if (k2 > e2 + 52) {
            return sign(bits) == 0 ? 0 : 1;
        }
        long m2 = sign(bits) == 0 ? mantissa(bits) : -mantissa(bits);
        return (int) ((m2 >> (k2 - e2)) & 1);
    }
}
