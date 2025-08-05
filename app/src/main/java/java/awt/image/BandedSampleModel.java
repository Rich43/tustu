package java.awt.image;

/* loaded from: rt.jar:java/awt/image/BandedSampleModel.class */
public final class BandedSampleModel extends ComponentSampleModel {
    public BandedSampleModel(int i2, int i3, int i4, int i5) {
        super(i2, i3, i4, 1, i3, createIndicesArray(i5), createOffsetArray(i5));
    }

    public BandedSampleModel(int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        super(i2, i3, i4, 1, i5, iArr, iArr2);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        int[] iArrOrderBands;
        if (this.numBanks == 1) {
            iArrOrderBands = orderBands(this.bandOffsets, i2 * i3);
        } else {
            iArrOrderBands = new int[this.bandOffsets.length];
        }
        return new BandedSampleModel(this.dataType, i2, i3, i2, this.bankIndices, iArrOrderBands);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
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
        return new BandedSampleModel(this.dataType, this.width, this.height, this.scanlineStride, iArr2, iArr3);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public DataBuffer createDataBuffer() {
        DataBuffer dataBufferDouble;
        int i2 = this.scanlineStride * this.height;
        switch (this.dataType) {
            case 0:
                dataBufferDouble = new DataBufferByte(i2, this.numBanks);
                break;
            case 1:
                dataBufferDouble = new DataBufferUShort(i2, this.numBanks);
                break;
            case 2:
                dataBufferDouble = new DataBufferShort(i2, this.numBanks);
                break;
            case 3:
                dataBufferDouble = new DataBufferInt(i2, this.numBanks);
                break;
            case 4:
                dataBufferDouble = new DataBufferFloat(i2, this.numBanks);
                break;
            case 5:
                dataBufferDouble = new DataBufferDouble(i2, this.numBanks);
                break;
            default:
                throw new IllegalArgumentException("dataType is not one of the supported types.");
        }
        return dataBufferDouble;
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
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
        int i4 = (i3 * this.scanlineStride) + i2;
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

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
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
        int i4 = (i3 * this.scanlineStride) + i2;
        for (int i5 = 0; i5 < this.numBands; i5++) {
            iArr2[i5] = dataBuffer.getElem(this.bankIndices[i5], i4 + this.bandOffsets[i5]);
        }
        return iArr2;
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
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
        for (int i8 = 0; i8 < this.numBands; i8++) {
            int i9 = (i3 * this.scanlineStride) + i2 + this.bandOffsets[i8];
            int i10 = i8;
            int i11 = this.bankIndices[i8];
            for (int i12 = 0; i12 < i5; i12++) {
                int i13 = i9;
                for (int i14 = 0; i14 < i4; i14++) {
                    int i15 = i13;
                    i13++;
                    iArr2[i10] = dataBuffer.getElem(i11, i15);
                    i10 += this.numBands;
                }
                i9 += this.scanlineStride;
            }
        }
        return iArr2;
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public int getSample(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return dataBuffer.getElem(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4]);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public float getSampleFloat(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return dataBuffer.getElemFloat(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4]);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public double getSampleDouble(int i2, int i3, int i4, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        return dataBuffer.getElemDouble(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4]);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
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
        int i7 = (i3 * this.scanlineStride) + i2 + this.bandOffsets[i6];
        int i8 = 0;
        int i9 = this.bankIndices[i6];
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            for (int i12 = 0; i12 < i4; i12++) {
                int i13 = i8;
                i8++;
                int i14 = i11;
                i11++;
                iArr2[i13] = dataBuffer.getElem(i9, i14);
            }
            i7 += this.scanlineStride;
        }
        return iArr2;
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int transferType = getTransferType();
        int numDataElements = getNumDataElements();
        int i4 = (i3 * this.scanlineStride) + i2;
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

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = (i3 * this.scanlineStride) + i2;
        for (int i5 = 0; i5 < this.numBands; i5++) {
            dataBuffer.setElem(this.bankIndices[i5], i4 + this.bandOffsets[i5], iArr[i5]);
        }
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i6 < 0 || i6 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i7 < 0 || i7 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        for (int i8 = 0; i8 < this.numBands; i8++) {
            int i9 = (i3 * this.scanlineStride) + i2 + this.bandOffsets[i8];
            int i10 = i8;
            int i11 = this.bankIndices[i8];
            for (int i12 = 0; i12 < i5; i12++) {
                int i13 = i9;
                for (int i14 = 0; i14 < i4; i14++) {
                    int i15 = i13;
                    i13++;
                    dataBuffer.setElem(i11, i15, iArr[i10]);
                    i10 += this.numBands;
                }
                i9 += this.scanlineStride;
            }
        }
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, int i5, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElem(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4], i5);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, float f2, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElemFloat(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4], f2);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setSample(int i2, int i3, int i4, double d2, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 >= this.width || i3 >= this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        dataBuffer.setElemDouble(this.bankIndices[i4], (i3 * this.scanlineStride) + i2 + this.bandOffsets[i4], d2);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        if (i2 < 0 || i3 < 0 || i2 + i4 > this.width || i3 + i5 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = (i3 * this.scanlineStride) + i2 + this.bandOffsets[i6];
        int i8 = 0;
        int i9 = this.bankIndices[i6];
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            for (int i12 = 0; i12 < i4; i12++) {
                int i13 = i11;
                i11++;
                int i14 = i8;
                i8++;
                dataBuffer.setElem(i9, i13, iArr[i14]);
            }
            i7 += this.scanlineStride;
        }
    }

    private static int[] createOffsetArray(int i2) {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = 0;
        }
        return iArr;
    }

    private static int[] createIndicesArray(int i2) {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = i3;
        }
        return iArr;
    }

    @Override // java.awt.image.ComponentSampleModel
    public int hashCode() {
        return super.hashCode() ^ 2;
    }
}
