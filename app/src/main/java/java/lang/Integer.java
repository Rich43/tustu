package java.lang;

import java.time.Year;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;
import sun.misc.VM;
import sun.text.normalizer.NormalizerImpl;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/lang/Integer.class */
public final class Integer extends Number implements Comparable<Integer> {
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final Class<Integer> TYPE = Class.getPrimitiveClass("int");
    static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static final char[] DigitTens = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
    static final char[] DigitOnes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, Year.MAX_VALUE, Integer.MAX_VALUE};
    private final int value;
    public static final int SIZE = 32;
    public static final int BYTES = 4;
    private static final long serialVersionUID = 1360826667806852920L;

    public static String toString(int i2, int i3) {
        if (i3 < 2 || i3 > 36) {
            i3 = 10;
        }
        if (i3 == 10) {
            return toString(i2);
        }
        char[] cArr = new char[33];
        boolean z2 = i2 < 0;
        int i4 = 32;
        if (!z2) {
            i2 = -i2;
        }
        while (i2 <= (-i3)) {
            int i5 = i4;
            i4--;
            cArr[i5] = digits[-(i2 % i3)];
            i2 /= i3;
        }
        cArr[i4] = digits[-i2];
        if (z2) {
            i4--;
            cArr[i4] = '-';
        }
        return new String(cArr, i4, 33 - i4);
    }

    public static String toUnsignedString(int i2, int i3) {
        return Long.toUnsignedString(toUnsignedLong(i2), i3);
    }

    public static String toHexString(int i2) {
        return toUnsignedString0(i2, 4);
    }

    public static String toOctalString(int i2) {
        return toUnsignedString0(i2, 3);
    }

    public static String toBinaryString(int i2) {
        return toUnsignedString0(i2, 1);
    }

    private static String toUnsignedString0(int i2, int i3) {
        int iMax = Math.max(((32 - numberOfLeadingZeros(i2)) + (i3 - 1)) / i3, 1);
        char[] cArr = new char[iMax];
        formatUnsignedInt(i2, i3, cArr, 0, iMax);
        return new String(cArr, true);
    }

    static int formatUnsignedInt(int i2, int i3, char[] cArr, int i4, int i5) {
        int i6 = i5;
        int i7 = (1 << i3) - 1;
        do {
            i6--;
            cArr[i4 + i6] = digits[i2 & i7];
            i2 >>>= i3;
            if (i2 == 0) {
                break;
            }
        } while (i6 > 0);
        return i6;
    }

    public static String toString(int i2) {
        if (i2 == Integer.MIN_VALUE) {
            return "-2147483648";
        }
        int iStringSize = i2 < 0 ? stringSize(-i2) + 1 : stringSize(i2);
        char[] cArr = new char[iStringSize];
        getChars(i2, iStringSize, cArr);
        return new String(cArr, true);
    }

    public static String toUnsignedString(int i2) {
        return Long.toString(toUnsignedLong(i2));
    }

    static void getChars(int i2, int i3, char[] cArr) {
        int i4 = i3;
        char c2 = 0;
        if (i2 < 0) {
            c2 = '-';
            i2 = -i2;
        }
        while (i2 >= 65536) {
            int i5 = i2 / 100;
            int i6 = i2 - (((i5 << 6) + (i5 << 5)) + (i5 << 2));
            i2 = i5;
            int i7 = i4 - 1;
            cArr[i7] = DigitOnes[i6];
            i4 = i7 - 1;
            cArr[i4] = DigitTens[i6];
        }
        do {
            int i8 = (i2 * 52429) >>> 19;
            i4--;
            cArr[i4] = digits[i2 - ((i8 << 3) + (i8 << 1))];
            i2 = i8;
        } while (i2 != 0);
        if (c2 != 0) {
            cArr[i4 - 1] = c2;
        }
    }

    static int stringSize(int i2) {
        int i3 = 0;
        while (i2 > sizeTable[i3]) {
            i3++;
        }
        return i3 + 1;
    }

    public static int parseInt(String str, int i2) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        if (i2 < 2) {
            throw new NumberFormatException("radix " + i2 + " less than Character.MIN_RADIX");
        }
        if (i2 > 36) {
            throw new NumberFormatException("radix " + i2 + " greater than Character.MAX_RADIX");
        }
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        int length = str.length();
        int i5 = -2147483647;
        if (length > 0) {
            char cCharAt = str.charAt(0);
            if (cCharAt < '0') {
                if (cCharAt == '-') {
                    z2 = true;
                    i5 = Integer.MIN_VALUE;
                } else if (cCharAt != '+') {
                    throw NumberFormatException.forInputString(str);
                }
                if (length == 1) {
                    throw NumberFormatException.forInputString(str);
                }
                i4 = 0 + 1;
            }
            int i6 = i5 / i2;
            while (i4 < length) {
                int i7 = i4;
                i4++;
                int iDigit = Character.digit(str.charAt(i7), i2);
                if (iDigit < 0) {
                    throw NumberFormatException.forInputString(str);
                }
                if (i3 < i6) {
                    throw NumberFormatException.forInputString(str);
                }
                int i8 = i3 * i2;
                if (i8 < i5 + iDigit) {
                    throw NumberFormatException.forInputString(str);
                }
                i3 = i8 - iDigit;
            }
            return z2 ? i3 : -i3;
        }
        throw NumberFormatException.forInputString(str);
    }

    public static int parseInt(String str) throws NumberFormatException {
        return parseInt(str, 10);
    }

    public static int parseUnsignedInt(String str, int i2) throws NumberFormatException {
        if (str == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        int length = str.length();
        if (length > 0) {
            if (str.charAt(0) == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", str));
            }
            if (length <= 5 || (i2 == 10 && length <= 9)) {
                return parseInt(str, i2);
            }
            long j2 = Long.parseLong(str, i2);
            if ((j2 & (-4294967296L)) == 0) {
                return (int) j2;
            }
            throw new NumberFormatException(String.format("String value %s exceeds range of unsigned int.", str));
        }
        throw NumberFormatException.forInputString(str);
    }

    public static int parseUnsignedInt(String str) throws NumberFormatException {
        return parseUnsignedInt(str, 10);
    }

    public static Integer valueOf(String str, int i2) throws NumberFormatException {
        return valueOf(parseInt(str, i2));
    }

    public static Integer valueOf(String str) throws NumberFormatException {
        return valueOf(parseInt(str, 10));
    }

    /* loaded from: rt.jar:java/lang/Integer$IntegerCache.class */
    private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer[] cache;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Integer.class.desiredAssertionStatus();
            int iMin = 127;
            String savedProperty = VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (savedProperty != null) {
                try {
                    iMin = Math.min(Math.max(Integer.parseInt(savedProperty), 127), 2147483518);
                } catch (NumberFormatException e2) {
                }
            }
            high = iMin;
            cache = new Integer[(high - (-128)) + 1];
            int i2 = -128;
            for (int i3 = 0; i3 < cache.length; i3++) {
                int i4 = i2;
                i2++;
                cache[i3] = new Integer(i4);
            }
            if (!$assertionsDisabled && high < 127) {
                throw new AssertionError();
            }
        }

        private IntegerCache() {
        }
    }

    public static Integer valueOf(int i2) {
        if (i2 >= -128 && i2 <= IntegerCache.high) {
            return IntegerCache.cache[i2 + 128];
        }
        return new Integer(i2);
    }

    public Integer(int i2) {
        this.value = i2;
    }

    public Integer(String str) throws NumberFormatException {
        this.value = parseInt(str, 10);
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
        return this.value;
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

    public static int hashCode(int i2) {
        return i2;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Integer) && this.value == ((Integer) obj).intValue();
    }

    public static Integer getInteger(String str) {
        return getInteger(str, (Integer) null);
    }

    public static Integer getInteger(String str, int i2) {
        Integer integer = getInteger(str, (Integer) null);
        return integer == null ? valueOf(i2) : integer;
    }

    public static Integer getInteger(String str, Integer num) {
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
        return num;
    }

    public static Integer decode(String str) throws NumberFormatException {
        Integer numValueOf;
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
            Integer numValueOf2 = valueOf(str.substring(i3), i2);
            numValueOf = z2 ? valueOf(-numValueOf2.intValue()) : numValueOf2;
        } catch (NumberFormatException e2) {
            numValueOf = valueOf(z2 ? LanguageTag.SEP + str.substring(i3) : str.substring(i3), i2);
        }
        return numValueOf;
    }

    @Override // java.lang.Comparable
    public int compareTo(Integer num) {
        return compare(this.value, num.value);
    }

    public static int compare(int i2, int i3) {
        if (i2 < i3) {
            return -1;
        }
        return i2 == i3 ? 0 : 1;
    }

    public static int compareUnsigned(int i2, int i3) {
        return compare(i2 - 2147483648, i3 - 2147483648);
    }

    public static long toUnsignedLong(int i2) {
        return i2 & 4294967295L;
    }

    public static int divideUnsigned(int i2, int i3) {
        return (int) (toUnsignedLong(i2) / toUnsignedLong(i3));
    }

    public static int remainderUnsigned(int i2, int i3) {
        return (int) (toUnsignedLong(i2) % toUnsignedLong(i3));
    }

    public static int highestOneBit(int i2) {
        int i3 = i2 | (i2 >> 1);
        int i4 = i3 | (i3 >> 2);
        int i5 = i4 | (i4 >> 4);
        int i6 = i5 | (i5 >> 8);
        int i7 = i6 | (i6 >> 16);
        return i7 - (i7 >>> 1);
    }

    public static int lowestOneBit(int i2) {
        return i2 & (-i2);
    }

    public static int numberOfLeadingZeros(int i2) {
        if (i2 == 0) {
            return 32;
        }
        int i3 = 1;
        if ((i2 >>> 16) == 0) {
            i3 = 1 + 16;
            i2 <<= 16;
        }
        if ((i2 >>> 24) == 0) {
            i3 += 8;
            i2 <<= 8;
        }
        if ((i2 >>> 28) == 0) {
            i3 += 4;
            i2 <<= 4;
        }
        if ((i2 >>> 30) == 0) {
            i3 += 2;
            i2 <<= 2;
        }
        return i3 - (i2 >>> 31);
    }

    public static int numberOfTrailingZeros(int i2) {
        if (i2 == 0) {
            return 32;
        }
        int i3 = 31;
        int i4 = i2 << 16;
        if (i4 != 0) {
            i3 = 31 - 16;
            i2 = i4;
        }
        int i5 = i2 << 8;
        if (i5 != 0) {
            i3 -= 8;
            i2 = i5;
        }
        int i6 = i2 << 4;
        if (i6 != 0) {
            i3 -= 4;
            i2 = i6;
        }
        int i7 = i2 << 2;
        if (i7 != 0) {
            i3 -= 2;
            i2 = i7;
        }
        return i3 - ((i2 << 1) >>> 31);
    }

    public static int bitCount(int i2) {
        int i3 = i2 - ((i2 >>> 1) & 1431655765);
        int i4 = (i3 & 858993459) + ((i3 >>> 2) & 858993459);
        int i5 = (i4 + (i4 >>> 4)) & 252645135;
        int i6 = i5 + (i5 >>> 8);
        return (i6 + (i6 >>> 16)) & 63;
    }

    public static int rotateLeft(int i2, int i3) {
        return (i2 << i3) | (i2 >>> (-i3));
    }

    public static int rotateRight(int i2, int i3) {
        return (i2 >>> i3) | (i2 << (-i3));
    }

    public static int reverse(int i2) {
        int i3 = ((i2 & 1431655765) << 1) | ((i2 >>> 1) & 1431655765);
        int i4 = ((i3 & 858993459) << 2) | ((i3 >>> 2) & 858993459);
        int i5 = ((i4 & 252645135) << 4) | ((i4 >>> 4) & 252645135);
        return (i5 << 24) | ((i5 & NormalizerImpl.CC_MASK) << 8) | ((i5 >>> 8) & NormalizerImpl.CC_MASK) | (i5 >>> 24);
    }

    public static int signum(int i2) {
        return (i2 >> 31) | ((-i2) >>> 31);
    }

    public static int reverseBytes(int i2) {
        return (i2 >>> 24) | ((i2 >> 8) & NormalizerImpl.CC_MASK) | ((i2 << 8) & 16711680) | (i2 << 24);
    }

    public static int sum(int i2, int i3) {
        return i2 + i3;
    }

    public static int max(int i2, int i3) {
        return Math.max(i2, i3);
    }

    public static int min(int i2, int i3) {
        return Math.min(i2, i3);
    }
}
