package sun.util.locale;

import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:sun/util/locale/LocaleUtils.class */
public final class LocaleUtils {
    private LocaleUtils() {
    }

    public static boolean caseIgnoreMatch(String str, String str2) {
        if (str == str2) {
            return true;
        }
        int length = str.length();
        if (length != str2.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt != cCharAt2 && toLower(cCharAt) != toLower(cCharAt2)) {
                return false;
            }
        }
        return true;
    }

    static int caseIgnoreCompare(String str, String str2) {
        if (str == str2) {
            return 0;
        }
        return toLowerString(str).compareTo(toLowerString(str2));
    }

    static char toUpper(char c2) {
        return isLower(c2) ? (char) (c2 - ' ') : c2;
    }

    static char toLower(char c2) {
        return isUpper(c2) ? (char) (c2 + ' ') : c2;
    }

    public static String toLowerString(String str) {
        int length = str.length();
        int i2 = 0;
        while (i2 < length && !isUpper(str.charAt(i2))) {
            i2++;
        }
        if (i2 == length) {
            return str;
        }
        char[] cArr = new char[length];
        int i3 = 0;
        while (i3 < length) {
            char cCharAt = str.charAt(i3);
            cArr[i3] = i3 < i2 ? cCharAt : toLower(cCharAt);
            i3++;
        }
        return new String(cArr);
    }

    static String toUpperString(String str) {
        int length = str.length();
        int i2 = 0;
        while (i2 < length && !isLower(str.charAt(i2))) {
            i2++;
        }
        if (i2 == length) {
            return str;
        }
        char[] cArr = new char[length];
        int i3 = 0;
        while (i3 < length) {
            char cCharAt = str.charAt(i3);
            cArr[i3] = i3 < i2 ? cCharAt : toUpper(cCharAt);
            i3++;
        }
        return new String(cArr);
    }

    static String toTitleString(String str) {
        int length = str.length();
        if (length == 0) {
            return str;
        }
        int i2 = 0;
        if (!isLower(str.charAt(0))) {
            i2 = 1;
            while (i2 < length && !isUpper(str.charAt(i2))) {
                i2++;
            }
        }
        if (i2 == length) {
            return str;
        }
        char[] cArr = new char[length];
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = str.charAt(i3);
            if (i3 == 0 && i2 == 0) {
                cArr[i3] = toUpper(cCharAt);
            } else if (i3 < i2) {
                cArr[i3] = cCharAt;
            } else {
                cArr[i3] = toLower(cCharAt);
            }
        }
        return new String(cArr);
    }

    private static boolean isUpper(char c2) {
        return c2 >= 'A' && c2 <= 'Z';
    }

    private static boolean isLower(char c2) {
        return c2 >= 'a' && c2 <= 'z';
    }

    static boolean isAlpha(char c2) {
        return (c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z');
    }

    static boolean isAlphaString(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (!isAlpha(str.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    static boolean isNumeric(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    static boolean isNumericString(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (!isNumeric(str.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    static boolean isAlphaNumeric(char c2) {
        return isAlpha(c2) || isNumeric(c2);
    }

    public static boolean isAlphaNumericString(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (!isAlphaNumeric(str.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    static boolean isEmpty(Set<?> set) {
        return set == null || set.isEmpty();
    }

    static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
