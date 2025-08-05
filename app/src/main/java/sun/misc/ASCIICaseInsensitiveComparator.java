package sun.misc;

import java.util.Comparator;

/* loaded from: rt.jar:sun/misc/ASCIICaseInsensitiveComparator.class */
public class ASCIICaseInsensitiveComparator implements Comparator<String> {
    public static final Comparator<String> CASE_INSENSITIVE_ORDER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ASCIICaseInsensitiveComparator.class.desiredAssertionStatus();
        CASE_INSENSITIVE_ORDER = new ASCIICaseInsensitiveComparator();
    }

    @Override // java.util.Comparator
    public int compare(String str, String str2) {
        char lower;
        char lower2;
        int length = str.length();
        int length2 = str2.length();
        int i2 = length < length2 ? length : length2;
        for (int i3 = 0; i3 < i2; i3++) {
            char cCharAt = str.charAt(i3);
            char cCharAt2 = str2.charAt(i3);
            if (!$assertionsDisabled && (cCharAt > 127 || cCharAt2 > 127)) {
                throw new AssertionError();
            }
            if (cCharAt != cCharAt2 && (lower = (char) toLower(cCharAt)) != (lower2 = (char) toLower(cCharAt2))) {
                return lower - lower2;
            }
        }
        return length - length2;
    }

    public static int lowerCaseHashCode(String str) {
        int lower = 0;
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            lower = (31 * lower) + toLower(str.charAt(i2));
        }
        return lower;
    }

    static boolean isLower(int i2) {
        return ((i2 - 97) | (122 - i2)) >= 0;
    }

    static boolean isUpper(int i2) {
        return ((i2 - 65) | (90 - i2)) >= 0;
    }

    static int toLower(int i2) {
        return isUpper(i2) ? i2 + 32 : i2;
    }

    static int toUpper(int i2) {
        return isLower(i2) ? i2 - 32 : i2;
    }
}
