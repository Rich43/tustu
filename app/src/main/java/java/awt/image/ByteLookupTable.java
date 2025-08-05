package java.awt.image;

/* loaded from: rt.jar:java/awt/image/ByteLookupTable.class */
public class ByteLookupTable extends LookupTable {
    byte[][] data;

    /* JADX WARN: Type inference failed for: r1v8, types: [byte[], byte[][]] */
    public ByteLookupTable(int i2, byte[][] bArr) {
        super(i2, bArr.length);
        this.numComponents = bArr.length;
        this.numEntries = bArr[0].length;
        this.data = new byte[this.numComponents];
        for (int i3 = 0; i3 < this.numComponents; i3++) {
            this.data[i3] = bArr[i3];
        }
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [byte[], byte[][]] */
    public ByteLookupTable(int i2, byte[] bArr) {
        super(i2, bArr.length);
        this.numComponents = 1;
        this.numEntries = bArr.length;
        this.data = new byte[1];
        this.data[0] = bArr;
    }

    public final byte[][] getTable() {
        return this.data;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v19 */
    @Override // java.awt.image.LookupTable
    public int[] lookupPixel(int[] iArr, int[] iArr2) {
        if (iArr2 == null) {
            iArr2 = new int[iArr.length];
        }
        if (this.numComponents == 1) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = iArr[i2] - this.offset;
                if (i3 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i2 + "]-offset is less than zero");
                }
                iArr2[i2] = this.data[0][i3];
            }
        } else {
            for (int i4 = 0; i4 < iArr.length; i4++) {
                int i5 = iArr[i4] - this.offset;
                if (i5 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i4 + "]-offset is less than zero");
                }
                iArr2[i4] = this.data[i4][i5];
            }
        }
        return iArr2;
    }

    public byte[] lookupPixel(byte[] bArr, byte[] bArr2) {
        if (bArr2 == null) {
            bArr2 = new byte[bArr.length];
        }
        if (this.numComponents == 1) {
            for (int i2 = 0; i2 < bArr.length; i2++) {
                int i3 = (bArr[i2] & 255) - this.offset;
                if (i3 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i2 + "]-offset is less than zero");
                }
                bArr2[i2] = this.data[0][i3];
            }
        } else {
            for (int i4 = 0; i4 < bArr.length; i4++) {
                int i5 = (bArr[i4] & 255) - this.offset;
                if (i5 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i4 + "]-offset is less than zero");
                }
                bArr2[i4] = this.data[i4][i5];
            }
        }
        return bArr2;
    }
}
