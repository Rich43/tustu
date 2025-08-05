package java.lang;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.math.BigInteger;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/lang/Long.class */
public final class Long extends Number implements Comparable<Long> {
    public static final long MIN_VALUE = Long.MIN_VALUE;
    public static final long MAX_VALUE = Long.MAX_VALUE;
    public static final Class<Long> TYPE = Class.getPrimitiveClass(SchemaSymbols.ATTVAL_LONG);
    private final long value;
    public static final int SIZE = 64;
    public static final int BYTES = 8;
    private static final long serialVersionUID = 4290774380558885855L;

    public static String toString(long j2, int i2) {
        if (i2 < 2 || i2 > 36) {
            i2 = 10;
        }
        if (i2 == 10) {
            return toString(j2);
        }
        char[] cArr = new char[65];
        int i3 = 64;
        boolean z2 = j2 < 0;
        if (!z2) {
            j2 = -j2;
        }
        while (j2 <= (-i2)) {
            int i4 = i3;
            i3--;
            cArr[i4] = Integer.digits[(int) (-(j2 % i2))];
            j2 /= i2;
        }
        cArr[i3] = Integer.digits[(int) (-j2)];
        if (z2) {
            i3--;
            cArr[i3] = '-';
        }
        return new String(cArr, i3, 65 - i3);
    }

    public static String toUnsignedString(long j2, int i2) {
        if (j2 >= 0) {
            return toString(j2, i2);
        }
        switch (i2) {
            case 2:
                return toBinaryString(j2);
            case 4:
                return toUnsignedString0(j2, 2);
            case 8:
                return toOctalString(j2);
            case 10:
                long j3 = (j2 >>> 1) / 5;
                return toString(j3) + (j2 - (j3 * 10));
            case 16:
                return toHexString(j2);
            case 32:
                return toUnsignedString0(j2, 5);
            default:
                return toUnsignedBigInteger(j2).toString(i2);
        }
    }

    private static BigInteger toUnsignedBigInteger(long j2) {
        if (j2 >= 0) {
            return BigInteger.valueOf(j2);
        }
        return BigInteger.valueOf(Integer.toUnsignedLong((int) (j2 >>> 32))).shiftLeft(32).add(BigInteger.valueOf(Integer.toUnsignedLong((int) j2)));
    }

    public static String toHexString(long j2) {
        return toUnsignedString0(j2, 4);
    }

    public static String toOctalString(long j2) {
        return toUnsignedString0(j2, 3);
    }

    public static String toBinaryString(long j2) {
        return toUnsignedString0(j2, 1);
    }

    static String toUnsignedString0(long j2, int i2) {
        int iMax = Math.max(((64 - numberOfLeadingZeros(j2)) + (i2 - 1)) / i2, 1);
        char[] cArr = new char[iMax];
        formatUnsignedLong(j2, i2, cArr, 0, iMax);
        return new String(cArr, true);
    }

    static int formatUnsignedLong(long j2, int i2, char[] cArr, int i3, int i4) {
        int i5 = i4;
        int i6 = (1 << i2) - 1;
        do {
            i5--;
            cArr[i3 + i5] = Integer.digits[((int) j2) & i6];
            j2 >>>= i2;
            if (j2 == 0) {
                break;
            }
        } while (i5 > 0);
        return i5;
    }

    public static String toString(long j2) {
        if (j2 == Long.MIN_VALUE) {
            return "-9223372036854775808";
        }
        int iStringSize = j2 < 0 ? stringSize(-j2) + 1 : stringSize(j2);
        char[] cArr = new char[iStringSize];
        getChars(j2, iStringSize, cArr);
        return new String(cArr, true);
    }

    public static String toUnsignedString(long j2) {
        return toUnsignedString(j2, 10);
    }

    static void getChars(long j2, int i2, char[] cArr) {
        int i3 = i2;
        char c2 = 0;
        if (j2 < 0) {
            c2 = '-';
            j2 = -j2;
        }
        while (j2 > 2147483647L) {
            long j3 = j2 / 100;
            int i4 = (int) (j2 - (((j3 << 6) + (j3 << 5)) + (j3 << 2)));
            j2 = j3;
            int i5 = i3 - 1;
            cArr[i5] = Integer.DigitOnes[i4];
            i3 = i5 - 1;
            cArr[i3] = Integer.DigitTens[i4];
        }
        int i6 = (int) j2;
        while (i6 >= 65536) {
            int i7 = i6 / 100;
            int i8 = i6 - (((i7 << 6) + (i7 << 5)) + (i7 << 2));
            i6 = i7;
            int i9 = i3 - 1;
            cArr[i9] = Integer.DigitOnes[i8];
            i3 = i9 - 1;
            cArr[i3] = Integer.DigitTens[i8];
        }
        do {
            int i10 = (i6 * 52429) >>> 19;
            i3--;
            cArr[i3] = Integer.digits[i6 - ((i10 << 3) + (i10 << 1))];
            i6 = i10;
        } while (i6 != 0);
        if (c2 != 0) {
            cArr[i3 - 1] = c2;
        }
    }

    static int stringSize(long j2) {
        long j3 = 10;
        for (int i2 = 1; i2 < 19; i2++) {
            if (j2 < j3) {
                return i2;
            }
            j3 = 10 * j3;
        }
        return 19;
    }

    public static long parseLong(String str, int i2) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        if (i2 < 2) {
            throw new NumberFormatException("radix " + i2 + " less than Character.MIN_RADIX");
        }
        if (i2 > 36) {
            throw new NumberFormatException("radix " + i2 + " greater than Character.MAX_RADIX");
        }
        long j2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int length = str.length();
        long j3 = -9223372036854775807L;
        if (length > 0) {
            char cCharAt = str.charAt(0);
            if (cCharAt < '0') {
                if (cCharAt == '-') {
                    z2 = true;
                    j3 = Long.MIN_VALUE;
                } else if (cCharAt != '+') {
                    throw NumberFormatException.forInputString(str);
                }
                if (length == 1) {
                    throw NumberFormatException.forInputString(str);
                }
                i3 = 0 + 1;
            }
            long j4 = j3 / i2;
            while (i3 < length) {
                int i4 = i3;
                i3++;
                int iDigit = Character.digit(str.charAt(i4), i2);
                if (iDigit < 0) {
                    throw NumberFormatException.forInputString(str);
                }
                if (j2 < j4) {
                    throw NumberFormatException.forInputString(str);
                }
                long j5 = j2 * i2;
                if (j5 < j3 + iDigit) {
                    throw NumberFormatException.forInputString(str);
                }
                j2 = j5 - iDigit;
            }
            return z2 ? j2 : -j2;
        }
        throw NumberFormatException.forInputString(str);
    }

    public static long parseLong(String str) throws NumberFormatException {
        return parseLong(str, 10);
    }

    public static long parseUnsignedLong(String str, int i2) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        int length = str.length();
        if (length > 0) {
            if (str.charAt(0) == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", str));
            }
            if (length <= 12 || (i2 == 10 && length <= 18)) {
                return parseLong(str, i2);
            }
            long j2 = parseLong(str.substring(0, length - 1), i2);
            int iDigit = Character.digit(str.charAt(length - 1), i2);
            if (iDigit < 0) {
                throw new NumberFormatException("Bad digit at end of " + str);
            }
            long j3 = (j2 * i2) + iDigit;
            if (compareUnsigned(j3, j2) < 0) {
                throw new NumberFormatException(String.format("String value %s exceeds range of unsigned long.", str));
            }
            return j3;
        }
        throw NumberFormatException.forInputString(str);
    }

    public static long parseUnsignedLong(String str) throws NumberFormatException {
        return parseUnsignedLong(str, 10);
    }

    public static Long valueOf(String str, int i2) throws NumberFormatException {
        return valueOf(parseLong(str, i2));
    }

    public static Long valueOf(String str) throws NumberFormatException {
        return valueOf(parseLong(str, 10));
    }

    /* loaded from: rt.jar:java/lang/Long$LongCache.class */
    private static class LongCache {
        static final Long[] cache = new Long[256];

        private LongCache() {
        }

        static {
            for (int i2 = 0; i2 < cache.length; i2++) {
                cache[i2] = new Long(i2 - 128);
            }
        }
    }

    public static Long valueOf(long j2) {
        if (j2 >= -128 && j2 <= 127) {
            return LongCache.cache[((int) j2) + 128];
        }
        return new Long(j2);
    }

    public static Long decode(String str) throws NumberFormatException {
        Long lValueOf;
        int i2 = 10;
        int i3 = 0;
        boolean z2 = false;
        if (str.length() == 0) {
            throw new NumberFormatException("Zero length string");
        }
        char cCharAt = str.charAt(0);
        if (cCharAt == '-') {
            z2 = true;
            i3 = 0 + 1;
        } else if (cCharAt == '+') {
            i3 = 0 + 1;
        }
        if (str.startsWith("0x", i3) || str.startsWith("0X", i3)) {
            i3 += 2;
            i2 = 16;
        } else if (str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX, i3)) {
            i3++;
            i2 = 16;
        } else if (str.startsWith("0", i3) && str.length() > 1 + i3) {
            i3++;
            i2 = 8;
        }
        if (str.startsWith(LanguageTag.SEP, i3) || str.startsWith(Marker.ANY_NON_NULL_MARKER, i3)) {
            throw new NumberFormatException("Sign character in wrong position");
        }
        try {
            Long lValueOf2 = valueOf(str.substring(i3), i2);
            lValueOf = z2 ? valueOf(-lValueOf2.longValue()) : lValueOf2;
        } catch (NumberFormatException e2) {
            lValueOf = valueOf(z2 ? LanguageTag.SEP + str.substring(i3) : str.substring(i3), i2);
        }
        return lValueOf;
    }

    public Long(long j2) {
        this.value = j2;
    }

    public Long(String str) throws NumberFormatException {
        this.value = parseLong(str, 10);
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return (byte) this.value;
    }

    @Override // java.lang.Number
    public short shortValue() {
        return (short) this.value;
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public String toString() {
        return toString(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(long j2) {
        return (int) (j2 ^ (j2 >>> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof Long) && this.value == ((Long) obj).longValue();
    }

    public static Long getLong(String str) {
        return getLong(str, (Long) null);
    }

    public static Long getLong(String str, long j2) {
        Long l2 = getLong(str, (Long) null);
        return l2 == null ? valueOf(j2) : l2;
    }

    public static Long getLong(String str, Long l2) {
        String property = null;
        try {
            property = System.getProperty(str);
        } catch (IllegalArgumentException | NullPointerException e2) {
        }
        if (property != null) {
            try {
                return decode(property);
            } catch (NumberFormatException e3) {
            }
        }
        return l2;
    }

    @Override // java.lang.Comparable
    public int compareTo(Long l2) {
        return compare(this.value, l2.value);
    }

    public static int compare(long j2, long j3) {
        if (j2 < j3) {
            return -1;
        }
        return j2 == j3 ? 0 : 1;
    }

    public static int compareUnsigned(long j2, long j3) {
        return compare(j2 - Long.MIN_VALUE, j3 - Long.MIN_VALUE);
    }

    public static long divideUnsigned(long j2, long j3) {
        if (j3 < 0) {
            return compareUnsigned(j2, j3) < 0 ? 0L : 1L;
        }
        if (j2 > 0) {
            return j2 / j3;
        }
        return toUnsignedBigInteger(j2).divide(toUnsignedBigInteger(j3)).longValue();
    }

    public static long remainderUnsigned(long j2, long j3) {
        if (j2 > 0 && j3 > 0) {
            return j2 % j3;
        }
        if (compareUnsigned(j2, j3) < 0) {
            return j2;
        }
        return toUnsignedBigInteger(j2).remainder(toUnsignedBigInteger(j3)).longValue();
    }

    public static long highestOneBit(long j2) {
        long j3 = j2 | (j2 >> 1);
        long j4 = j3 | (j3 >> 2);
        long j5 = j4 | (j4 >> 4);
        long j6 = j5 | (j5 >> 8);
        long j7 = j6 | (j6 >> 16);
        long j8 = j7 | (j7 >> 32);
        return j8 - (j8 >>> 1);
    }

    public static long lowestOneBit(long j2) {
        return j2 & (-j2);
    }

    public static int numberOfLeadingZeros(long j2) {
        if (j2 == 0) {
            return 64;
        }
        int i2 = 1;
        int i3 = (int) (j2 >>> 32);
        if (i3 == 0) {
            i2 = 1 + 32;
            i3 = (int) j2;
        }
        if ((i3 >>> 16) == 0) {
            i2 += 16;
            i3 <<= 16;
        }
        if ((i3 >>> 24) == 0) {
            i2 += 8;
            i3 <<= 8;
        }
        if ((i3 >>> 28) == 0) {
            i2 += 4;
            i3 <<= 4;
        }
        if ((i3 >>> 30) == 0) {
            i2 += 2;
            i3 <<= 2;
        }
        return i2 - (i3 >>> 31);
    }

    public static int numberOfTrailingZeros(long j2) {
        int i2;
        if (j2 == 0) {
            return 64;
        }
        int i3 = 63;
        int i4 = (int) j2;
        if (i4 != 0) {
            i3 = 63 - 32;
            i2 = i4;
        } else {
            i2 = (int) (j2 >>> 32);
        }
        int i5 = i2 << 16;
        if (i5 != 0) {
            i3 -= 16;
            i2 = i5;
        }
        int i6 = i2 << 8;
        if (i6 != 0) {
            i3 -= 8;
            i2 = i6;
        }
        int i7 = i2 << 4;
        if (i7 != 0) {
            i3 -= 4;
            i2 = i7;
        }
        int i8 = i2 << 2;
        if (i8 != 0) {
            i3 -= 2;
            i2 = i8;
        }
        return i3 - ((i2 << 1) >>> 31);
    }

    public static int bitCount(long j2) {
        long j3 = j2 - ((j2 >>> 1) & 6148914691236517205L);
        long j4 = (j3 & 3689348814741910323L) + ((j3 >>> 2) & 3689348814741910323L);
        long j5 = (j4 + (j4 >>> 4)) & 1085102592571150095L;
        long j6 = j5 + (j5 >>> 8);
        long j7 = j6 + (j6 >>> 16);
        return ((int) (j7 + (j7 >>> 32))) & 127;
    }

    public static long rotateLeft(long j2, int i2) {
        return (j2 << i2) | (j2 >>> (-i2));
    }

    public static long rotateRight(long j2, int i2) {
        return (j2 >>> i2) | (j2 << (-i2));
    }

    public static long reverse(long j2) {
        long j3 = ((j2 & 6148914691236517205L) << 1) | ((j2 >>> 1) & 6148914691236517205L);
        long j4 = ((j3 & 3689348814741910323L) << 2) | ((j3 >>> 2) & 3689348814741910323L);
        long j5 = ((j4 & 1085102592571150095L) << 4) | ((j4 >>> 4) & 1085102592571150095L);
        long j6 = ((j5 & 71777214294589695L) << 8) | ((j5 >>> 8) & 71777214294589695L);
        return (j6 << 48) | ((j6 & 4294901760L) << 16) | ((j6 >>> 16) & 4294901760L) | (j6 >>> 48);
    }

    public static int signum(long j2) {
        return (int) ((j2 >> 63) | ((-j2) >>> 63));
    }

    public static long reverseBytes(long j2) {
        long j3 = ((j2 & 71777214294589695L) << 8) | ((j2 >>> 8) & 71777214294589695L);
        return (j3 << 48) | ((j3 & 4294901760L) << 16) | ((j3 >>> 16) & 4294901760L) | (j3 >>> 48);
    }

    public static long sum(long j2, long j3) {
        return j2 + j3;
    }

    public static long max(long j2, long j3) {
        return Math.max(j2, j3);
    }

    public static long min(long j2, long j3) {
        return Math.min(j2, j3);
    }
}
