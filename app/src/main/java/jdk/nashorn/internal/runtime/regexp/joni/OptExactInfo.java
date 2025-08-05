package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/OptExactInfo.class */
final class OptExactInfo {
    static final int OPT_EXACT_MAXLEN = 24;
    boolean reachEnd;
    boolean ignoreCase;
    int length;
    private static final int COMP_EM_BASE = 20;
    final MinMaxLen mmd = new MinMaxLen();
    final OptAnchorInfo anchor = new OptAnchorInfo();
    final char[] chars = new char[24];

    OptExactInfo() {
    }

    boolean isFull() {
        return this.length >= 24;
    }

    void clear() {
        this.mmd.clear();
        this.anchor.clear();
        this.reachEnd = false;
        this.ignoreCase = false;
        this.length = 0;
    }

    void copy(OptExactInfo other) {
        this.mmd.copy(other.mmd);
        this.anchor.copy(other.anchor);
        this.reachEnd = other.reachEnd;
        this.ignoreCase = other.ignoreCase;
        this.length = other.length;
        System.arraycopy(other.chars, 0, this.chars, 0, 24);
    }

    void concat(OptExactInfo other) {
        if (!this.ignoreCase && other.ignoreCase) {
            if (this.length >= other.length) {
                return;
            } else {
                this.ignoreCase = true;
            }
        }
        int p2 = 0;
        int end = 0 + other.length;
        int i2 = this.length;
        while (p2 < end && i2 + 1 <= 24) {
            int i3 = i2;
            i2++;
            int i4 = p2;
            p2++;
            this.chars[i3] = other.chars[i4];
        }
        this.length = i2;
        this.reachEnd = p2 == end ? other.reachEnd : false;
        OptAnchorInfo tmp = new OptAnchorInfo();
        tmp.concat(this.anchor, other.anchor, 1, 1);
        if (!other.reachEnd) {
            tmp.rightAnchor = 0;
        }
        this.anchor.copy(tmp);
    }

    void concatStr(char[] lchars, int pp, int end, boolean raw) {
        int p2 = pp;
        int i2 = this.length;
        while (p2 < end && i2 < 24 && i2 + 1 <= 24) {
            int i3 = i2;
            i2++;
            int i4 = p2;
            p2++;
            this.chars[i3] = lchars[i4];
        }
        this.length = i2;
    }

    void altMerge(OptExactInfo other, OptEnvironment env) {
        if (other.length == 0 || this.length == 0) {
            clear();
            return;
        }
        if (!this.mmd.equal(other.mmd)) {
            clear();
            return;
        }
        int i2 = 0;
        while (i2 < this.length && i2 < other.length && this.chars[i2] == other.chars[i2]) {
            i2++;
        }
        if (!other.reachEnd || i2 < other.length || i2 < this.length) {
            this.reachEnd = false;
        }
        this.length = i2;
        this.ignoreCase |= other.ignoreCase;
        this.anchor.altMerge(other.anchor);
        if (!this.reachEnd) {
            this.anchor.rightAnchor = 0;
        }
    }

    void select(OptExactInfo alt) {
        int v1 = this.length;
        int v2 = alt.length;
        if (v2 == 0) {
            return;
        }
        if (v1 == 0) {
            copy(alt);
            return;
        }
        if (v1 <= 2 && v2 <= 2) {
            v2 = OptMapInfo.positionValue(this.chars[0] & 255);
            v1 = OptMapInfo.positionValue(alt.chars[0] & 255);
            if (this.length > 1) {
                v1 += 5;
            }
            if (alt.length > 1) {
                v2 += 5;
            }
        }
        if (!this.ignoreCase) {
            v1 *= 2;
        }
        if (!alt.ignoreCase) {
            v2 *= 2;
        }
        if (this.mmd.compareDistanceValue(alt.mmd, v1, v2) > 0) {
            copy(alt);
        }
    }

    int compare(OptMapInfo m2) {
        if (m2.value <= 0) {
            return -1;
        }
        int ve = 20 * this.length * (this.ignoreCase ? 1 : 2);
        int vm = 200 / m2.value;
        return this.mmd.compareDistanceValue(m2.mmd, ve, vm);
    }
}
