package java.awt.image;

/* loaded from: rt.jar:java/awt/image/SampleModel.class */
public abstract class SampleModel {
    protected int width;
    protected int height;
    protected int numBands;
    protected int dataType;

    private static native void initIDs();

    public abstract int getNumDataElements();

    public abstract Object getDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer);

    public abstract void setDataElements(int i2, int i3, Object obj, DataBuffer dataBuffer);

    public abstract int getSample(int i2, int i3, int i4, DataBuffer dataBuffer);

    public abstract void setSample(int i2, int i3, int i4, int i5, DataBuffer dataBuffer);

    public abstract SampleModel createCompatibleSampleModel(int i2, int i3);

    public abstract SampleModel createSubsetSampleModel(int[] iArr);

    public abstract DataBuffer createDataBuffer();

    public abstract int[] getSampleSize();

    public abstract int getSampleSize(int i2);

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public SampleModel(int i2, int i3, int i4, int i5) {
        long j2 = i3 * i4;
        if (i3 <= 0 || i4 <= 0) {
            throw new IllegalArgumentException("Width (" + i3 + ") and height (" + i4 + ") must be > 0");
        }
        if (j2 >= 2147483647L) {
            throw new IllegalArgumentException("Dimensions (width=" + i3 + " height=" + i4 + ") are too large");
        }
        if (i2 < 0 || (i2 > 5 && i2 != 32)) {
            throw new IllegalArgumentException("Unsupported dataType: " + i2);
        }
        if (i5 <= 0) {
            throw new IllegalArgumentException("Number of bands must be > 0");
        }
        this.dataType = i2;
        this.width = i3;
        this.height = i4;
        this.numBands = i5;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getNumBands() {
        return this.numBands;
    }

    public final int getDataType() {
        return this.dataType;
    }

    public int getTransferType() {
        return this.dataType;
    }

    public int[] getPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[this.numBands];
        }
        for (int i4 = 0; i4 < this.numBands; i4++) {
            iArr2[i4] = getSample(i2, i3, i4, dataBuffer);
        }
        return iArr2;
    }

    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj, DataBuffer dataBuffer) {
        double[] dArr;
        float[] fArr;
        int[] iArr;
        short[] sArr;
        byte[] bArr;
        int transferType = getTransferType();
        int numDataElements = getNumDataElements();
        int i6 = 0;
        Object dataElements = null;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        switch (transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[numDataElements * i4 * i5];
                } else {
                    bArr = (byte[]) obj;
                }
                for (int i9 = i3; i9 < i8; i9++) {
                    for (int i10 = i2; i10 < i7; i10++) {
                        dataElements = getDataElements(i10, i9, dataElements, dataBuffer);
                        byte[] bArr2 = (byte[]) dataElements;
                        for (int i11 = 0; i11 < numDataElements; i11++) {
                            int i12 = i6;
                            i6++;
                            bArr[i12] = bArr2[i11];
                        }
                    }
                }
                obj = bArr;
                break;
            case 1:
            case 2:
                if (obj == null) {
                    sArr = new short[numDataElements * i4 * i5];
                } else {
                    sArr = (short[]) obj;
                }
                for (int i13 = i3; i13 < i8; i13++) {
                    for (int i14 = i2; i14 < i7; i14++) {
                        dataElements = getDataElements(i14, i13, dataElements, dataBuffer);
                        short[] sArr2 = (short[]) dataElements;
                        for (int i15 = 0; i15 < numDataElements; i15++) {
                            int i16 = i6;
                            i6++;
                            sArr[i16] = sArr2[i15];
                        }
                    }
                }
                obj = sArr;
                break;
            case 3:
                if (obj == null) {
                    iArr = new int[numDataElements * i4 * i5];
                } else {
                    iArr = (int[]) obj;
                }
                for (int i17 = i3; i17 < i8; i17++) {
                    for (int i18 = i2; i18 < i7; i18++) {
                        dataElements = getDataElements(i18, i17, dataElements, dataBuffer);
                        int[] iArr2 = (int[]) dataElements;
                        for (int i19 = 0; i19 < numDataElements; i19++) {
                            int i20 = i6;
                            i6++;
                            iArr[i20] = iArr2[i19];
                        }
                    }
                }
                obj = iArr;
                break;
            case 4:
                if (obj == null) {
                    fArr = new float[numDataElements * i4 * i5];
                } else {
                    fArr = (float[]) obj;
                }
                for (int i21 = i3; i21 < i8; i21++) {
                    for (int i22 = i2; i22 < i7; i22++) {
                        dataElements = getDataElements(i22, i21, dataElements, dataBuffer);
                        float[] fArr2 = (float[]) dataElements;
                        for (int i23 = 0; i23 < numDataElements; i23++) {
                            int i24 = i6;
                            i6++;
                            fArr[i24] = fArr2[i23];
                        }
                    }
                }
                obj = fArr;
                break;
            case 5:
                if (obj == null) {
                    dArr = new double[numDataElements * i4 * i5];
                } else {
                    dArr = (double[]) obj;
                }
                for (int i25 = i3; i25 < i8; i25++) {
                    for (int i26 = i2; i26 < i7; i26++) {
                        dataElements = getDataElements(i26, i25, dataElements, dataBuffer);
                        double[] dArr2 = (double[]) dataElements;
                        for (int i27 = 0; i27 < numDataElements; i27++) {
                            int i28 = i6;
                            i6++;
                            dArr[i28] = dArr2[i27];
                        }
                    }
                }
                obj = dArr;
                break;
        }
        return obj;
    }

    public void setDataElements(int i2, int i3, int i4, int i5, Object obj, DataBuffer dataBuffer) {
        int i6 = 0;
        int transferType = getTransferType();
        int numDataElements = getNumDataElements();
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        switch (transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                byte[] bArr2 = new byte[numDataElements];
                for (int i9 = i3; i9 < i8; i9++) {
                    for (int i10 = i2; i10 < i7; i10++) {
                        for (int i11 = 0; i11 < numDataElements; i11++) {
                            int i12 = i6;
                            i6++;
                            bArr2[i11] = bArr[i12];
                        }
                        setDataElements(i10, i9, bArr2, dataBuffer);
                    }
                }
                return;
            case 1:
            case 2:
                short[] sArr = (short[]) obj;
                short[] sArr2 = new short[numDataElements];
                for (int i13 = i3; i13 < i8; i13++) {
                    for (int i14 = i2; i14 < i7; i14++) {
                        for (int i15 = 0; i15 < numDataElements; i15++) {
                            int i16 = i6;
                            i6++;
                            sArr2[i15] = sArr[i16];
                        }
                        setDataElements(i14, i13, sArr2, dataBuffer);
                    }
                }
                return;
            case 3:
                int[] iArr = (int[]) obj;
                int[] iArr2 = new int[numDataElements];
                for (int i17 = i3; i17 < i8; i17++) {
                    for (int i18 = i2; i18 < i7; i18++) {
                        for (int i19 = 0; i19 < numDataElements; i19++) {
                            int i20 = i6;
                            i6++;
                            iArr2[i19] = iArr[i20];
                        }
                        setDataElements(i18, i17, iArr2, dataBuffer);
                    }
                }
                return;
            case 4:
                float[] fArr = (float[]) obj;
                float[] fArr2 = new float[numDataElements];
                for (int i21 = i3; i21 < i8; i21++) {
                    for (int i22 = i2; i22 < i7; i22++) {
                        for (int i23 = 0; i23 < numDataElements; i23++) {
                            int i24 = i6;
                            i6++;
                            fArr2[i23] = fArr[i24];
                        }
                        setDataElements(i22, i21, fArr2, dataBuffer);
                    }
                }
                return;
            case 5:
                double[] dArr = (double[]) obj;
                double[] dArr2 = new double[numDataElements];
                for (int i25 = i3; i25 < i8; i25++) {
                    for (int i26 = i2; i26 < i7; i26++) {
                        for (int i27 = 0; i27 < numDataElements; i27++) {
                            int i28 = i6;
                            i6++;
                            dArr2[i27] = dArr[i28];
                        }
                        setDataElements(i26, i25, dArr2, dataBuffer);
                    }
                }
                return;
            default:
                return;
        }
    }

    public float[] getPixel(int i2, int i3, float[] fArr, DataBuffer dataBuffer) {
        float[] fArr2;
        if (fArr != null) {
            fArr2 = fArr;
        } else {
            fArr2 = new float[this.numBands];
        }
        for (int i4 = 0; i4 < this.numBands; i4++) {
            fArr2[i4] = getSampleFloat(i2, i3, i4, dataBuffer);
        }
        return fArr2;
    }

    public double[] getPixel(int i2, int i3, double[] dArr, DataBuffer dataBuffer) {
        double[] dArr2;
        if (dArr != null) {
            dArr2 = dArr;
        } else {
            dArr2 = new double[this.numBands];
        }
        for (int i4 = 0; i4 < this.numBands; i4++) {
            dArr2[i4] = getSampleDouble(i2, i3, i4, dataBuffer);
        }
        return dArr2;
    }

    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[this.numBands * i4 * i5];
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    iArr2[i12] = getSample(i10, i9, i11, dataBuffer);
                }
            }
        }
        return iArr2;
    }

    public float[] getPixels(int i2, int i3, int i4, int i5, float[] fArr, DataBuffer dataBuffer) {
        float[] fArr2;
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        if (fArr != null) {
            fArr2 = fArr;
        } else {
            fArr2 = new float[this.numBands * i4 * i5];
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    fArr2[i12] = getSampleFloat(i10, i9, i11, dataBuffer);
                }
            }
        }
        return fArr2;
    }

    public double[] getPixels(int i2, int i3, int i4, int i5, double[] dArr, DataBuffer dataBuffer) {
        double[] dArr2;
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        if (dArr != null) {
            dArr2 = dArr;
        } else {
            dArr2 = new double[this.numBands * i4 * i5];
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    dArr2[i12] = getSampleDouble(i10, i9, i11, dataBuffer);
                }
            }
        }
        return dArr2;
    }

    public float getSampleFloat(int i2, int i3, int i4, DataBuffer dataBuffer) {
        return getSample(i2, i3, i4, dataBuffer);
    }

    public double getSampleDouble(int i2, int i3, int i4, DataBuffer dataBuffer) {
        return getSample(i2, i3, i4, dataBuffer);
    }

    public int[] getSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        int[] iArr2;
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i8 < i2 || i8 > this.width || i3 < 0 || i9 < i3 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5];
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                iArr2[i12] = getSample(i11, i10, i6, dataBuffer);
            }
        }
        return iArr2;
    }

    public float[] getSamples(int i2, int i3, int i4, int i5, int i6, float[] fArr, DataBuffer dataBuffer) {
        float[] fArr2;
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i8 < i2 || i8 > this.width || i3 < 0 || i9 < i3 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates");
        }
        if (fArr != null) {
            fArr2 = fArr;
        } else {
            fArr2 = new float[i4 * i5];
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                fArr2[i12] = getSampleFloat(i11, i10, i6, dataBuffer);
            }
        }
        return fArr2;
    }

    public double[] getSamples(int i2, int i3, int i4, int i5, int i6, double[] dArr, DataBuffer dataBuffer) {
        double[] dArr2;
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i8 < i2 || i8 > this.width || i3 < 0 || i9 < i3 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates");
        }
        if (dArr != null) {
            dArr2 = dArr;
        } else {
            dArr2 = new double[i4 * i5];
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                dArr2[i12] = getSampleDouble(i11, i10, i6, dataBuffer);
            }
        }
        return dArr2;
    }

    public void setPixel(int i2, int i3, int[] iArr, DataBuffer dataBuffer) {
        for (int i4 = 0; i4 < this.numBands; i4++) {
            setSample(i2, i3, i4, iArr[i4], dataBuffer);
        }
    }

    public void setPixel(int i2, int i3, float[] fArr, DataBuffer dataBuffer) {
        for (int i4 = 0; i4 < this.numBands; i4++) {
            setSample(i2, i3, i4, fArr[i4], dataBuffer);
        }
    }

    public void setPixel(int i2, int i3, double[] dArr, DataBuffer dataBuffer) {
        for (int i4 = 0; i4 < this.numBands; i4++) {
            setSample(i2, i3, i4, dArr[i4], dataBuffer);
        }
    }

    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr, DataBuffer dataBuffer) {
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    setSample(i10, i9, i11, iArr[i12], dataBuffer);
                }
            }
        }
    }

    public void setPixels(int i2, int i3, int i4, int i5, float[] fArr, DataBuffer dataBuffer) {
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    setSample(i10, i9, i11, fArr[i12], dataBuffer);
                }
            }
        }
    }

    public void setPixels(int i2, int i3, int i4, int i5, double[] dArr, DataBuffer dataBuffer) {
        int i6 = 0;
        int i7 = i2 + i4;
        int i8 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i7 < 0 || i7 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i8 < 0 || i8 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i9 = i3; i9 < i8; i9++) {
            for (int i10 = i2; i10 < i7; i10++) {
                for (int i11 = 0; i11 < this.numBands; i11++) {
                    int i12 = i6;
                    i6++;
                    setSample(i10, i9, i11, dArr[i12], dataBuffer);
                }
            }
        }
    }

    public void setSample(int i2, int i3, int i4, float f2, DataBuffer dataBuffer) {
        setSample(i2, i3, i4, (int) f2, dataBuffer);
    }

    public void setSample(int i2, int i3, int i4, double d2, DataBuffer dataBuffer) {
        setSample(i2, i3, i4, (int) d2, dataBuffer);
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr, DataBuffer dataBuffer) {
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i8 < 0 || i8 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i9 < 0 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                setSample(i11, i10, i6, iArr[i12], dataBuffer);
            }
        }
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, float[] fArr, DataBuffer dataBuffer) {
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i8 < 0 || i8 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i9 < 0 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                setSample(i11, i10, i6, fArr[i12], dataBuffer);
            }
        }
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, double[] dArr, DataBuffer dataBuffer) {
        int i7 = 0;
        int i8 = i2 + i4;
        int i9 = i3 + i5;
        if (i2 < 0 || i2 >= this.width || i4 > this.width || i8 < 0 || i8 > this.width || i3 < 0 || i3 >= this.height || i5 > this.height || i9 < 0 || i9 > this.height) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinates.");
        }
        for (int i10 = i3; i10 < i9; i10++) {
            for (int i11 = i2; i11 < i8; i11++) {
                int i12 = i7;
                i7++;
                setSample(i11, i10, i6, dArr[i12], dataBuffer);
            }
        }
    }
}
