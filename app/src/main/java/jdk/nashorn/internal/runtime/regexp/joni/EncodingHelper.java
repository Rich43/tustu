package jdk.nashorn.internal.runtime.regexp.joni;

import java.util.Arrays;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/EncodingHelper.class */
public final class EncodingHelper {
    static final int NEW_LINE = 10;
    static final int RETURN = 13;
    static final int LINE_SEPARATOR = 8232;
    static final int PARAGRAPH_SEPARATOR = 8233;
    static final char[] EMPTYCHARS = new char[0];
    static final int[][] codeRanges = new int[15];

    public static int digitVal(int code) {
        return code - 48;
    }

    public static int odigitVal(int code) {
        return digitVal(code);
    }

    public static boolean isXDigit(int code) {
        return Character.isDigit(code) || (code >= 97 && code <= 102) || (code >= 65 && code <= 70);
    }

    public static int xdigitVal(int code) {
        if (Character.isDigit(code)) {
            return code - 48;
        }
        if (code >= 97 && code <= 102) {
            return (code - 97) + 10;
        }
        return (code - 65) + 10;
    }

    public static boolean isDigit(int code) {
        return code >= 48 && code <= 57;
    }

    public static boolean isWord(int code) {
        return ((1 << Character.getType(code)) & CharacterType.WORD_MASK) != 0;
    }

    public static boolean isNewLine(int code) {
        return code == 10 || code == 13 || code == LINE_SEPARATOR || code == PARAGRAPH_SEPARATOR;
    }

    public static boolean isNewLine(char[] chars, int p2, int end) {
        return p2 < end && isNewLine(chars[p2]);
    }

    public static int prevCharHead(int p2, int s2) {
        if (s2 <= p2) {
            return -1;
        }
        return s2 - 1;
    }

    public static int rightAdjustCharHeadWithPrev(int s2, IntHolder prev) {
        if (prev != null) {
            prev.value = -1;
        }
        return s2;
    }

    public static int stepBack(int p2, int sp, int np) {
        int s2 = sp;
        int n2 = np;
        while (s2 != -1) {
            int i2 = n2;
            n2--;
            if (i2 <= 0) {
                break;
            }
            if (s2 <= p2) {
                return -1;
            }
            s2--;
        }
        return s2;
    }

    public static int mbcodeStartPosition() {
        return 128;
    }

    public static char[] caseFoldCodesByString(int flag, char c2) {
        char c3;
        char[] codes = EMPTYCHARS;
        char upper = toUpperCase(c2);
        if (upper != toLowerCase(upper)) {
            int count = 0;
            char ch = 0;
            do {
                char u2 = toUpperCase(ch);
                if (u2 == upper && ch != c2) {
                    codes = count == 0 ? new char[1] : Arrays.copyOf(codes, count + 1);
                    int i2 = count;
                    count++;
                    codes[i2] = ch;
                }
                c3 = ch;
                ch = (char) (ch + 1);
            } while (c3 < 65535);
        }
        return codes;
    }

    public static void applyAllCaseFold(int flag, ApplyCaseFold fun, Object arg) {
        int upper;
        int upper2;
        for (int c2 = 0; c2 < 65535; c2++) {
            if (Character.isLowerCase(c2) && (upper2 = toUpperCase(c2)) != c2) {
                ApplyCaseFold.apply(c2, upper2, arg);
            }
        }
        for (int c3 = 0; c3 < 65535; c3++) {
            if (Character.isLowerCase(c3) && (upper = toUpperCase(c3)) != c3) {
                ApplyCaseFold.apply(upper, c3, arg);
            }
        }
    }

    public static char toLowerCase(char c2) {
        return (char) toLowerCase((int) c2);
    }

    public static int toLowerCase(int c2) {
        if (c2 < 128) {
            return (65 > c2 || c2 > 90) ? c2 : c2 + 32;
        }
        int lower = Character.toLowerCase(c2);
        return lower < 128 ? c2 : lower;
    }

    public static char toUpperCase(char c2) {
        return (char) toUpperCase((int) c2);
    }

    public static int toUpperCase(int c2) {
        if (c2 < 128) {
            return (97 > c2 || c2 > 122) ? c2 : c2 - 32;
        }
        int upper = Character.toUpperCase(c2);
        return upper < 128 ? c2 : upper;
    }

    public static int[] ctypeCodeRange(int ctype, IntHolder sbOut) {
        sbOut.value = 256;
        int[] range = null;
        if (ctype < codeRanges.length) {
            range = codeRanges[ctype];
            if (range == null) {
                range = new int[16];
                int rangeCount = 0;
                int lastCode = -2;
                for (int code = 0; code <= 65535; code++) {
                    if (isCodeCType(code, ctype)) {
                        if (lastCode < code - 1) {
                            if ((rangeCount * 2) + 2 >= range.length) {
                                range = Arrays.copyOf(range, range.length * 2);
                            }
                            range[(rangeCount * 2) + 1] = code;
                            rangeCount++;
                        }
                        int i2 = code;
                        lastCode = i2;
                        range[rangeCount * 2] = i2;
                    }
                }
                if ((rangeCount * 2) + 1 < range.length) {
                    range = Arrays.copyOf(range, (rangeCount * 2) + 1);
                }
                range[0] = rangeCount;
                codeRanges[ctype] = range;
            }
        }
        return range;
    }

    public static boolean isInCodeRange(int[] p2, int offset, int code) {
        int low = 0;
        int n2 = p2[offset];
        int high = n2;
        while (low < high) {
            int x2 = (low + high) >> 1;
            if (code > p2[(x2 << 1) + 2 + offset]) {
                low = x2 + 1;
            } else {
                high = x2;
            }
        }
        return low < n2 && code >= p2[((low << 1) + 1) + offset];
    }

    public static boolean isCodeCType(int code, int ctype) {
        switch (ctype) {
            case 0:
                return isNewLine(code);
            case 1:
                return ((1 << Character.getType(code)) & CharacterType.ALPHA_MASK) != 0;
            case 2:
                return code == 9 || Character.getType(code) == 12;
            case 3:
                int type = Character.getType(code);
                return ((1 << type) & CharacterType.CNTRL_MASK) != 0 || type == 0;
            case 4:
                return isDigit(code);
            case 5:
                switch (code) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        return false;
                    default:
                        int type2 = Character.getType(code);
                        return ((1 << type2) & CharacterType.GRAPH_MASK) == 0 && type2 != 0;
                }
            case 6:
                return Character.isLowerCase(code);
            case 7:
                int type3 = Character.getType(code);
                return ((1 << type3) & CharacterType.PRINT_MASK) == 0 && type3 != 0;
            case 8:
                return ((1 << Character.getType(code)) & CharacterType.PUNCT_MASK) != 0;
            case 9:
                switch (code) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        return true;
                    default:
                        return ((1 << Character.getType(code)) & CharacterType.SPACE_MASK) != 0 || code == 65279;
                }
            case 10:
                return Character.isUpperCase(code);
            case 11:
                return isXDigit(code);
            case 12:
                return ((1 << Character.getType(code)) & CharacterType.WORD_MASK) != 0;
            case 13:
                return ((1 << Character.getType(code)) & 1022) != 0;
            case 14:
                return code < 128;
            default:
                throw new RuntimeException("illegal character type: " + ctype);
        }
    }
}
