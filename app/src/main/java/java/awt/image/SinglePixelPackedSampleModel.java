package java.awt.image;

import java.util.Arrays;

/* loaded from: rt.jar:java/awt/image/SinglePixelPackedSampleModel.class */
public class SinglePixelPackedSampleModel extends SampleModel {
    private int[] bitMasks;
    private int[] bitOffsets;
    private int[] bitSizes;
    private int maxBitSize;
    private int scanlineStride;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public SinglePixelPackedSampleModel(int i2, int i3, int i4, int[] iArr) {
        this(i2, i3, i4, i3, iArr);
        if (i2 != 0 && i2 != 1 && i2 != 3) {
            throw new IllegalArgumentException("Unsupported data type " + i2);
        }
    }

    public SinglePixelPackedSampleModel(int i2, int i3, int i4, int i5, int[] iArr) {
        super(i2, i3, i4, iArr.length);
        if (i2 != 0 && i2 != 1 && i2 != 3) {
            throw new IllegalArgumentException("Unsupported data type " + i2);
        }
        this.dataType = i2;
        this.bitMasks = (int[]) iArr.clone();
        this.scanlineStride = i5;
        this.bitOffsets = new int[this.numBands];
        this.bitSizes = new int[this.numBands];
        int dataTypeSize = (int) ((1 << DataBuffer.getDataTypeSize(i2)) - 1);
        this.maxBitSize = 0;
        for (int i6 = 0; i6 < this.numBands; i6++) {
            int i7 = 0;
            int i8 = 0;
            int[] iArr2 = this.bitMasks;
            int i9 = i6;
            iArr2[i9] = iArr2[i9] & dataTypeSize;
            int i10 = this.bitMasks[i6];
            if (i10 != 0) {
                while ((i10 & 1) == 0) {
                    i10 >>>= 1;
                    i7++;
                }
                while ((i10 & 1) == 1) {
                    i10 >>>= 1;
                    i8++;
                }
                if (i10 != 0) {
                    throw new IllegalArgumentException("Mask " + iArr[i6] + " must be contiguous");
                }
            }
            this.bitOffsets[i6] = i7;
            this.bitSizes[i6] = i8;
            if (i8 > this.maxBitSize) {
                this.maxBitSize = i8;
            }
        }
    }

    @Override // java.awt.image.SampleModel
    public int getNumDataElements() {
        return 1;
    }

    private long getBufferSize() {
        return (this.scanlineStride * (this.height - 1)) + this.width;
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        return new SinglePixelPackedSampleModel(this.dataType, i2, i3, this.bitMasks);
    }

    @Override // java.awt.image.SampleModel
    public DataBuffer createDataBuffer() {
        DataBuffer dataBufferInt = null;
        int bufferSize = (int) getBufferSize();
        switch (this.dataType) {
            case 0:
                dataBufferInt = new DataBufferByte(bufferSize);
                break;
            case 1:
                dataBufferInt = new DataBufferUShort(bufferSize);
                break;
            case 3:
                dataBufferInt = new DataBufferInt(bufferSize);
                break;
        }
        return dataBufferInt;
    }

    @Override // java.awt.image.SampleModel
    public int[] getSampleSize() {
        return (int[]) this.bitSizes.clone();
    }

    @Override // java.awt.image.SampleModel
    public int getSampleSize(int i2) {
        return this.bitSizes[i2];
    }

    public int getOffset(int i2, int i3) {
        return (i3 * this.scanlineStride) + i2;
    }

    public int[] getBitOffsets() {
        return (int[]) this.bitOffsets.clone();
    }

    public int[] getBitMasks() {
        return (int[]) this.bitMasks.clone();
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createSubsetSampleModel(int[] iArr) {
        if (iArr.length > this.numBands) {
            throw new RasterFormatException("There are only " + this.numBands + " bands");
        }
        int[] iArr2 = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr2[i2] = this.bitMasks[iArr[i2]];
        }
        return new SinglePixelPackedSampleModel(this.dataType, this.width, this.height, this.scanlineStride, iArr2);
    }

    @Override // java.awt.image.SampleModel
    public Object getDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        int[] iArr;
        short[] sArr;
        byte[] bArr;
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        switch (getTransferType()) {
            case 0:
                if (obj == null) {
                    bArr = new byte[1];
                } else {
                    bArr = (byte[]) obj;
                }
                bArr[0] = (byte) dataBuffer.getElem((i3 * this.scanlineStride) + i2);
                obj = bArr;
                break;
            case 1:
                if (obj == null) {
                    sArr = new short[1];
                } else {
                    sArr = (short[]) obj;
                }
                sArr[0] = (short) dataBuffer.getElem((i3 * this.scanlineStride) + i2);
                obj = sArr;
                break;
            case 3:
                if (obj == null) {
                    iArr = new int[1];
                } else {
                    iArr = (int[]) obj;
                }
                iArr[0] = dataBuffer.getElem((i3 * this.scanlineStride) + i2);
                obj = iArr;
                break;
        }
        return obj;
    }

    @Override // java.awt.image.SampleModel
    public int[] getPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr == null) {
            iArr2 = new int[this.numBands];
        } else {
            iArr2 = iArr;
        }
        int elem = dataBuffer.getElem((i3 * this.scanlineStride) + i2);
        for (int i4 = 0; i4 < this.numBands; i4++) {
            iArr2[i4] = (elem & this.bitMasks[i4]) >>> this.bitOffsets[i4];
        }
        return iArr2;
    }

    @Override // java.awt.image.SampleModel
    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i6 < 0 || i6 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i7 < 0 || i7 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5 * this.numBands];
        }
        int i8 = (i3 * this.scanlineStride) + i2;
        int i9 = 0;
        for (int i10 = 0; i10 < i5; i10++) {
            for (int i11 = 0; i11 < i4; i11++) {
                int elem = dataBuffer.getElem(i8 + i11);
                for (int i12 = 0; i12 < this.numBands; i12++) {
                    int i13 = i9;
                    i9++;
                    iArr2[i13] = (elem & this.bitMasks[i12]) >>> this.bitOffsets[i12];
                }
            }
            i8 += this.scanlineStride;
        }
        return iArr2;
    }

    @Override // java.awt.image.SampleModel
    public int getSample(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return (dataBuffer.getElem((i3 * this.scanlineStride) + i2) & this.bitMasks[i4]) >>> this.bitOffsets[i4];
    }

    @Override // java.awt.image.SampleModel
    public int[] getSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        if (i2 < 0 || i3 < 0 || i2 + i4 > this.width || i3 + i5 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5];
        }
        int i7 = (i3 * this.scanlineStride) + i2;
        int i8 = 0;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < i4; i10++) {
                int i11 = i8;
                i8++;
                iArr2[i11] = (dataBuffer.getElem(i7 + i10) & this.bitMasks[i6]) >>> this.bitOffsets[i6];
            }
            i7 += this.scanlineStride;
        }
        return iArr2;
    }

    @Override // java.awt.image.SampleModel
    public void setDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        switch (getTransferType()) {
            case 0:
                dataBuffer.setElem((i3 * this.scanlineStride) + i2, ((byte[]) obj)[0] & 255);
                return;
            case 1:
                dataBuffer.setElem((i3 * this.scanlineStride) + i2, ((short[]) obj)[0] & 65535);
                return;
            case 2:
            default:
                return;
            case 3:
                dataBuffer.setElem((i3 * this.scanlineStride) + i2, ((int[]) obj)[0]);
                return;
        }
    }

    @Override // java.awt.image.SampleModel
    public void setPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = (i3 * this.scanlineStride) + i2;
        int elem = dataBuffer.getElem(i4);
        for (int i5 = 0; i5 < this.numBands; i5++) {
            elem = (elem & (this.bitMasks[i5] ^ (-1))) | ((iArr[i5] << this.bitOffsets[i5]) & this.bitMasks[i5]);
        }
        dataBuffer.setElem(i4, elem);
    }

    @Override // java.awt.image.SampleModel
    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i6 < 0 || i6 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i7 < 0 || i7 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i8 = (i3 * this.scanlineStride) + i2;
        int i9 = 0;
        for (int i10 = 0; i10 < i5; i10++) {
            for (int i11 = 0; i11 < i4; i11++) {
                int elem = dataBuffer.getElem(i8 + i11);
                for (int i12 = 0; i12 < this.numBands; i12++) {
                    int i13 = i9;
                    i9++;
                    elem = (elem & (this.bitMasks[i12] ^ (-1))) | ((iArr[i13] << this.bitOffsets[i12]) & this.bitMasks[i12]);
                }
                dataBuffer.setElem(i8 + i11, elem);
            }
            i8 += this.scanlineStride;
        }
    }

    @Override // java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, int i5, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElem((i3 * this.scanlineStride) + i2, (dataBuffer.getElem((i3 * this.scanlineStride) + i2) & (this.bitMasks[i4] ^ (-1))) | ((i5 << this.bitOffsets[i4]) & this.bitMasks[i4]));
    }

    @Override // java.awt.image.SampleModel
    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 + i4 > this.width || i3 + i5 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = (i3 * this.scanlineStride) + i2;
        int i8 = 0;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < i4; i10++) {
                int i11 = i8;
                i8++;
                dataBuffer.setElem(i7 + i10, (dataBuffer.getElem(i7 + i10) & (this.bitMasks[i6] ^ (-1))) | ((iArr[i11] << this.bitOffsets[i6]) & this.bitMasks[i6]));
            }
            i7 += this.scanlineStride;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SinglePixelPackedSampleModel)) {
            return false;
        }
        SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) obj;
        return this.width == singlePixelPackedSampleModel.width && this.height == singlePixelPackedSampleModel.height && this.numBands == singlePixelPackedSampleModel.numBands && this.dataType == singlePixelPackedSampleModel.dataType && Arrays.equals(this.bitMasks, singlePixelPackedSampleModel.bitMasks) && Arrays.equals(this.bitOffsets, singlePixelPackedSampleModel.bitOffsets) && Arrays.equals(this.bitSizes, singlePixelPackedSampleModel.bitSizes) && this.maxBitSize == singlePixelPackedSampleModel.maxBitSize && this.scanlineStride == singlePixelPackedSampleModel.scanlineStride;
    }

    public int hashCode() {
        int i2 = ((((((this.width << 8) ^ this.height) << 8) ^ this.numBands) << 8) ^ this.dataType) << 8;
        for (int i3 = 0; i3 < this.bitMasks.length; i3++) {
            i2 = (i2 ^ this.bitMasks[i3]) << 8;
        }
        for (int i4 = 0; i4 < this.bitOffsets.length; i4++) {
            i2 = (i2 ^ this.bitOffsets[i4]) << 8;
        }
        for (int i5 = 0; i5 < this.bitSizes.length; i5++) {
            i2 = (i2 ^ this.bitSizes[i5]) << 8;
        }
        return ((i2 ^ this.maxBitSize) << 8) ^ this.scanlineStride;
    }
}
