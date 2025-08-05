package org.apache.commons.math3.dfp;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/DfpDec.class */
public class DfpDec extends Dfp {
    protected DfpDec(DfpField factory) {
        super(factory);
    }

    protected DfpDec(DfpField factory, byte x2) {
        super(factory, x2);
    }

    protected DfpDec(DfpField factory, int x2) {
        super(factory, x2);
    }

    protected DfpDec(DfpField factory, long x2) {
        super(factory, x2);
    }

    protected DfpDec(DfpField factory, double x2) {
        super(factory, x2);
        round(0);
    }

    public DfpDec(Dfp d2) {
        super(d2);
        round(0);
    }

    protected DfpDec(DfpField factory, String s2) {
        super(factory, s2);
        round(0);
    }

    protected DfpDec(DfpField factory, byte sign, byte nans) {
        super(factory, sign, nans);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance() {
        return new DfpDec(getField2());
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(byte x2) {
        return new DfpDec(getField2(), x2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(int x2) {
        return new DfpDec(getField2(), x2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(long x2) {
        return new DfpDec(getField2(), x2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(double x2) {
        return new DfpDec(getField2(), x2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(Dfp d2) {
        if (getField2().getRadixDigits() != d2.getField2().getRadixDigits()) {
            getField2().setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, "newInstance", d2, result);
        }
        return new DfpDec(d2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(String s2) {
        return new DfpDec(getField2(), s2);
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp newInstance(byte sign, byte nans) {
        return new DfpDec(getField2(), sign, nans);
    }

    protected int getDecimalDigits() {
        return (getRadixDigits() * 4) - 3;
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    protected int round(int in) {
        int n2;
        int discarded;
        boolean inc;
        int msb = this.mant[this.mant.length - 1];
        if (msb == 0) {
            return 0;
        }
        int cmaxdigits = this.mant.length * 4;
        int lsbthreshold = 1000;
        while (lsbthreshold > msb) {
            lsbthreshold /= 10;
            cmaxdigits--;
        }
        int digits = getDecimalDigits();
        int lsbshift = cmaxdigits - digits;
        int lsd = lsbshift / 4;
        int lsbthreshold2 = 1;
        for (int i2 = 0; i2 < lsbshift % 4; i2++) {
            lsbthreshold2 *= 10;
        }
        int lsb = this.mant[lsd];
        if (lsbthreshold2 <= 1 && digits == (4 * this.mant.length) - 3) {
            return super.round(in);
        }
        if (lsbthreshold2 == 1) {
            n2 = (this.mant[lsd - 1] / 1000) % 10;
            int[] iArr = this.mant;
            int i3 = lsd - 1;
            iArr[i3] = iArr[i3] % 1000;
            discarded = in | this.mant[lsd - 1];
        } else {
            n2 = ((lsb * 10) / lsbthreshold2) % 10;
            discarded = in | (lsb % (lsbthreshold2 / 10));
        }
        for (int i4 = 0; i4 < lsd; i4++) {
            discarded |= this.mant[i4];
            this.mant[i4] = 0;
        }
        this.mant[lsd] = (lsb / lsbthreshold2) * lsbthreshold2;
        switch (getField2().getRoundingMode()) {
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                inc = (n2 == 0 && discarded == 0) ? false : true;
                break;
            case ROUND_HALF_UP:
                inc = n2 >= 5;
                break;
            case ROUND_HALF_DOWN:
                inc = n2 > 5;
                break;
            case ROUND_HALF_EVEN:
                inc = n2 > 5 || (n2 == 5 && discarded != 0) || (n2 == 5 && discarded == 0 && ((lsb / lsbthreshold2) & 1) == 1);
                break;
            case ROUND_HALF_ODD:
                inc = n2 > 5 || (n2 == 5 && discarded != 0) || (n2 == 5 && discarded == 0 && ((lsb / lsbthreshold2) & 1) == 0);
                break;
            case ROUND_CEIL:
                inc = this.sign == 1 && !(n2 == 0 && discarded == 0);
                break;
            case ROUND_FLOOR:
            default:
                inc = this.sign == -1 && !(n2 == 0 && discarded == 0);
                break;
        }
        if (inc) {
            int rh = lsbthreshold2;
            for (int i5 = lsd; i5 < this.mant.length; i5++) {
                int r2 = this.mant[i5] + rh;
                rh = r2 / 10000;
                this.mant[i5] = r2 % 10000;
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        if (this.exp < -32767) {
            getField2().setIEEEFlagsBits(8);
            return 8;
        }
        if (this.exp > 32768) {
            getField2().setIEEEFlagsBits(4);
            return 4;
        }
        if (n2 != 0 || discarded != 0) {
            getField2().setIEEEFlagsBits(16);
            return 16;
        }
        return 0;
    }

    @Override // org.apache.commons.math3.dfp.Dfp
    public Dfp nextAfter(Dfp x2) {
        Dfp inc;
        Dfp result;
        if (getField2().getRadixDigits() != x2.getField2().getRadixDigits()) {
            getField2().setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, "nextAfter", x2, result2);
        }
        boolean up = false;
        if (lessThan(x2)) {
            up = true;
        }
        if (equals(x2)) {
            return newInstance(x2);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            Dfp inc2 = copysign(power10((intLog10() - getDecimalDigits()) + 1), this);
            if (equals(getZero())) {
                inc2 = power10K((Dfp.MIN_EXP - this.mant.length) - 1);
            }
            if (inc2.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = add(inc2);
            }
        } else {
            Dfp inc3 = copysign(power10(intLog10()), this);
            if (equals(inc3)) {
                inc = inc3.divide(power10(getDecimalDigits()));
            } else {
                inc = inc3.divide(power10(getDecimalDigits() - 1));
            }
            if (equals(getZero())) {
                inc = power10K((Dfp.MIN_EXP - this.mant.length) - 1);
            }
            if (inc.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = subtract(inc);
            }
        }
        if (result.classify() == 1 && classify() != 1) {
            getField2().setIEEEFlagsBits(16);
            result = dotrap(16, "nextAfter", x2, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            getField2().setIEEEFlagsBits(16);
            result = dotrap(16, "nextAfter", x2, result);
        }
        return result;
    }
}
