package org.apache.commons.math3.dfp;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/DfpMath.class */
public class DfpMath {
    private static final String POW_TRAP = "pow";

    private DfpMath() {
    }

    protected static Dfp[] split(DfpField field, String a2) {
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
            if (sig == (field.getRadixDigits() / 2) * 4) {
                sp = i2;
                break;
            }
            if (buf[i2] >= '0' && buf[i2] <= '9' && !leading) {
                sig++;
            }
            i2++;
        }
        result[0] = field.newDfp(new String(buf, 0, sp));
        for (int i3 = 0; i3 < buf.length; i3++) {
            buf[i3] = a2.charAt(i3);
            if (buf[i3] >= '0' && buf[i3] <= '9' && i3 < sp) {
                buf[i3] = '0';
            }
        }
        result[1] = field.newDfp(new String(buf));
        return result;
    }

    protected static Dfp[] split(Dfp a2) {
        Dfp[] result = new Dfp[2];
        Dfp shift = a2.multiply(a2.power10K(a2.getRadixDigits() / 2));
        result[0] = a2.add(shift).subtract(shift);
        result[1] = a2.subtract(result[0]);
        return result;
    }

    protected static Dfp[] splitMult(Dfp[] a2, Dfp[] b2) {
        Dfp[] result = {a2[0].multiply(b2[0]), a2[0].getZero()};
        if (result[0].classify() == 1 || result[0].equals(result[1])) {
            return result;
        }
        result[1] = a2[0].multiply(b2[1]).add(a2[1].multiply(b2[0])).add(a2[1].multiply(b2[1]));
        return result;
    }

    protected static Dfp[] splitDiv(Dfp[] a2, Dfp[] b2) {
        Dfp[] result = {a2[0].divide(b2[0]), a2[1].multiply(b2[0]).subtract(a2[0].multiply(b2[1]))};
        result[1] = result[1].divide(b2[0].multiply(b2[0]).add(b2[0].multiply(b2[1])));
        return result;
    }

    protected static Dfp splitPow(Dfp[] base, int a2) {
        int prevtrial;
        boolean invert = false;
        Dfp[] r2 = new Dfp[2];
        Dfp[] result = {base[0].getOne(), base[0].getZero()};
        if (a2 == 0) {
            return result[0].add(result[1]);
        }
        if (a2 < 0) {
            invert = true;
            a2 = -a2;
        }
        do {
            r2[0] = new Dfp(base[0]);
            r2[1] = new Dfp(base[1]);
            int trial = 1;
            while (true) {
                prevtrial = trial;
                trial *= 2;
                if (trial > a2) {
                    break;
                }
                r2 = splitMult(r2, r2);
            }
            a2 -= prevtrial;
            result = splitMult(result, r2);
        } while (a2 >= 1);
        result[0] = result[0].add(result[1]);
        if (invert) {
            result[0] = base[0].getOne().divide(result[0]);
        }
        return result[0];
    }

    public static Dfp pow(Dfp base, int a2) {
        Dfp prevr;
        int prevtrial;
        boolean invert = false;
        Dfp result = base.getOne();
        if (a2 == 0) {
            return result;
        }
        if (a2 < 0) {
            invert = true;
            a2 = -a2;
        }
        do {
            Dfp r2 = new Dfp(base);
            int trial = 1;
            do {
                prevr = new Dfp(r2);
                prevtrial = trial;
                r2 = r2.multiply(r2);
                trial *= 2;
            } while (a2 > trial);
            a2 -= prevtrial;
            result = result.multiply(prevr);
        } while (a2 >= 1);
        if (invert) {
            result = base.getOne().divide(result);
        }
        return base.newInstance(result);
    }

    public static Dfp exp(Dfp a2) {
        Dfp inta = a2.rint();
        Dfp fraca = a2.subtract(inta);
        int ia = inta.intValue();
        if (ia > 2147483646) {
            return a2.newInstance((byte) 1, (byte) 1);
        }
        if (ia < -2147483646) {
            return a2.newInstance();
        }
        Dfp einta = splitPow(a2.getField2().getESplit(), ia);
        Dfp efraca = expInternal(fraca);
        return einta.multiply(efraca);
    }

    protected static Dfp expInternal(Dfp a2) {
        Dfp y2 = a2.getOne();
        Dfp x2 = a2.getOne();
        Dfp fact = a2.getOne();
        Dfp py = new Dfp(y2);
        for (int i2 = 1; i2 < 90; i2++) {
            x2 = x2.multiply(a2);
            fact = fact.divide(i2);
            y2 = y2.add(x2.multiply(fact));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2;
    }

    public static Dfp log(Dfp a2) {
        int p2 = 0;
        if (a2.equals(a2.getZero()) || a2.lessThan(a2.getZero()) || a2.isNaN()) {
            a2.getField2().setIEEEFlagsBits(1);
            return a2.dotrap(1, "ln", a2, a2.newInstance((byte) 1, (byte) 3));
        }
        if (a2.classify() == 1) {
            return a2;
        }
        Dfp x2 = new Dfp(a2);
        int lr = x2.log10K();
        Dfp x3 = x2.divide(pow(a2.newInstance(10000), lr));
        int ix = x3.floor().intValue();
        while (ix > 2) {
            ix >>= 1;
            p2++;
        }
        Dfp[] spx = split(x3);
        Dfp[] spy = new Dfp[2];
        spy[0] = pow(a2.getTwo(), p2);
        spx[0] = spx[0].divide(spy[0]);
        spx[1] = spx[1].divide(spy[0]);
        spy[0] = a2.newInstance("1.33333");
        while (spx[0].add(spx[1]).greaterThan(spy[0])) {
            spx[0] = spx[0].divide(2);
            spx[1] = spx[1].divide(2);
            p2++;
        }
        Dfp[] spz = logInternal(spx);
        spx[0] = a2.newInstance(new StringBuilder().append(p2 + (4 * lr)).toString());
        spx[1] = a2.getZero();
        Dfp[] spy2 = splitMult(a2.getField2().getLn2Split(), spx);
        spz[0] = spz[0].add(spy2[0]);
        spz[1] = spz[1].add(spy2[1]);
        spx[0] = a2.newInstance(new StringBuilder().append(4 * lr).toString());
        spx[1] = a2.getZero();
        Dfp[] spy3 = splitMult(a2.getField2().getLn5Split(), spx);
        spz[0] = spz[0].add(spy3[0]);
        spz[1] = spz[1].add(spy3[1]);
        return a2.newInstance(spz[0].add(spz[1]));
    }

    protected static Dfp[] logInternal(Dfp[] a2) {
        Dfp t2 = a2[0].divide(4).add(a2[1].divide(4));
        Dfp x2 = t2.add(a2[0].newInstance("-0.25")).divide(t2.add(a2[0].newInstance("0.25")));
        Dfp y2 = new Dfp(x2);
        Dfp num = new Dfp(x2);
        Dfp py = new Dfp(y2);
        int den = 1;
        for (int i2 = 0; i2 < 10000; i2++) {
            num = num.multiply(x2).multiply(x2);
            den += 2;
            y2 = y2.add(num.divide(den));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return split(y2.multiply(a2[0].getTwo()));
    }

    public static Dfp pow(Dfp x2, Dfp y2) {
        Dfp r2;
        if (x2.getField2().getRadixDigits() != y2.getField2().getRadixDigits()) {
            x2.getField2().setIEEEFlagsBits(1);
            Dfp result = x2.newInstance(x2.getZero());
            result.nans = (byte) 3;
            return x2.dotrap(1, POW_TRAP, x2, result);
        }
        Dfp zero = x2.getZero();
        Dfp one = x2.getOne();
        Dfp two = x2.getTwo();
        boolean invert = false;
        if (y2.equals(zero)) {
            return x2.newInstance(one);
        }
        if (y2.equals(one)) {
            if (x2.isNaN()) {
                x2.getField2().setIEEEFlagsBits(1);
                return x2.dotrap(1, POW_TRAP, x2, x2);
            }
            return x2;
        }
        if (x2.isNaN() || y2.isNaN()) {
            x2.getField2().setIEEEFlagsBits(1);
            return x2.dotrap(1, POW_TRAP, x2, x2.newInstance((byte) 1, (byte) 3));
        }
        if (x2.equals(zero)) {
            if (Dfp.copysign(one, x2).greaterThan(zero)) {
                if (y2.greaterThan(zero)) {
                    return x2.newInstance(zero);
                }
                return x2.newInstance(x2.newInstance((byte) 1, (byte) 1));
            }
            if (y2.classify() == 0 && y2.rint().equals(y2) && !y2.remainder(two).equals(zero)) {
                if (y2.greaterThan(zero)) {
                    return x2.newInstance(zero.negate());
                }
                return x2.newInstance(x2.newInstance((byte) -1, (byte) 1));
            }
            if (y2.greaterThan(zero)) {
                return x2.newInstance(zero);
            }
            return x2.newInstance(x2.newInstance((byte) 1, (byte) 1));
        }
        if (x2.lessThan(zero)) {
            x2 = x2.negate();
            invert = true;
        }
        if (x2.greaterThan(one) && y2.classify() == 1) {
            if (y2.greaterThan(zero)) {
                return y2;
            }
            return x2.newInstance(zero);
        }
        if (x2.lessThan(one) && y2.classify() == 1) {
            if (y2.greaterThan(zero)) {
                return x2.newInstance(zero);
            }
            return x2.newInstance(Dfp.copysign(y2, one));
        }
        if (x2.equals(one) && y2.classify() == 1) {
            x2.getField2().setIEEEFlagsBits(1);
            return x2.dotrap(1, POW_TRAP, x2, x2.newInstance((byte) 1, (byte) 3));
        }
        if (x2.classify() == 1) {
            if (invert) {
                if (y2.classify() == 0 && y2.rint().equals(y2) && !y2.remainder(two).equals(zero)) {
                    if (y2.greaterThan(zero)) {
                        return x2.newInstance(x2.newInstance((byte) -1, (byte) 1));
                    }
                    return x2.newInstance(zero.negate());
                }
                if (y2.greaterThan(zero)) {
                    return x2.newInstance(x2.newInstance((byte) 1, (byte) 1));
                }
                return x2.newInstance(zero);
            }
            if (y2.greaterThan(zero)) {
                return x2;
            }
            return x2.newInstance(zero);
        }
        if (invert && !y2.rint().equals(y2)) {
            x2.getField2().setIEEEFlagsBits(1);
            return x2.dotrap(1, POW_TRAP, x2, x2.newInstance((byte) 1, (byte) 3));
        }
        if (y2.lessThan(x2.newInstance(100000000)) && y2.greaterThan(x2.newInstance(-100000000))) {
            Dfp u2 = y2.rint();
            int ui = u2.intValue();
            Dfp v2 = y2.subtract(u2);
            if (v2.unequal(zero)) {
                Dfp a2 = v2.multiply(log(x2));
                Dfp b2 = a2.divide(x2.getField2().getLn2()).rint();
                Dfp c2 = a2.subtract(b2.multiply(x2.getField2().getLn2()));
                Dfp r3 = splitPow(split(x2), ui);
                r2 = r3.multiply(pow(two, b2.intValue())).multiply(exp(c2));
            } else {
                r2 = splitPow(split(x2), ui);
            }
        } else {
            r2 = exp(log(x2).multiply(y2));
        }
        if (invert && y2.rint().equals(y2) && !y2.remainder(two).equals(zero)) {
            r2 = r2.negate();
        }
        return x2.newInstance(r2);
    }

    protected static Dfp sinInternal(Dfp[] a2) {
        Dfp c2 = a2[0].add(a2[1]);
        Dfp y2 = c2;
        Dfp c3 = c2.multiply(c2);
        Dfp x2 = y2;
        Dfp fact = a2[0].getOne();
        Dfp py = new Dfp(y2);
        for (int i2 = 3; i2 < 90; i2 += 2) {
            x2 = x2.multiply(c3).negate();
            fact = fact.divide((i2 - 1) * i2);
            y2 = y2.add(x2.multiply(fact));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2;
    }

    protected static Dfp cosInternal(Dfp[] a2) {
        Dfp one = a2[0].getOne();
        Dfp x2 = one;
        Dfp y2 = one;
        Dfp c2 = a2[0].add(a2[1]);
        Dfp c3 = c2.multiply(c2);
        Dfp fact = one;
        Dfp py = new Dfp(y2);
        for (int i2 = 2; i2 < 90; i2 += 2) {
            x2 = x2.multiply(c3).negate();
            fact = fact.divide((i2 - 1) * i2);
            y2 = y2.add(x2.multiply(fact));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2;
    }

    public static Dfp sin(Dfp a2) {
        Dfp y2;
        Dfp pi = a2.getField2().getPi();
        Dfp zero = a2.getField2().getZero();
        boolean neg = false;
        Dfp x2 = a2.remainder(pi.multiply(2));
        if (x2.lessThan(zero)) {
            x2 = x2.negate();
            neg = true;
        }
        if (x2.greaterThan(pi.divide(2))) {
            x2 = pi.subtract(x2);
        }
        if (x2.lessThan(pi.divide(4))) {
            y2 = sinInternal(split(x2));
        } else {
            Dfp[] piSplit = a2.getField2().getPiSplit();
            Dfp[] c2 = {piSplit[0].divide(2).subtract(x2), piSplit[1].divide(2)};
            y2 = cosInternal(c2);
        }
        if (neg) {
            y2 = y2.negate();
        }
        return a2.newInstance(y2);
    }

    public static Dfp cos(Dfp a2) {
        Dfp y2;
        Dfp pi = a2.getField2().getPi();
        Dfp zero = a2.getField2().getZero();
        boolean neg = false;
        Dfp x2 = a2.remainder(pi.multiply(2));
        if (x2.lessThan(zero)) {
            x2 = x2.negate();
        }
        if (x2.greaterThan(pi.divide(2))) {
            x2 = pi.subtract(x2);
            neg = true;
        }
        if (x2.lessThan(pi.divide(4))) {
            Dfp[] c2 = {x2, zero};
            y2 = cosInternal(c2);
        } else {
            Dfp[] piSplit = a2.getField2().getPiSplit();
            Dfp[] c3 = {piSplit[0].divide(2).subtract(x2), piSplit[1].divide(2)};
            y2 = sinInternal(c3);
        }
        if (neg) {
            y2 = y2.negate();
        }
        return a2.newInstance(y2);
    }

    public static Dfp tan(Dfp a2) {
        return sin(a2).divide(cos(a2));
    }

    protected static Dfp atanInternal(Dfp a2) {
        Dfp y2 = new Dfp(a2);
        Dfp x2 = new Dfp(y2);
        Dfp py = new Dfp(y2);
        for (int i2 = 3; i2 < 90; i2 += 2) {
            x2 = x2.multiply(a2).multiply(a2).negate();
            y2 = y2.add(x2.divide(i2));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return y2;
    }

    public static Dfp atan(Dfp a2) {
        Dfp zero = a2.getField2().getZero();
        Dfp one = a2.getField2().getOne();
        Dfp[] sqr2Split = a2.getField2().getSqr2Split();
        Dfp[] piSplit = a2.getField2().getPiSplit();
        boolean recp = false;
        boolean neg = false;
        boolean sub = false;
        Dfp ty = sqr2Split[0].subtract(one).add(sqr2Split[1]);
        Dfp x2 = new Dfp(a2);
        if (x2.lessThan(zero)) {
            neg = true;
            x2 = x2.negate();
        }
        if (x2.greaterThan(one)) {
            recp = true;
            x2 = one.divide(x2);
        }
        if (x2.greaterThan(ty)) {
            sub = true;
            Dfp[] sty = {sqr2Split[0].subtract(one), sqr2Split[1]};
            Dfp[] xs = split(x2);
            Dfp[] ds = splitMult(xs, sty);
            ds[0] = ds[0].add(one);
            xs[0] = xs[0].subtract(sty[0]);
            xs[1] = xs[1].subtract(sty[1]);
            Dfp[] xs2 = splitDiv(xs, ds);
            x2 = xs2[0].add(xs2[1]);
        }
        Dfp y2 = atanInternal(x2);
        if (sub) {
            y2 = y2.add(piSplit[0].divide(8)).add(piSplit[1].divide(8));
        }
        if (recp) {
            y2 = piSplit[0].divide(2).subtract(y2).add(piSplit[1].divide(2));
        }
        if (neg) {
            y2 = y2.negate();
        }
        return a2.newInstance(y2);
    }

    public static Dfp asin(Dfp a2) {
        return atan(a2.divide(a2.getOne().subtract(a2.multiply(a2)).sqrt()));
    }

    public static Dfp acos(Dfp a2) {
        boolean negative = false;
        if (a2.lessThan(a2.getZero())) {
            negative = true;
        }
        Dfp a3 = Dfp.copysign(a2, a2.getOne());
        Dfp result = atan(a3.getOne().subtract(a3.multiply(a3)).sqrt().divide(a3));
        if (negative) {
            result = a3.getField2().getPi().subtract(result);
        }
        return a3.newInstance(result);
    }
}
