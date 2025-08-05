package jdk.nashorn.internal.runtime;

import java.math.BigInteger;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/NumberToString.class */
public final class NumberToString {
    private final boolean isNaN;
    private boolean isNegative;
    private int decimalExponent;
    private char[] digits;
    private int nDigits;
    private static final int expMask = 2047;
    private static final long fractMask = 4503599627370495L;
    private static final int expShift = 52;
    private static final int expBias = 1023;
    private static final long fractHOB = 4503599627370496L;
    private static final long expOne = 4607182418800017408L;
    private static final int maxSmallBinExp = 62;
    private static final int minSmallBinExp = -21;
    private static final long[] powersOf5 = {1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L};
    private static final int[] nBitsPowerOf5 = {0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61};
    private static final char[] infinityDigits = {'I', 'n', 'f', 'i', 'n', 'i', 't', 'y'};
    private static final char[] nanDigits = {'N', 'a', 'N'};
    private static final char[] zeroes = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    private static BigInteger[] powerOf5Cache;

    public static String stringFor(double value) {
        return new NumberToString(value).toString();
    }

    private NumberToString(double value) {
        int nSignificantBits;
        int ndigit;
        boolean low;
        boolean high;
        long lowDigitDifference;
        long halfULP;
        long bits;
        int c2;
        long bits2 = Double.doubleToLongBits(value);
        int upper = (int) (bits2 >> 32);
        this.isNegative = upper < 0;
        int exponent = (upper >> 20) & 2047;
        long bits3 = bits2 & 4503599627370495L;
        if (exponent == 2047) {
            this.isNaN = true;
            if (bits3 == 0) {
                this.digits = infinityDigits;
            } else {
                this.digits = nanDigits;
                this.isNegative = false;
            }
            this.nDigits = this.digits.length;
            return;
        }
        this.isNaN = false;
        if (exponent != 0) {
            bits3 |= fractHOB;
            nSignificantBits = 53;
        } else if (bits3 == 0) {
            this.decimalExponent = 0;
            this.digits = zeroes;
            this.nDigits = 1;
            return;
        } else {
            while ((bits3 & fractHOB) == 0) {
                bits3 <<= 1;
                exponent--;
            }
            nSignificantBits = 52 + exponent + 1;
            exponent++;
        }
        int exponent2 = exponent - 1023;
        int nFractBits = countSignificantBits(bits3);
        int nTinyBits = Math.max(0, (nFractBits - exponent2) - 1);
        if (exponent2 <= 62 && exponent2 >= minSmallBinExp && nTinyBits < powersOf5.length && nFractBits + nBitsPowerOf5[nTinyBits] < 64 && nTinyBits == 0) {
            if (exponent2 > nSignificantBits) {
                halfULP = 1 << ((exponent2 - nSignificantBits) - 1);
            } else {
                halfULP = 0;
            }
            if (exponent2 >= 52) {
                bits = bits3 << (exponent2 - 52);
            } else {
                bits = bits3 >>> (52 - exponent2);
            }
            int i2 = 0;
            while (halfULP >= 10) {
                halfULP /= 10;
                i2++;
            }
            int decExp = 0;
            if (i2 != 0) {
                long powerOf10 = powersOf5[i2] << i2;
                long residue = bits % powerOf10;
                bits /= powerOf10;
                decExp = 0 + i2;
                if (residue >= (powerOf10 >> 1)) {
                    bits++;
                }
            }
            char[] digits0 = new char[26];
            int digitno = 20 - 1;
            while (true) {
                c2 = (int) (bits % 10);
                bits /= 10;
                if (c2 != 0) {
                    break;
                } else {
                    decExp++;
                }
            }
            while (bits != 0) {
                int i3 = digitno;
                digitno--;
                digits0[i3] = (char) (c2 + 48);
                decExp++;
                c2 = (int) (bits % 10);
                bits /= 10;
            }
            digits0[digitno] = (char) (c2 + 48);
            int ndigits = 20 - digitno;
            char[] result = new char[ndigits];
            System.arraycopy(digits0, digitno, result, 0, ndigits);
            this.digits = result;
            this.decimalExponent = decExp + 1;
            this.nDigits = ndigits;
            return;
        }
        double d2 = Double.longBitsToDouble(expOne | (bits3 & (-4503599627370497L)));
        int decExponent = (int) Math.floor(((d2 - 1.5d) * 0.289529654d) + 0.176091259d + (exponent2 * 0.301029995663981d));
        int B5 = Math.max(0, -decExponent);
        int B2 = B5 + nTinyBits + exponent2;
        int S5 = Math.max(0, decExponent);
        int S2 = S5 + nTinyBits;
        int M2 = B2 - nSignificantBits;
        long bits4 = bits3 >>> (53 - nFractBits);
        int B22 = B2 - (nFractBits - 1);
        int common2factor = Math.min(B22, S2);
        int B23 = B22 - common2factor;
        int S22 = S2 - common2factor;
        int M22 = M2 - common2factor;
        M22 = nFractBits == 1 ? M22 - 1 : M22;
        if (M22 < 0) {
            B23 -= M22;
            S22 -= M22;
            M22 = 0;
        }
        char[] digits02 = new char[32];
        this.digits = digits02;
        int Bbits = nFractBits + B23 + (B5 < nBitsPowerOf5.length ? nBitsPowerOf5[B5] : B5 * 3);
        int tenSbits = S22 + 1 + (S5 + 1 < nBitsPowerOf5.length ? nBitsPowerOf5[S5 + 1] : (S5 + 1) * 3);
        if (Bbits < 64 && tenSbits < 64) {
            long b2 = (bits4 * powersOf5[B5]) << B23;
            long s2 = powersOf5[S5] << S22;
            long m2 = powersOf5[B5] << M22;
            long tens = s2 * 10;
            ndigit = 0;
            int q2 = (int) (b2 / s2);
            long b3 = 10 * (b2 % s2);
            long m3 = m2 * 10;
            low = b3 < m3;
            high = b3 + m3 > tens;
            if (q2 != 0 || high) {
                ndigit = 0 + 1;
                digits02[0] = (char) (48 + q2);
            } else {
                decExponent--;
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = false;
                high = false;
            }
            while (!low && !high) {
                int q3 = (int) (b3 / s2);
                b3 = 10 * (b3 % s2);
                m3 *= 10;
                if (m3 > 0) {
                    low = b3 < m3;
                    high = b3 + m3 > tens;
                } else {
                    low = true;
                    high = true;
                }
                if (low && q3 == 0) {
                    break;
                }
                int i4 = ndigit;
                ndigit++;
                digits02[i4] = (char) (48 + q3);
            }
            lowDigitDifference = (b3 << 1) - tens;
        } else {
            BigInteger Bval = multiplyPowerOf5And2(BigInteger.valueOf(bits4), B5, B23);
            BigInteger Sval = constructPowerOf5And2(S5, S22);
            BigInteger Mval = constructPowerOf5And2(B5, M22);
            int shiftBias = Long.numberOfLeadingZeros(bits4) - 4;
            BigInteger Bval2 = Bval.shiftLeft(shiftBias);
            BigInteger Mval2 = Mval.shiftLeft(shiftBias);
            BigInteger Sval2 = Sval.shiftLeft(shiftBias);
            BigInteger tenSval = Sval2.multiply(BigInteger.TEN);
            ndigit = 0;
            BigInteger[] quoRem = Bval2.divideAndRemainder(Sval2);
            int q4 = quoRem[0].intValue();
            BigInteger Bval3 = quoRem[1].multiply(BigInteger.TEN);
            BigInteger Mval3 = Mval2.multiply(BigInteger.TEN);
            low = Bval3.compareTo(Mval3) < 0;
            high = Bval3.add(Mval3).compareTo(tenSval) > 0;
            if (q4 != 0 || high) {
                ndigit = 0 + 1;
                digits02[0] = (char) (48 + q4);
            } else {
                decExponent--;
            }
            if (decExponent < -3 || decExponent >= 8) {
                low = false;
                high = false;
            }
            while (!low && !high) {
                BigInteger[] quoRem2 = Bval3.divideAndRemainder(Sval2);
                int q5 = quoRem2[0].intValue();
                Bval3 = quoRem2[1].multiply(BigInteger.TEN);
                Mval3 = Mval3.multiply(BigInteger.TEN);
                low = Bval3.compareTo(Mval3) < 0;
                high = Bval3.add(Mval3).compareTo(tenSval) > 0;
                if (low && q5 == 0) {
                    break;
                }
                int i5 = ndigit;
                ndigit++;
                digits02[i5] = (char) (48 + q5);
            }
            if (high && low) {
                lowDigitDifference = Bval3.shiftLeft(1).compareTo(tenSval);
            } else {
                lowDigitDifference = 0;
            }
        }
        this.decimalExponent = decExponent + 1;
        this.digits = digits02;
        this.nDigits = ndigit;
        if (high) {
            if (!low) {
                roundup();
                return;
            }
            if (lowDigitDifference == 0) {
                if ((digits02[this.nDigits - 1] & 1) != 0) {
                    roundup();
                }
            } else if (lowDigitDifference > 0) {
                roundup();
            }
        }
    }

    private static int countSignificantBits(long bits) {
        if (bits != 0) {
            return (64 - Long.numberOfLeadingZeros(bits)) - Long.numberOfTrailingZeros(bits);
        }
        return 0;
    }

    private static BigInteger bigPowerOf5(int power) {
        if (powerOf5Cache == null) {
            powerOf5Cache = new BigInteger[power + 1];
        } else if (powerOf5Cache.length <= power) {
            BigInteger[] t2 = new BigInteger[power + 1];
            System.arraycopy(powerOf5Cache, 0, t2, 0, powerOf5Cache.length);
            powerOf5Cache = t2;
        }
        if (powerOf5Cache[power] != null) {
            return powerOf5Cache[power];
        }
        if (power < powersOf5.length) {
            BigInteger[] bigIntegerArr = powerOf5Cache;
            BigInteger bigIntegerValueOf = BigInteger.valueOf(powersOf5[power]);
            bigIntegerArr[power] = bigIntegerValueOf;
            return bigIntegerValueOf;
        }
        int q2 = power >> 1;
        int r2 = power - q2;
        BigInteger bigQ = powerOf5Cache[q2];
        if (bigQ == null) {
            bigQ = bigPowerOf5(q2);
        }
        if (r2 < powersOf5.length) {
            BigInteger[] bigIntegerArr2 = powerOf5Cache;
            BigInteger bigIntegerMultiply = bigQ.multiply(BigInteger.valueOf(powersOf5[r2]));
            bigIntegerArr2[power] = bigIntegerMultiply;
            return bigIntegerMultiply;
        }
        BigInteger bigR = powerOf5Cache[r2];
        if (bigR == null) {
            bigR = bigPowerOf5(r2);
        }
        BigInteger[] bigIntegerArr3 = powerOf5Cache;
        BigInteger bigIntegerMultiply2 = bigQ.multiply(bigR);
        bigIntegerArr3[power] = bigIntegerMultiply2;
        return bigIntegerMultiply2;
    }

    private static BigInteger multiplyPowerOf5And2(BigInteger value, int p5, int p2) {
        BigInteger returnValue = value;
        if (p5 != 0) {
            returnValue = returnValue.multiply(bigPowerOf5(p5));
        }
        if (p2 != 0) {
            returnValue = returnValue.shiftLeft(p2);
        }
        return returnValue;
    }

    private static BigInteger constructPowerOf5And2(int p5, int p2) {
        BigInteger v2 = bigPowerOf5(p5);
        if (p2 != 0) {
            v2 = v2.shiftLeft(p2);
        }
        return v2;
    }

    private void roundup() {
        int q2;
        char[] cArr = this.digits;
        int i2 = this.nDigits - 1;
        int i3 = i2;
        char c2 = cArr[i2];
        while (true) {
            q2 = c2;
            if (q2 != 57 || i3 <= 0) {
                break;
            }
            if (this.decimalExponent < 0) {
                this.nDigits--;
            } else {
                this.digits[i3] = '0';
            }
            i3--;
            c2 = this.digits[i3];
        }
        if (q2 == 57) {
            this.decimalExponent++;
            this.digits[0] = '1';
        } else {
            this.digits[i3] = (char) (q2 + 1);
        }
    }

    public String toString() {
        int e2;
        int exponent;
        StringBuilder sb = new StringBuilder(32);
        if (this.isNegative) {
            sb.append('-');
        }
        if (this.isNaN) {
            sb.append(this.digits, 0, this.nDigits);
        } else if (this.decimalExponent > 0 && this.decimalExponent <= 21) {
            int charLength = Math.min(this.nDigits, this.decimalExponent);
            sb.append(this.digits, 0, charLength);
            if (charLength < this.decimalExponent) {
                sb.append(zeroes, 0, this.decimalExponent - charLength);
            } else if (charLength < this.nDigits) {
                sb.append('.');
                sb.append(this.digits, charLength, this.nDigits - charLength);
            }
        } else if (this.decimalExponent <= 0 && this.decimalExponent > -6) {
            sb.append('0');
            sb.append('.');
            if (this.decimalExponent != 0) {
                sb.append(zeroes, 0, -this.decimalExponent);
            }
            sb.append(this.digits, 0, this.nDigits);
        } else {
            sb.append(this.digits[0]);
            if (this.nDigits > 1) {
                sb.append('.');
                sb.append(this.digits, 1, this.nDigits - 1);
            }
            sb.append('e');
            if (this.decimalExponent <= 0) {
                sb.append('-');
                int i2 = (-this.decimalExponent) + 1;
                e2 = i2;
                exponent = i2;
            } else {
                sb.append('+');
                int i3 = this.decimalExponent - 1;
                e2 = i3;
                exponent = i3;
            }
            if (exponent > 99) {
                sb.append((char) ((e2 / 100) + 48));
                e2 %= 100;
            }
            if (exponent > 9) {
                sb.append((char) ((e2 / 10) + 48));
                e2 %= 10;
            }
            sb.append((char) (e2 + 48));
        }
        return sb.toString();
    }
}
