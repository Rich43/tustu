package java.awt.image;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.util.Arrays;

/* loaded from: rt.jar:java/awt/image/DirectColorModel.class */
public class DirectColorModel extends PackedColorModel {
    private int red_mask;
    private int green_mask;
    private int blue_mask;
    private int alpha_mask;
    private int red_offset;
    private int green_offset;
    private int blue_offset;
    private int alpha_offset;
    private int red_scale;
    private int green_scale;
    private int blue_scale;
    private int alpha_scale;
    private boolean is_LinearRGB;
    private int lRGBprecision;
    private byte[] tosRGB8LUT;
    private byte[] fromsRGB8LUT8;
    private short[] fromsRGB8LUT16;

    public DirectColorModel(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, 0);
    }

    public DirectColorModel(int i2, int i3, int i4, int i5, int i6) {
        super(ColorSpace.getInstance(1000), i2, i3, i4, i5, i6, false, i6 == 0 ? 1 : 3, ColorModel.getDefaultTransferType(i2));
        setFields();
    }

    public DirectColorModel(ColorSpace colorSpace, int i2, int i3, int i4, int i5, int i6, boolean z2, int i7) {
        super(colorSpace, i2, i3, i4, i5, i6, z2, i6 == 0 ? 1 : 3, i7);
        if (ColorModel.isLinearRGBspace(this.colorSpace)) {
            this.is_LinearRGB = true;
            if (this.maxBits <= 8) {
                this.lRGBprecision = 8;
                this.tosRGB8LUT = ColorModel.getLinearRGB8TosRGB8LUT();
                this.fromsRGB8LUT8 = ColorModel.getsRGB8ToLinearRGB8LUT();
            } else {
                this.lRGBprecision = 16;
                this.tosRGB8LUT = ColorModel.getLinearRGB16TosRGB8LUT();
                this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
            }
        } else if (!this.is_sRGB) {
            for (int i8 = 0; i8 < 3; i8++) {
                if (colorSpace.getMinValue(i8) != 0.0f || colorSpace.getMaxValue(i8) != 1.0f) {
                    throw new IllegalArgumentException("Illegal min/max RGB component value");
                }
            }
        }
        setFields();
    }

    public final int getRedMask() {
        return this.maskArray[0];
    }

    public final int getGreenMask() {
        return this.maskArray[1];
    }

    public final int getBlueMask() {
        return this.maskArray[2];
    }

    public final int getAlphaMask() {
        if (this.supportsAlpha) {
            return this.maskArray[3];
        }
        return 0;
    }

    private float[] getDefaultRGBComponents(int i2) {
        return this.colorSpace.toRGB(getNormalizedComponents(getComponents(i2, (int[]) null, 0), 0, null, 0));
    }

    private int getsRGBComponentFromsRGB(int i2, int i3) {
        int i4 = (i2 & this.maskArray[i3]) >>> this.maskOffsets[i3];
        if (this.isAlphaPremultiplied) {
            int i5 = (i2 & this.maskArray[3]) >>> this.maskOffsets[3];
            i4 = i5 == 0 ? 0 : (int) ((((i4 * this.scaleFactors[i3]) * 255.0f) / (i5 * this.scaleFactors[3])) + 0.5f);
        } else if (this.scaleFactors[i3] != 1.0f) {
            i4 = (int) ((i4 * this.scaleFactors[i3]) + 0.5f);
        }
        return i4;
    }

    private int getsRGBComponentFromLinearRGB(int i2, int i3) {
        int i4 = (i2 & this.maskArray[i3]) >>> this.maskOffsets[i3];
        if (this.isAlphaPremultiplied) {
            float f2 = (1 << this.lRGBprecision) - 1;
            int i5 = (i2 & this.maskArray[3]) >>> this.maskOffsets[3];
            i4 = i5 == 0 ? 0 : (int) ((((i4 * this.scaleFactors[i3]) * f2) / (i5 * this.scaleFactors[3])) + 0.5f);
        } else if (this.nBits[i3] != this.lRGBprecision) {
            if (this.lRGBprecision == 16) {
                i4 = (int) ((i4 * this.scaleFactors[i3] * 257.0f) + 0.5f);
            } else {
                i4 = (int) ((i4 * this.scaleFactors[i3]) + 0.5f);
            }
        }
        return this.tosRGB8LUT[i4] & 255;
    }

    @Override // java.awt.image.ColorModel
    public final int getRed(int i2) {
        if (this.is_sRGB) {
            return getsRGBComponentFromsRGB(i2, 0);
        }
        if (this.is_LinearRGB) {
            return getsRGBComponentFromLinearRGB(i2, 0);
        }
        return (int) ((getDefaultRGBComponents(i2)[0] * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public final int getGreen(int i2) {
        if (this.is_sRGB) {
            return getsRGBComponentFromsRGB(i2, 1);
        }
        if (this.is_LinearRGB) {
            return getsRGBComponentFromLinearRGB(i2, 1);
        }
        return (int) ((getDefaultRGBComponents(i2)[1] * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public final int getBlue(int i2) {
        if (this.is_sRGB) {
            return getsRGBComponentFromsRGB(i2, 2);
        }
        if (this.is_LinearRGB) {
            return getsRGBComponentFromLinearRGB(i2, 2);
        }
        return (int) ((getDefaultRGBComponents(i2)[2] * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public final int getAlpha(int i2) {
        if (!this.supportsAlpha) {
            return 255;
        }
        int i3 = (i2 & this.maskArray[3]) >>> this.maskOffsets[3];
        if (this.scaleFactors[3] != 1.0f) {
            i3 = (int) ((i3 * this.scaleFactors[3]) + 0.5f);
        }
        return i3;
    }

    @Override // java.awt.image.ColorModel
    public final int getRGB(int i2) {
        if (this.is_sRGB || this.is_LinearRGB) {
            return (getAlpha(i2) << 24) | (getRed(i2) << 16) | (getGreen(i2) << 8) | (getBlue(i2) << 0);
        }
        float[] defaultRGBComponents = getDefaultRGBComponents(i2);
        return (getAlpha(i2) << 24) | (((int) ((defaultRGBComponents[0] * 255.0f) + 0.5f)) << 16) | (((int) ((defaultRGBComponents[1] * 255.0f) + 0.5f)) << 8) | (((int) ((defaultRGBComponents[2] * 255.0f) + 0.5f)) << 0);
    }

    @Override // java.awt.image.ColorModel
    public int getRed(Object obj) {
        int i2;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i2 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i2 = ((int[]) obj)[0];
                break;
        }
        return getRed(i2);
    }

    @Override // java.awt.image.ColorModel
    public int getGreen(Object obj) {
        int i2;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i2 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i2 = ((int[]) obj)[0];
                break;
        }
        return getGreen(i2);
    }

    @Override // java.awt.image.ColorModel
    public int getBlue(Object obj) {
        int i2;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i2 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i2 = ((int[]) obj)[0];
                break;
        }
        return getBlue(i2);
    }

    @Override // java.awt.image.ColorModel
    public int getAlpha(Object obj) {
        int i2;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i2 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i2 = ((int[]) obj)[0];
                break;
        }
        return getAlpha(i2);
    }

    @Override // java.awt.image.ColorModel
    public int getRGB(Object obj) {
        int i2;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i2 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i2 = ((int[]) obj)[0];
                break;
        }
        return getRGB(i2);
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(int i2, Object obj) {
        int[] iArr;
        int i3;
        float f2;
        short[] sArr;
        byte[] bArr;
        if (this.transferType == 3 && obj != null) {
            iArr = (int[]) obj;
            iArr[0] = 0;
        } else {
            iArr = new int[1];
        }
        ColorModel rGBdefault = ColorModel.getRGBdefault();
        if (this == rGBdefault || equals(rGBdefault)) {
            iArr[0] = i2;
            return iArr;
        }
        int i4 = (i2 >> 16) & 255;
        int i5 = (i2 >> 8) & 255;
        int i6 = i2 & 255;
        if (this.is_sRGB || this.is_LinearRGB) {
            if (this.is_LinearRGB) {
                if (this.lRGBprecision == 8) {
                    i4 = this.fromsRGB8LUT8[i4] & 255;
                    i5 = this.fromsRGB8LUT8[i5] & 255;
                    i6 = this.fromsRGB8LUT8[i6] & 255;
                    i3 = 8;
                    f2 = 0.003921569f;
                } else {
                    i4 = this.fromsRGB8LUT16[i4] & 65535;
                    i5 = this.fromsRGB8LUT16[i5] & 65535;
                    i6 = this.fromsRGB8LUT16[i6] & 65535;
                    i3 = 16;
                    f2 = 1.5259022E-5f;
                }
            } else {
                i3 = 8;
                f2 = 0.003921569f;
            }
            if (this.supportsAlpha) {
                int i7 = (i2 >> 24) & 255;
                if (this.isAlphaPremultiplied) {
                    f2 *= i7 * 0.003921569f;
                    i3 = -1;
                }
                if (this.nBits[3] != 8) {
                    i7 = (int) ((i7 * 0.003921569f * ((1 << this.nBits[3]) - 1)) + 0.5f);
                    if (i7 > (1 << this.nBits[3]) - 1) {
                        i7 = (1 << this.nBits[3]) - 1;
                    }
                }
                iArr[0] = i7 << this.maskOffsets[3];
            }
            if (this.nBits[0] != i3) {
                i4 = (int) ((i4 * f2 * ((1 << this.nBits[0]) - 1)) + 0.5f);
            }
            if (this.nBits[1] != i3) {
                i5 = (int) ((i5 * f2 * ((1 << this.nBits[1]) - 1)) + 0.5f);
            }
            if (this.nBits[2] != i3) {
                i6 = (int) ((i6 * f2 * ((1 << this.nBits[2]) - 1)) + 0.5f);
            }
        } else {
            float[] fArrFromRGB = this.colorSpace.fromRGB(new float[]{i4 * 0.003921569f, i5 * 0.003921569f, i6 * 0.003921569f});
            if (this.supportsAlpha) {
                int i8 = (i2 >> 24) & 255;
                if (this.isAlphaPremultiplied) {
                    float f3 = 0.003921569f * i8;
                    for (int i9 = 0; i9 < 3; i9++) {
                        int i10 = i9;
                        fArrFromRGB[i10] = fArrFromRGB[i10] * f3;
                    }
                }
                if (this.nBits[3] != 8) {
                    i8 = (int) ((i8 * 0.003921569f * ((1 << this.nBits[3]) - 1)) + 0.5f);
                    if (i8 > (1 << this.nBits[3]) - 1) {
                        i8 = (1 << this.nBits[3]) - 1;
                    }
                }
                iArr[0] = i8 << this.maskOffsets[3];
            }
            i4 = (int) ((fArrFromRGB[0] * ((1 << this.nBits[0]) - 1)) + 0.5f);
            i5 = (int) ((fArrFromRGB[1] * ((1 << this.nBits[1]) - 1)) + 0.5f);
            i6 = (int) ((fArrFromRGB[2] * ((1 << this.nBits[2]) - 1)) + 0.5f);
        }
        if (this.maxBits > 23) {
            if (i4 > (1 << this.nBits[0]) - 1) {
                i4 = (1 << this.nBits[0]) - 1;
            }
            if (i5 > (1 << this.nBits[1]) - 1) {
                i5 = (1 << this.nBits[1]) - 1;
            }
            if (i6 > (1 << this.nBits[2]) - 1) {
                i6 = (1 << this.nBits[2]) - 1;
            }
        }
        int[] iArr2 = iArr;
        iArr2[0] = iArr2[0] | (i4 << this.maskOffsets[0]) | (i5 << this.maskOffsets[1]) | (i6 << this.maskOffsets[2]);
        switch (this.transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[1];
                } else {
                    bArr = (byte[]) obj;
                }
                bArr[0] = (byte) (255 & iArr[0]);
                return bArr;
            case 1:
                if (obj == null) {
                    sArr = new short[1];
                } else {
                    sArr = (short[]) obj;
                }
                sArr[0] = (short) (iArr[0] & 65535);
                return sArr;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                return iArr;
        }
    }

    @Override // java.awt.image.ColorModel
    public final int[] getComponents(int i2, int[] iArr, int i3) {
        if (iArr == null) {
            iArr = new int[i3 + this.numComponents];
        }
        for (int i4 = 0; i4 < this.numComponents; i4++) {
            iArr[i3 + i4] = (i2 & this.maskArray[i4]) >>> this.maskOffsets[i4];
        }
        return iArr;
    }

    @Override // java.awt.image.ColorModel
    public final int[] getComponents(Object obj, int[] iArr, int i2) {
        int i3;
        switch (this.transferType) {
            case 0:
                i3 = ((byte[]) obj)[0] & 255;
                break;
            case 1:
                i3 = ((short[]) obj)[0] & 65535;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i3 = ((int[]) obj)[0];
                break;
        }
        return getComponents(i3, iArr, i2);
    }

    @Override // java.awt.image.ColorModel
    public final WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        int[] iArr;
        if (i2 <= 0 || i3 <= 0) {
            throw new IllegalArgumentException("Width (" + i2 + ") and height (" + i3 + ") cannot be <= 0");
        }
        if (this.supportsAlpha) {
            iArr = new int[4];
            iArr[3] = this.alpha_mask;
        } else {
            iArr = new int[3];
        }
        iArr[0] = this.red_mask;
        iArr[1] = this.green_mask;
        iArr[2] = this.blue_mask;
        if (this.pixel_bits > 16) {
            return Raster.createPackedRaster(3, i2, i3, iArr, (Point) null);
        }
        if (this.pixel_bits > 8) {
            return Raster.createPackedRaster(1, i2, i3, iArr, (Point) null);
        }
        return Raster.createPackedRaster(0, i2, i3, iArr, (Point) null);
    }

    @Override // java.awt.image.ColorModel
    public int getDataElement(int[] iArr, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.numComponents; i4++) {
            i3 |= (iArr[i2 + i4] << this.maskOffsets[i4]) & this.maskArray[i4];
        }
        return i3;
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(int[] iArr, int i2, Object obj) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.numComponents; i4++) {
            i3 |= (iArr[i2 + i4] << this.maskOffsets[i4]) & this.maskArray[i4];
        }
        switch (this.transferType) {
            case 0:
                if (obj instanceof byte[]) {
                    byte[] bArr = (byte[]) obj;
                    bArr[0] = (byte) (i3 & 255);
                    return bArr;
                }
                return new byte[]{(byte) (i3 & 255)};
            case 1:
                if (obj instanceof short[]) {
                    short[] sArr = (short[]) obj;
                    sArr[0] = (short) (i3 & 65535);
                    return sArr;
                }
                return new short[]{(short) (i3 & 65535)};
            case 2:
            default:
                throw new ClassCastException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                if (obj instanceof int[]) {
                    int[] iArr2 = (int[]) obj;
                    iArr2[0] = i3;
                    return iArr2;
                }
                return new int[]{i3};
        }
    }

    @Override // java.awt.image.ColorModel
    public final ColorModel coerceData(WritableRaster writableRaster, boolean z2) {
        if (!this.supportsAlpha || isAlphaPremultiplied() == z2) {
            return this;
        }
        int width = writableRaster.getWidth();
        int height = writableRaster.getHeight();
        int i2 = this.numColorComponents;
        float f2 = 1.0f / ((1 << this.nBits[i2]) - 1);
        int minX = writableRaster.getMinX();
        int minY = writableRaster.getMinY();
        int[] pixel = null;
        int[] iArr = null;
        if (z2) {
            switch (this.transferType) {
                case 0:
                    int i3 = 0;
                    while (i3 < height) {
                        int i4 = minX;
                        int i5 = 0;
                        while (i5 < width) {
                            pixel = writableRaster.getPixel(i4, minY, pixel);
                            float f3 = pixel[i2] * f2;
                            if (f3 != 0.0f) {
                                for (int i6 = 0; i6 < i2; i6++) {
                                    pixel[i6] = (int) ((pixel[i6] * f3) + 0.5f);
                                }
                                writableRaster.setPixel(i4, minY, pixel);
                            } else {
                                if (iArr == null) {
                                    iArr = new int[this.numComponents];
                                    Arrays.fill(iArr, 0);
                                }
                                writableRaster.setPixel(i4, minY, iArr);
                            }
                            i5++;
                            i4++;
                        }
                        i3++;
                        minY++;
                    }
                    break;
                case 1:
                    int i7 = 0;
                    while (i7 < height) {
                        int i8 = minX;
                        int i9 = 0;
                        while (i9 < width) {
                            pixel = writableRaster.getPixel(i8, minY, pixel);
                            float f4 = pixel[i2] * f2;
                            if (f4 != 0.0f) {
                                for (int i10 = 0; i10 < i2; i10++) {
                                    pixel[i10] = (int) ((pixel[i10] * f4) + 0.5f);
                                }
                                writableRaster.setPixel(i8, minY, pixel);
                            } else {
                                if (iArr == null) {
                                    iArr = new int[this.numComponents];
                                    Arrays.fill(iArr, 0);
                                }
                                writableRaster.setPixel(i8, minY, iArr);
                            }
                            i9++;
                            i8++;
                        }
                        i7++;
                        minY++;
                    }
                    break;
                case 2:
                default:
                    throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
                case 3:
                    int i11 = 0;
                    while (i11 < height) {
                        int i12 = minX;
                        int i13 = 0;
                        while (i13 < width) {
                            pixel = writableRaster.getPixel(i12, minY, pixel);
                            float f5 = pixel[i2] * f2;
                            if (f5 != 0.0f) {
                                for (int i14 = 0; i14 < i2; i14++) {
                                    pixel[i14] = (int) ((pixel[i14] * f5) + 0.5f);
                                }
                                writableRaster.setPixel(i12, minY, pixel);
                            } else {
                                if (iArr == null) {
                                    iArr = new int[this.numComponents];
                                    Arrays.fill(iArr, 0);
                                }
                                writableRaster.setPixel(i12, minY, iArr);
                            }
                            i13++;
                            i12++;
                        }
                        i11++;
                        minY++;
                    }
                    break;
            }
        } else {
            switch (this.transferType) {
                case 0:
                    int i15 = 0;
                    while (i15 < height) {
                        int i16 = minX;
                        int i17 = 0;
                        while (i17 < width) {
                            pixel = writableRaster.getPixel(i16, minY, pixel);
                            float f6 = pixel[i2] * f2;
                            if (f6 != 0.0f) {
                                float f7 = 1.0f / f6;
                                for (int i18 = 0; i18 < i2; i18++) {
                                    pixel[i18] = (int) ((pixel[i18] * f7) + 0.5f);
                                }
                                writableRaster.setPixel(i16, minY, pixel);
                            }
                            i17++;
                            i16++;
                        }
                        i15++;
                        minY++;
                    }
                    break;
                case 1:
                    int i19 = 0;
                    while (i19 < height) {
                        int i20 = minX;
                        int i21 = 0;
                        while (i21 < width) {
                            pixel = writableRaster.getPixel(i20, minY, pixel);
                            float f8 = pixel[i2] * f2;
                            if (f8 != 0.0f) {
                                float f9 = 1.0f / f8;
                                for (int i22 = 0; i22 < i2; i22++) {
                                    pixel[i22] = (int) ((pixel[i22] * f9) + 0.5f);
                                }
                                writableRaster.setPixel(i20, minY, pixel);
                            }
                            i21++;
                            i20++;
                        }
                        i19++;
                        minY++;
                    }
                    break;
                case 2:
                default:
                    throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
                case 3:
                    int i23 = 0;
                    while (i23 < height) {
                        int i24 = minX;
                        int i25 = 0;
                        while (i25 < width) {
                            pixel = writableRaster.getPixel(i24, minY, pixel);
                            float f10 = pixel[i2] * f2;
                            if (f10 != 0.0f) {
                                float f11 = 1.0f / f10;
                                for (int i26 = 0; i26 < i2; i26++) {
                                    pixel[i26] = (int) ((pixel[i26] * f11) + 0.5f);
                                }
                                writableRaster.setPixel(i24, minY, pixel);
                            }
                            i25++;
                            i24++;
                        }
                        i23++;
                        minY++;
                    }
                    break;
            }
        }
        return new DirectColorModel(this.colorSpace, this.pixel_bits, this.maskArray[0], this.maskArray[1], this.maskArray[2], this.maskArray[3], z2, this.transferType);
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleRaster(Raster raster) {
        SampleModel sampleModel = raster.getSampleModel();
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            if (singlePixelPackedSampleModel.getNumBands() != getNumComponents()) {
                return false;
            }
            int[] bitMasks = singlePixelPackedSampleModel.getBitMasks();
            for (int i2 = 0; i2 < this.numComponents; i2++) {
                if (bitMasks[i2] != this.maskArray[i2]) {
                    return false;
                }
            }
            return raster.getTransferType() == this.transferType;
        }
        return false;
    }

    private void setFields() {
        this.red_mask = this.maskArray[0];
        this.red_offset = this.maskOffsets[0];
        this.green_mask = this.maskArray[1];
        this.green_offset = this.maskOffsets[1];
        this.blue_mask = this.maskArray[2];
        this.blue_offset = this.maskOffsets[2];
        if (this.nBits[0] < 8) {
            this.red_scale = (1 << this.nBits[0]) - 1;
        }
        if (this.nBits[1] < 8) {
            this.green_scale = (1 << this.nBits[1]) - 1;
        }
        if (this.nBits[2] < 8) {
            this.blue_scale = (1 << this.nBits[2]) - 1;
        }
        if (this.supportsAlpha) {
            this.alpha_mask = this.maskArray[3];
            this.alpha_offset = this.maskOffsets[3];
            if (this.nBits[3] < 8) {
                this.alpha_scale = (1 << this.nBits[3]) - 1;
            }
        }
    }

    @Override // java.awt.image.ColorModel
    public String toString() {
        return new String("DirectColorModel: rmask=" + Integer.toHexString(this.red_mask) + " gmask=" + Integer.toHexString(this.green_mask) + " bmask=" + Integer.toHexString(this.blue_mask) + " amask=" + Integer.toHexString(this.alpha_mask));
    }
}
