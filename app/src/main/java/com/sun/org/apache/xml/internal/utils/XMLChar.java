package com.sun.org.apache.xml.internal.utils;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.awt.Event;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import sun.security.krb5.internal.Krb5;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLChar.class */
public class XMLChar {
    private static final byte[] CHARS = new byte[65536];
    public static final int MASK_VALID = 1;
    public static final int MASK_SPACE = 2;
    public static final int MASK_NAME_START = 4;
    public static final int MASK_NAME = 8;
    public static final int MASK_PUBID = 16;
    public static final int MASK_CONTENT = 32;
    public static final int MASK_NCNAME_START = 64;
    public static final int MASK_NCNAME = 128;

    static {
        int[] charRange = {9, 10, 13, 13, 32, 55295, 57344, 65533};
        int[] spaceChar = {32, 9, 13, 10};
        int[] nameChar = {45, 46};
        int[] nameStartChar = {58, 95};
        int[] pubidChar = {10, 13, 32, 33, 35, 36, 37, 61, 95};
        int[] pubidRange = {39, 59, 63, 90, 97, 122};
        int[] letterRange = {65, 90, 97, 122, 192, 214, 216, 246, 248, 305, 308, 318, EncodingConstants.OCTET_STRING_LENGTH_2ND_BIT_MEDIUM_LIMIT, 328, 330, 382, 384, 451, 461, 496, 500, 501, 506, FTPReply.FAILED_SECURITY_CHECK, 592, 680, 699, 705, Krb5.ASN1_OVERFLOW, Krb5.ASN1_BAD_ID, Krb5.ASN1_BAD_CLASS, 929, 931, 974, 976, 982, 994, Event.F4, Event.INSERT, 1036, 1038, 1103, 1105, 1116, 1118, 1153, 1168, 1220, 1223, 1224, 1227, 1228, 1232, 1259, 1262, 1269, 1272, 1273, 1329, 1366, 1377, 1414, 1488, 1514, 1520, 1522, 1569, 1594, 1601, 1610, 1649, 1719, 1722, 1726, 1728, 1742, 1744, 1747, 1765, 1766, 2309, 2361, 2392, 2401, 2437, 2444, 2447, 2448, 2451, 2472, 2474, 2480, 2486, 2489, 2524, 2525, 2527, 2529, 2544, 2545, 2565, 2570, 2575, 2576, 2579, 2600, 2602, 2608, 2610, 2611, 2613, 2614, 2616, 2617, 2649, 2652, 2674, 2676, 2693, 2699, 2703, 2705, 2707, 2728, 2730, 2736, 2738, 2739, 2741, 2745, 2821, 2828, 2831, 2832, 2835, 2856, 2858, 2864, 2866, 2867, 2870, 2873, 2908, 2909, 2911, 2913, 2949, 2954, 2958, 2960, 2962, 2965, 2969, 2970, 2974, 2975, 2979, 2980, 2984, 2986, 2990, 2997, 2999, 3001, 3077, 3084, 3086, 3088, 3090, 3112, 3114, 3123, 3125, 3129, 3168, 3169, 3205, 3212, 3214, 3216, 3218, 3240, 3242, 3251, 3253, 3257, 3296, 3297, 3333, 3340, 3342, 3344, 3346, 3368, 3370, 3385, 3424, 3425, 3585, 3630, 3634, 3635, 3648, 3653, 3713, 3714, 3719, 3720, 3732, 3735, 3737, 3743, 3745, 3747, 3754, 3755, 3757, 3758, 3762, 3763, 3776, 3780, 3904, 3911, 3913, 3945, 4256, 4293, 4304, 4342, 4354, 4355, 4357, 4359, 4363, 4364, 4366, 4370, 4436, 4437, 4447, NormalizerImpl.JAMO_V_BASE, 4461, 4462, 4466, 4467, 4526, 4527, 4535, 4536, 4540, 4546, 7680, 7835, 7840, 7929, 7936, 7957, 7960, 7965, 7968, 8005, 8008, 8013, 8016, 8023, 8031, 8061, 8064, 8116, 8118, 8124, 8130, 8132, 8134, 8140, 8144, 8147, 8150, 8155, 8160, 8172, 8178, 8180, 8182, 8188, 8490, 8491, 8576, 8578, 12353, 12436, 12449, 12538, 12549, 12588, NormalizerImpl.HANGUL_BASE, 55203, 12321, 12329, 19968, 40869};
        int[] letterChar = {Krb5.ASN1_MISPLACED_FIELD, Krb5.ASN1_BAD_FORMAT, 986, 988, FTPSClient.DEFAULT_FTPS_PORT, 992, 1369, 1749, 2365, 2482, 2654, 2701, 2749, 2784, 2877, 2972, 3294, 3632, 3716, 3722, 3725, 3749, 3751, 3760, 3773, NormalizerImpl.JAMO_L_BASE, 4361, 4412, 4414, 4416, 4428, 4430, 4432, 4441, 4451, 4453, 4455, 4457, 4469, 4510, 4520, 4523, 4538, 4587, 4592, 4601, 8025, 8027, 8029, 8126, 8486, 8494, 12295};
        int[] combiningCharRange = {768, 837, 864, 865, 1155, 1158, 1425, 1441, 1443, Krb5.KDC_DEFAULT_UDP_PREF_LIMIT, 1467, 1469, 1473, 1474, 1611, 1618, 1750, 1756, 1757, 1759, 1760, 1764, 1767, 1768, 1770, 1773, 2305, 2307, 2366, 2380, 2385, 2388, 2402, 2403, 2433, 2435, 2496, 2500, 2503, 2504, 2507, 2509, 2530, 2531, 2624, 2626, 2631, 2632, 2635, 2637, 2672, 2673, 2689, 2691, 2750, 2757, 2759, 2761, 2763, 2765, 2817, 2819, 2878, 2883, 2887, 2888, 2891, 2893, 2902, 2903, 2946, 2947, 3006, 3010, 3014, 3016, 3018, 3021, 3073, 3075, 3134, 3140, 3142, 3144, 3146, 3149, 3157, 3158, 3202, 3203, 3262, 3268, 3270, 3272, 3274, 3277, 3285, 3286, 3330, 3331, 3390, 3395, 3398, 3400, 3402, 3405, 3636, 3642, 3655, 3662, 3764, 3769, 3771, 3772, 3784, 3789, 3864, 3865, 3953, 3972, 3974, 3979, 3984, 3989, 3993, 4013, 4017, 4023, 8400, 8412, 12330, 12335};
        int[] combiningCharChar = {1471, 1476, 1648, 2364, 2381, 2492, 2494, 2495, 2519, 2562, 2620, 2622, 2623, 2748, 2876, 3031, 3415, 3633, 3761, 3893, 3895, 3897, 3902, 3903, 3991, 4025, 8417, 12441, 12442};
        int[] digitRange = {48, 57, 1632, 1641, 1776, 1785, 2406, 2415, 2534, 2543, 2662, 2671, 2790, 2799, 2918, 2927, 3047, 3055, 3174, 3183, 3302, 3311, 3430, 3439, 3664, 3673, 3792, 3801, 3872, 3881};
        int[] extenderRange = {12337, 12341, 12445, 12446, 12540, 12542};
        int[] extenderChar = {183, 720, 721, Krb5.ASN1_TYPE_MISMATCH, 1600, 3654, 3782, 12293};
        int[] specialChar = {60, 38, 10, 13, 93};
        for (int i2 = 0; i2 < charRange.length; i2 += 2) {
            for (int j2 = charRange[i2]; j2 <= charRange[i2 + 1]; j2++) {
                byte[] bArr = CHARS;
                int i3 = j2;
                bArr[i3] = (byte) (bArr[i3] | 33);
            }
        }
        for (int i4 = 0; i4 < specialChar.length; i4++) {
            CHARS[specialChar[i4]] = (byte) (CHARS[specialChar[i4]] & (-33));
        }
        for (int i5 : spaceChar) {
            byte[] bArr2 = CHARS;
            bArr2[i5] = (byte) (bArr2[i5] | 2);
        }
        for (int i6 : nameStartChar) {
            byte[] bArr3 = CHARS;
            bArr3[i6] = (byte) (bArr3[i6] | 204);
        }
        for (int i7 = 0; i7 < letterRange.length; i7 += 2) {
            for (int j3 = letterRange[i7]; j3 <= letterRange[i7 + 1]; j3++) {
                byte[] bArr4 = CHARS;
                int i8 = j3;
                bArr4[i8] = (byte) (bArr4[i8] | 204);
            }
        }
        for (int i9 : letterChar) {
            byte[] bArr5 = CHARS;
            bArr5[i9] = (byte) (bArr5[i9] | 204);
        }
        for (int i10 : nameChar) {
            byte[] bArr6 = CHARS;
            bArr6[i10] = (byte) (bArr6[i10] | 136);
        }
        for (int i11 = 0; i11 < digitRange.length; i11 += 2) {
            for (int j4 = digitRange[i11]; j4 <= digitRange[i11 + 1]; j4++) {
                byte[] bArr7 = CHARS;
                int i12 = j4;
                bArr7[i12] = (byte) (bArr7[i12] | 136);
            }
        }
        for (int i13 = 0; i13 < combiningCharRange.length; i13 += 2) {
            for (int j5 = combiningCharRange[i13]; j5 <= combiningCharRange[i13 + 1]; j5++) {
                byte[] bArr8 = CHARS;
                int i14 = j5;
                bArr8[i14] = (byte) (bArr8[i14] | 136);
            }
        }
        for (int i15 : combiningCharChar) {
            byte[] bArr9 = CHARS;
            bArr9[i15] = (byte) (bArr9[i15] | 136);
        }
        for (int i16 = 0; i16 < extenderRange.length; i16 += 2) {
            for (int j6 = extenderRange[i16]; j6 <= extenderRange[i16 + 1]; j6++) {
                byte[] bArr10 = CHARS;
                int i17 = j6;
                bArr10[i17] = (byte) (bArr10[i17] | 136);
            }
        }
        for (int i18 : extenderChar) {
            byte[] bArr11 = CHARS;
            bArr11[i18] = (byte) (bArr11[i18] | 136);
        }
        byte[] bArr12 = CHARS;
        bArr12[58] = (byte) (bArr12[58] & (-193));
        for (int i19 : pubidChar) {
            byte[] bArr13 = CHARS;
            bArr13[i19] = (byte) (bArr13[i19] | 16);
        }
        for (int i20 = 0; i20 < pubidRange.length; i20 += 2) {
            for (int j7 = pubidRange[i20]; j7 <= pubidRange[i20 + 1]; j7++) {
                byte[] bArr14 = CHARS;
                int i21 = j7;
                bArr14[i21] = (byte) (bArr14[i21] | 16);
            }
        }
    }

    public static boolean isSupplemental(int c2) {
        return c2 >= 65536 && c2 <= 1114111;
    }

    public static int supplemental(char h2, char l2) {
        return ((h2 - 55296) * 1024) + (l2 - 56320) + 65536;
    }

    public static char highSurrogate(int c2) {
        return (char) (((c2 - 65536) >> 10) + 55296);
    }

    public static char lowSurrogate(int c2) {
        return (char) (((c2 - 65536) & 1023) + 56320);
    }

    public static boolean isHighSurrogate(int c2) {
        return 55296 <= c2 && c2 <= 56319;
    }

    public static boolean isLowSurrogate(int c2) {
        return 56320 <= c2 && c2 <= 57343;
    }

    public static boolean isValid(int c2) {
        return (c2 < 65536 && (CHARS[c2] & 1) != 0) || (65536 <= c2 && c2 <= 1114111);
    }

    public static boolean isInvalid(int c2) {
        return !isValid(c2);
    }

    public static boolean isContent(int c2) {
        return (c2 < 65536 && (CHARS[c2] & 32) != 0) || (65536 <= c2 && c2 <= 1114111);
    }

    public static boolean isMarkup(int c2) {
        return c2 == 60 || c2 == 38 || c2 == 37;
    }

    public static boolean isSpace(int c2) {
        return c2 < 65536 && (CHARS[c2] & 2) != 0;
    }

    public static boolean isNameStart(int c2) {
        return c2 < 65536 && (CHARS[c2] & 4) != 0;
    }

    public static boolean isName(int c2) {
        return c2 < 65536 && (CHARS[c2] & 8) != 0;
    }

    public static boolean isNCNameStart(int c2) {
        return c2 < 65536 && (CHARS[c2] & 64) != 0;
    }

    public static boolean isNCName(int c2) {
        return c2 < 65536 && (CHARS[c2] & 128) != 0;
    }

    public static boolean isPubid(int c2) {
        return c2 < 65536 && (CHARS[c2] & 16) != 0;
    }

    public static boolean isValidName(String name) {
        if (name.length() == 0) {
            return false;
        }
        char ch = name.charAt(0);
        if (!isNameStart(ch)) {
            return false;
        }
        for (int i2 = 1; i2 < name.length(); i2++) {
            char ch2 = name.charAt(i2);
            if (!isName(ch2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidNCName(String ncName) {
        if (ncName.length() == 0) {
            return false;
        }
        char ch = ncName.charAt(0);
        if (!isNCNameStart(ch)) {
            return false;
        }
        for (int i2 = 1; i2 < ncName.length(); i2++) {
            char ch2 = ncName.charAt(i2);
            if (!isNCName(ch2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidNmtoken(String nmtoken) {
        if (nmtoken.length() == 0) {
            return false;
        }
        for (int i2 = 0; i2 < nmtoken.length(); i2++) {
            char ch = nmtoken.charAt(i2);
            if (!isName(ch)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidIANAEncoding(String ianaEncoding) {
        int length;
        if (ianaEncoding != null && (length = ianaEncoding.length()) > 0) {
            char c2 = ianaEncoding.charAt(0);
            if ((c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z')) {
                for (int i2 = 1; i2 < length; i2++) {
                    char c3 = ianaEncoding.charAt(i2);
                    if ((c3 < 'A' || c3 > 'Z') && ((c3 < 'a' || c3 > 'z') && ((c3 < '0' || c3 > '9') && c3 != '.' && c3 != '_' && c3 != '-'))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isValidJavaEncoding(String javaEncoding) {
        int length;
        if (javaEncoding != null && (length = javaEncoding.length()) > 0) {
            for (int i2 = 1; i2 < length; i2++) {
                char c2 = javaEncoding.charAt(i2);
                if ((c2 < 'A' || c2 > 'Z') && ((c2 < 'a' || c2 > 'z') && ((c2 < '0' || c2 > '9') && c2 != '.' && c2 != '_' && c2 != '-'))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isValidQName(String str) {
        int colon = str.indexOf(58);
        if (colon == 0 || colon == str.length() - 1) {
            return false;
        }
        if (colon > 0) {
            String prefix = str.substring(0, colon);
            String localPart = str.substring(colon + 1);
            return isValidNCName(prefix) && isValidNCName(localPart);
        }
        return isValidNCName(str);
    }
}
