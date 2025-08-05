package org.apache.commons.math3.util;

import java.math.BigInteger;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/ArithmeticUtils.class */
public final class ArithmeticUtils {
    private ArithmeticUtils() {
    }

    public static int addAndCheck(int x2, int y2) throws MathArithmeticException {
        long s2 = x2 + y2;
        if (s2 < -2147483648L || s2 > 2147483647L) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(x2), Integer.valueOf(y2));
        }
        return (int) s2;
    }

    public static long addAndCheck(long a2, long b2) throws MathArithmeticException {
        return addAndCheck(a2, b2, LocalizedFormats.OVERFLOW_IN_ADDITION);
    }

    @Deprecated
    public static long binomialCoefficient(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficient(n2, k2);
    }

    @Deprecated
    public static double binomialCoefficientDouble(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficientDouble(n2, k2);
    }

    @Deprecated
    public static double binomialCoefficientLog(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficientLog(n2, k2);
    }

    @Deprecated
    public static long factorial(int n2) throws NotPositiveException, MathArithmeticException {
        return CombinatoricsUtils.factorial(n2);
    }

    @Deprecated
    public static double factorialDouble(int n2) throws NotPositiveException {
        return CombinatoricsUtils.factorialDouble(n2);
    }

    @Deprecated
    public static double factorialLog(int n2) throws NotPositiveException {
        return CombinatoricsUtils.factorialLog(n2);
    }

    public static int gcd(int p2, int q2) throws MathArithmeticException {
        int a2 = p2;
        int b2 = q2;
        if (a2 == 0 || b2 == 0) {
            if (a2 == Integer.MIN_VALUE || b2 == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p2), Integer.valueOf(q2));
            }
            return FastMath.abs(a2 + b2);
        }
        long al2 = a2;
        long bl2 = b2;
        boolean useLong = false;
        if (a2 < 0) {
            if (Integer.MIN_VALUE == a2) {
                useLong = true;
            } else {
                a2 = -a2;
            }
            al2 = -al2;
        }
        if (b2 < 0) {
            if (Integer.MIN_VALUE == b2) {
                useLong = true;
            } else {
                b2 = -b2;
            }
            bl2 = -bl2;
        }
        if (useLong) {
            if (al2 == bl2) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p2), Integer.valueOf(q2));
            }
            long blbu = bl2;
            long bl3 = al2;
            long al3 = blbu % al2;
            if (al3 == 0) {
                if (bl3 > 2147483647L) {
                    throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p2), Integer.valueOf(q2));
                }
                return (int) bl3;
            }
            b2 = (int) al3;
            a2 = (int) (bl3 % al3);
        }
        return gcdPositive(a2, b2);
    }

    private static int gcdPositive(int a2, int b2) {
        if (a2 == 0) {
            return b2;
        }
        if (b2 == 0) {
            return a2;
        }
        int aTwos = Integer.numberOfTrailingZeros(a2);
        int a3 = a2 >> aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b2);
        int b3 = b2 >> bTwos;
        int shift = FastMath.min(aTwos, bTwos);
        while (a3 != b3) {
            int delta = a3 - b3;
            b3 = Math.min(a3, b3);
            int a4 = Math.abs(delta);
            a3 = a4 >> Integer.numberOfTrailingZeros(a4);
        }
        return a3 << shift;
    }

    public static long gcd(long p2, long q2) throws MathArithmeticException {
        long u2 = p2;
        long v2 = q2;
        if (u2 == 0 || v2 == 0) {
            if (u2 == Long.MIN_VALUE || v2 == Long.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, Long.valueOf(p2), Long.valueOf(q2));
            }
            return FastMath.abs(u2) + FastMath.abs(v2);
        }
        if (u2 > 0) {
            u2 = -u2;
        }
        if (v2 > 0) {
            v2 = -v2;
        }
        int k2 = 0;
        while ((u2 & 1) == 0 && (v2 & 1) == 0 && k2 < 63) {
            u2 /= 2;
            v2 /= 2;
            k2++;
        }
        if (k2 == 63) {
            throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, Long.valueOf(p2), Long.valueOf(q2));
        }
        long t2 = (u2 & 1) == 1 ? v2 : -(u2 / 2);
        while (true) {
            if ((t2 & 1) == 0) {
                t2 /= 2;
            } else {
                if (t2 > 0) {
                    u2 = -t2;
                } else {
                    v2 = t2;
                }
                t2 = (v2 - u2) / 2;
                if (t2 == 0) {
                    return (-u2) * (1 << k2);
                }
            }
        }
    }

    public static int lcm(int a2, int b2) throws MathArithmeticException {
        if (a2 == 0 || b2 == 0) {
            return 0;
        }
        int lcm = FastMath.abs(mulAndCheck(a2 / gcd(a2, b2), b2));
        if (lcm == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_32_BITS, Integer.valueOf(a2), Integer.valueOf(b2));
        }
        return lcm;
    }

    public static long lcm(long a2, long b2) throws MathArithmeticException {
        if (a2 == 0 || b2 == 0) {
            return 0L;
        }
        long lcm = FastMath.abs(mulAndCheck(a2 / gcd(a2, b2), b2));
        if (lcm == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_64_BITS, Long.valueOf(a2), Long.valueOf(b2));
        }
        return lcm;
    }

    public static int mulAndCheck(int x2, int y2) throws MathArithmeticException {
        long m2 = x2 * y2;
        if (m2 < -2147483648L || m2 > 2147483647L) {
            throw new MathArithmeticException();
        }
        return (int) m2;
    }

    public static long mulAndCheck(long a2, long b2) throws MathArithmeticException {
        long ret;
        if (a2 > b2) {
            ret = mulAndCheck(b2, a2);
        } else if (a2 < 0) {
            if (b2 < 0) {
                if (a2 >= Long.MAX_VALUE / b2) {
                    ret = a2 * b2;
                } else {
                    throw new MathArithmeticException();
                }
            } else if (b2 <= 0) {
                ret = 0;
            } else if (Long.MIN_VALUE / b2 <= a2) {
                ret = a2 * b2;
            } else {
                throw new MathArithmeticException();
            }
        } else if (a2 <= 0) {
            ret = 0;
        } else if (a2 <= Long.MAX_VALUE / b2) {
            ret = a2 * b2;
        } else {
            throw new MathArithmeticException();
        }
        return ret;
    }

    public static int subAndCheck(int x2, int y2) throws MathArithmeticException {
        long s2 = x2 - y2;
        if (s2 < -2147483648L || s2 > 2147483647L) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(x2), Integer.valueOf(y2));
        }
        return (int) s2;
    }

    public static long subAndCheck(long a2, long b2) throws MathArithmeticException {
        long ret;
        if (b2 != Long.MIN_VALUE) {
            ret = addAndCheck(a2, -b2, LocalizedFormats.OVERFLOW_IN_ADDITION);
        } else if (a2 < 0) {
            ret = a2 - b2;
        } else {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(a2), Long.valueOf(-b2));
        }
        return ret;
    }

    public static int pow(int k2, int e2) throws NotPositiveException, MathArithmeticException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e2));
        }
        int exp = e2;
        int result = 1;
        int k2p = k2;
        while (true) {
            try {
                if ((exp & 1) != 0) {
                    result = mulAndCheck(result, k2p);
                }
                exp >>= 1;
                if (exp != 0) {
                    k2p = mulAndCheck(k2p, k2p);
                } else {
                    return result;
                }
            } catch (MathArithmeticException mae) {
                mae.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
                mae.getContext().addMessage(LocalizedFormats.BASE, Integer.valueOf(k2));
                mae.getContext().addMessage(LocalizedFormats.EXPONENT, Integer.valueOf(e2));
                throw mae;
            }
        }
    }

    @Deprecated
    public static int pow(int k2, long e2) throws NotPositiveException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e2));
        }
        int result = 1;
        int k2p = k2;
        while (e2 != 0) {
            if ((e2 & 1) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e2 >>= 1;
        }
        return result;
    }

    public static long pow(long k2, int e2) throws NotPositiveException, MathArithmeticException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e2));
        }
        int exp = e2;
        long result = 1;
        long k2p = k2;
        while (true) {
            try {
                if ((exp & 1) != 0) {
                    result = mulAndCheck(result, k2p);
                }
                exp >>= 1;
                if (exp != 0) {
                    k2p = mulAndCheck(k2p, k2p);
                } else {
                    return result;
                }
            } catch (MathArithmeticException mae) {
                mae.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
                mae.getContext().addMessage(LocalizedFormats.BASE, Long.valueOf(k2));
                mae.getContext().addMessage(LocalizedFormats.EXPONENT, Integer.valueOf(e2));
                throw mae;
            }
        }
    }

    @Deprecated
    public static long pow(long k2, long e2) throws NotPositiveException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e2));
        }
        long result = 1;
        long k2p = k2;
        while (e2 != 0) {
            if ((e2 & 1) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
            e2 >>= 1;
        }
        return result;
    }

    public static BigInteger pow(BigInteger k2, int e2) throws NotPositiveException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e2));
        }
        return k2.pow(e2);
    }

    public static BigInteger pow(BigInteger k2, long e2) throws NotPositiveException {
        if (e2 < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e2));
        }
        BigInteger result = BigInteger.ONE;
        BigInteger k2p = k2;
        while (e2 != 0) {
            if ((e2 & 1) != 0) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e2 >>= 1;
        }
        return result;
    }

    public static BigInteger pow(BigInteger k2, BigInteger e2) throws NotPositiveException {
        if (e2.compareTo(BigInteger.ZERO) < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, e2);
        }
        BigInteger result = BigInteger.ONE;
        BigInteger k2p = k2;
        while (!BigInteger.ZERO.equals(e2)) {
            if (e2.testBit(0)) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e2 = e2.shiftRight(1);
        }
        return result;
    }

    @Deprecated
    public static long stirlingS2(int n2, int k2) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.stirlingS2(n2, k2);
    }

    private static long addAndCheck(long a2, long b2, Localizable pattern) throws MathArithmeticException {
        long result = a2 + b2;
        if (!(((a2 ^ b2) < 0) | ((a2 ^ result) >= 0))) {
            throw new MathArithmeticException(pattern, Long.valueOf(a2), Long.valueOf(b2));
        }
        return result;
    }

    public static boolean isPowerOfTwo(long n2) {
        return n2 > 0 && (n2 & (n2 - 1)) == 0;
    }
}
