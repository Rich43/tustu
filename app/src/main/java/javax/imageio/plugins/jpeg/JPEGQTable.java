package javax.imageio.plugins.jpeg;

import java.util.Arrays;

/* loaded from: rt.jar:javax/imageio/plugins/jpeg/JPEGQTable.class */
public class JPEGQTable {
    private static final int[] k1 = {16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99};
    private static final int[] k1div2 = {8, 6, 5, 8, 12, 20, 26, 31, 6, 6, 7, 10, 13, 29, 30, 28, 7, 7, 8, 12, 20, 29, 35, 28, 7, 9, 11, 15, 26, 44, 40, 31, 9, 11, 19, 28, 34, 55, 52, 39, 12, 18, 28, 32, 41, 52, 57, 46, 25, 32, 39, 44, 52, 61, 60, 51, 36, 46, 48, 49, 56, 50, 52, 50};
    private static final int[] k2 = {17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
    private static final int[] k2div2 = {9, 9, 12, 24, 50, 50, 50, 50, 9, 11, 13, 33, 50, 50, 50, 50, 12, 13, 28, 50, 50, 50, 50, 50, 24, 33, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
    public static final JPEGQTable K1Luminance = new JPEGQTable(k1, false);
    public static final JPEGQTable K1Div2Luminance = new JPEGQTable(k1div2, false);
    public static final JPEGQTable K2Chrominance = new JPEGQTable(k2, false);
    public static final JPEGQTable K2Div2Chrominance = new JPEGQTable(k2div2, false);
    private int[] qTable;

    private JPEGQTable(int[] iArr, boolean z2) {
        this.qTable = z2 ? Arrays.copyOf(iArr, iArr.length) : iArr;
    }

    public JPEGQTable(int[] iArr) {
        if (iArr == null) {
            throw new IllegalArgumentException("table must not be null.");
        }
        if (iArr.length != 64) {
            throw new IllegalArgumentException("table.length != 64");
        }
        this.qTable = Arrays.copyOf(iArr, iArr.length);
    }

    public int[] getTable() {
        return Arrays.copyOf(this.qTable, this.qTable.length);
    }

    public JPEGQTable getScaledInstance(float f2, boolean z2) {
        int i2 = z2 ? 255 : Short.MAX_VALUE;
        int[] iArr = new int[this.qTable.length];
        for (int i3 = 0; i3 < this.qTable.length; i3++) {
            int i4 = (int) ((this.qTable[i3] * f2) + 0.5f);
            if (i4 < 1) {
                i4 = 1;
            }
            if (i4 > i2) {
                i4 = i2;
            }
            iArr[i3] = i4;
        }
        return new JPEGQTable(iArr);
    }

    public String toString() {
        String property = System.getProperty("line.separator", "\n");
        StringBuilder sb = new StringBuilder("JPEGQTable:" + property);
        for (int i2 = 0; i2 < this.qTable.length; i2++) {
            if (i2 % 8 == 0) {
                sb.append('\t');
            }
            sb.append(this.qTable[i2]);
            sb.append(i2 % 8 == 7 ? property : ' ');
        }
        return sb.toString();
    }
}
