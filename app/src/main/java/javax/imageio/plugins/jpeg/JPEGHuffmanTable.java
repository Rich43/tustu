package javax.imageio.plugins.jpeg;

import java.util.Arrays;

/* loaded from: rt.jar:javax/imageio/plugins/jpeg/JPEGHuffmanTable.class */
public class JPEGHuffmanTable {
    private static final short[] StdDCLuminanceLengths = {0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0};
    private static final short[] StdDCLuminanceValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final short[] StdDCChrominanceLengths = {0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
    private static final short[] StdDCChrominanceValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final short[] StdACLuminanceLengths = {0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125};
    private static final short[] StdACLuminanceValues = {1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, 129, 145, 161, 8, 35, 66, 177, 193, 21, 82, 209, 240, 36, 51, 98, 114, 130, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250};
    private static final short[] StdACChrominanceLengths = {0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119};
    private static final short[] StdACChrominanceValues = {0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, 129, 8, 20, 66, 145, 161, 177, 193, 9, 35, 51, 82, 240, 21, 98, 114, 209, 10, 22, 36, 52, 225, 37, 241, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, 130, 131, 132, 133, 134, 135, 136, 137, 138, 146, 147, 148, 149, 150, 151, 152, 153, 154, 162, 163, 164, 165, 166, 167, 168, 169, 170, 178, 179, 180, 181, 182, 183, 184, 185, 186, 194, 195, 196, 197, 198, 199, 200, 201, 202, 210, 211, 212, 213, 214, 215, 216, 217, 218, 226, 227, 228, 229, 230, 231, 232, 233, 234, 242, 243, 244, 245, 246, 247, 248, 249, 250};
    public static final JPEGHuffmanTable StdDCLuminance = new JPEGHuffmanTable(StdDCLuminanceLengths, StdDCLuminanceValues, false);
    public static final JPEGHuffmanTable StdDCChrominance = new JPEGHuffmanTable(StdDCChrominanceLengths, StdDCChrominanceValues, false);
    public static final JPEGHuffmanTable StdACLuminance = new JPEGHuffmanTable(StdACLuminanceLengths, StdACLuminanceValues, false);
    public static final JPEGHuffmanTable StdACChrominance = new JPEGHuffmanTable(StdACChrominanceLengths, StdACChrominanceValues, false);
    private short[] lengths;
    private short[] values;

    public JPEGHuffmanTable(short[] sArr, short[] sArr2) {
        if (sArr == null || sArr2 == null || sArr.length == 0 || sArr2.length == 0 || sArr.length > 16 || sArr2.length > 256) {
            throw new IllegalArgumentException("Illegal lengths or values");
        }
        for (int i2 = 0; i2 < sArr.length; i2++) {
            if (sArr[i2] < 0) {
                throw new IllegalArgumentException("lengths[" + i2 + "] < 0");
            }
        }
        for (int i3 = 0; i3 < sArr2.length; i3++) {
            if (sArr2[i3] < 0) {
                throw new IllegalArgumentException("values[" + i3 + "] < 0");
            }
        }
        this.lengths = Arrays.copyOf(sArr, sArr.length);
        this.values = Arrays.copyOf(sArr2, sArr2.length);
        validate();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [int] */
    private void validate() {
        short s2 = 0;
        for (int i2 = 0; i2 < this.lengths.length; i2++) {
            s2 += this.lengths[i2];
        }
        if (s2 != this.values.length) {
            throw new IllegalArgumentException("lengths do not correspond to length of value table");
        }
    }

    private JPEGHuffmanTable(short[] sArr, short[] sArr2, boolean z2) {
        if (z2) {
            this.lengths = Arrays.copyOf(sArr, sArr.length);
            this.values = Arrays.copyOf(sArr2, sArr2.length);
        } else {
            this.lengths = sArr;
            this.values = sArr2;
        }
    }

    public short[] getLengths() {
        return Arrays.copyOf(this.lengths, this.lengths.length);
    }

    public short[] getValues() {
        return Arrays.copyOf(this.values, this.values.length);
    }

    public String toString() {
        String property = System.getProperty("line.separator", "\n");
        StringBuilder sb = new StringBuilder("JPEGHuffmanTable");
        sb.append(property).append("lengths:");
        for (int i2 = 0; i2 < this.lengths.length; i2++) {
            sb.append(" ").append((int) this.lengths[i2]);
        }
        sb.append(property).append("values:");
        for (int i3 = 0; i3 < this.values.length; i3++) {
            sb.append(" ").append((int) this.values[i3]);
        }
        return sb.toString();
    }
}
