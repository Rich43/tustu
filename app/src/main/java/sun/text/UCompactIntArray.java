package sun.text;

/* loaded from: rt.jar:sun/text/UCompactIntArray.class */
public final class UCompactIntArray implements Cloneable {
    private static final int PLANEMASK = 196608;
    private static final int PLANESHIFT = 16;
    private static final int PLANECOUNT = 16;
    private static final int CODEPOINTMASK = 65535;
    private static final int UNICODECOUNT = 65536;
    private static final int BLOCKSHIFT = 7;
    private static final int BLOCKCOUNT = 128;
    private static final int INDEXSHIFT = 9;
    private static final int INDEXCOUNT = 512;
    private static final int BLOCKMASK = 127;
    private int defaultValue;
    private int[][] values;
    private short[][] indices;
    private boolean isCompact;
    private boolean[][] blockTouched;
    private boolean[] planeTouched;

    /* JADX WARN: Type inference failed for: r1v1, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v3, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v5, types: [boolean[], boolean[][]] */
    public UCompactIntArray() {
        this.values = new int[16];
        this.indices = new short[16];
        this.blockTouched = new boolean[16];
        this.planeTouched = new boolean[16];
    }

    public UCompactIntArray(int i2) {
        this();
        this.defaultValue = i2;
    }

    public int elementAt(int i2) {
        int i3 = (i2 & PLANEMASK) >> 16;
        if (!this.planeTouched[i3]) {
            return this.defaultValue;
        }
        int i4 = i2 & 65535;
        return this.values[i3][(this.indices[i3][i4 >> 7] & 65535) + (i4 & 127)];
    }

    public void setElementAt(int i2, int i3) {
        if (this.isCompact) {
            expand();
        }
        int i4 = (i2 & PLANEMASK) >> 16;
        if (!this.planeTouched[i4]) {
            initPlane(i4);
        }
        int i5 = i2 & 65535;
        this.values[i4][i5] = i3;
        this.blockTouched[i4][i5 >> 7] = true;
    }

    public void compact() {
        if (this.isCompact) {
            return;
        }
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.planeTouched[i2]) {
                int i3 = 0;
                int i4 = 0;
                short s2 = -1;
                int i5 = 0;
                while (i5 < this.indices[i2].length) {
                    this.indices[i2][i5] = -1;
                    if (!this.blockTouched[i2][i5] && s2 != -1) {
                        this.indices[i2][i5] = s2;
                    } else {
                        int i6 = i3 * 128;
                        if (i5 > i3) {
                            System.arraycopy(this.values[i2], i4, this.values[i2], i6, 128);
                        }
                        if (!this.blockTouched[i2][i5]) {
                            s2 = (short) i6;
                        }
                        this.indices[i2][i5] = (short) i6;
                        i3++;
                    }
                    i5++;
                    i4 += 128;
                }
                int i7 = i3 * 128;
                int[] iArr = new int[i7];
                System.arraycopy(this.values[i2], 0, iArr, 0, i7);
                this.values[i2] = iArr;
                this.blockTouched[i2] = null;
            }
        }
        this.isCompact = true;
    }

    private void expand() {
        if (this.isCompact) {
            for (int i2 = 0; i2 < 16; i2++) {
                if (this.planeTouched[i2]) {
                    this.blockTouched[i2] = new boolean[512];
                    int[] iArr = new int[65536];
                    for (int i3 = 0; i3 < 65536; i3++) {
                        iArr[i3] = this.values[i2][this.indices[i2][i3 >> 7] & (65535 + (i3 & 127))];
                        this.blockTouched[i2][i3 >> 7] = true;
                    }
                    for (int i4 = 0; i4 < 512; i4++) {
                        this.indices[i2][i4] = (short) (i4 << 7);
                    }
                    this.values[i2] = iArr;
                }
            }
            this.isCompact = false;
        }
    }

    private void initPlane(int i2) {
        this.values[i2] = new int[65536];
        this.indices[i2] = new short[512];
        this.blockTouched[i2] = new boolean[512];
        this.planeTouched[i2] = true;
        if (this.planeTouched[0] && i2 != 0) {
            System.arraycopy(this.indices[0], 0, this.indices[i2], 0, 512);
        } else {
            for (int i3 = 0; i3 < 512; i3++) {
                this.indices[i2][i3] = (short) (i3 << 7);
            }
        }
        for (int i4 = 0; i4 < 65536; i4++) {
            this.values[i2][i4] = this.defaultValue;
        }
    }

    public int getKSize() {
        int length = 0;
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.planeTouched[i2]) {
                length += (this.values[i2].length * 4) + (this.indices[i2].length * 2);
            }
        }
        return length / 1024;
    }
}
