package com.sun.java.util.jar.pack;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Histogram.class */
final class Histogram {
    protected final int[][] matrix;
    protected final int totalWeight;
    protected final int[] values;
    protected final int[] counts;
    private static final long LOW32 = 4294967295L;
    private static double log2;
    private final BitMetric bitMetric;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Histogram$BitMetric.class */
    public interface BitMetric {
        double getBitLength(int i2);
    }

    static {
        $assertionsDisabled = !Histogram.class.desiredAssertionStatus();
        log2 = Math.log(2.0d);
    }

    public Histogram(int[] iArr) {
        this.bitMetric = new BitMetric() { // from class: com.sun.java.util.jar.pack.Histogram.1
            @Override // com.sun.java.util.jar.pack.Histogram.BitMetric
            public double getBitLength(int i2) {
                return Histogram.this.getBitLength(i2);
            }
        };
        long[] jArrComputeHistogram2Col = computeHistogram2Col(maybeSort(iArr));
        int[][] iArrMakeTable = makeTable(jArrComputeHistogram2Col);
        this.values = iArrMakeTable[0];
        this.counts = iArrMakeTable[1];
        this.matrix = makeMatrix(jArrComputeHistogram2Col);
        this.totalWeight = iArr.length;
        if (!$assertionsDisabled && !assertWellFormed(iArr)) {
            throw new AssertionError();
        }
    }

    public Histogram(int[] iArr, int i2, int i3) {
        this(sortedSlice(iArr, i2, i3));
    }

    public Histogram(int[][] iArr) {
        this.bitMetric = new BitMetric() { // from class: com.sun.java.util.jar.pack.Histogram.1
            @Override // com.sun.java.util.jar.pack.Histogram.BitMetric
            public double getBitLength(int i2) {
                return Histogram.this.getBitLength(i2);
            }
        };
        int[][] iArrNormalizeMatrix = normalizeMatrix(iArr);
        this.matrix = iArrNormalizeMatrix;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < iArrNormalizeMatrix.length; i4++) {
            int length = iArrNormalizeMatrix[i4].length - 1;
            i2 += length;
            i3 += iArrNormalizeMatrix[i4][0] * length;
        }
        this.totalWeight = i3;
        long[] jArr = new long[i2];
        int i5 = 0;
        for (int i6 = 0; i6 < iArrNormalizeMatrix.length; i6++) {
            for (int i7 = 1; i7 < iArrNormalizeMatrix[i6].length; i7++) {
                int i8 = i5;
                i5++;
                jArr[i8] = (iArrNormalizeMatrix[i6][i7] << 32) | (4294967295L & iArrNormalizeMatrix[i6][0]);
            }
        }
        if (!$assertionsDisabled && i5 != jArr.length) {
            throw new AssertionError();
        }
        Arrays.sort(jArr);
        int[][] iArrMakeTable = makeTable(jArr);
        this.values = iArrMakeTable[1];
        this.counts = iArrMakeTable[0];
        if (!$assertionsDisabled && !assertWellFormed(null)) {
            throw new AssertionError();
        }
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public int getRowCount() {
        return this.matrix.length;
    }

    public int getRowFrequency(int i2) {
        return this.matrix[i2][0];
    }

    public int getRowLength(int i2) {
        return this.matrix[i2].length - 1;
    }

    public int getRowValue(int i2, int i3) {
        return this.matrix[i2][i3 + 1];
    }

    public int getRowWeight(int i2) {
        return getRowFrequency(i2) * getRowLength(i2);
    }

    public int getTotalWeight() {
        return this.totalWeight;
    }

    public int getTotalLength() {
        return this.values.length;
    }

    public int[] getAllValues() {
        return this.values;
    }

    public int[] getAllFrequencies() {
        return this.counts;
    }

    public int getFrequency(int i2) {
        int iBinarySearch = Arrays.binarySearch(this.values, i2);
        if (iBinarySearch < 0) {
            return 0;
        }
        if ($assertionsDisabled || this.values[iBinarySearch] == i2) {
            return this.counts[iBinarySearch];
        }
        throw new AssertionError();
    }

    public double getBitLength(int i2) {
        return (-Math.log(getFrequency(i2) / getTotalWeight())) / log2;
    }

    public double getRowBitLength(int i2) {
        return (-Math.log(getRowFrequency(i2) / getTotalWeight())) / log2;
    }

    public BitMetric getBitMetric() {
        return this.bitMetric;
    }

    public double getBitLength() {
        double rowBitLength = 0.0d;
        for (int i2 = 0; i2 < this.matrix.length; i2++) {
            rowBitLength += getRowBitLength(i2) * getRowWeight(i2);
        }
        if ($assertionsDisabled || 0.1d > Math.abs(rowBitLength - getBitLength(this.bitMetric))) {
            return rowBitLength;
        }
        throw new AssertionError();
    }

    public double getBitLength(BitMetric bitMetric) {
        double bitLength = 0.0d;
        for (int i2 = 0; i2 < this.matrix.length; i2++) {
            for (int i3 = 1; i3 < this.matrix[i2].length; i3++) {
                bitLength += this.matrix[i2][0] * bitMetric.getBitLength(this.matrix[i2][i3]);
            }
        }
        return bitLength;
    }

    private static double round(double d2, double d3) {
        return Math.round(d2 * d3) / d3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v24, types: [int[], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v60, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v66 */
    /* JADX WARN: Type inference failed for: r0v69 */
    /* JADX WARN: Type inference failed for: r0v74 */
    /* JADX WARN: Type inference failed for: r0v8, types: [int[]] */
    public int[][] normalizeMatrix(int[][] iArr) {
        int[] iArr2;
        int i2;
        long[] jArr = new long[iArr.length];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (iArr[i3].length > 1 && (i2 = iArr[i3][0]) > 0) {
                jArr[i3] = (i2 << 32) | i3;
            }
        }
        Arrays.sort(jArr);
        ?? r0 = new int[iArr.length];
        int i4 = -1;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            if (i7 < iArr.length) {
                long j2 = jArr[(jArr.length - i7) - 1];
                if (j2 == 0) {
                    continue;
                    i7++;
                } else {
                    iArr2 = iArr[(int) j2];
                    if (!$assertionsDisabled && (j2 >>> 32) != iArr2[0]) {
                        throw new AssertionError();
                    }
                }
            } else {
                iArr2 = new int[]{-1};
            }
            if (iArr2[0] != i4 && i6 > i5) {
                int length = 0;
                for (int i8 = i5; i8 < i6; i8++) {
                    ?? r02 = r0[i8];
                    if (!$assertionsDisabled && r02[0] != i4) {
                        throw new AssertionError();
                    }
                    length += r02.length - 1;
                }
                int[] iArr3 = new int[1 + length];
                iArr3[0] = i4;
                int length2 = 1;
                for (int i9 = i5; i9 < i6; i9++) {
                    ?? r03 = r0[i9];
                    if (!$assertionsDisabled && r03[0] != i4) {
                        throw new AssertionError();
                    }
                    System.arraycopy(r03, 1, iArr3, length2, r03.length - 1);
                    length2 += r03.length - 1;
                }
                if (!isSorted(iArr3, 1, true)) {
                    Arrays.sort(iArr3, 1, iArr3.length);
                    int i10 = 2;
                    for (int i11 = 2; i11 < iArr3.length; i11++) {
                        if (iArr3[i11] != iArr3[i11 - 1]) {
                            int i12 = i10;
                            i10++;
                            iArr3[i12] = iArr3[i11];
                        }
                    }
                    if (i10 < iArr3.length) {
                        int[] iArr4 = new int[i10];
                        System.arraycopy(iArr3, 0, iArr4, 0, i10);
                        iArr3 = iArr4;
                    }
                }
                int i13 = i5;
                i5++;
                r0[i13] = iArr3;
                i6 = i5;
            }
            if (i7 != iArr.length) {
                i4 = iArr2[0];
                int i14 = i6;
                i6++;
                r0[i14] = iArr2;
                i7++;
            } else {
                if (!$assertionsDisabled && i5 != i6) {
                    throw new AssertionError();
                }
                int[][] iArr5 = r0;
                if (i5 < iArr5.length) {
                    ?? r04 = new int[i5];
                    System.arraycopy(iArr5, 0, r04, 0, i5);
                    iArr5 = r04;
                }
                return iArr5;
            }
        }
    }

    public String[] getRowTitles(String str) {
        int totalLength = getTotalLength();
        int totalWeight = getTotalWeight();
        String[] strArr = new String[this.matrix.length];
        int rowWeight = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.matrix.length; i3++) {
            int rowFrequency = getRowFrequency(i3);
            int rowLength = getRowLength(i3);
            rowWeight += getRowWeight(i3);
            i2 += rowLength;
            long j2 = ((rowWeight * 100) + (totalWeight / 2)) / totalWeight;
            long j3 = ((i2 * 100) + (totalLength / 2)) / totalLength;
            double rowBitLength = getRowBitLength(i3);
            if (!$assertionsDisabled && 0.1d <= Math.abs(rowBitLength - getBitLength(this.matrix[i3][1]))) {
                throw new AssertionError();
            }
            strArr[i3] = str + "[" + i3 + "] len=" + round(rowBitLength, 10.0d) + " (" + rowFrequency + "*[" + rowLength + "]) (" + rowWeight + CallSiteDescriptor.TOKEN_DELIMITER + j2 + "%) [" + i2 + CallSiteDescriptor.TOKEN_DELIMITER + j3 + "%]";
        }
        return strArr;
    }

    public void print(PrintStream printStream) {
        print("hist", printStream);
    }

    public void print(String str, PrintStream printStream) {
        print(str, getRowTitles(str), printStream);
    }

    public void print(String str, String[] strArr, PrintStream printStream) {
        int totalLength = getTotalLength();
        int totalWeight = getTotalWeight();
        double bitLength = getBitLength();
        String str2 = str + " len=" + round(bitLength, 10.0d) + " avgLen=" + round(bitLength / totalWeight, 10.0d) + " weight(" + totalWeight + ") unique[" + totalLength + "] avgWeight(" + round(totalWeight / totalLength, 100.0d) + ")";
        if (strArr == null) {
            printStream.println(str2);
            return;
        }
        printStream.println(str2 + " {");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.matrix.length; i2++) {
            stringBuffer.setLength(0);
            stringBuffer.append(sun.security.pkcs11.wrapper.Constants.INDENT).append(strArr[i2]).append(" {");
            for (int i3 = 1; i3 < this.matrix[i2].length; i3++) {
                stringBuffer.append(" ").append(this.matrix[i2][i3]);
            }
            stringBuffer.append(" }");
            printStream.println(stringBuffer);
        }
        printStream.println("}");
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [int[], int[][]] */
    private static int[][] makeMatrix(long[] jArr) {
        Arrays.sort(jArr);
        int[] iArr = new int[jArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = (int) (jArr[i2] >>> 32);
        }
        long[] jArrComputeHistogram2Col = computeHistogram2Col(iArr);
        ?? r0 = new int[jArrComputeHistogram2Col.length];
        int i3 = 0;
        int i4 = 0;
        int length = r0.length;
        while (true) {
            length--;
            if (length >= 0) {
                int i5 = i4;
                i4++;
                long j2 = jArrComputeHistogram2Col[i5];
                int i6 = (int) j2;
                int i7 = (int) (j2 >>> 32);
                int[] iArr2 = new int[1 + i7];
                iArr2[0] = i6;
                for (int i8 = 0; i8 < i7; i8++) {
                    int i9 = i3;
                    i3++;
                    long j3 = jArr[i9];
                    if (!$assertionsDisabled && (j3 >>> 32) != i6) {
                        throw new AssertionError();
                    }
                    iArr2[1 + i8] = (int) j3;
                }
                r0[length] = iArr2;
            } else {
                if ($assertionsDisabled || i3 == jArr.length) {
                    return r0;
                }
                throw new AssertionError();
            }
        }
    }

    private static int[][] makeTable(long[] jArr) {
        int[][] iArr = new int[2][jArr.length];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            iArr[0][i2] = (int) jArr[i2];
            iArr[1][i2] = (int) (jArr[i2] >>> 32);
        }
        return iArr;
    }

    private static long[] computeHistogram2Col(int[] iArr) {
        int i2;
        switch (iArr.length) {
            case 0:
                return new long[0];
            case 1:
                return new long[]{EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH | (4294967295L & iArr[0])};
            default:
                long[] jArr = null;
                boolean z2 = true;
                while (true) {
                    boolean z3 = z2;
                    int i3 = -1;
                    int i4 = iArr[0] ^ (-1);
                    int i5 = 0;
                    for (int i6 = 0; i6 <= iArr.length; i6++) {
                        if (i6 < iArr.length) {
                            i2 = iArr[i6];
                        } else {
                            i2 = i4 ^ (-1);
                        }
                        if (i2 == i4) {
                            i5++;
                        } else {
                            if (!z3 && i5 != 0) {
                                jArr[i3] = (i5 << 32) | (4294967295L & i4);
                            }
                            i4 = i2;
                            i5 = 1;
                            i3++;
                        }
                    }
                    if (z3) {
                        jArr = new long[i3];
                        z2 = false;
                    } else {
                        return jArr;
                    }
                }
                break;
        }
    }

    /* JADX WARN: Type inference failed for: r0v19, types: [int[], int[][]] */
    private static int[][] regroupHistogram(int[][] iArr, int[] iArr2) {
        long length = 0;
        for (int[] iArr3 : iArr) {
            length += iArr3.length - 1;
        }
        long j2 = 0;
        for (int i2 : iArr2) {
            j2 += i2;
        }
        if (j2 > length) {
            int length2 = iArr2.length;
            long j3 = length;
            int i3 = 0;
            while (true) {
                if (i3 >= iArr2.length) {
                    break;
                }
                if (j3 < iArr2[i3]) {
                    int[] iArr4 = new int[i3 + 1];
                    System.arraycopy(iArr2, 0, iArr4, 0, i3 + 1);
                    iArr2 = iArr4;
                    iArr2[i3] = (int) j3;
                    break;
                }
                j3 -= iArr2[i3];
                i3++;
            }
        } else {
            int[] iArr5 = new int[iArr2.length + 1];
            System.arraycopy(iArr2, 0, iArr5, 0, iArr2.length);
            iArr5[iArr2.length] = (int) (length - j2);
            iArr2 = iArr5;
        }
        ?? r0 = new int[iArr2.length];
        int i4 = 0;
        int i5 = 1;
        int length3 = iArr[0].length;
        for (int i6 = 0; i6 < iArr2.length; i6++) {
            int i7 = iArr2[i6];
            int[] iArr6 = new int[1 + i7];
            long j4 = 0;
            r0[i6] = iArr6;
            int i8 = 1;
            while (true) {
                int i9 = i8;
                if (i9 < iArr6.length) {
                    int length4 = iArr6.length - i9;
                    while (i5 == length3) {
                        i5 = 1;
                        i4++;
                        length3 = iArr[i4].length;
                    }
                    if (length4 > length3 - i5) {
                        length4 = length3 - i5;
                    }
                    j4 += iArr[i4][0] * length4;
                    System.arraycopy(iArr[i4], length3 - length4, iArr6, i9, length4);
                    length3 -= length4;
                    i8 = i9 + length4;
                }
            }
            Arrays.sort(iArr6, 1, iArr6.length);
            iArr6[0] = (int) ((j4 + (i7 / 2)) / i7);
        }
        if (!$assertionsDisabled && i5 != length3) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || i4 == iArr.length - 1) {
            return r0;
        }
        throw new AssertionError();
    }

    public static Histogram makeByteHistogram(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[4096];
        int[] iArr = new int[256];
        while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 <= 0) {
                break;
            }
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = bArr[i3] & 255;
                iArr[i4] = iArr[i4] + 1;
            }
        }
        int[][] iArr2 = new int[256][2];
        for (int i5 = 0; i5 < iArr.length; i5++) {
            iArr2[i5][0] = iArr[i5];
            iArr2[i5][1] = i5;
        }
        return new Histogram(iArr2);
    }

    private static int[] sortedSlice(int[] iArr, int i2, int i3) {
        if (i2 == 0 && i3 == iArr.length && isSorted(iArr, 0, false)) {
            return iArr;
        }
        int[] iArr2 = new int[i3 - i2];
        System.arraycopy(iArr, i2, iArr2, 0, iArr2.length);
        Arrays.sort(iArr2);
        return iArr2;
    }

    private static boolean isSorted(int[] iArr, int i2, boolean z2) {
        for (int i3 = i2 + 1; i3 < iArr.length; i3++) {
            if (z2) {
                if (iArr[i3 - 1] >= iArr[i3]) {
                    return false;
                }
            } else if (iArr[i3 - 1] > iArr[i3]) {
                return false;
            }
        }
        return true;
    }

    private static int[] maybeSort(int[] iArr) {
        if (!isSorted(iArr, 0, false)) {
            iArr = (int[]) iArr.clone();
            Arrays.sort(iArr);
        }
        return iArr;
    }

    private boolean assertWellFormed(int[] iArr) {
        return true;
    }
}
