package java.awt.image;

/* loaded from: rt.jar:java/awt/image/ShortLookupTable.class */
public class ShortLookupTable extends LookupTable {
    short[][] data;

    /* JADX WARN: Type inference failed for: r1v8, types: [short[], short[][]] */
    public ShortLookupTable(int i2, short[][] sArr) {
        super(i2, sArr.length);
        this.numComponents = sArr.length;
        this.numEntries = sArr[0].length;
        this.data = new short[this.numComponents];
        for (int i3 = 0; i3 < this.numComponents; i3++) {
            this.data[i3] = sArr[i3];
        }
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [short[], short[][]] */
    public ShortLookupTable(int i2, short[] sArr) {
        super(i2, sArr.length);
        this.numComponents = 1;
        this.numEntries = sArr.length;
        this.data = new short[1];
        this.data[0] = sArr;
    }

    public final short[][] getTable() {
        return this.data;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v21 */
    @Override // java.awt.image.LookupTable
    public int[] lookupPixel(int[] iArr, int[] iArr2) {
        if (iArr2 == null) {
            iArr2 = new int[iArr.length];
        }
        if (this.numComponents == 1) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = (iArr[i2] & 65535) - this.offset;
                if (i3 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i2 + "]-offset is less than zero");
                }
                iArr2[i2] = this.data[0][i3];
            }
        } else {
            for (int i4 = 0; i4 < iArr.length; i4++) {
                int i5 = (iArr[i4] & 65535) - this.offset;
                if (i5 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i4 + "]-offset is less than zero");
                }
                iArr2[i4] = this.data[i4][i5];
            }
        }
        return iArr2;
    }

    public short[] lookupPixel(short[] sArr, short[] sArr2) {
        if (sArr2 == null) {
            sArr2 = new short[sArr.length];
        }
        if (this.numComponents == 1) {
            for (int i2 = 0; i2 < sArr.length; i2++) {
                int i3 = (sArr[i2] & 65535) - this.offset;
                if (i3 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i2 + "]-offset is less than zero");
                }
                sArr2[i2] = this.data[0][i3];
            }
        } else {
            for (int i4 = 0; i4 < sArr.length; i4++) {
                int i5 = (sArr[i4] & 65535) - this.offset;
                if (i5 < 0) {
                    throw new ArrayIndexOutOfBoundsException("src[" + i4 + "]-offset is less than zero");
                }
                sArr2[i4] = this.data[i4][i5];
            }
        }
        return sArr2;
    }
}
