package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/OptMapInfo.class */
final class OptMapInfo {
    int value;

    /* renamed from: z, reason: collision with root package name */
    private static final int f12879z = 32768;
    static final short[] ByteValTable = {5, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 12, 4, 7, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 1};
    final MinMaxLen mmd = new MinMaxLen();
    final OptAnchorInfo anchor = new OptAnchorInfo();
    final byte[] map = new byte[256];

    OptMapInfo() {
    }

    void clear() {
        this.mmd.clear();
        this.anchor.clear();
        this.value = 0;
        for (int i2 = 0; i2 < this.map.length; i2++) {
            this.map[i2] = 0;
        }
    }

    void copy(OptMapInfo other) {
        this.mmd.copy(other.mmd);
        this.anchor.copy(other.anchor);
        this.value = other.value;
        System.arraycopy(other.map, 0, this.map, 0, other.map.length);
    }

    void addChar(int c2) {
        int c_ = c2 & 255;
        if (this.map[c_] == 0) {
            this.map[c_] = 1;
            this.value += positionValue(c_);
        }
    }

    void addCharAmb(char[] chars, int p2, int end, int caseFoldFlag) {
        addChar(chars[p2]);
        char[] items = EncodingHelper.caseFoldCodesByString(caseFoldFlag & (-1073741825), chars[p2]);
        for (char c2 : items) {
            addChar(c2);
        }
    }

    void select(OptMapInfo alt) {
        if (alt.value == 0) {
            return;
        }
        if (this.value == 0) {
            copy(alt);
            return;
        }
        int v1 = 32768 / this.value;
        int v2 = 32768 / alt.value;
        if (this.mmd.compareDistanceValue(alt.mmd, v1, v2) > 0) {
            copy(alt);
        }
    }

    void altMerge(OptMapInfo other) {
        if (this.value == 0) {
            return;
        }
        if (other.value == 0 || this.mmd.max < other.mmd.max) {
            clear();
            return;
        }
        this.mmd.altMerge(other.mmd);
        int val = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            if (other.map[i2] != 0) {
                this.map[i2] = 1;
            }
            if (this.map[i2] != 0) {
                val += positionValue(i2);
            }
        }
        this.value = val;
        this.anchor.altMerge(other.anchor);
    }

    static int positionValue(int i2) {
        if (i2 < ByteValTable.length) {
            return ByteValTable[i2];
        }
        return 4;
    }
}
