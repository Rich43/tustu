package sun.text.normalizer;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/text/normalizer/Utility.class */
public final class Utility {
    private static final char[] UNESCAPE_MAP = {'a', 7, 'b', '\b', 'e', 27, 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', 11};
    static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final boolean arrayRegionMatches(char[] cArr, int i2, char[] cArr2, int i3, int i4) {
        int i5 = i2 + i4;
        int i6 = i3 - i2;
        for (int i7 = i2; i7 < i5; i7++) {
            if (cArr[i7] != cArr2[i7 + i6]) {
                return false;
            }
        }
        return true;
    }

    public static final String escape(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int charCount = 0;
        while (charCount < str.length()) {
            int iCharAt = UTF16.charAt(str, charCount);
            charCount += UTF16.getCharCount(iCharAt);
            if (iCharAt >= 32 && iCharAt <= 127) {
                if (iCharAt == 92) {
                    stringBuffer.append("\\\\");
                } else {
                    stringBuffer.append((char) iCharAt);
                }
            } else {
                boolean z2 = iCharAt <= 65535;
                stringBuffer.append(z2 ? "\\u" : "\\U");
                hex(iCharAt, z2 ? 4 : 8, stringBuffer);
            }
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0106 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0108  */
    /* JADX WARN: Type inference failed for: r0v15, types: [int] */
    /* JADX WARN: Type inference failed for: r0v81, types: [int] */
    /* JADX WARN: Type inference failed for: r0v89, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int unescapeAt(java.lang.String r5, int[] r6) {
        /*
            Method dump skipped, instructions count: 480
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.text.normalizer.Utility.unescapeAt(java.lang.String, int[]):int");
    }

    public static StringBuffer hex(int i2, int i3, StringBuffer stringBuffer) {
        return appendNumber(stringBuffer, i2, 16, i3);
    }

    public static String hex(int i2, int i3) {
        return appendNumber(new StringBuffer(), i2, 16, i3).toString();
    }

    public static int skipWhitespace(String str, int i2) {
        while (i2 < str.length()) {
            int iCharAt = UTF16.charAt(str, i2);
            if (!UCharacterProperty.isRuleWhiteSpace(iCharAt)) {
                break;
            }
            i2 += UTF16.getCharCount(iCharAt);
        }
        return i2;
    }

    private static void recursiveAppendNumber(StringBuffer stringBuffer, int i2, int i3, int i4) {
        int i5 = i2 % i3;
        if (i2 >= i3 || i4 > 1) {
            recursiveAppendNumber(stringBuffer, i2 / i3, i3, i4 - 1);
        }
        stringBuffer.append(DIGITS[i5]);
    }

    public static StringBuffer appendNumber(StringBuffer stringBuffer, int i2, int i3, int i4) throws IllegalArgumentException {
        if (i3 < 2 || i3 > 36) {
            throw new IllegalArgumentException("Illegal radix " + i3);
        }
        int i5 = i2;
        if (i2 < 0) {
            i5 = -i2;
            stringBuffer.append(LanguageTag.SEP);
        }
        recursiveAppendNumber(stringBuffer, i5, i3, i4);
        return stringBuffer;
    }

    public static boolean isUnprintable(int i2) {
        return i2 < 32 || i2 > 126;
    }

    public static boolean escapeUnprintable(StringBuffer stringBuffer, int i2) {
        if (isUnprintable(i2)) {
            stringBuffer.append('\\');
            if ((i2 & DTMManager.IDENT_DTM_DEFAULT) != 0) {
                stringBuffer.append('U');
                stringBuffer.append(DIGITS[15 & (i2 >> 28)]);
                stringBuffer.append(DIGITS[15 & (i2 >> 24)]);
                stringBuffer.append(DIGITS[15 & (i2 >> 20)]);
                stringBuffer.append(DIGITS[15 & (i2 >> 16)]);
            } else {
                stringBuffer.append('u');
            }
            stringBuffer.append(DIGITS[15 & (i2 >> 12)]);
            stringBuffer.append(DIGITS[15 & (i2 >> 8)]);
            stringBuffer.append(DIGITS[15 & (i2 >> 4)]);
            stringBuffer.append(DIGITS[15 & i2]);
            return true;
        }
        return false;
    }

    public static void getChars(StringBuffer stringBuffer, int i2, int i3, char[] cArr, int i4) {
        if (i2 == i3) {
            return;
        }
        stringBuffer.getChars(i2, i3, cArr, i4);
    }
}
