package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/BitSet.class */
public final class BitSet {
    static final int BITS_PER_BYTE = 8;
    public static final int SINGLE_BYTE_SIZE = 256;
    private static final int BITS_IN_ROOM = 32;
    static final int BITSET_SIZE = 8;
    static final int ROOM_SHIFT = log2(32);
    final int[] bits = new int[8];
    private static final int BITS_TO_STRING_WRAP = 4;

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("BitSet");
        for (int i2 = 0; i2 < 256; i2++) {
            if (i2 % 64 == 0) {
                buffer.append("\n  ");
            }
            buffer.append(at(i2) ? "1" : "0");
        }
        return buffer.toString();
    }

    public boolean at(int pos) {
        return (this.bits[pos >>> ROOM_SHIFT] & bit(pos)) != 0;
    }

    public void set(int pos) {
        int[] iArr = this.bits;
        int i2 = pos >>> ROOM_SHIFT;
        iArr[i2] = iArr[i2] | bit(pos);
    }

    public void clear(int pos) {
        int[] iArr = this.bits;
        int i2 = pos >>> ROOM_SHIFT;
        iArr[i2] = iArr[i2] & (bit(pos) ^ (-1));
    }

    public void clear() {
        for (int i2 = 0; i2 < 8; i2++) {
            this.bits[i2] = 0;
        }
    }

    public boolean isEmpty() {
        for (int i2 = 0; i2 < 8; i2++) {
            if (this.bits[i2] != 0) {
                return false;
            }
        }
        return true;
    }

    public void setRange(int from, int to) {
        for (int i2 = from; i2 <= to && i2 < 256; i2++) {
            set(i2);
        }
    }

    public void invert() {
        for (int i2 = 0; i2 < 8; i2++) {
            this.bits[i2] = this.bits[i2] ^ (-1);
        }
    }

    public void invertTo(BitSet to) {
        for (int i2 = 0; i2 < 8; i2++) {
            to.bits[i2] = this.bits[i2] ^ (-1);
        }
    }

    public void and(BitSet other) {
        for (int i2 = 0; i2 < 8; i2++) {
            int[] iArr = this.bits;
            int i3 = i2;
            iArr[i3] = iArr[i3] & other.bits[i2];
        }
    }

    public void or(BitSet other) {
        for (int i2 = 0; i2 < 8; i2++) {
            int[] iArr = this.bits;
            int i3 = i2;
            iArr[i3] = iArr[i3] | other.bits[i2];
        }
    }

    public void copy(BitSet other) {
        for (int i2 = 0; i2 < 8; i2++) {
            this.bits[i2] = other.bits[i2];
        }
    }

    public int numOn() {
        int num = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            if (at(i2)) {
                num++;
            }
        }
        return num;
    }

    static int bit(int pos) {
        return 1 << (pos % 256);
    }

    private static int log2(int np) {
        int log = 0;
        int n2 = np;
        while (true) {
            int i2 = n2 >>> 1;
            n2 = i2;
            if (i2 != 0) {
                log++;
            } else {
                return log;
            }
        }
    }
}
