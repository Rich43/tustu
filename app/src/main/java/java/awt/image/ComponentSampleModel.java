package java.awt.image;

import java.util.Arrays;

/* loaded from: rt.jar:java/awt/image/ComponentSampleModel.class */
public class ComponentSampleModel extends SampleModel {
    protected int[] bandOffsets;
    protected int[] bankIndices;
    protected int numBands;
    protected int numBanks;
    protected int scanlineStride;
    protected int pixelStride;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public ComponentSampleModel(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        super(i2, i3, i4, iArr.length);
        this.numBands = 1;
        this.numBanks = 1;
        this.dataType = i2;
        this.pixelStride = i5;
        this.scanlineStride = i6;
        this.bandOffsets = (int[]) iArr.clone();
        this.numBands = this.bandOffsets.length;
        if (i5 < 0) {
            throw new IllegalArgumentException("Pixel stride must be >= 0");
        }
        if (i6 < 0) {
            throw new IllegalArgumentException("Scanline stride must be >= 0");
        }
        if (this.numBands < 1) {
            throw new IllegalArgumentException("Must have at least one band.");
        }
        if (i2 < 0 || i2 > 5) {
            throw new IllegalArgumentException("Unsupported dataType.");
        }
        this.bankIndices = new int[this.numBands];
        for (int i7 = 0; i7 < this.numBands; i7++) {
            this.bankIndices[i7] = 0;
        }
        verify();
    }

    public ComponentSampleModel(int i2, int i3, int i4, int i5, int i6, int[] iArr, int[] iArr2) {
        super(i2, i3, i4, iArr2.length);
        this.numBands = 1;
        this.numBanks = 1;
        this.dataType = i2;
        this.pixelStride = i5;
        this.scanlineStride = i6;
        this.bandOffsets = (int[]) iArr2.clone();
        this.bankIndices = (int[]) iArr.clone();
        if (i5 < 0) {
            throw new IllegalArgumentException("Pixel stride must be >= 0");
        }
        if (i6 < 0) {
            throw new IllegalArgumentException("Scanline stride must be >= 0");
        }
        if (i2 < 0 || i2 > 5) {
            throw new IllegalArgumentException("Unsupported dataType.");
        }
        int i7 = this.bankIndices[0];
        if (i7 < 0) {
            throw new IllegalArgumentException("Index of bank 0 is less than 0 (" + i7 + ")");
        }
        for (int i8 = 1; i8 < this.bankIndices.length; i8++) {
            if (this.bankIndices[i8] > i7) {
                i7 = this.bankIndices[i8];
            } else if (this.bankIndices[i8] < 0) {
                throw new IllegalArgumentException("Index of bank " + i8 + " is less than 0 (" + i7 + ")");
            }
        }
        this.numBanks = i7 + 1;
        this.numBands = this.bandOffsets.length;
        if (this.bandOffsets.length != this.bankIndices.length) {
            throw new IllegalArgumentException("Length of bandOffsets must equal length of bankIndices.");
        }
        verify();
    }

    private void verify() {
        getBufferSize();
    }

    private int getBufferSize() {
        int iMax = this.bandOffsets[0];
        for (int i2 = 1; i2 < this.bandOffsets.length; i2++) {
            iMax = Math.max(iMax, this.bandOffsets[i2]);
        }
        if (iMax < 0 || iMax > 2147483646) {
            throw new IllegalArgumentException("Invalid band offset");
        }
        if (this.pixelStride < 0 || this.pixelStride > Integer.MAX_VALUE / this.width) {
            throw new IllegalArgumentException("Invalid pixel stride");
        }
        if (this.scanlineStride < 0 || this.scanlineStride > Integer.MAX_VALUE / this.height) {
            throw new IllegalArgumentException("Invalid scanline stride");
        }
        int i3 = iMax + 1;
        int i4 = this.pixelStride * (this.width - 1);
        if (i4 > Integer.MAX_VALUE - i3) {
            throw new IllegalArgumentException("Invalid pixel stride");
        }
        int i5 = i3 + i4;
        int i6 = this.scanlineStride * (this.height - 1);
        if (i6 > Integer.MAX_VALUE - i5) {
            throw new IllegalArgumentException("Invalid scan stride");
        }
        return i5 + i6;
    }

    int[] orderBands(int[] iArr, int i2) {
        int[] iArr2 = new int[iArr.length];
        int[] iArr3 = new int[iArr.length];
        for (int i3 = 0; i3 < iArr2.length; i3++) {
            iArr2[i3] = i3;
        }
        for (int i4 = 0; i4 < iArr3.length; i4++) {
            int i5 = i4;
            for (int i6 = i4 + 1; i6 < iArr3.length; i6++) {
                if (iArr[iArr2[i5]] > iArr[iArr2[i6]]) {
                    i5 = i6;
                }
            }
            iArr3[iArr2[i5]] = i4 * i2;
            iArr2[i5] = iArr2[i4];
        }
        return iArr3;
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        int[] iArrOrderBands;
        int iMin = this.bandOffsets[0];
        int iMax = this.bandOffsets[0];
        for (int i4 = 1; i4 < this.bandOffsets.length; i4++) {
            iMin = Math.min(iMin, this.bandOffsets[i4]);
            iMax = Math.max(iMax, this.bandOffsets[i4]);
        }
        int length = this.bandOffsets.length;
        int iAbs = Math.abs(this.pixelStride);
        int iAbs2 = Math.abs(this.scanlineStride);
        int iAbs3 = Math.abs(iMax - iMin);
        if (iAbs > iAbs2) {
            if (iAbs > iAbs3) {
                if (iAbs2 > iAbs3) {
                    iArrOrderBands = new int[this.bandOffsets.length];
                    for (int i5 = 0; i5 < length; i5++) {
                        iArrOrderBands[i5] = this.bandOffsets[i5] - iMin;
                    }
                    iAbs2 = iAbs3 + 1;
                    iAbs = iAbs2 * i3;
                } else {
                    iArrOrderBands = orderBands(this.bandOffsets, iAbs2 * i3);
                    iAbs = length * iAbs2 * i3;
                }
            } else {
                iAbs = iAbs2 * i3;
                iArrOrderBands = orderBands(this.bandOffsets, iAbs * i2);
            }
        } else if (iAbs > iAbs3) {
            iArrOrderBands = new int[this.bandOffsets.length];
            for (int i6 = 0; i6 < length; i6++) {
                iArrOrderBands[i6] = this.bandOffsets[i6] - iMin;
            }
            iAbs = iAbs3 + 1;
            iAbs2 = iAbs * i2;
        } else if (iAbs2 > iAbs3) {
            iArrOrderBands = orderBands(this.bandOffsets, iAbs * i2);
            iAbs2 = length * iAbs * i2;
        } else {
            iAbs2 = iAbs * i2;
            iArrOrderBands = orderBands(this.bandOffsets, iAbs2 * i3);
        }
        int i7 = 0;
        if (this.scanlineStride < 0) {
            i7 = 0 + (iAbs2 * i3);
            iAbs2 *= -1;
        }
        if (this.pixelStride < 0) {
            i7 += iAbs * i2;
            iAbs *= -1;
        }
        for (int i8 = 0; i8 < length; i8++) {
            int[] iArr = iArrOrderBands;
            int i9 = i8;
            iArr[i9] = iArr[i9] + i7;
        }
        return new ComponentSampleModel(this.dataType, i2, i3, iAbs, iAbs2, this.bankIndices, iArrOrderBands);
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createSubsetSampleModel(int[] iArr) {
        if (iArr.length > this.bankIndices.length) {
            throw new RasterFormatException("There are only " + this.bankIndices.length + " bands");
        }
        int[] iArr2 = new int[iArr.length];
        int[] iArr3 = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr2[i2] = this.bankIndices[iArr[i2]];
            iArr3[i2] = this.bandOffsets[iArr[i2]];
        }
        return new ComponentSampleModel(this.dataType, this.width, this.height, this.pixelStride, this.scanlineStride, iArr2, iArr3);
    }

    @Override // java.awt.image.SampleModel
    public DataBuffer createDataBuffer() {
        DataBuffer dataBufferDouble = null;
        int bufferSize = getBufferSize();
        switch (this.dataType) {
            case 0:
                dataBufferDouble = new DataBufferByte(bufferSize, this.numBanks);
                break;
            case 1:
                dataBufferDouble = new DataBufferUShort(bufferSize, this.numBanks);
                break;
            case 2:
                dataBufferDouble = new DataBufferShort(bufferSize, this.numBanks);
                break;
            case 3:
                dataBufferDouble = new DataBufferInt(bufferSize, this.numBanks);
                break;
            case 4:
                dataBufferDouble = new DataBufferFloat(bufferSize, this.numBanks);
                break;
            case 5:
                dataBufferDouble = new DataBufferDouble(bufferSize, this.numBanks);
                break;
        }
        return dataBufferDouble;
    }

    public int getOffset(int i2, int i3) {
        return (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[0];
    }

    public int getOffset(int i2, int i3, int i4) {
        return (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4];
    }

    @Override // java.awt.image.SampleModel
    public final int[] getSampleSize() {
        int[] iArr = new int[this.numBands];
        int sampleSize = getSampleSize(0);
        for (int i2 = 0; i2 < this.numBands; i2++) {
            iArr[i2] = sampleSize;
        }
        return iArr;
    }

    @Override // java.awt.image.SampleModel
    public final int getSampleSize(int i2) {
        return DataBuffer.getDataTypeSize(this.dataType);
    }

    public final int[] getBankIndices() {
        return (int[]) this.bankIndices.clone();
    }

    public final int[] getBandOffsets() {
        return (int[]) this.bandOffsets.clone();
    }

    public final int getScanlineStride() {
        return this.scanlineStride;
    }

    public final int getPixelStride() {
        return this.pixelStride;
    }

    @Override // java.awt.image.SampleModel
    public final int getNumDataElements() {
        return getNumBands();
    }

    @Override // java.awt.image.SampleModel
    public Object getDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        double[] dArr;
        float[] fArr;
        int[] iArr;
        short[] sArr;
        byte[] bArr;
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int transferType = getTransferType();
        int numDataElements = getNumDataElements();
        int i4 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        switch (transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[numDataElements];
                } else {
                    bArr = (byte[]) obj;
                }
                for (int i5 = 0; i5 < numDataElements; i5++) {
                    bArr[i5] = (byte) dataBuffer.getElem(this.bankIndices[i5], i4 + this.bandOffsets[i5]);
                }
                obj = bArr;
                break;
            case 1:
            case 2:
                if (obj == null) {
                    sArr = new short[numDataElements];
                } else {
                    sArr = (short[]) obj;
                }
                for (int i6 = 0; i6 < numDataElements; i6++) {
                    sArr[i6] = (short) dataBuffer.getElem(this.bankIndices[i6], i4 + this.bandOffsets[i6]);
                }
                obj = sArr;
                break;
            case 3:
                if (obj == null) {
                    iArr = new int[numDataElements];
                } else {
                    iArr = (int[]) obj;
                }
                for (int i7 = 0; i7 < numDataElements; i7++) {
                    iArr[i7] = dataBuffer.getElem(this.bankIndices[i7], i4 + this.bandOffsets[i7]);
                }
                obj = iArr;
                break;
            case 4:
                if (obj == null) {
                    fArr = new float[numDataElements];
                } else {
                    fArr = (float[]) obj;
                }
                for (int i8 = 0; i8 < numDataElements; i8++) {
                    fArr[i8] = dataBuffer.getElemFloat(this.bankIndices[i8], i4 + this.bandOffsets[i8]);
                }
                obj = fArr;
                break;
            case 5:
                if (obj == null) {
                    dArr = new double[numDataElements];
                } else {
                    dArr = (double[]) obj;
                }
                for (int i9 = 0; i9 < numDataElements; i9++) {
                    dArr[i9] = dataBuffer.getElemDouble(this.bankIndices[i9], i4 + this.bandOffsets[i9]);
                }
                obj = dArr;
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
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[this.numBands];
        }
        int i4 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        for (int i5 = 0; i5 < this.numBands; i5++) {
            iArr2[i5] = dataBuffer.getElem(this.bankIndices[i5], i4 + this.bandOffsets[i5]);
        }
        return iArr2;
    }

    @Override // java.awt.image.SampleModel
    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i6 < 0 || i6 > this.width || i3 < 0 || i3 >= this.height || i3 > this.height || i7 < 0 || i7 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5 * this.numBands];
        }
        int i8 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i9 = 0;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i8;
            for (int i12 = 0; i12 < i4; i12++) {
                for (int i13 = 0; i13 < this.numBands; i13++) {
                    int i14 = i9;
                    i9++;
                    iArr2[i14] = dataBuffer.getElem(this.bankIndices[i13], i11 + this.bandOffsets[i13]);
                }
                i11 += this.pixelStride;
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
        return dataBuffer.getElem(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4]);
    }

    @Override // java.awt.image.SampleModel
    public float getSampleFloat(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return dataBuffer.getElemFloat(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4]);
    }

    @Override // java.awt.image.SampleModel
    public double getSampleDouble(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return dataBuffer.getElemDouble(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4]);
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
        int i7 = (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i6];
        int i8 = 0;
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = i7;
            for (int i11 = 0; i11 < i4; i11++) {
                int i12 = i8;
                i8++;
                iArr2[i12] = dataBuffer.getElem(this.bankIndices[i6], i10);
                i10 += this.pixelStride;
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
        int transferType = getTransferType();
        int numDataElements = getNumDataElements();
        int i4 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        switch (transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                for (int i5 = 0; i5 < numDataElements; i5++) {
                    dataBuffer.setElem(this.bankIndices[i5], i4 + this.bandOffsets[i5], bArr[i5] & 255);
                }
                return;
            case 1:
            case 2:
                short[] sArr = (short[]) obj;
                for (int i6 = 0; i6 < numDataElements; i6++) {
                    dataBuffer.setElem(this.bankIndices[i6], i4 + this.bandOffsets[i6], sArr[i6] & 65535);
                }
                return;
            case 3:
                int[] iArr = (int[]) obj;
                for (int i7 = 0; i7 < numDataElements; i7++) {
                    dataBuffer.setElem(this.bankIndices[i7], i4 + this.bandOffsets[i7], iArr[i7]);
                }
                return;
            case 4:
                float[] fArr = (float[]) obj;
                for (int i8 = 0; i8 < numDataElements; i8++) {
                    dataBuffer.setElemFloat(this.bankIndices[i8], i4 + this.bandOffsets[i8], fArr[i8]);
                }
                return;
            case 5:
                double[] dArr = (double[]) obj;
                for (int i9 = 0; i9 < numDataElements; i9++) {
                    dataBuffer.setElemDouble(this.bankIndices[i9], i4 + this.bandOffsets[i9], dArr[i9]);
                }
                return;
            default:
                return;
        }
    }

    @Override // java.awt.image.SampleModel
    public void setPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        for (int i5 = 0; i5 < this.numBands; i5++) {
            dataBuffer.setElem(this.bankIndices[i5], i4 + this.bandOffsets[i5], iArr[i5]);
        }
    }

    @Override // java.awt.image.SampleModel
    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i6 < 0 || i6 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i7 < 0 || i7 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i8 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i9 = 0;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i8;
            for (int i12 = 0; i12 < i4; i12++) {
                for (int i13 = 0; i13 < this.numBands; i13++) {
                    int i14 = i9;
                    i9++;
                    dataBuffer.setElem(this.bankIndices[i13], i11 + this.bandOffsets[i13], iArr[i14]);
                }
                i11 += this.pixelStride;
            }
            i8 += this.scanlineStride;
        }
    }

    @Override // java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, int i5, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElem(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4], i5);
    }

    @Override // java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, float f2, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElemFloat(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4], f2);
    }

    @Override // java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, double d2, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElemDouble(this.bankIndices[i4], (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i4], d2);
    }

    @Override // java.awt.image.SampleModel
    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 + i4 > this.width || i3 + i5 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = (i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.bandOffsets[i6];
        int i8 = 0;
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = i7;
            for (int i11 = 0; i11 < i4; i11++) {
                int i12 = i8;
                i8++;
                dataBuffer.setElem(this.bankIndices[i6], i10, iArr[i12]);
                i10 += this.pixelStride;
            }
            i7 += this.scanlineStride;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ComponentSampleModel)) {
            return false;
        }
        ComponentSampleModel componentSampleModel = (ComponentSampleModel) obj;
        return this.width == componentSampleModel.width && this.height == componentSampleModel.height && this.numBands == componentSampleModel.numBands && this.dataType == componentSampleModel.dataType && Arrays.equals(this.bandOffsets, componentSampleModel.bandOffsets) && Arrays.equals(this.bankIndices, componentSampleModel.bankIndices) && this.numBands == componentSampleModel.numBands && this.numBanks == componentSampleModel.numBanks && this.scanlineStride == componentSampleModel.scanlineStride && this.pixelStride == componentSampleModel.pixelStride;
    }

    public int hashCode() {
        int i2 = ((((((this.width << 8) ^ this.height) << 8) ^ this.numBands) << 8) ^ this.dataType) << 8;
        for (int i3 = 0; i3 < this.bandOffsets.length; i3++) {
            i2 = (i2 ^ this.bandOffsets[i3]) << 8;
        }
        for (int i4 = 0; i4 < this.bankIndices.length; i4++) {
            i2 = (i2 ^ this.bankIndices[i4]) << 8;
        }
        return ((((((i2 ^ this.numBands) << 8) ^ this.numBanks) << 8) ^ this.scanlineStride) << 8) ^ this.pixelStride;
    }
}
