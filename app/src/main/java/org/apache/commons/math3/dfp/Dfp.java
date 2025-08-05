package org.apache.commons.math3.dfp;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.util.Arrays;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import sun.misc.DoubleConsts;
import sun.util.locale.LanguageTag;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/Dfp.class */
public class Dfp implements RealFieldElement<Dfp> {
    public static final int RADIX = 10000;
    public static final int MIN_EXP = -32767;
    public static final int MAX_EXP = 32768;
    public static final int ERR_SCALE = 32760;
    public static final byte FINITE = 0;
    public static final byte INFINITE = 1;
    public static final byte SNAN = 2;
    public static final byte QNAN = 3;
    private static final String NAN_STRING = "NaN";
    private static final String POS_INFINITY_STRING = "Infinity";
    private static final String NEG_INFINITY_STRING = "-Infinity";
    private static final String ADD_TRAP = "add";
    private static final String MULTIPLY_TRAP = "multiply";
    private static final String DIVIDE_TRAP = "divide";
    private static final String SQRT_TRAP = "sqrt";
    private static final String ALIGN_TRAP = "align";
    private static final String TRUNC_TRAP = "trunc";
    private static final String NEXT_AFTER_TRAP = "nextAfter";
    private static final String LESS_THAN_TRAP = "lessThan";
    private static final String GREATER_THAN_TRAP = "greaterThan";
    private static final String NEW_INSTANCE_TRAP = "newInstance";
    protected int[] mant;
    protected byte sign;
    protected int exp;
    protected byte nans;
    private final DfpField field;

    protected Dfp(DfpField field) {
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
    }

    protected Dfp(DfpField field, byte x2) {
        this(field, x2);
    }

    protected Dfp(DfpField field, int x2) {
        this(field, x2);
    }

    protected Dfp(DfpField field, long x2) {
        this.mant = new int[field.getRadixDigits()];
        this.nans = (byte) 0;
        this.field = field;
        boolean isLongMin = false;
        if (x2 == Long.MIN_VALUE) {
            isLongMin = true;
            x2++;
        }
        if (x2 < 0) {
            this.sign = (byte) -1;
            x2 = -x2;
        } else {
            this.sign = (byte) 1;
        }
        this.exp = 0;
        while (x2 != 0) {
            System.arraycopy(this.mant, this.mant.length - this.exp, this.mant, (this.mant.length - 1) - this.exp, this.exp);
            this.mant[this.mant.length - 1] = (int) (x2 % 10000);
            x2 /= 10000;
            this.exp++;
        }
        if (isLongMin) {
            for (int i2 = 0; i2 < this.mant.length - 1; i2++) {
                if (this.mant[i2] != 0) {
                    int[] iArr = this.mant;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] + 1;
                    return;
                }
            }
        }
    }

    protected Dfp(DfpField field, double x2) {
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
        long bits = Double.doubleToLongBits(x2);
        long mantissa = bits & DoubleConsts.SIGNIF_BIT_MASK;
        int exponent = ((int) ((bits & DoubleConsts.EXP_BIT_MASK) >> 52)) - 1023;
        if (exponent == -1023) {
            if (x2 == 0.0d) {
                if ((bits & Long.MIN_VALUE) != 0) {
                    this.sign = (byte) -1;
                    return;
                }
                return;
            } else {
                exponent++;
                while ((mantissa & 4503599627370496L) == 0) {
                    exponent--;
                    mantissa <<= 1;
                }
                mantissa &= DoubleConsts.SIGNIF_BIT_MASK;
            }
        }
        if (exponent != 1024) {
            Dfp xdfp = new Dfp(field, mantissa).divide(new Dfp(field, 4503599627370496L)).add(field.getOne()).multiply(DfpMath.pow(field.getTwo(), exponent));
            xdfp = (bits & Long.MIN_VALUE) != 0 ? xdfp.negate() : xdfp;
            System.arraycopy(xdfp.mant, 0, this.mant, 0, this.mant.length);
            this.sign = xdfp.sign;
            this.exp = xdfp.exp;
            this.nans = xdfp.nans;
            return;
        }
        if (x2 != x2) {
            this.sign = (byte) 1;
            this.nans = (byte) 3;
        } else if (x2 < 0.0d) {
            this.sign = (byte) -1;
            this.nans = (byte) 1;
        } else {
            this.sign = (byte) 1;
            this.nans = (byte) 1;
        }
    }

    public Dfp(Dfp d2) {
        this.mant = (int[]) d2.mant.clone();
        this.sign = d2.sign;
        this.exp = d2.exp;
        this.nans = d2.nans;
        this.field = d2.field;
    }

    protected Dfp(DfpField field, String s2) {
        String fpdecimal;
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
        boolean decimalFound = false;
        char[] striped = new char[(getRadixDigits() * 4) + 8];
        if (s2.equals("Infinity")) {
            this.sign = (byte) 1;
            this.nans = (byte) 1;
            return;
        }
        if (s2.equals(NEG_INFINITY_STRING)) {
            this.sign = (byte) -1;
            this.nans = (byte) 1;
            return;
        }
        if (s2.equals("NaN")) {
            this.sign = (byte) 1;
            this.nans = (byte) 3;
            return;
        }
        int p2 = s2.indexOf("e");
        p2 = p2 == -1 ? s2.indexOf("E") : p2;
        int sciexp = 0;
        if (p2 != -1) {
            fpdecimal = s2.substring(0, p2);
            String fpexp = s2.substring(p2 + 1);
            boolean negative = false;
            for (int i2 = 0; i2 < fpexp.length(); i2++) {
                if (fpexp.charAt(i2) == '-') {
                    negative = true;
                } else if (fpexp.charAt(i2) >= '0' && fpexp.charAt(i2) <= '9') {
                    sciexp = ((sciexp * 10) + fpexp.charAt(i2)) - 48;
                }
            }
            if (negative) {
                sciexp = -sciexp;
            }
        } else {
            fpdecimal = s2;
        }
        if (fpdecimal.indexOf(LanguageTag.SEP) != -1) {
            this.sign = (byte) -1;
        }
        int p3 = 0;
        int decimalPos = 0;
        do {
            if (fpdecimal.charAt(p3) >= '1' && fpdecimal.charAt(p3) <= '9') {
                break;
            }
            if (decimalFound && fpdecimal.charAt(p3) == '0') {
                decimalPos--;
            }
            decimalFound = fpdecimal.charAt(p3) == '.' ? true : decimalFound;
            p3++;
        } while (p3 != fpdecimal.length());
        int q2 = 4;
        striped[0] = '0';
        striped[1] = '0';
        striped[2] = '0';
        striped[3] = '0';
        int significantDigits = 0;
        while (p3 != fpdecimal.length() && q2 != (this.mant.length * 4) + 4 + 1) {
            if (fpdecimal.charAt(p3) == '.') {
                decimalFound = true;
                decimalPos = significantDigits;
                p3++;
            } else if (fpdecimal.charAt(p3) < '0' || fpdecimal.charAt(p3) > '9') {
                p3++;
            } else {
                striped[q2] = fpdecimal.charAt(p3);
                q2++;
                p3++;
                significantDigits++;
            }
        }
        if (decimalFound && q2 != 4) {
            while (true) {
                q2--;
                if (q2 == 4 || striped[q2] != '0') {
                    break;
                } else {
                    significantDigits--;
                }
            }
        }
        if (decimalFound && significantDigits == 0) {
            decimalPos = 0;
        }
        decimalPos = decimalFound ? decimalPos : q2 - 4;
        int p4 = (significantDigits - 1) + 4;
        while (p4 > 4 && striped[p4] == '0') {
            p4--;
        }
        int i3 = ((400 - decimalPos) - (sciexp % 4)) % 4;
        int q3 = 4 - i3;
        int decimalPos2 = decimalPos + i3;
        while (p4 - q3 < this.mant.length * 4) {
            for (int i4 = 0; i4 < 4; i4++) {
                p4++;
                striped[p4] = '0';
            }
        }
        for (int i5 = this.mant.length - 1; i5 >= 0; i5--) {
            this.mant[i5] = ((striped[q3] - '0') * 1000) + ((striped[q3 + 1] - '0') * 100) + ((striped[q3 + 2] - '0') * 10) + (striped[q3 + 3] - '0');
            q3 += 4;
        }
        this.exp = (decimalPos2 + sciexp) / 4;
        if (q3 < striped.length) {
            round((striped[q3] - '0') * 1000);
        }
    }

    protected Dfp(DfpField field, byte sign, byte nans) {
        this.field = field;
        this.mant = new int[field.getRadixDigits()];
        this.sign = sign;
        this.exp = 0;
        this.nans = nans;
    }

    public Dfp newInstance() {
        return new Dfp(getField());
    }

    public Dfp newInstance(byte x2) {
        return new Dfp(getField(), x2);
    }

    public Dfp newInstance(int x2) {
        return new Dfp(getField(), x2);
    }

    public Dfp newInstance(long x2) {
        return new Dfp(getField(), x2);
    }

    public Dfp newInstance(double x2) {
        return new Dfp(getField(), x2);
    }

    public Dfp newInstance(Dfp d2) {
        if (this.field.getRadixDigits() != d2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, NEW_INSTANCE_TRAP, d2, result);
        }
        return new Dfp(d2);
    }

    public Dfp newInstance(String s2) {
        return new Dfp(this.field, s2);
    }

    public Dfp newInstance(byte sig, byte code) {
        return this.field.newDfp(sig, code);
    }

    @Override // org.apache.commons.math3.FieldElement
    public DfpField getField() {
        return this.field;
    }

    public int getRadixDigits() {
        return this.field.getRadixDigits();
    }

    public Dfp getZero() {
        return this.field.getZero();
    }

    public Dfp getOne() {
        return this.field.getOne();
    }

    public Dfp getTwo() {
        return this.field.getTwo();
    }

    protected void shiftLeft() {
        for (int i2 = this.mant.length - 1; i2 > 0; i2--) {
            this.mant[i2] = this.mant[i2 - 1];
        }
        this.mant[0] = 0;
        this.exp--;
    }

    protected void shiftRight() {
        for (int i2 = 0; i2 < this.mant.length - 1; i2++) {
            this.mant[i2] = this.mant[i2 + 1];
        }
        this.mant[this.mant.length - 1] = 0;
        this.exp++;
    }

    protected int align(int e2) {
        int lostdigit = 0;
        boolean inexact = false;
        int diff = this.exp - e2;
        int adiff = diff;
        if (adiff < 0) {
            adiff = -adiff;
        }
        if (diff == 0) {
            return 0;
        }
        if (adiff > this.mant.length + 1) {
            Arrays.fill(this.mant, 0);
            this.exp = e2;
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
            return 0;
        }
        for (int i2 = 0; i2 < adiff; i2++) {
            if (diff < 0) {
                if (lostdigit != 0) {
                    inexact = true;
                }
                lostdigit = this.mant[0];
                shiftRight();
            } else {
                shiftLeft();
            }
        }
        if (inexact) {
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
        }
        return lostdigit;
    }

    public boolean lessThan(Dfp x2) {
        if (this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            dotrap(1, LESS_THAN_TRAP, x2, result);
            return false;
        }
        if (!isNaN() && !x2.isNaN()) {
            return compare(this, x2) < 0;
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, x2, newInstance(getZero()));
        return false;
    }

    public boolean greaterThan(Dfp x2) {
        if (this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            dotrap(1, GREATER_THAN_TRAP, x2, result);
            return false;
        }
        if (!isNaN() && !x2.isNaN()) {
            return compare(this, x2) > 0;
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, GREATER_THAN_TRAP, x2, newInstance(getZero()));
        return false;
    }

    public boolean negativeOrNull() {
        if (!isNaN()) {
            return this.sign < 0 || (this.mant[this.mant.length - 1] == 0 && !isInfinite());
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
        return false;
    }

    public boolean strictlyNegative() {
        if (!isNaN()) {
            return this.sign < 0 && (this.mant[this.mant.length - 1] != 0 || isInfinite());
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
        return false;
    }

    public boolean positiveOrNull() {
        if (!isNaN()) {
            return this.sign > 0 || (this.mant[this.mant.length - 1] == 0 && !isInfinite());
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
        return false;
    }

    public boolean strictlyPositive() {
        if (!isNaN()) {
            return this.sign > 0 && (this.mant[this.mant.length - 1] != 0 || isInfinite());
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp abs() {
        Dfp result = newInstance(this);
        result.sign = (byte) 1;
        return result;
    }

    public boolean isInfinite() {
        return this.nans == 1;
    }

    public boolean isNaN() {
        return this.nans == 3 || this.nans == 2;
    }

    public boolean isZero() {
        if (!isNaN()) {
            return this.mant[this.mant.length - 1] == 0 && !isInfinite();
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
        return false;
    }

    public boolean equals(Object other) {
        if (other instanceof Dfp) {
            Dfp x2 = (Dfp) other;
            return !isNaN() && !x2.isNaN() && this.field.getRadixDigits() == x2.field.getRadixDigits() && compare(this, x2) == 0;
        }
        return false;
    }

    public int hashCode() {
        return 17 + (isZero() ? 0 : this.sign << 8) + (this.nans << 16) + this.exp + Arrays.hashCode(this.mant);
    }

    public boolean unequal(Dfp x2) {
        if (isNaN() || x2.isNaN() || this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            return false;
        }
        return greaterThan(x2) || lessThan(x2);
    }

    private static int compare(Dfp a2, Dfp b2) {
        if (a2.mant[a2.mant.length - 1] == 0 && b2.mant[b2.mant.length - 1] == 0 && a2.nans == 0 && b2.nans == 0) {
            return 0;
        }
        if (a2.sign != b2.sign) {
            if (a2.sign == -1) {
                return -1;
            }
            return 1;
        }
        if (a2.nans == 1 && b2.nans == 0) {
            return a2.sign;
        }
        if (a2.nans == 0 && b2.nans == 1) {
            return -b2.sign;
        }
        if (a2.nans == 1 && b2.nans == 1) {
            return 0;
        }
        if (b2.mant[b2.mant.length - 1] != 0 && a2.mant[b2.mant.length - 1] != 0) {
            if (a2.exp < b2.exp) {
                return -a2.sign;
            }
            if (a2.exp > b2.exp) {
                return a2.sign;
            }
        }
        for (int i2 = a2.mant.length - 1; i2 >= 0; i2--) {
            if (a2.mant[i2] > b2.mant[i2]) {
                return a2.sign;
            }
            if (a2.mant[i2] < b2.mant[i2]) {
                return -a2.sign;
            }
        }
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp rint() {
        return trunc(DfpField.RoundingMode.ROUND_HALF_EVEN);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp floor() {
        return trunc(DfpField.RoundingMode.ROUND_FLOOR);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp ceil() {
        return trunc(DfpField.RoundingMode.ROUND_CEIL);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp remainder(Dfp d2) {
        Dfp result = subtract(divide(d2).rint().multiply(d2));
        if (result.mant[this.mant.length - 1] == 0) {
            result.sign = this.sign;
        }
        return result;
    }

    protected Dfp trunc(DfpField.RoundingMode rmode) {
        boolean changed = false;
        if (isNaN()) {
            return newInstance(this);
        }
        if (this.nans == 1) {
            return newInstance(this);
        }
        if (this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.exp < 0) {
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, newInstance(getZero()));
        }
        if (this.exp >= this.mant.length) {
            return newInstance(this);
        }
        Dfp result = newInstance(this);
        for (int i2 = 0; i2 < this.mant.length - result.exp; i2++) {
            changed |= result.mant[i2] != 0;
            result.mant[i2] = 0;
        }
        if (changed) {
            switch (rmode) {
                case ROUND_FLOOR:
                    if (result.sign == -1) {
                        result = result.add(newInstance(-1));
                        break;
                    }
                    break;
                case ROUND_CEIL:
                    if (result.sign == 1) {
                        result = result.add(getOne());
                        break;
                    }
                    break;
                case ROUND_HALF_EVEN:
                default:
                    Dfp half = newInstance(JPEG.version);
                    Dfp a2 = subtract(result);
                    a2.sign = (byte) 1;
                    if (a2.greaterThan(half)) {
                        a2 = newInstance(getOne());
                        a2.sign = this.sign;
                        result = result.add(a2);
                    }
                    if (a2.equals(half) && result.exp > 0 && (result.mant[this.mant.length - result.exp] & 1) != 0) {
                        Dfp a3 = newInstance(getOne());
                        a3.sign = this.sign;
                        result = result.add(a3);
                        break;
                    }
                    break;
            }
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, result);
        }
        return result;
    }

    public int intValue() {
        int result = 0;
        Dfp rounded = rint();
        if (rounded.greaterThan(newInstance(Integer.MAX_VALUE))) {
            return Integer.MAX_VALUE;
        }
        if (rounded.lessThan(newInstance(Integer.MIN_VALUE))) {
            return Integer.MIN_VALUE;
        }
        for (int i2 = this.mant.length - 1; i2 >= this.mant.length - rounded.exp; i2--) {
            result = (result * 10000) + rounded.mant[i2];
        }
        if (rounded.sign == -1) {
            result = -result;
        }
        return result;
    }

    public int log10K() {
        return this.exp - 1;
    }

    public Dfp power10K(int e2) {
        Dfp d2 = newInstance(getOne());
        d2.exp = e2 + 1;
        return d2;
    }

    public int intLog10() {
        if (this.mant[this.mant.length - 1] > 1000) {
            return (this.exp * 4) - 1;
        }
        if (this.mant[this.mant.length - 1] > 100) {
            return (this.exp * 4) - 2;
        }
        if (this.mant[this.mant.length - 1] > 10) {
            return (this.exp * 4) - 3;
        }
        return (this.exp * 4) - 4;
    }

    public Dfp power10(int e2) {
        Dfp d2 = newInstance(getOne());
        if (e2 >= 0) {
            d2.exp = (e2 / 4) + 1;
        } else {
            d2.exp = (e2 + 1) / 4;
        }
        switch (((e2 % 4) + 4) % 4) {
            case 0:
                break;
            case 1:
                d2 = d2.multiply(10);
                break;
            case 2:
                d2 = d2.multiply(100);
                break;
            default:
                d2 = d2.multiply(1000);
                break;
        }
        return d2;
    }

    protected int complement(int extra) {
        int extra2 = 10000 - extra;
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            this.mant[i2] = (10000 - this.mant[i2]) - 1;
        }
        int rh = extra2 / 10000;
        int extra3 = extra2 - (rh * 10000);
        for (int i3 = 0; i3 < this.mant.length; i3++) {
            int r2 = this.mant[i3] + rh;
            rh = r2 / 10000;
            this.mant[i3] = r2 - (rh * 10000);
        }
        return extra3;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp add(Dfp x2) {
        if (this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, ADD_TRAP, x2, result);
        }
        if (this.nans != 0 || x2.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (x2.isNaN()) {
                return x2;
            }
            if (this.nans == 1 && x2.nans == 0) {
                return this;
            }
            if (x2.nans == 1 && this.nans == 0) {
                return x2;
            }
            if (x2.nans == 1 && this.nans == 1 && this.sign == x2.sign) {
                return x2;
            }
            if (x2.nans == 1 && this.nans == 1 && this.sign != x2.sign) {
                this.field.setIEEEFlagsBits(1);
                Dfp result2 = newInstance(getZero());
                result2.nans = (byte) 3;
                return dotrap(1, ADD_TRAP, x2, result2);
            }
        }
        Dfp a2 = newInstance(this);
        Dfp b2 = newInstance(x2);
        Dfp result3 = newInstance(getZero());
        byte asign = a2.sign;
        byte bsign = b2.sign;
        a2.sign = (byte) 1;
        b2.sign = (byte) 1;
        byte rsign = bsign;
        if (compare(a2, b2) > 0) {
            rsign = asign;
        }
        if (b2.mant[this.mant.length - 1] == 0) {
            b2.exp = a2.exp;
        }
        if (a2.mant[this.mant.length - 1] == 0) {
            a2.exp = b2.exp;
        }
        int aextradigit = 0;
        int bextradigit = 0;
        if (a2.exp < b2.exp) {
            aextradigit = a2.align(b2.exp);
        } else {
            bextradigit = b2.align(a2.exp);
        }
        if (asign != bsign) {
            if (asign == rsign) {
                bextradigit = b2.complement(bextradigit);
            } else {
                aextradigit = a2.complement(aextradigit);
            }
        }
        int rh = 0;
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            int r2 = a2.mant[i2] + b2.mant[i2] + rh;
            rh = r2 / 10000;
            result3.mant[i2] = r2 - (rh * 10000);
        }
        result3.exp = a2.exp;
        result3.sign = rsign;
        if (rh != 0 && asign == bsign) {
            int lostdigit = result3.mant[0];
            result3.shiftRight();
            result3.mant[this.mant.length - 1] = rh;
            int excp = result3.round(lostdigit);
            if (excp != 0) {
                result3 = dotrap(excp, ADD_TRAP, x2, result3);
            }
        }
        for (int i3 = 0; i3 < this.mant.length && result3.mant[this.mant.length - 1] == 0; i3++) {
            result3.shiftLeft();
            if (i3 == 0) {
                result3.mant[0] = aextradigit + bextradigit;
                aextradigit = 0;
                bextradigit = 0;
            }
        }
        if (result3.mant[this.mant.length - 1] == 0) {
            result3.exp = 0;
            if (asign != bsign) {
                result3.sign = (byte) 1;
            }
        }
        int excp2 = result3.round(aextradigit + bextradigit);
        if (excp2 != 0) {
            result3 = dotrap(excp2, ADD_TRAP, x2, result3);
        }
        return result3;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp negate() {
        Dfp result = newInstance(this);
        result.sign = (byte) (-result.sign);
        return result;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp subtract(Dfp x2) {
        return add(x2.negate());
    }

    protected int round(int n2) {
        boolean inc;
        switch (this.field.getRoundingMode()) {
            case ROUND_FLOOR:
            default:
                inc = this.sign == -1 && n2 != 0;
                break;
            case ROUND_CEIL:
                inc = this.sign == 1 && n2 != 0;
                break;
            case ROUND_HALF_EVEN:
                inc = n2 > 5000 || (n2 == 5000 && (this.mant[0] & 1) == 1);
                break;
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                inc = n2 != 0;
                break;
            case ROUND_HALF_UP:
                inc = n2 >= 5000;
                break;
            case ROUND_HALF_DOWN:
                inc = n2 > 5000;
                break;
            case ROUND_HALF_ODD:
                inc = n2 > 5000 || (n2 == 5000 && (this.mant[0] & 1) == 0);
                break;
        }
        if (inc) {
            int rh = 1;
            for (int i2 = 0; i2 < this.mant.length; i2++) {
                int r2 = this.mant[i2] + rh;
                rh = r2 / 10000;
                this.mant[i2] = r2 - (rh * 10000);
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        if (this.exp < -32767) {
            this.field.setIEEEFlagsBits(8);
            return 8;
        }
        if (this.exp > 32768) {
            this.field.setIEEEFlagsBits(4);
            return 4;
        }
        if (n2 != 0) {
            this.field.setIEEEFlagsBits(16);
            return 16;
        }
        return 0;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp multiply(Dfp x2) {
        int excp;
        if (this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, MULTIPLY_TRAP, x2, result);
        }
        Dfp result2 = newInstance(getZero());
        if (this.nans != 0 || x2.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (x2.isNaN()) {
                return x2;
            }
            if (this.nans == 1 && x2.nans == 0 && x2.mant[this.mant.length - 1] != 0) {
                Dfp result3 = newInstance(this);
                result3.sign = (byte) (this.sign * x2.sign);
                return result3;
            }
            if (x2.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                Dfp result4 = newInstance(x2);
                result4.sign = (byte) (this.sign * x2.sign);
                return result4;
            }
            if (x2.nans == 1 && this.nans == 1) {
                Dfp result5 = newInstance(this);
                result5.sign = (byte) (this.sign * x2.sign);
                return result5;
            }
            if ((x2.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] == 0) || (this.nans == 1 && x2.nans == 0 && x2.mant[this.mant.length - 1] == 0)) {
                this.field.setIEEEFlagsBits(1);
                Dfp result6 = newInstance(getZero());
                result6.nans = (byte) 3;
                return dotrap(1, MULTIPLY_TRAP, x2, result6);
            }
        }
        int[] product = new int[this.mant.length * 2];
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            int rh = 0;
            for (int j2 = 0; j2 < this.mant.length; j2++) {
                int r2 = (this.mant[i2] * x2.mant[j2]) + product[i2 + j2] + rh;
                rh = r2 / 10000;
                product[i2 + j2] = r2 - (rh * 10000);
            }
            product[i2 + this.mant.length] = rh;
        }
        int md = (this.mant.length * 2) - 1;
        int i3 = (this.mant.length * 2) - 1;
        while (true) {
            if (i3 < 0) {
                break;
            }
            if (product[i3] != 0) {
                md = i3;
                break;
            }
            i3--;
        }
        for (int i4 = 0; i4 < this.mant.length; i4++) {
            result2.mant[(this.mant.length - i4) - 1] = product[md - i4];
        }
        result2.exp = (((this.exp + x2.exp) + md) - (2 * this.mant.length)) + 1;
        result2.sign = (byte) (this.sign == x2.sign ? 1 : -1);
        if (result2.mant[this.mant.length - 1] == 0) {
            result2.exp = 0;
        }
        if (md > this.mant.length - 1) {
            excp = result2.round(product[md - this.mant.length]);
        } else {
            excp = result2.round(0);
        }
        if (excp != 0) {
            result2 = dotrap(excp, MULTIPLY_TRAP, x2, result2);
        }
        return result2;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp multiply(int x2) {
        if (x2 >= 0 && x2 < 10000) {
            return multiplyFast(x2);
        }
        return multiply(newInstance(x2));
    }

    private Dfp multiplyFast(int x2) {
        Dfp result = newInstance(this);
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1 && x2 != 0) {
                return newInstance(this);
            }
            if (this.nans == 1 && x2 == 0) {
                this.field.setIEEEFlagsBits(1);
                Dfp result2 = newInstance(getZero());
                result2.nans = (byte) 3;
                return dotrap(1, MULTIPLY_TRAP, newInstance(getZero()), result2);
            }
        }
        if (x2 < 0 || x2 >= 10000) {
            this.field.setIEEEFlagsBits(1);
            Dfp result3 = newInstance(getZero());
            result3.nans = (byte) 3;
            return dotrap(1, MULTIPLY_TRAP, result3, result3);
        }
        int rh = 0;
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            int r2 = (this.mant[i2] * x2) + rh;
            rh = r2 / 10000;
            result.mant[i2] = r2 - (rh * 10000);
        }
        int lostdigit = 0;
        if (rh != 0) {
            lostdigit = result.mant[0];
            result.shiftRight();
            result.mant[this.mant.length - 1] = rh;
        }
        if (result.mant[this.mant.length - 1] == 0) {
            result.exp = 0;
        }
        int excp = result.round(lostdigit);
        if (excp != 0) {
            result = dotrap(excp, MULTIPLY_TRAP, result, result);
        }
        return result;
    }

    @Override // org.apache.commons.math3.FieldElement
    public Dfp divide(Dfp divisor) {
        int excp;
        int trial = 0;
        if (this.field.getRadixDigits() != divisor.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, DIVIDE_TRAP, divisor, result);
        }
        Dfp result2 = newInstance(getZero());
        if (this.nans != 0 || divisor.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (divisor.isNaN()) {
                return divisor;
            }
            if (this.nans == 1 && divisor.nans == 0) {
                Dfp result3 = newInstance(this);
                result3.sign = (byte) (this.sign * divisor.sign);
                return result3;
            }
            if (divisor.nans == 1 && this.nans == 0) {
                Dfp result4 = newInstance(getZero());
                result4.sign = (byte) (this.sign * divisor.sign);
                return result4;
            }
            if (divisor.nans == 1 && this.nans == 1) {
                this.field.setIEEEFlagsBits(1);
                Dfp result5 = newInstance(getZero());
                result5.nans = (byte) 3;
                return dotrap(1, DIVIDE_TRAP, divisor, result5);
            }
        }
        if (divisor.mant[this.mant.length - 1] == 0) {
            this.field.setIEEEFlagsBits(2);
            Dfp result6 = newInstance(getZero());
            result6.sign = (byte) (this.sign * divisor.sign);
            result6.nans = (byte) 1;
            return dotrap(2, DIVIDE_TRAP, divisor, result6);
        }
        int[] dividend = new int[this.mant.length + 1];
        int[] quotient = new int[this.mant.length + 2];
        int[] remainder = new int[this.mant.length + 1];
        dividend[this.mant.length] = 0;
        quotient[this.mant.length] = 0;
        quotient[this.mant.length + 1] = 0;
        remainder[this.mant.length] = 0;
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            dividend[i2] = this.mant[i2];
            quotient[i2] = 0;
            remainder[i2] = 0;
        }
        int nsqd = 0;
        for (int qd = this.mant.length + 1; qd >= 0; qd--) {
            int divMsb = (dividend[this.mant.length] * 10000) + dividend[this.mant.length - 1];
            int min = divMsb / (divisor.mant[this.mant.length - 1] + 1);
            int max = (divMsb + 1) / divisor.mant[this.mant.length - 1];
            boolean trialgood = false;
            while (!trialgood) {
                trial = (min + max) / 2;
                int rh = 0;
                int i3 = 0;
                while (i3 < this.mant.length + 1) {
                    int dm = i3 < this.mant.length ? divisor.mant[i3] : 0;
                    int r2 = (dm * trial) + rh;
                    rh = r2 / 10000;
                    remainder[i3] = r2 - (rh * 10000);
                    i3++;
                }
                int rh2 = 1;
                for (int i4 = 0; i4 < this.mant.length + 1; i4++) {
                    int r3 = (9999 - remainder[i4]) + dividend[i4] + rh2;
                    rh2 = r3 / 10000;
                    remainder[i4] = r3 - (rh2 * 10000);
                }
                if (rh2 == 0) {
                    max = trial - 1;
                } else {
                    int minadj = ((remainder[this.mant.length] * 10000) + remainder[this.mant.length - 1]) / (divisor.mant[this.mant.length - 1] + 1);
                    if (minadj >= 2) {
                        min = trial + minadj;
                    } else {
                        trialgood = false;
                        for (int i5 = this.mant.length - 1; i5 >= 0; i5--) {
                            if (divisor.mant[i5] > remainder[i5]) {
                                trialgood = true;
                            }
                            if (divisor.mant[i5] < remainder[i5]) {
                                break;
                            }
                        }
                        if (remainder[this.mant.length] != 0) {
                            trialgood = false;
                        }
                        if (!trialgood) {
                            min = trial + 1;
                        }
                    }
                }
            }
            quotient[qd] = trial;
            if (trial != 0 || nsqd != 0) {
                nsqd++;
            }
            if ((this.field.getRoundingMode() == DfpField.RoundingMode.ROUND_DOWN && nsqd == this.mant.length) || nsqd > this.mant.length) {
                break;
            }
            dividend[0] = 0;
            for (int i6 = 0; i6 < this.mant.length; i6++) {
                dividend[i6 + 1] = remainder[i6];
            }
        }
        int md = this.mant.length;
        int i7 = this.mant.length + 1;
        while (true) {
            if (i7 < 0) {
                break;
            }
            if (quotient[i7] != 0) {
                md = i7;
                break;
            }
            i7--;
        }
        for (int i8 = 0; i8 < this.mant.length; i8++) {
            result2.mant[(this.mant.length - i8) - 1] = quotient[md - i8];
        }
        result2.exp = ((this.exp - divisor.exp) + md) - this.mant.length;
        result2.sign = (byte) (this.sign == divisor.sign ? 1 : -1);
        if (result2.mant[this.mant.length - 1] == 0) {
            result2.exp = 0;
        }
        if (md > this.mant.length - 1) {
            excp = result2.round(quotient[md - this.mant.length]);
        } else {
            excp = result2.round(0);
        }
        if (excp != 0) {
            result2 = dotrap(excp, DIVIDE_TRAP, divisor, result2);
        }
        return result2;
    }

    public Dfp divide(int divisor) {
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1) {
                return newInstance(this);
            }
        }
        if (divisor == 0) {
            this.field.setIEEEFlagsBits(2);
            Dfp result = newInstance(getZero());
            result.sign = this.sign;
            result.nans = (byte) 1;
            return dotrap(2, DIVIDE_TRAP, getZero(), result);
        }
        if (divisor < 0 || divisor >= 10000) {
            this.field.setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, DIVIDE_TRAP, result2, result2);
        }
        Dfp result3 = newInstance(this);
        int rl = 0;
        for (int i2 = this.mant.length - 1; i2 >= 0; i2--) {
            int r2 = (rl * 10000) + result3.mant[i2];
            int rh = r2 / divisor;
            rl = r2 - (rh * divisor);
            result3.mant[i2] = rh;
        }
        if (result3.mant[this.mant.length - 1] == 0) {
            result3.shiftLeft();
            int r3 = rl * 10000;
            int rh2 = r3 / divisor;
            rl = r3 - (rh2 * divisor);
            result3.mant[0] = rh2;
        }
        int excp = result3.round((rl * 10000) / divisor);
        if (excp != 0) {
            result3 = dotrap(excp, DIVIDE_TRAP, result3, result3);
        }
        return result3;
    }

    @Override // org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.FieldElement
    public Dfp reciprocal() {
        return this.field.getOne().divide(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp sqrt() {
        if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.nans != 0) {
            if (this.nans == 1 && this.sign == 1) {
                return newInstance(this);
            }
            if (this.nans == 3) {
                return newInstance(this);
            }
            if (this.nans == 2) {
                this.field.setIEEEFlagsBits(1);
                return dotrap(1, SQRT_TRAP, null, newInstance(this));
            }
        }
        if (this.sign == -1) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(this);
            result.nans = (byte) 3;
            return dotrap(1, SQRT_TRAP, null, result);
        }
        Dfp x2 = newInstance(this);
        if (x2.exp < -1 || x2.exp > 1) {
            x2.exp = this.exp / 2;
        }
        switch (x2.mant[this.mant.length - 1] / 2000) {
            case 0:
                x2.mant[this.mant.length - 1] = (x2.mant[this.mant.length - 1] / 2) + 1;
                break;
            case 1:
            default:
                x2.mant[this.mant.length - 1] = 3000;
                break;
            case 2:
                x2.mant[this.mant.length - 1] = 1500;
                break;
            case 3:
                x2.mant[this.mant.length - 1] = 2200;
                break;
        }
        newInstance(x2);
        Dfp px = getZero();
        getZero();
        while (x2.unequal(px)) {
            Dfp dx = newInstance(x2);
            dx.sign = (byte) -1;
            Dfp dx2 = dx.add(divide(x2)).divide(2);
            Dfp ppx = px;
            px = x2;
            x2 = x2.add(dx2);
            if (x2.equals(ppx) || dx2.mant[this.mant.length - 1] == 0) {
                return x2;
            }
        }
        return x2;
    }

    public String toString() {
        if (this.nans != 0) {
            if (this.nans == 1) {
                return this.sign < 0 ? NEG_INFINITY_STRING : "Infinity";
            }
            return "NaN";
        }
        if (this.exp > this.mant.length || this.exp < -1) {
            return dfp2sci();
        }
        return dfp2string();
    }

    protected String dfp2sci() {
        int p2;
        char[] rawdigits = new char[this.mant.length * 4];
        char[] outputbuffer = new char[(this.mant.length * 4) + 20];
        int p3 = 0;
        for (int i2 = this.mant.length - 1; i2 >= 0; i2--) {
            int i3 = p3;
            int p4 = p3 + 1;
            rawdigits[i3] = (char) ((this.mant[i2] / 1000) + 48);
            int p5 = p4 + 1;
            rawdigits[p4] = (char) (((this.mant[i2] / 100) % 10) + 48);
            int p6 = p5 + 1;
            rawdigits[p5] = (char) (((this.mant[i2] / 10) % 10) + 48);
            p3 = p6 + 1;
            rawdigits[p6] = (char) ((this.mant[i2] % 10) + 48);
        }
        int p7 = 0;
        while (p7 < rawdigits.length && rawdigits[p7] == '0') {
            p7++;
        }
        int shf = p7;
        int q2 = 0;
        if (this.sign == -1) {
            q2 = 0 + 1;
            outputbuffer[0] = '-';
        }
        if (p7 != rawdigits.length) {
            int i4 = q2;
            int q3 = q2 + 1;
            int i5 = p7;
            int p8 = p7 + 1;
            outputbuffer[i4] = rawdigits[i5];
            int q4 = q3 + 1;
            outputbuffer[q3] = '.';
            while (p8 < rawdigits.length) {
                int i6 = q4;
                q4++;
                int i7 = p8;
                p8++;
                outputbuffer[i6] = rawdigits[i7];
            }
            int i8 = q4;
            int q5 = q4 + 1;
            outputbuffer[i8] = 'e';
            int e2 = ((this.exp * 4) - shf) - 1;
            int ae2 = e2;
            if (e2 < 0) {
                ae2 = -e2;
            }
            int i9 = 1000000000;
            while (true) {
                p2 = i9;
                if (p2 <= ae2) {
                    break;
                }
                i9 = p2 / 10;
            }
            if (e2 < 0) {
                q5++;
                outputbuffer[q5] = '-';
            }
            while (p2 > 0) {
                int i10 = q5;
                q5++;
                outputbuffer[i10] = (char) ((ae2 / p2) + 48);
                ae2 %= p2;
                p2 /= 10;
            }
            return new String(outputbuffer, 0, q5);
        }
        int i11 = q2;
        int q6 = q2 + 1;
        outputbuffer[i11] = '0';
        int q7 = q6 + 1;
        outputbuffer[q6] = '.';
        int q8 = q7 + 1;
        outputbuffer[q7] = '0';
        int q9 = q8 + 1;
        outputbuffer[q8] = 'e';
        int i12 = q9 + 1;
        outputbuffer[q9] = '0';
        return new String(outputbuffer, 0, 5);
    }

    protected String dfp2string() {
        char[] buffer = new char[(this.mant.length * 4) + 20];
        int p2 = 1;
        int e2 = this.exp;
        boolean pointInserted = false;
        buffer[0] = ' ';
        if (e2 <= 0) {
            int p3 = 1 + 1;
            buffer[1] = '0';
            p2 = p3 + 1;
            buffer[p3] = '.';
            pointInserted = true;
        }
        while (e2 < 0) {
            int i2 = p2;
            int p4 = p2 + 1;
            buffer[i2] = '0';
            int p5 = p4 + 1;
            buffer[p4] = '0';
            int p6 = p5 + 1;
            buffer[p5] = '0';
            p2 = p6 + 1;
            buffer[p6] = '0';
            e2++;
        }
        for (int i3 = this.mant.length - 1; i3 >= 0; i3--) {
            int i4 = p2;
            int p7 = p2 + 1;
            buffer[i4] = (char) ((this.mant[i3] / 1000) + 48);
            int p8 = p7 + 1;
            buffer[p7] = (char) (((this.mant[i3] / 100) % 10) + 48);
            int p9 = p8 + 1;
            buffer[p8] = (char) (((this.mant[i3] / 10) % 10) + 48);
            p2 = p9 + 1;
            buffer[p9] = (char) ((this.mant[i3] % 10) + 48);
            e2--;
            if (e2 == 0) {
                p2++;
                buffer[p2] = '.';
                pointInserted = true;
            }
        }
        while (e2 > 0) {
            int i5 = p2;
            int p10 = p2 + 1;
            buffer[i5] = '0';
            int p11 = p10 + 1;
            buffer[p10] = '0';
            int p12 = p11 + 1;
            buffer[p11] = '0';
            p2 = p12 + 1;
            buffer[p12] = '0';
            e2--;
        }
        if (!pointInserted) {
            int i6 = p2;
            p2++;
            buffer[i6] = '.';
        }
        int q2 = 1;
        while (buffer[q2] == '0') {
            q2++;
        }
        if (buffer[q2] == '.') {
            q2--;
        }
        while (buffer[p2 - 1] == '0') {
            p2--;
        }
        if (this.sign < 0) {
            q2--;
            buffer[q2] = '-';
        }
        return new String(buffer, q2, p2 - q2);
    }

    public Dfp dotrap(int type, String what, Dfp oper, Dfp result) {
        Dfp def = result;
        switch (type) {
            case 1:
                def = newInstance(getZero());
                def.sign = result.sign;
                def.nans = (byte) 3;
                break;
            case 2:
                if (this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                    def = newInstance(getZero());
                    def.sign = (byte) (this.sign * oper.sign);
                    def.nans = (byte) 1;
                }
                if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                }
                if (this.nans == 1 || this.nans == 3) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                }
                if (this.nans == 1 || this.nans == 2) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                    break;
                }
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                def = result;
                break;
            case 4:
                result.exp -= ERR_SCALE;
                def = newInstance(getZero());
                def.sign = result.sign;
                def.nans = (byte) 1;
                break;
            case 8:
                if (result.exp + this.mant.length < -32767) {
                    def = newInstance(getZero());
                    def.sign = result.sign;
                } else {
                    def = newInstance(result);
                }
                result.exp += ERR_SCALE;
                break;
        }
        return trap(type, what, oper, def, result);
    }

    protected Dfp trap(int type, String what, Dfp oper, Dfp def, Dfp result) {
        return def;
    }

    public int classify() {
        return this.nans;
    }

    public static Dfp copysign(Dfp x2, Dfp y2) {
        Dfp result = x2.newInstance(x2);
        result.sign = y2.sign;
        return result;
    }

    public Dfp nextAfter(Dfp x2) {
        Dfp result;
        if (this.field.getRadixDigits() != x2.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, NEXT_AFTER_TRAP, x2, result2);
        }
        boolean up = false;
        if (lessThan(x2)) {
            up = true;
        }
        if (compare(this, x2) == 0) {
            return newInstance(x2);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            Dfp inc = newInstance(getOne());
            inc.exp = (this.exp - this.mant.length) + 1;
            inc.sign = this.sign;
            if (equals(getZero())) {
                inc.exp = MIN_EXP - this.mant.length;
            }
            result = add(inc);
        } else {
            Dfp inc2 = newInstance(getOne());
            inc2.exp = this.exp;
            inc2.sign = this.sign;
            if (equals(inc2)) {
                inc2.exp = this.exp - this.mant.length;
            } else {
                inc2.exp = (this.exp - this.mant.length) + 1;
            }
            if (equals(getZero())) {
                inc2.exp = MIN_EXP - this.mant.length;
            }
            result = subtract(inc2);
        }
        if (result.classify() == 1 && classify() != 1) {
            this.field.setIEEEFlagsBits(16);
            result = dotrap(16, NEXT_AFTER_TRAP, x2, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            this.field.setIEEEFlagsBits(16);
            result = dotrap(16, NEXT_AFTER_TRAP, x2, result);
        }
        return result;
    }

    public double toDouble() throws NumberFormatException {
        if (isInfinite()) {
            if (lessThan(getZero())) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        if (isNaN()) {
            return Double.NaN;
        }
        Dfp y2 = this;
        boolean negate = false;
        int cmp0 = compare(this, getZero());
        if (cmp0 == 0) {
            return this.sign < 0 ? -0.0d : 0.0d;
        }
        if (cmp0 < 0) {
            y2 = negate();
            negate = true;
        }
        int exponent = (int) (y2.intLog10() * 3.32d);
        if (exponent < 0) {
            exponent--;
        }
        Dfp tempDfp = DfpMath.pow(getTwo(), exponent);
        while (true) {
            if (!tempDfp.lessThan(y2) && !tempDfp.equals(y2)) {
                break;
            }
            tempDfp = tempDfp.multiply(2);
            exponent++;
        }
        int exponent2 = exponent - 1;
        Dfp y3 = y2.divide(DfpMath.pow(getTwo(), exponent2));
        if (exponent2 > -1023) {
            y3 = y3.subtract(getOne());
        }
        if (exponent2 < -1074) {
            return 0.0d;
        }
        if (exponent2 > 1023) {
            return negate ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        String str = y3.multiply(newInstance(4503599627370496L)).rint().toString();
        long mantissa = Long.parseLong(str.substring(0, str.length() - 1));
        if (mantissa == 4503599627370496L) {
            mantissa = 0;
            exponent2++;
        }
        if (exponent2 <= -1023) {
            exponent2--;
        }
        while (exponent2 < -1023) {
            exponent2++;
            mantissa >>>= 1;
        }
        long bits = mantissa | ((exponent2 + 1023) << 52);
        double x2 = Double.longBitsToDouble(bits);
        if (negate) {
            x2 = -x2;
        }
        return x2;
    }

    public double[] toSplitDouble() {
        double[] split = new double[2];
        split[0] = Double.longBitsToDouble(Double.doubleToLongBits(toDouble()) & (-1073741824));
        split[1] = subtract(newInstance(split[0])).toDouble();
        return split;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public double getReal() {
        return toDouble();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp add(double a2) {
        return add(newInstance(a2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp subtract(double a2) {
        return subtract(newInstance(a2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp multiply(double a2) {
        return multiply(newInstance(a2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp divide(double a2) {
        return divide(newInstance(a2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp remainder(double a2) {
        return remainder(newInstance(a2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public long round() {
        return FastMath.round(toDouble());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp signum() {
        if (isNaN() || isZero()) {
            return this;
        }
        return newInstance(this.sign > 0 ? 1 : -1);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp copySign(Dfp s2) {
        if ((this.sign >= 0 && s2.sign >= 0) || (this.sign < 0 && s2.sign < 0)) {
            return this;
        }
        return negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp copySign(double s2) {
        long sb = Double.doubleToLongBits(s2);
        if ((this.sign >= 0 && sb >= 0) || (this.sign < 0 && sb < 0)) {
            return this;
        }
        return negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp scalb(int n2) {
        return multiply(DfpMath.pow(getTwo(), n2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp hypot(Dfp y2) {
        return multiply(this).add(y2.multiply(y2)).sqrt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp cbrt() {
        return rootN(3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp rootN(int n2) {
        return this.sign >= 0 ? DfpMath.pow(this, getOne().divide(n2)) : DfpMath.pow(negate(), getOne().divide(n2)).negate();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp pow(double p2) {
        return DfpMath.pow(this, newInstance(p2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp pow(int n2) {
        return DfpMath.pow(this, n2);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp pow(Dfp e2) {
        return DfpMath.pow(this, e2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp exp() {
        return DfpMath.exp(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp expm1() {
        return DfpMath.exp(this).subtract(getOne());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp log() {
        return DfpMath.log(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp log1p() {
        return DfpMath.log(add(getOne()));
    }

    @Deprecated
    public int log10() {
        return intLog10();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp cos() {
        return DfpMath.cos(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp sin() {
        return DfpMath.sin(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp tan() {
        return DfpMath.tan(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp acos() {
        return DfpMath.acos(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp asin() {
        return DfpMath.asin(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp atan() {
        return DfpMath.atan(this);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp atan2(Dfp x2) throws DimensionMismatchException {
        Dfp r2 = x2.multiply(x2).add(multiply(this)).sqrt();
        if (x2.sign >= 0) {
            return getTwo().multiply(divide(r2.add(x2)).atan());
        }
        Dfp tmp = getTwo().multiply(divide(r2.subtract(x2)).atan());
        Dfp pmPi = newInstance(tmp.sign <= 0 ? -3.141592653589793d : 3.141592653589793d);
        return pmPi.subtract(tmp);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp cosh() {
        return DfpMath.exp(this).add(DfpMath.exp(negate())).divide(2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp sinh() {
        return DfpMath.exp(this).subtract(DfpMath.exp(negate())).divide(2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp tanh() {
        Dfp ePlus = DfpMath.exp(this);
        Dfp eMinus = DfpMath.exp(negate());
        return ePlus.subtract(eMinus).divide(ePlus.add(eMinus));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp acosh() {
        return multiply(this).subtract(getOne()).sqrt().add(this).log();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp asinh() {
        return multiply(this).add(getOne()).sqrt().add(this).log();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp atanh() {
        return getOne().add(this).divide(getOne().subtract(this)).log().divide(2);
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(Dfp[] a2, Dfp[] b2) throws DimensionMismatchException {
        if (a2.length != b2.length) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        Dfp r2 = getZero();
        for (int i2 = 0; i2 < a2.length; i2++) {
            r2 = r2.add(a2[i2].multiply(b2[i2]));
        }
        return r2;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(double[] a2, Dfp[] b2) throws DimensionMismatchException {
        if (a2.length != b2.length) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        Dfp r2 = getZero();
        for (int i2 = 0; i2 < a2.length; i2++) {
            r2 = r2.add(b2[i2].multiply(a2[i2]));
        }
        return r2;
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2) {
        return a1.multiply(b1).add(a2.multiply(b2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2) {
        return b1.multiply(a1).add(b2.multiply(a2));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2, Dfp a3, Dfp b3) {
        return a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2, double a3, Dfp b3) {
        return b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2, Dfp a3, Dfp b3, Dfp a4, Dfp b4) {
        return a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3)).add(a4.multiply(b4));
    }

    @Override // org.apache.commons.math3.RealFieldElement
    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2, double a3, Dfp b3, double a4, Dfp b4) {
        return b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3)).add(b4.multiply(a4));
    }
}
