package java.awt.image;

/* loaded from: rt.jar:java/awt/image/MultiPixelPackedSampleModel.class */
public class MultiPixelPackedSampleModel extends SampleModel {
    int pixelBitStride;
    int bitMask;
    int pixelsPerDataElement;
    int dataElementSize;
    int dataBitOffset;
    int scanlineStride;

    public MultiPixelPackedSampleModel(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, (((i3 * i5) + DataBuffer.getDataTypeSize(i2)) - 1) / DataBuffer.getDataTypeSize(i2), 0);
        if (i2 != 0 && i2 != 1 && i2 != 3) {
            throw new IllegalArgumentException("Unsupported data type " + i2);
        }
    }

    public MultiPixelPackedSampleModel(int i2, int i3, int i4, int i5, int i6, int i7) {
        super(i2, i3, i4, 1);
        if (i2 != 0 && i2 != 1 && i2 != 3) {
            throw new IllegalArgumentException("Unsupported data type " + i2);
        }
        this.dataType = i2;
        this.pixelBitStride = i5;
        this.scanlineStride = i6;
        this.dataBitOffset = i7;
        this.dataElementSize = DataBuffer.getDataTypeSize(i2);
        this.pixelsPerDataElement = this.dataElementSize / i5;
        if (this.pixelsPerDataElement * i5 != this.dataElementSize) {
            throw new RasterFormatException("MultiPixelPackedSampleModel does not allow pixels to span data element boundaries");
        }
        this.bitMask = (1 << i5) - 1;
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        return new MultiPixelPackedSampleModel(this.dataType, i2, i3, this.pixelBitStride);
    }

    @Override // java.awt.image.SampleModel
    public DataBuffer createDataBuffer() {
        DataBuffer dataBufferInt = null;
        int i2 = this.scanlineStride * this.height;
        switch (this.dataType) {
            case 0:
                dataBufferInt = new DataBufferByte(i2 + ((this.dataBitOffset + 7) / 8));
                break;
            case 1:
                dataBufferInt = new DataBufferUShort(i2 + ((this.dataBitOffset + 15) / 16));
                break;
            case 3:
                dataBufferInt = new DataBufferInt(i2 + ((this.dataBitOffset + 31) / 32));
                break;
        }
        return dataBufferInt;
    }

    @Override // java.awt.image.SampleModel
    public int getNumDataElements() {
        return 1;
    }

    @Override // java.awt.image.SampleModel
    public int[] getSampleSize() {
        return new int[]{this.pixelBitStride};
    }

    @Override // java.awt.image.SampleModel
    public int getSampleSize(int i2) {
        return this.pixelBitStride;
    }

    public int getOffset(int i2, int i3) {
        return (i3 * this.scanlineStride) + (((i2 * this.pixelBitStride) + this.dataBitOffset) / this.dataElementSize);
    }

    public int getBitOffset(int i2) {
        return ((i2 * this.pixelBitStride) + this.dataBitOffset) % this.dataElementSize;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelBitStride() {
        return this.pixelBitStride;
    }

    public int getDataBitOffset() {
        return this.dataBitOffset;
    }

    @Override // java.awt.image.SampleModel
    public int getTransferType() {
        if (this.pixelBitStride > 16) {
            return 3;
        }
        if (this.pixelBitStride > 8) {
            return 1;
        }
        return 0;
    }

    @Override // java.awt.image.SampleModel
    public SampleModel createSubsetSampleModel(int[] iArr) {
        if (iArr != null && iArr.length != 1) {
            throw new RasterFormatException("MultiPixelPackedSampleModel has only one band.");
        }
        return createCompatibleSampleModel(this.width, this.height);
    }

    @Override // java.awt.image.SampleModel
    public int getSample(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height || i4 != 0) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i5 = this.dataBitOffset + (i2 * this.pixelBitStride);
        return (dataBuffer.getElem((i3 * this.scanlineStride) + (i5 / this.dataElementSize)) >> ((this.dataElementSize - (i5 & (this.dataElementSize - 1))) - this.pixelBitStride)) & this.bitMask;
    }

    @Override // java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, int i5, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height || i4 != 0) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = this.dataBitOffset + (i2 * this.pixelBitStride);
        int i7 = (i3 * this.scanlineStride) + (i6 / this.dataElementSize);
        int i8 = (this.dataElementSize - (i6 & (this.dataElementSize - 1))) - this.pixelBitStride;
        dataBuffer.setElem(i7, (dataBuffer.getElem(i7) & ((this.bitMask << i8) ^ (-1))) | ((i5 & this.bitMask) << i8));
    }

    @Override // java.awt.image.SampleModel
    public Object getDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        int[] iArr;
        short[] sArr;
        byte[] bArr;
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int transferType = getTransferType();
        int i4 = this.dataBitOffset + (i2 * this.pixelBitStride);
        int i5 = (this.dataElementSize - (i4 & (this.dataElementSize - 1))) - this.pixelBitStride;
        switch (transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[1];
                } else {
                    bArr = (byte[]) obj;
                }
                bArr[0] = (byte) ((dataBuffer.getElem((i3 * this.scanlineStride) + (i4 / this.dataElementSize)) >> i5) & this.bitMask);
                obj = bArr;
                break;
            case 1:
                if (obj == null) {
                    sArr = new short[1];
                } else {
                    sArr = (short[]) obj;
                }
                sArr[0] = (short) ((dataBuffer.getElem((i3 * this.scanlineStride) + (i4 / this.dataElementSize)) >> i5) & this.bitMask);
                obj = sArr;
                break;
            case 3:
                if (obj == null) {
                    iArr = new int[1];
                } else {
                    iArr = (int[]) obj;
                }
                iArr[0] = (dataBuffer.getElem((i3 * this.scanlineStride) + (i4 / this.dataElementSize)) >> i5) & this.bitMask;
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
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[this.numBands];
        }
        int i4 = this.dataBitOffset + (i2 * this.pixelBitStride);
        iArr2[0] = (dataBuffer.getElem((i3 * this.scanlineStride) + (i4 / this.dataElementSize)) >> ((this.dataElementSize - (i4 & (this.dataElementSize - 1))) - this.pixelBitStride)) & this.bitMask;
        return iArr2;
    }

    @Override // java.awt.image.SampleModel
    public void setDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int transferType = getTransferType();
        int i4 = this.dataBitOffset + (i2 * this.pixelBitStride);
        int i5 = (i3 * this.scanlineStride) + (i4 / this.dataElementSize);
        int i6 = (this.dataElementSize - (i4 & (this.dataElementSize - 1))) - this.pixelBitStride;
        int elem = dataBuffer.getElem(i5) & ((this.bitMask << i6) ^ (-1));
        switch (transferType) {
            case 0:
                dataBuffer.setElem(i5, elem | (((((byte[]) obj)[0] & 255) & this.bitMask) << i6));
                return;
            case 1:
                dataBuffer.setElem(i5, elem | (((((short[]) obj)[0] & 65535) & this.bitMask) << i6));
                return;
            case 2:
            default:
                return;
            case 3:
                dataBuffer.setElem(i5, elem | ((((int[]) obj)[0] & this.bitMask) << i6));
                return;
        }
    }

    @Override // java.awt.image.SampleModel
    public void setPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = this.dataBitOffset + (i2 * this.pixelBitStride);
        int i5 = (i3 * this.scanlineStride) + (i4 / this.dataElementSize);
        int i6 = (this.dataElementSize - (i4 & (this.dataElementSize - 1))) - this.pixelBitStride;
        dataBuffer.setElem(i5, (dataBuffer.getElem(i5) & ((this.bitMask << i6) ^ (-1))) | ((iArr[0] & this.bitMask) << i6));
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MultiPixelPackedSampleModel)) {
            return false;
        }
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) obj;
        return this.width == multiPixelPackedSampleModel.width && this.height == multiPixelPackedSampleModel.height && this.numBands == multiPixelPackedSampleModel.numBands && this.dataType == multiPixelPackedSampleModel.dataType && this.pixelBitStride == multiPixelPackedSampleModel.pixelBitStride && this.bitMask == multiPixelPackedSampleModel.bitMask && this.pixelsPerDataElement == multiPixelPackedSampleModel.pixelsPerDataElement && this.dataElementSize == multiPixelPackedSampleModel.dataElementSize && this.dataBitOffset == multiPixelPackedSampleModel.dataBitOffset && this.scanlineStride == multiPixelPackedSampleModel.scanlineStride;
    }

    public int hashCode() {
        return (((((((((((((((((this.width << 8) ^ this.height) << 8) ^ this.numBands) << 8) ^ this.dataType) << 8) ^ this.pixelBitStride) << 8) ^ this.bitMask) << 8) ^ this.pixelsPerDataElement) << 8) ^ this.dataElementSize) << 8) ^ this.dataBitOffset) << 8) ^ this.scanlineStride;
    }
}
