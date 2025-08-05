package org.apache.commons.math3.dfp;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/DfpField.class */
public class DfpField implements Field<Dfp> {
    public static final int FLAG_INVALID = 1;
    public static final int FLAG_DIV_ZERO = 2;
    public static final int FLAG_OVERFLOW = 4;
    public static final int FLAG_UNDERFLOW = 8;
    public static final int FLAG_INEXACT = 16;
    private static String sqr2String;
    private static String sqr2ReciprocalString;
    private static String sqr3String;
    private static String sqr3ReciprocalString;
    private static String piString;
    private static String eString;
    private static String ln2String;
    private static String ln5String;
    private static String ln10String;
    private final int radixDigits;
    private final Dfp zero;
    private final Dfp one;
    private final Dfp two;
    private final Dfp sqr2;
    private final Dfp[] sqr2Split;
    private final Dfp sqr2Reciprocal;
    private final Dfp sqr3;
    private final Dfp sqr3Reciprocal;
    private final Dfp pi;
    private final Dfp[] piSplit;

    /* renamed from: e, reason: collision with root package name */
    private final Dfp f12985e;
    private final Dfp[] eSplit;
    private final Dfp ln2;
    private final Dfp[] ln2Split;
    private final Dfp ln5;
    private final Dfp[] ln5Split;
    private final Dfp ln10;
    private RoundingMode rMode;
    private int ieeeFlags;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/DfpField$RoundingMode.class */
    public enum RoundingMode {
        ROUND_DOWN,
        ROUND_UP,
        ROUND_HALF_UP,
        ROUND_HALF_DOWN,
        ROUND_HALF_EVEN,
        ROUND_HALF_ODD,
        ROUND_CEIL,
        ROUND_FLOOR
    }

    public DfpField(int decimalDigits) {
        this(decimalDigits, true);
    }

    private DfpField(int decimalDigits, boolean computeConstants) {
        this.radixDigits = decimalDigits < 13 ? 4 : (decimalDigits + 3) / 4;
        this.rMode = RoundingMode.ROUND_HALF_EVEN;
        this.ieeeFlags = 0;
        this.zero = new Dfp(this, 0);
        this.one = new Dfp(this, 1);
        this.two = new Dfp(this, 2);
        if (computeConstants) {
            synchronized (DfpField.class) {
                computeStringConstants(decimalDigits < 67 ? 200 : 3 * decimalDigits);
                this.sqr2 = new Dfp(this, sqr2String);
                this.sqr2Split = split(sqr2String);
                this.sqr2Reciprocal = new Dfp(this, sqr2ReciprocalString);
                this.sqr3 = new Dfp(this, sqr3String);
                this.sqr3Reciprocal = new Dfp(this, sqr3ReciprocalString);
                this.pi = new Dfp(this, piString);
                this.piSplit = split(piString);
                this.f12985e = new Dfp(this, eString);
                this.eSplit = split(eString);
                this.ln2 = new Dfp(this, ln2String);
                this.ln2Split = split(ln2String);
                this.ln5 = new Dfp(this, ln5String);
                this.ln5Split = split(ln5String);
                this.ln10 = new Dfp(this, ln10String);
            }
            return;
        }
        this.sqr2 = null;
        this.sqr2Split = null;
        this.sqr2Reciprocal = null;
        this.sqr3 = null;
        this.sqr3Reciprocal = null;
        this.pi = null;
        this.piSplit = null;
        this.f12985e = null;
        this.eSplit = null;
        this.ln2 = null;
        this.ln2Split = null;
        this.ln5 = null;
        this.ln5Split = null;
        this.ln10 = null;
    }

    public int getRadixDigits() {
        return this.radixDigits;
    }

    public void setRoundingMode(RoundingMode mode) {
        this.rMode = mode;
    }

    public RoundingMode getRoundingMode() {
        return this.rMode;
    }

    public int getIEEEFlags() {
        return this.ieeeFlags;
    }

    public void clearIEEEFlags() {
        this.ieeeFlags = 0;
    }

    public void setIEEEFlags(int flags) {
        this.ieeeFlags = flags & 31;
    }

    public void setIEEEFlagsBits(int bits) {
        this.ieeeFlags |= bits & 31;
    }

    public Dfp newDfp() {
        return new Dfp(this);
    }

    public Dfp newDfp(byte x2) {
        return new Dfp(this, x2);
    }

    public Dfp newDfp(int x2) {
        return new Dfp(this, x2);
    }

    public Dfp newDfp(long x2) {
        return new Dfp(this, x2);
    }

    public Dfp newDfp(double x2) {
        return new Dfp(this, x2);
    }

    public Dfp newDfp(Dfp d2) {
        return new Dfp(d2);
    }

    public Dfp newDfp(String s2) {
        return new Dfp(this, s2);
    }

    public Dfp newDfp(byte sign, byte nans) {
        return new Dfp(this, sign, nans);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public Dfp getZero() {
        return this.zero;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.Field
    public Dfp getOne() {
        return this.one;
    }

    @Override // org.apache.commons.math3.Field
    public Class<? extends FieldElement<Dfp>> getRuntimeClass() {
        return Dfp.class;
    }

    public Dfp getTwo() {
        return this.two;
    }

    public Dfp getSqr2() {
        return this.sqr2;
    }

    public Dfp[] getSqr2Split() {
        return (Dfp[]) this.sqr2Split.clone();
    }

    public Dfp getSqr2Reciprocal() {
        return this.sqr2Reciprocal;
    }

    public Dfp getSqr3() {
        return this.sqr3;
    }

    public Dfp getSqr3Reciprocal() {
        return this.sqr3Reciprocal;
    }

    public Dfp getPi() {
        return this.pi;
    }

    public Dfp[] getPiSplit() {
        return (Dfp[]) this.piSplit.clone();
    }

    public Dfp getE() {
        return this.f12985e;
    }

    public Dfp[] getESplit() {
        return (Dfp[]) this.eSplit.clone();
    }

    public Dfp getLn2() {
        return this.ln2;
    }

    public Dfp[] getLn2Split() {
        return (Dfp[]) this.ln2Split.clone();
    }

    public Dfp getLn5() {
        return this.ln5;
    }

    public Dfp[] getLn5Split() {
        return (Dfp[]) this.ln5Split.clone();
    }

    public Dfp getLn10() {
        return this.ln10;
    }

    private Dfp[] split(String a2) {
        Dfp[] result = new Dfp[2];
        boolean leading = true;
        int sp = 0;
        int sig = 0;
        char[] buf = new char[a2.length()];
        int i2 = 0;
        while (true) {
            if (i2 >= buf.length) {
                break;
            }
            buf[i2] = a2.charAt(i2);
            if (buf[i2] >= '1' && buf[i2] <= '9') {
                leading = false;
            }
            if (buf[i2] == '.') {
                sig += (400 - sig) % 4;
                leading = false;
            }
            if (sig == (this.radixDigits / 2) * 4) {
                sp = i2;
                break;
            }
            if (buf[i2] >= '0' && buf[i2] <= '9' && !leading) {
                sig++;
            }
            i2++;
        }
        result[0] = new Dfp(this, new String(buf, 0, sp));
        for (int i3 = 0; i3 < buf.length; i3++) {
            buf[i3] = a2.charAt(i3);
            if (buf[i3] >= '0' && buf[i3] <= '9' && i3 < sp) {
                buf[i3] = '0';
            }
        }
        result[1] = new Dfp(this, new String(buf));
        return result;
    }

    private static void computeStringConstants(int highPrecisionDecimalDigits) {
        if (sqr2String == null || sqr2String.length() < highPrecisionDecimalDigits - 3) {
            DfpField highPrecisionField = new DfpField(highPrecisionDecimalDigits, false);
            Dfp highPrecisionOne = new Dfp(highPrecisionField, 1);
            Dfp highPrecisionTwo = new Dfp(highPrecisionField, 2);
            Dfp highPrecisionThree = new Dfp(highPrecisionField, 3);
            Dfp highPrecisionSqr2 = highPrecisionTwo.sqrt();
            sqr2String = highPrecisionSqr2.toString();
            sqr2ReciprocalString = highPrecisionOne.divide(highPrecisionSqr2).toString();
            Dfp highPrecisionSqr3 = highPrecisionThree.sqrt();
            sqr3String = highPrecisionSqr3.toString();
            sqr3ReciprocalString = highPrecisionOne.divide(highPrecisionSqr3).toString();
            piString = computePi(highPrecisionOne, highPrecisionTwo, highPrecisionThree).toString();
            eString = computeExp(highPrecisionOne, highPrecisionOne).toString();
            ln2String = computeLn(highPrecisionTwo, highPrecisionOne, highPrecisionTwo).toString();
            ln5String = computeLn(new Dfp(highPrecisionField, 5), highPrecisionOne, highPrecisionTwo).toString();
            ln10String = computeLn(new Dfp(highPrecisionField, 10), highPrecisionOne, highPrecisionTwo).toString();
        }
    }

    private static Dfp computePi(Dfp one, Dfp two, Dfp three) {
        Dfp sqrt2 = two.sqrt();
        Dfp yk = sqrt2.subtract(one);
        Dfp four = two.add(two);
        Dfp two2kp3 = two;
        Dfp ak2 = two.multiply(three.subtract(two.multiply(sqrt2)));
        for (int i2 = 1; i2 < 20; i2++) {
            Dfp ykM1 = yk;
            Dfp y2 = yk.multiply(yk);
            Dfp oneMinusY4 = one.subtract(y2.multiply(y2));
            Dfp s2 = oneMinusY4.sqrt().sqrt();
            yk = one.subtract(s2).divide(one.add(s2));
            two2kp3 = two2kp3.multiply(four);
            Dfp p2 = one.add(yk);
            Dfp p22 = p2.multiply(p2);
            ak2 = ak2.multiply(p22.multiply(p22)).subtract(two2kp3.multiply(yk).multiply(one.add(yk).add(yk.multiply(yk))));
            if (yk.equals(ykM1)) {
                break;
            }
        }
        return one.divide(ak2);
    }

    public static Dfp computeExp(Dfp a2, Dfp one) {
        Dfp y2 = new Dfp(one);
        Dfp py = new Dfp(one);
        Dfp f2 = new Dfp(one);
        Dfp fi = new Dfp(one);
        Dfp x2 = new Dfp(one);
        for (int i2 = 0; i2 < 10000; i2++) {
            x2 = x2.multiply(a2);
            y2 = y2.add(x2.divide(f2));
            fi = fi.add(one);
            f2 = f2.multiply(fi);
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2;
    }

    public static Dfp computeLn(Dfp a2, Dfp one, Dfp two) {
        int den = 1;
        Dfp x2 = a2.add(new Dfp(a2.getField2(), -1)).divide(a2.add(one));
        Dfp y2 = new Dfp(x2);
        Dfp num = new Dfp(x2);
        Dfp py = new Dfp(y2);
        for (int i2 = 0; i2 < 10000; i2++) {
            num = num.multiply(x2).multiply(x2);
            den += 2;
            Dfp t2 = num.divide(den);
            y2 = y2.add(t2);
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2.multiply(two);
    }
}
