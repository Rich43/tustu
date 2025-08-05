package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/SearchAlgorithm.class */
public abstract class SearchAlgorithm {
    public static final SearchAlgorithm NONE = new SearchAlgorithm() { // from class: jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm.1
        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final String getName() {
            return "NONE";
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            return textP;
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            return textP;
        }
    };
    public static final SearchAlgorithm SLOW = new SearchAlgorithm() { // from class: jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm.2
        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final String getName() {
            return "EXACT";
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int end = textEnd - ((targetEnd - targetP) - 1);
            if (end > textRange) {
                end = textRange;
            }
            for (int s2 = textP; s2 < end; s2++) {
                if (text[s2] == target[targetP]) {
                    int p2 = s2 + 1;
                    int t2 = targetP + 1;
                    while (t2 < targetEnd) {
                        int i2 = p2;
                        p2++;
                        if (target[t2] != text[i2]) {
                            break;
                        }
                        t2++;
                    }
                    if (t2 == targetEnd) {
                        return s2;
                    }
                }
            }
            return -1;
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int s2 = textEnd - (targetEnd - targetP);
            if (s2 > textStart) {
                s2 = textStart;
            }
            while (s2 >= textP) {
                if (text[s2] == target[targetP]) {
                    int p2 = s2 + 1;
                    int t2 = targetP + 1;
                    while (t2 < targetEnd) {
                        int i2 = p2;
                        p2++;
                        if (target[t2] != text[i2]) {
                            break;
                        }
                        t2++;
                    }
                    if (t2 == targetEnd) {
                        return s2;
                    }
                }
                s2--;
            }
            return -1;
        }
    };
    public static final SearchAlgorithm BM = new SearchAlgorithm() { // from class: jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm.3
        private static final int BM_BACKWARD_SEARCH_LENGTH_THRESHOLD = 100;

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final String getName() {
            return "EXACT_BM";
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int end = (textRange + (targetEnd - targetP)) - 1;
            if (end > textEnd) {
                end = textEnd;
            }
            int tail = targetEnd - 1;
            int s2 = (textP + (targetEnd - targetP)) - 1;
            if (regex.intMap == null) {
                while (s2 < end) {
                    int p2 = s2;
                    for (int t2 = tail; text[p2] == target[t2]; t2--) {
                        if (t2 == targetP) {
                            return p2;
                        }
                        p2--;
                    }
                    s2 += regex.map[text[s2] & 255];
                }
                return -1;
            }
            while (s2 < end) {
                int p3 = s2;
                for (int t3 = tail; text[p3] == target[t3]; t3--) {
                    if (t3 == targetP) {
                        return p3;
                    }
                    p3--;
                }
                s2 += regex.intMap[text[s2] & 255];
            }
            return -1;
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            if (regex.intMapBackward == null) {
                if (s_ - range_ < 100) {
                    return SLOW.searchBackward(regex, text, textP, adjustText, textEnd, textStart, s_, range_);
                }
                setBmBackwardSkip(regex, target, targetP, targetEnd);
            }
            int s2 = textEnd - (targetEnd - targetP);
            if (textStart < s2) {
                s2 = textStart;
            }
            while (s2 >= textP) {
                int p2 = s2;
                int t2 = targetP;
                while (t2 < targetEnd && text[p2] == target[t2]) {
                    p2++;
                    t2++;
                }
                if (t2 == targetEnd) {
                    return s2;
                }
                s2 -= regex.intMapBackward[text[s2] & 255];
            }
            return -1;
        }

        private void setBmBackwardSkip(Regex regex, char[] chars, int p2, int end) {
            int[] skip;
            if (regex.intMapBackward == null) {
                skip = new int[256];
                regex.intMapBackward = skip;
            } else {
                skip = regex.intMapBackward;
            }
            int len = end - p2;
            for (int i2 = 0; i2 < 256; i2++) {
                skip[i2] = len;
            }
            for (int i3 = len - 1; i3 > 0; i3--) {
                skip[chars[i3] & 255] = i3;
            }
        }
    };
    public static final SearchAlgorithm MAP = new SearchAlgorithm() { // from class: jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm.4
        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final String getName() {
            return "MAP";
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            byte[] map = regex.map;
            for (int s2 = textP; s2 < textRange; s2++) {
                if (text[s2] > 255 || map[text[s2]] != 0) {
                    return s2;
                }
            }
            return -1;
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            byte[] map = regex.map;
            int s2 = textStart;
            if (s2 >= textEnd) {
                s2 = textEnd - 1;
            }
            while (s2 >= textP) {
                if (text[s2] > 255 || map[text[s2]] != 0) {
                    return s2;
                }
                s2--;
            }
            return -1;
        }
    };

    public abstract String getName();

    public abstract int search(Regex regex, char[] cArr, int i2, int i3, int i4);

    public abstract int searchBackward(Regex regex, char[] cArr, int i2, int i3, int i4, int i5, int i6, int i7);

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/SearchAlgorithm$SLOW_IC.class */
    public static final class SLOW_IC extends SearchAlgorithm {
        public SLOW_IC(Regex regex) {
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final String getName() {
            return "EXACT_IC";
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int end = textEnd - ((targetEnd - targetP) - 1);
            if (end > textRange) {
                end = textRange;
            }
            for (int s2 = textP; s2 < end; s2++) {
                if (lowerCaseMatch(target, targetP, targetEnd, text, s2, textEnd)) {
                    return s2;
                }
            }
            return -1;
        }

        @Override // jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int s2 = textEnd - (targetEnd - targetP);
            if (s2 > textStart) {
                s2 = textStart;
            }
            while (s2 >= textP) {
                if (lowerCaseMatch(target, targetP, targetEnd, text, s2, textEnd)) {
                    return s2;
                }
                s2 = EncodingHelper.prevCharHead(adjustText, s2);
            }
            return -1;
        }

        private static boolean lowerCaseMatch(char[] t2, int tPp, int tEnd, char[] chars, int pp, int end) {
            int tP = tPp;
            int p2 = pp;
            while (tP < tEnd) {
                int i2 = tP;
                tP++;
                int i3 = p2;
                p2++;
                if (t2[i2] != EncodingHelper.toLowerCase(chars[i3])) {
                    return false;
                }
            }
            return true;
        }
    }
}
