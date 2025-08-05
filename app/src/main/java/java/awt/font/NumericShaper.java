package java.awt.font;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import java.awt.Event;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import sun.security.krb5.internal.Krb5;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/font/NumericShaper.class */
public final class NumericShaper implements Serializable {
    private int key;
    private int mask;
    private Range shapingRange;
    private transient Set<Range> rangeSet;
    private transient Range[] rangeArray;
    private static final int BSEARCH_THRESHOLD = 3;
    private static final long serialVersionUID = -8022764705923730308L;
    public static final int EUROPEAN = 1;
    public static final int ARABIC = 2;
    public static final int EASTERN_ARABIC = 4;
    public static final int DEVANAGARI = 8;
    public static final int BENGALI = 16;
    public static final int GURMUKHI = 32;
    public static final int GUJARATI = 64;
    public static final int ORIYA = 128;
    public static final int TAMIL = 256;
    public static final int TELUGU = 512;
    public static final int KANNADA = 1024;
    public static final int MALAYALAM = 2048;
    public static final int THAI = 4096;
    public static final int LAO = 8192;
    public static final int TIBETAN = 16384;
    public static final int MYANMAR = 32768;
    public static final int ETHIOPIC = 65536;
    public static final int KHMER = 131072;
    public static final int MONGOLIAN = 262144;
    public static final int ALL_RANGES = 524287;
    private static final int EUROPEAN_KEY = 0;
    private static final int ARABIC_KEY = 1;
    private static final int EASTERN_ARABIC_KEY = 2;
    private static final int DEVANAGARI_KEY = 3;
    private static final int BENGALI_KEY = 4;
    private static final int GURMUKHI_KEY = 5;
    private static final int GUJARATI_KEY = 6;
    private static final int ORIYA_KEY = 7;
    private static final int TAMIL_KEY = 8;
    private static final int TELUGU_KEY = 9;
    private static final int KANNADA_KEY = 10;
    private static final int MALAYALAM_KEY = 11;
    private static final int THAI_KEY = 12;
    private static final int LAO_KEY = 13;
    private static final int TIBETAN_KEY = 14;
    private static final int MYANMAR_KEY = 15;
    private static final int ETHIOPIC_KEY = 16;
    private static final int KHMER_KEY = 17;
    private static final int MONGOLIAN_KEY = 18;
    private static final int NUM_KEYS = 19;
    private static final int CONTEXTUAL_MASK = Integer.MIN_VALUE;
    private volatile transient Range currentRange = Range.EUROPEAN;
    private volatile transient int stCache = 0;
    private static final char[] bases = {0, 1584, 1728, 2358, 2486, 2614, 2742, 2870, 2998, 3126, 3254, 3382, 3616, 3744, 3824, 4112, 4920, 6064, 6112};
    private static final char[] contexts = {0, 768, 1536, 1920, 1536, 1920, 2304, 2432, 2432, 2560, 2560, 2688, 2688, 2816, 2816, 2944, 2944, 3072, 3072, 3200, 3200, 3328, 3328, 3456, 3584, 3712, 3712, 3840, 3840, 4096, 4096, 4224, 4608, 4992, 6016, 6144, 6144, 6400, 65535};
    private static int ctCache = 0;
    private static int ctCacheLimit = contexts.length - 2;
    private static int[] strongTable = {0, 65, 91, 97, 123, 170, 171, 181, 182, 186, 187, 192, 215, 216, 247, 248, 697, 699, 706, 720, 722, 736, 741, 750, 751, 880, 884, 886, 894, Krb5.ASN1_MISPLACED_FIELD, Krb5.ASN1_TYPE_MISMATCH, Krb5.ASN1_OVERFLOW, Event.F7, Event.F8, 1155, 1162, 1418, 1470, 1471, 1472, 1473, 1475, 1476, 1478, 1479, 1488, 1536, 1544, 1545, 1547, 1548, 1549, 1550, 1563, 1611, 1645, 1648, 1649, 1750, 1765, 1767, 1774, 1776, 1786, 1809, 1810, 1840, 1869, 1958, 1969, 2027, 2036, 2038, 2042, 2070, 2074, 2075, 2084, 2085, 2088, ORBConstants.DEFAULT_INS_PORT, 2096, 2137, 2142, 2276, 2307, 2362, 2363, 2364, 2365, 2369, 2377, 2381, 2382, 2385, 2392, 2402, 2404, 2433, 2434, 2492, 2493, 2497, 2503, 2509, 2510, 2530, 2534, 2546, 2548, 2555, 2563, 2620, 2622, 2625, 2649, 2672, 2674, 2677, 2691, 2748, 2749, 2753, 2761, 2765, 2768, 2786, 2790, 2801, 2818, 2876, 2877, 2879, 2880, 2881, 2887, 2893, 2903, 2914, 2918, 2946, 2947, 3008, 3009, 3021, 3024, 3059, 3073, 3134, 3137, 3142, 3160, 3170, 3174, 3192, 3199, 3260, 3261, 3276, 3285, 3298, 3302, 3393, 3398, 3405, 3406, 3426, 3430, 3530, 3535, 3538, 3544, 3633, 3634, 3636, 3648, 3655, 3663, 3761, 3762, 3764, 3773, 3784, 3792, 3864, 3866, 3893, 3894, 3895, 3896, 3897, 3902, 3953, 3967, 3968, 3973, 3974, 3976, 3981, 4030, 4038, 4039, 4141, 4145, 4146, 4152, 4153, 4155, 4157, 4159, 4184, 4186, 4190, 4193, 4209, 4213, 4226, 4227, 4229, 4231, 4237, 4238, 4253, 4254, 4957, 4960, 5008, 5024, 5120, 5121, 5760, 5761, 5787, 5792, 5906, 5920, 5938, 5941, 5970, 5984, 6002, 6016, 6068, 6070, 6071, 6078, 6086, 6087, 6089, 6100, 6107, 6108, 6109, 6112, 6128, 6160, 6313, 6314, 6432, 6435, 6439, 6441, 6450, 6451, 6457, 6470, 6622, 6656, 6679, 6681, 6742, 6743, 6744, 6753, 6754, 6755, 6757, 6765, 6771, 6784, 6912, 6916, 6964, 6965, 6966, 6971, 6972, 6973, 6978, 6979, 7019, 7028, 7040, 7042, 7074, 7078, 7080, 7082, 7083, 7084, 7142, 7143, 7144, 7146, 7149, 7150, 7151, 7154, 7212, 7220, 7222, 7227, 7376, 7379, 7380, 7393, 7394, 7401, 7405, 7406, 7412, 7413, 7616, 7680, 8125, 8126, 8127, 8130, 8141, 8144, 8157, 8160, 8173, 8178, 8189, 8206, 8208, 8305, 8308, 8319, 8320, 8336, 8352, 8450, 8451, 8455, 8456, 8458, 8468, 8469, 8470, 8473, 8478, 8484, 8485, 8486, 8487, 8488, 8489, 8490, 8494, 8495, 8506, 8508, 8512, 8517, 8522, 8526, 8528, 8544, 8585, 9014, 9083, 9109, 9110, 9372, 9450, 9900, 9901, 10240, 10496, 11264, 11493, 11499, 11503, 11506, 11513, 11520, 11647, 11648, 11744, 12293, 12296, 12321, 12330, 12337, 12342, 12344, 12349, 12353, 12441, 12445, 12448, 12449, 12539, 12540, 12736, 12784, 12829, 12832, 12880, 12896, 12924, 12927, 12977, 12992, 13004, 13008, 13175, 13179, 13278, 13280, 13311, 13312, 19904, 19968, 42128, 42192, 42509, 42512, 42607, 42624, 42655, 42656, 42736, 42738, 42752, 42786, 42888, 42889, 43010, 43011, 43014, 43015, 43019, 43020, 43045, 43047, 43048, 43056, 43064, 43072, 43124, 43136, 43204, 43214, 43232, 43250, 43302, 43310, 43335, 43346, 43392, 43395, 43443, 43444, 43446, 43450, 43452, 43453, 43561, 43567, 43569, 43571, 43573, 43584, 43587, 43588, 43596, 43597, 43696, 43697, 43698, 43701, 43703, 43705, 43710, 43712, 43713, 43714, 43756, 43758, 43766, 43777, 44005, 44006, 44008, 44009, 44013, 44016, 64286, 64287, 64297, 64298, 64830, 64848, 65021, 65136, 65279, 65313, 65339, 65345, 65371, 65382, 65504, 65536, 65793, 65794, 65856, 66000, 66045, 66176, 67871, 67872, 68097, 68112, 68152, 68160, 68409, 68416, 69216, 69632, 69633, 69634, 69688, 69703, 69714, 69734, 69760, 69762, 69811, 69815, 69817, 69819, 69888, 69891, 69927, 69932, 69933, 69942, 70016, 70018, 70070, 70079, 71339, 71340, 71341, 71342, 71344, 71350, 71351, 71360, 94095, 94099, 119143, 119146, 119155, 119171, 119173, 119180, 119210, 119214, 119296, 119648, 120539, 120540, 120597, 120598, 120655, 120656, 120713, 120714, 120771, 120772, 120782, 126464, 126704, 127248, 127338, 127344, 127744, 128140, 128141, 128292, 128293, 131072, 917505, 983040, 1114110, 1114111};

    /* loaded from: rt.jar:java/awt/font/NumericShaper$Range.class */
    public enum Range {
        EUROPEAN(48, 0, 768),
        ARABIC(1632, 1536, 1920),
        EASTERN_ARABIC(1776, 1536, 1920),
        DEVANAGARI(2406, 2304, 2432),
        BENGALI(2534, 2432, StackType.VOID),
        GURMUKHI(2662, StackType.VOID, 2688),
        GUJARATI(2790, 2816, 2944),
        ORIYA(2918, 2816, 2944),
        TAMIL(3046, 2944, 3072),
        TELUGU(3174, 3072, 3200),
        KANNADA(3302, 3200, 3328),
        MALAYALAM(3430, 3328, 3456),
        THAI(3664, 3584, 3712),
        LAO(3792, 3712, WalkerFactory.BITS_RESERVED),
        TIBETAN(3872, WalkerFactory.BITS_RESERVED, 4096),
        MYANMAR(4160, 4096, 4224),
        ETHIOPIC(4969, 4608, 4992) { // from class: java.awt.font.NumericShaper.Range.1
            @Override // java.awt.font.NumericShaper.Range
            char getNumericBase() {
                return (char) 1;
            }
        },
        KHMER(6112, 6016, 6144),
        MONGOLIAN(6160, 6144, 6400),
        NKO(1984, 1984, 2048),
        MYANMAR_SHAN(4240, 4096, 4256),
        LIMBU(6470, 6400, 6480),
        NEW_TAI_LUE(6608, 6528, 6624),
        BALINESE(6992, 6912, 7040),
        SUNDANESE(7088, 7040, 7104),
        LEPCHA(7232, 7168, 7248),
        OL_CHIKI(7248, 7248, 7296),
        VAI(42528, 42240, 42560),
        SAURASHTRA(43216, 43136, 43232),
        KAYAH_LI(43264, 43264, 43312),
        CHAM(43600, 43520, 43616),
        TAI_THAM_HORA(6784, 6688, 6832),
        TAI_THAM_THAM(6800, 6688, 6832),
        JAVANESE(43472, 43392, 43488),
        MEETEI_MAYEK(44016, 43968, NormalizerImpl.HANGUL_BASE);

        private final int base;
        private final int start;
        private final int end;

        /* JADX INFO: Access modifiers changed from: private */
        public static int toRangeIndex(Range range) {
            int iOrdinal = range.ordinal();
            if (iOrdinal < 19) {
                return iOrdinal;
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Range indexToRange(int i2) {
            if (i2 < 19) {
                return values()[i2];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int toRangeMask(Set<Range> set) {
            int i2 = 0;
            Iterator<Range> it = set.iterator();
            while (it.hasNext()) {
                int iOrdinal = it.next().ordinal();
                if (iOrdinal < 19) {
                    i2 |= 1 << iOrdinal;
                }
            }
            return i2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Set<Range> maskToRangeSet(int i2) {
            EnumSet enumSetNoneOf = EnumSet.noneOf(Range.class);
            Range[] rangeArrValues = values();
            for (int i3 = 0; i3 < 19; i3++) {
                if ((i2 & (1 << i3)) != 0) {
                    enumSetNoneOf.add(rangeArrValues[i3]);
                }
            }
            return enumSetNoneOf;
        }

        Range(int i2, int i3, int i4) {
            this.base = i2 - ('0' + getNumericBase());
            this.start = i3;
            this.end = i4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDigitBase() {
            return this.base;
        }

        char getNumericBase() {
            return (char) 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean inRange(int i2) {
            return this.start <= i2 && i2 < this.end;
        }
    }

    private static int getContextKey(char c2) {
        if (c2 < contexts[ctCache]) {
            while (ctCache > 0 && c2 < contexts[ctCache]) {
                ctCache--;
            }
        } else if (c2 >= contexts[ctCache + 1]) {
            while (ctCache < ctCacheLimit && c2 >= contexts[ctCache + 1]) {
                ctCache++;
            }
        }
        if ((ctCache & 1) == 0) {
            return ctCache / 2;
        }
        return 0;
    }

    private Range rangeForCodePoint(int i2) {
        if (this.currentRange.inRange(i2)) {
            return this.currentRange;
        }
        Range[] rangeArr = this.rangeArray;
        if (rangeArr.length > 3) {
            int i3 = 0;
            int length = rangeArr.length - 1;
            while (i3 <= length) {
                int i4 = (i3 + length) / 2;
                Range range = rangeArr[i4];
                if (i2 < range.start) {
                    length = i4 - 1;
                } else if (i2 >= range.end) {
                    i3 = i4 + 1;
                } else {
                    this.currentRange = range;
                    return range;
                }
            }
        } else {
            for (int i5 = 0; i5 < rangeArr.length; i5++) {
                if (rangeArr[i5].inRange(i2)) {
                    return rangeArr[i5];
                }
            }
        }
        return Range.EUROPEAN;
    }

    private boolean isStrongDirectional(char c2) {
        int iSearch = this.stCache;
        if (c2 < strongTable[iSearch]) {
            iSearch = search(c2, strongTable, 0, iSearch);
        } else if (c2 >= strongTable[iSearch + 1]) {
            iSearch = search(c2, strongTable, iSearch + 1, (strongTable.length - iSearch) - 1);
        }
        boolean z2 = (iSearch & 1) == 1;
        this.stCache = iSearch;
        return z2;
    }

    private static int getKeyFromMask(int i2) {
        int i3 = 0;
        while (i3 < 19 && (i2 & (1 << i3)) == 0) {
            i3++;
        }
        if (i3 == 19 || (i2 & ((1 << i3) ^ (-1))) != 0) {
            throw new IllegalArgumentException("invalid shaper: " + Integer.toHexString(i2));
        }
        return i3;
    }

    public static NumericShaper getShaper(int i2) {
        return new NumericShaper(getKeyFromMask(i2), i2);
    }

    public static NumericShaper getShaper(Range range) {
        return new NumericShaper(range, EnumSet.of(range));
    }

    public static NumericShaper getContextualShaper(int i2) {
        return new NumericShaper(0, i2 | Integer.MIN_VALUE);
    }

    public static NumericShaper getContextualShaper(Set<Range> set) {
        NumericShaper numericShaper = new NumericShaper(Range.EUROPEAN, set);
        numericShaper.mask = Integer.MIN_VALUE;
        return numericShaper;
    }

    public static NumericShaper getContextualShaper(int i2, int i3) {
        return new NumericShaper(getKeyFromMask(i3), i2 | Integer.MIN_VALUE);
    }

    public static NumericShaper getContextualShaper(Set<Range> set, Range range) {
        if (range == null) {
            throw new NullPointerException();
        }
        NumericShaper numericShaper = new NumericShaper(range, set);
        numericShaper.mask = Integer.MIN_VALUE;
        return numericShaper;
    }

    private NumericShaper(int i2, int i3) {
        this.key = i2;
        this.mask = i3;
    }

    private NumericShaper(Range range, Set<Range> set) {
        this.shapingRange = range;
        this.rangeSet = EnumSet.copyOf(set);
        if (this.rangeSet.contains(Range.EASTERN_ARABIC) && this.rangeSet.contains(Range.ARABIC)) {
            this.rangeSet.remove(Range.ARABIC);
        }
        if (this.rangeSet.contains(Range.TAI_THAM_THAM) && this.rangeSet.contains(Range.TAI_THAM_HORA)) {
            this.rangeSet.remove(Range.TAI_THAM_HORA);
        }
        this.rangeArray = (Range[]) this.rangeSet.toArray(new Range[this.rangeSet.size()]);
        if (this.rangeArray.length > 3) {
            Arrays.sort(this.rangeArray, new Comparator<Range>() { // from class: java.awt.font.NumericShaper.1
                @Override // java.util.Comparator
                public int compare(Range range2, Range range3) {
                    if (range2.base > range3.base) {
                        return 1;
                    }
                    return range2.base == range3.base ? 0 : -1;
                }
            });
        }
    }

    public void shape(char[] cArr, int i2, int i3) {
        checkParams(cArr, i2, i3);
        if (isContextual()) {
            if (this.rangeSet == null) {
                shapeContextually(cArr, i2, i3, this.key);
                return;
            } else {
                shapeContextually(cArr, i2, i3, this.shapingRange);
                return;
            }
        }
        shapeNonContextually(cArr, i2, i3);
    }

    public void shape(char[] cArr, int i2, int i3, int i4) {
        checkParams(cArr, i2, i3);
        if (isContextual()) {
            int keyFromMask = getKeyFromMask(i4);
            if (this.rangeSet == null) {
                shapeContextually(cArr, i2, i3, keyFromMask);
                return;
            } else {
                shapeContextually(cArr, i2, i3, Range.values()[keyFromMask]);
                return;
            }
        }
        shapeNonContextually(cArr, i2, i3);
    }

    public void shape(char[] cArr, int i2, int i3, Range range) {
        checkParams(cArr, i2, i3);
        if (range == null) {
            throw new NullPointerException("context is null");
        }
        if (isContextual()) {
            if (this.rangeSet == null) {
                int rangeIndex = Range.toRangeIndex(range);
                if (rangeIndex >= 0) {
                    shapeContextually(cArr, i2, i3, rangeIndex);
                    return;
                } else {
                    shapeContextually(cArr, i2, i3, this.shapingRange);
                    return;
                }
            }
            shapeContextually(cArr, i2, i3, range);
            return;
        }
        shapeNonContextually(cArr, i2, i3);
    }

    private void checkParams(char[] cArr, int i2, int i3) {
        if (cArr == null) {
            throw new NullPointerException("text is null");
        }
        if (i2 < 0 || i2 > cArr.length || i2 + i3 < 0 || i2 + i3 > cArr.length) {
            throw new IndexOutOfBoundsException("bad start or count for text of length " + cArr.length);
        }
    }

    public boolean isContextual() {
        return (this.mask & Integer.MIN_VALUE) != 0;
    }

    public int getRanges() {
        return this.mask & Integer.MAX_VALUE;
    }

    public Set<Range> getRangeSet() {
        if (this.rangeSet == null) {
            return Range.maskToRangeSet(this.mask);
        }
        return EnumSet.copyOf(this.rangeSet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v21, types: [int] */
    private void shapeNonContextually(char[] cArr, int i2, int i3) {
        char digitBase;
        char numericBase = '0';
        if (this.shapingRange != null) {
            digitBase = this.shapingRange.getDigitBase();
            numericBase = (char) ('0' + this.shapingRange.getNumericBase());
        } else {
            digitBase = bases[this.key];
            if (this.key == 16) {
                numericBase = (char) (48 + 1);
            }
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            char c2 = cArr[i5];
            if (c2 >= numericBase && c2 <= '9') {
                cArr[i5] = (char) (c2 + digitBase);
            }
        }
    }

    private synchronized void shapeContextually(char[] cArr, int i2, int i3, int i4) {
        int contextKey;
        if ((this.mask & (1 << i4)) == 0) {
            i4 = 0;
        }
        int i5 = i4;
        char c2 = bases[i4];
        char c3 = i4 == 16 ? '1' : '0';
        synchronized (NumericShaper.class) {
            int i6 = i2 + i3;
            for (int i7 = i2; i7 < i6; i7++) {
                char c4 = cArr[i7];
                if (c4 >= c3 && c4 <= '9') {
                    cArr[i7] = (char) (c4 + c2);
                }
                if (isStrongDirectional(c4) && (contextKey = getContextKey(c4)) != i5) {
                    i5 = contextKey;
                    int i8 = contextKey;
                    if ((this.mask & 4) != 0 && (i8 == 1 || i8 == 2)) {
                        i8 = 2;
                    } else if ((this.mask & 2) != 0 && (i8 == 1 || i8 == 2)) {
                        i8 = 1;
                    } else if ((this.mask & (1 << i8)) == 0) {
                        i8 = 0;
                    }
                    c2 = bases[i8];
                    c3 = i8 == 16 ? '1' : '0';
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [int] */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    private void shapeContextually(char[] cArr, int i2, int i3, Range range) {
        Range rangeRangeForCodePoint;
        if (range == null || !this.rangeSet.contains(range)) {
            range = Range.EUROPEAN;
        }
        Range range2 = range;
        char digitBase = range.getDigitBase();
        char numericBase = (char) ('0' + range.getNumericBase());
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            char c2 = cArr[i5];
            if (c2 >= numericBase && c2 <= '9') {
                cArr[i5] = (char) (c2 + digitBase);
            } else if (isStrongDirectional(c2) && (rangeRangeForCodePoint = rangeForCodePoint(c2)) != range2) {
                range2 = rangeRangeForCodePoint;
                digitBase = rangeRangeForCodePoint.getDigitBase();
                numericBase = (char) ('0' + rangeRangeForCodePoint.getNumericBase());
            }
        }
    }

    public int hashCode() {
        int iHashCode = this.mask;
        if (this.rangeSet != null) {
            iHashCode = (iHashCode & Integer.MIN_VALUE) ^ this.rangeSet.hashCode();
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            try {
                NumericShaper numericShaper = (NumericShaper) obj;
                if (this.rangeSet != null) {
                    return numericShaper.rangeSet != null ? isContextual() == numericShaper.isContextual() && this.rangeSet.equals(numericShaper.rangeSet) && this.shapingRange == numericShaper.shapingRange : isContextual() == numericShaper.isContextual() && this.rangeSet.equals(Range.maskToRangeSet(numericShaper.mask)) && this.shapingRange == Range.indexToRange(numericShaper.key);
                }
                if (numericShaper.rangeSet != null) {
                    return isContextual() == numericShaper.isContextual() && Range.maskToRangeSet(this.mask).equals(numericShaper.rangeSet) && Range.indexToRange(this.key) == numericShaper.shapingRange;
                }
                return numericShaper.mask == this.mask && numericShaper.key == this.key;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("[contextual:").append(isContextual());
        if (isContextual()) {
            sb.append(", context:");
            sb.append((Object) (this.shapingRange == null ? Range.values()[this.key] : this.shapingRange));
        }
        if (this.rangeSet == null) {
            sb.append(", range(s): ");
            boolean z2 = true;
            for (int i2 = 0; i2 < 19; i2++) {
                if ((this.mask & (1 << i2)) != 0) {
                    if (z2) {
                        z2 = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append((Object) Range.values()[i2]);
                }
            }
        } else {
            sb.append(", range set: ").append((Object) this.rangeSet);
        }
        sb.append(']');
        return sb.toString();
    }

    private static int getHighBit(int i2) {
        if (i2 <= 0) {
            return -32;
        }
        int i3 = 0;
        if (i2 >= 65536) {
            i2 >>= 16;
            i3 = 0 + 16;
        }
        if (i2 >= 256) {
            i2 >>= 8;
            i3 += 8;
        }
        if (i2 >= 16) {
            i2 >>= 4;
            i3 += 4;
        }
        if (i2 >= 4) {
            i2 >>= 2;
            i3 += 2;
        }
        if (i2 >= 2) {
            i3++;
        }
        return i3;
    }

    private static int search(int i2, int[] iArr, int i3, int i4) {
        int highBit = 1 << getHighBit(i4);
        int i5 = i4 - highBit;
        int i6 = highBit;
        int i7 = i3;
        if (i2 >= iArr[i7 + i5]) {
            i7 += i5;
        }
        while (i6 > 1) {
            i6 >>= 1;
            if (i2 >= iArr[i7 + i6]) {
                i7 += i6;
            }
        }
        return i7;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int rangeIndex;
        if (this.shapingRange != null && (rangeIndex = Range.toRangeIndex(this.shapingRange)) >= 0) {
            this.key = rangeIndex;
        }
        if (this.rangeSet != null) {
            this.mask |= Range.toRangeMask(this.rangeSet);
        }
        objectOutputStream.defaultWriteObject();
    }
}
