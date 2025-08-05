package sun.net.idn;

import java.text.ParseException;
import sun.text.normalizer.UCharacter;
import sun.text.normalizer.UTF16;

/* loaded from: rt.jar:sun/net/idn/Punycode.class */
public final class Punycode {
    private static final int BASE = 36;
    private static final int TMIN = 1;
    private static final int TMAX = 26;
    private static final int SKEW = 38;
    private static final int DAMP = 700;
    private static final int INITIAL_BIAS = 72;
    private static final int INITIAL_N = 128;
    private static final int HYPHEN = 45;
    private static final int DELIMITER = 45;
    private static final int ZERO = 48;
    private static final int NINE = 57;
    private static final int SMALL_A = 97;
    private static final int SMALL_Z = 122;
    private static final int CAPITAL_A = 65;
    private static final int CAPITAL_Z = 90;
    private static final int MAX_CP_COUNT = 256;
    private static final int UINT_MAGIC = Integer.MIN_VALUE;
    private static final long ULONG_MAGIC = Long.MIN_VALUE;
    static final int[] basicToDigit = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    private static int adaptBias(int i2, int i3, boolean z2) {
        int i4;
        if (z2) {
            i4 = i2 / 700;
        } else {
            i4 = i2 / 2;
        }
        int i5 = i4 + (i4 / i3);
        int i6 = 0;
        while (i5 > 455) {
            i5 /= 35;
            i6 += 36;
        }
        return i6 + ((36 * i5) / (i5 + 38));
    }

    private static char asciiCaseMap(char c2, boolean z2) {
        if (z2) {
            if ('a' <= c2 && c2 <= 'z') {
                c2 = (char) (c2 - ' ');
            }
        } else if ('A' <= c2 && c2 <= 'Z') {
            c2 = (char) (c2 + ' ');
        }
        return c2;
    }

    private static char digitToBasic(int i2, boolean z2) {
        if (i2 < 26) {
            if (z2) {
                return (char) (65 + i2);
            }
            return (char) (97 + i2);
        }
        return (char) (22 + i2);
    }

    public static StringBuffer encode(StringBuffer stringBuffer, boolean[] zArr) throws ParseException {
        int codePoint;
        int[] iArr = new int[256];
        int length = stringBuffer.length();
        char[] cArr = new char[256];
        StringBuffer stringBuffer2 = new StringBuffer();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i4 < length) {
            if (i3 == 256) {
                throw new ParseException("Too many input code points", -1);
            }
            char cCharAt = stringBuffer.charAt(i4);
            if (isBasic(cCharAt)) {
                if (i2 < 256) {
                    int i5 = i3;
                    i3++;
                    iArr[i5] = 0;
                    cArr[i2] = zArr != null ? asciiCaseMap(cCharAt, zArr[i4]) : cCharAt;
                }
                i2++;
            } else {
                int i6 = ((zArr == null || !zArr[i4]) ? 0 : 1) << 31;
                if (!UTF16.isSurrogate(cCharAt)) {
                    codePoint = i6 | cCharAt;
                } else {
                    if (UTF16.isLeadSurrogate(cCharAt) && i4 + 1 < length) {
                        char cCharAt2 = stringBuffer.charAt(i4 + 1);
                        if (UTF16.isTrailSurrogate(cCharAt2)) {
                            i4++;
                            codePoint = i6 | UCharacter.getCodePoint(cCharAt, cCharAt2);
                        }
                    }
                    throw new ParseException("Illegal char found", -1);
                }
                int i7 = i3;
                i3++;
                iArr[i7] = codePoint;
            }
            i4++;
        }
        int i8 = i2;
        if (i8 > 0) {
            if (i2 < 256) {
                cArr[i2] = '-';
            }
            i2++;
        }
        int i9 = 128;
        int i10 = 0;
        int iAdaptBias = 72;
        int i11 = i8;
        while (i11 < i3) {
            int i12 = Integer.MAX_VALUE;
            for (int i13 = 0; i13 < i3; i13++) {
                int i14 = iArr[i13] & Integer.MAX_VALUE;
                if (i9 <= i14 && i14 < i12) {
                    i12 = i14;
                }
            }
            if (i12 - i9 > (2147483391 - i10) / (i11 + 1)) {
                throw new RuntimeException("Internal program error");
            }
            int i15 = i10 + ((i12 - i9) * (i11 + 1));
            int i16 = i12;
            for (int i17 = 0; i17 < i3; i17++) {
                int i18 = iArr[i17] & Integer.MAX_VALUE;
                if (i18 < i16) {
                    i15++;
                } else if (i18 == i16) {
                    int i19 = i15;
                    int i20 = 36;
                    while (true) {
                        int i21 = i20 - iAdaptBias;
                        if (i21 < 1) {
                            i21 = 1;
                        } else if (i20 >= iAdaptBias + 26) {
                            i21 = 26;
                        }
                        if (i19 < i21) {
                            break;
                        }
                        if (i2 < 256) {
                            int i22 = i2;
                            i2++;
                            cArr[i22] = digitToBasic(i21 + ((i19 - i21) % (36 - i21)), false);
                        }
                        i19 = (i19 - i21) / (36 - i21);
                        i20 += 36;
                    }
                    if (i2 < 256) {
                        int i23 = i2;
                        i2++;
                        cArr[i23] = digitToBasic(i19, iArr[i17] < 0);
                    }
                    iAdaptBias = adaptBias(i15, i11 + 1, i11 == i8);
                    i15 = 0;
                    i11++;
                }
            }
            i10 = i15 + 1;
            i9 = i16 + 1;
        }
        return stringBuffer2.append(cArr, 0, i2);
    }

    private static boolean isBasic(int i2) {
        return i2 < 128;
    }

    private static boolean isBasicUpperCase(int i2) {
        return 65 <= i2 && i2 <= 90;
    }

    private static boolean isSurrogate(int i2) {
        return (i2 & (-2048)) == 55296;
    }

    public static StringBuffer decode(StringBuffer stringBuffer, boolean[] zArr) throws ParseException {
        int iMoveCodePointOffset;
        int length = stringBuffer.length();
        StringBuffer stringBuffer2 = new StringBuffer();
        char[] cArr = new char[256];
        int i2 = length;
        while (i2 > 0) {
            i2--;
            if (stringBuffer.charAt(i2) == '-') {
                break;
            }
        }
        int i3 = i2;
        int i4 = i3;
        int i5 = i3;
        while (i2 > 0) {
            i2--;
            char cCharAt = stringBuffer.charAt(i2);
            if (!isBasic(cCharAt)) {
                throw new ParseException("Illegal char found", -1);
            }
            if (i2 < 256) {
                cArr[i2] = cCharAt;
                if (zArr != null) {
                    zArr[i2] = isBasicUpperCase(cCharAt);
                }
            }
        }
        int i6 = 128;
        int i7 = 0;
        int iAdaptBias = 72;
        int i8 = 1000000000;
        int i9 = i3 > 0 ? i3 + 1 : 0;
        while (i9 < length) {
            int i10 = i7;
            int i11 = 1;
            int i12 = 36;
            while (i9 < length) {
                int i13 = i9;
                i9++;
                int i14 = basicToDigit[(byte) stringBuffer.charAt(i13)];
                if (i14 < 0) {
                    throw new ParseException("Invalid char found", -1);
                }
                if (i14 > (Integer.MAX_VALUE - i7) / i11) {
                    throw new ParseException("Illegal char found", -1);
                }
                i7 += i14 * i11;
                int i15 = i12 - iAdaptBias;
                if (i15 < 1) {
                    i15 = 1;
                } else if (i12 >= iAdaptBias + 26) {
                    i15 = 26;
                }
                if (i14 >= i15) {
                    if (i11 > Integer.MAX_VALUE / (36 - i15)) {
                        throw new ParseException("Illegal char found", -1);
                    }
                    i11 *= 36 - i15;
                    i12 += 36;
                } else {
                    i4++;
                    iAdaptBias = adaptBias(i7 - i10, i4, i10 == 0);
                    if (i7 / i4 > Integer.MAX_VALUE - i6) {
                        throw new ParseException("Illegal char found", -1);
                    }
                    i6 += i7 / i4;
                    int i16 = i7 % i4;
                    if (i6 > 1114111 || isSurrogate(i6)) {
                        throw new ParseException("Illegal char found", -1);
                    }
                    int charCount = UTF16.getCharCount(i6);
                    if (i5 + charCount < 256) {
                        if (i16 <= i8) {
                            iMoveCodePointOffset = i16;
                            if (charCount > 1) {
                                i8 = iMoveCodePointOffset;
                            } else {
                                i8++;
                            }
                        } else {
                            int i17 = i8;
                            iMoveCodePointOffset = UTF16.moveCodePointOffset(cArr, 0, i5, i17, i16 - i17);
                        }
                        if (iMoveCodePointOffset < i5) {
                            System.arraycopy(cArr, iMoveCodePointOffset, cArr, iMoveCodePointOffset + charCount, i5 - iMoveCodePointOffset);
                            if (zArr != null) {
                                System.arraycopy(zArr, iMoveCodePointOffset, zArr, iMoveCodePointOffset + charCount, i5 - iMoveCodePointOffset);
                            }
                        }
                        if (charCount == 1) {
                            cArr[iMoveCodePointOffset] = (char) i6;
                        } else {
                            cArr[iMoveCodePointOffset] = UTF16.getLeadSurrogate(i6);
                            cArr[iMoveCodePointOffset + 1] = UTF16.getTrailSurrogate(i6);
                        }
                        if (zArr != null) {
                            zArr[iMoveCodePointOffset] = isBasicUpperCase(stringBuffer.charAt(i9 - 1));
                            if (charCount == 2) {
                                zArr[iMoveCodePointOffset + 1] = false;
                            }
                        }
                    }
                    i5 += charCount;
                    i7 = i16 + 1;
                }
            }
            throw new ParseException("Illegal char found", -1);
        }
        stringBuffer2.append(cArr, 0, i5);
        return stringBuffer2;
    }
}
