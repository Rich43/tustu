package java.awt.image;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.util.Arrays;

/* loaded from: rt.jar:java/awt/image/ComponentColorModel.class */
public class ComponentColorModel extends ColorModel {
    private boolean signed;
    private boolean is_sRGB_stdScale;
    private boolean is_LinearRGB_stdScale;
    private boolean is_LinearGray_stdScale;
    private boolean is_ICCGray_stdScale;
    private byte[] tosRGB8LUT;
    private byte[] fromsRGB8LUT8;
    private short[] fromsRGB8LUT16;
    private byte[] fromLinearGray16ToOtherGray8LUT;
    private short[] fromLinearGray16ToOtherGray16LUT;
    private boolean needScaleInit;
    private boolean noUnnorm;
    private boolean nonStdScale;
    private float[] min;
    private float[] diffMinMax;
    private float[] compOffset;
    private float[] compScale;

    public ComponentColorModel(ColorSpace colorSpace, int[] iArr, boolean z2, boolean z3, int i2, int i3) {
        super(bitsHelper(i3, colorSpace, z2), bitsArrayHelper(iArr, i3, colorSpace, z2), colorSpace, z2, z3, i2, i3);
        switch (i3) {
            case 0:
            case 1:
            case 3:
                this.signed = false;
                this.needScaleInit = true;
                break;
            case 2:
                this.signed = true;
                this.needScaleInit = true;
                break;
            case 4:
            case 5:
                this.signed = true;
                this.needScaleInit = false;
                this.noUnnorm = true;
                this.nonStdScale = false;
                break;
            default:
                throw new IllegalArgumentException("This constructor is not compatible with transferType " + i3);
        }
        setupLUTs();
    }

    public ComponentColorModel(ColorSpace colorSpace, boolean z2, boolean z3, int i2, int i3) {
        this(colorSpace, null, z2, z3, i2, i3);
    }

    private static int bitsHelper(int i2, ColorSpace colorSpace, boolean z2) {
        int dataTypeSize = DataBuffer.getDataTypeSize(i2);
        int numComponents = colorSpace.getNumComponents();
        if (z2) {
            numComponents++;
        }
        return dataTypeSize * numComponents;
    }

    private static int[] bitsArrayHelper(int[] iArr, int i2, ColorSpace colorSpace, boolean z2) {
        switch (i2) {
            case 0:
            case 1:
            case 3:
                if (iArr != null) {
                    return iArr;
                }
                break;
        }
        int dataTypeSize = DataBuffer.getDataTypeSize(i2);
        int numComponents = colorSpace.getNumComponents();
        if (z2) {
            numComponents++;
        }
        int[] iArr2 = new int[numComponents];
        for (int i3 = 0; i3 < numComponents; i3++) {
            iArr2[i3] = dataTypeSize;
        }
        return iArr2;
    }

    private void setupLUTs() {
        if (this.is_sRGB) {
            this.is_sRGB_stdScale = true;
            this.nonStdScale = false;
            return;
        }
        if (ColorModel.isLinearRGBspace(this.colorSpace)) {
            this.is_LinearRGB_stdScale = true;
            this.nonStdScale = false;
            if (this.transferType == 0) {
                this.tosRGB8LUT = ColorModel.getLinearRGB8TosRGB8LUT();
                this.fromsRGB8LUT8 = ColorModel.getsRGB8ToLinearRGB8LUT();
                return;
            } else {
                this.tosRGB8LUT = ColorModel.getLinearRGB16TosRGB8LUT();
                this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
                return;
            }
        }
        if (this.colorSpaceType == 6 && (this.colorSpace instanceof ICC_ColorSpace) && this.colorSpace.getMinValue(0) == 0.0f && this.colorSpace.getMaxValue(0) == 1.0f) {
            ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) this.colorSpace;
            this.is_ICCGray_stdScale = true;
            this.nonStdScale = false;
            this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
            if (ColorModel.isLinearGRAYspace(iCC_ColorSpace)) {
                this.is_LinearGray_stdScale = true;
                if (this.transferType == 0) {
                    this.tosRGB8LUT = ColorModel.getGray8TosRGB8LUT(iCC_ColorSpace);
                    return;
                } else {
                    this.tosRGB8LUT = ColorModel.getGray16TosRGB8LUT(iCC_ColorSpace);
                    return;
                }
            }
            if (this.transferType == 0) {
                this.tosRGB8LUT = ColorModel.getGray8TosRGB8LUT(iCC_ColorSpace);
                this.fromLinearGray16ToOtherGray8LUT = ColorModel.getLinearGray16ToOtherGray8LUT(iCC_ColorSpace);
                return;
            } else {
                this.tosRGB8LUT = ColorModel.getGray16TosRGB8LUT(iCC_ColorSpace);
                this.fromLinearGray16ToOtherGray16LUT = ColorModel.getLinearGray16ToOtherGray16LUT(iCC_ColorSpace);
                return;
            }
        }
        if (this.needScaleInit) {
            this.nonStdScale = false;
            for (int i2 = 0; i2 < this.numColorComponents; i2++) {
                if (this.colorSpace.getMinValue(i2) != 0.0f || this.colorSpace.getMaxValue(i2) != 1.0f) {
                    this.nonStdScale = true;
                    break;
                }
            }
            if (this.nonStdScale) {
                this.min = new float[this.numColorComponents];
                this.diffMinMax = new float[this.numColorComponents];
                for (int i3 = 0; i3 < this.numColorComponents; i3++) {
                    this.min[i3] = this.colorSpace.getMinValue(i3);
                    this.diffMinMax[i3] = this.colorSpace.getMaxValue(i3) - this.min[i3];
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0273 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void initScale() {
        /*
            Method dump skipped, instructions count: 628
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.image.ComponentColorModel.initScale():void");
    }

    private int getRGBComponent(int i2, int i3) {
        if (this.numComponents > 1) {
            throw new IllegalArgumentException("More than one component per pixel");
        }
        if (this.signed) {
            throw new IllegalArgumentException("Component value is signed");
        }
        if (this.needScaleInit) {
            initScale();
        }
        int[] iArr = null;
        switch (this.transferType) {
            case 0:
                iArr = new byte[]{(byte) i2};
                break;
            case 1:
                iArr = new short[]{(short) i2};
                break;
            case 3:
                iArr = new int[]{i2};
                break;
        }
        return (int) ((this.colorSpace.toRGB(getNormalizedComponents(iArr, null, 0))[i3] * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public int getRed(int i2) {
        return getRGBComponent(i2, 0);
    }

    @Override // java.awt.image.ColorModel
    public int getGreen(int i2) {
        return getRGBComponent(i2, 1);
    }

    @Override // java.awt.image.ColorModel
    public int getBlue(int i2) {
        return getRGBComponent(i2, 2);
    }

    @Override // java.awt.image.ColorModel
    public int getAlpha(int i2) {
        if (!this.supportsAlpha) {
            return 255;
        }
        if (this.numComponents > 1) {
            throw new IllegalArgumentException("More than one component per pixel");
        }
        if (this.signed) {
            throw new IllegalArgumentException("Component value is signed");
        }
        return (int) (((i2 / ((1 << this.nBits[0]) - 1)) * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public int getRGB(int i2) {
        if (this.numComponents > 1) {
            throw new IllegalArgumentException("More than one component per pixel");
        }
        if (this.signed) {
            throw new IllegalArgumentException("Component value is signed");
        }
        return (getAlpha(i2) << 24) | (getRed(i2) << 16) | (getGreen(i2) << 8) | (getBlue(i2) << 0);
    }

    private int extractComponent(Object obj, int i2, int i3) {
        int i4;
        boolean z2 = this.supportsAlpha && this.isAlphaPremultiplied;
        int i5 = 0;
        int i6 = (1 << this.nBits[i2]) - 1;
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                i4 = bArr[i2] & i6;
                i3 = 8;
                if (z2) {
                    i5 = bArr[this.numColorComponents] & i6;
                    break;
                }
                break;
            case 1:
                short[] sArr = (short[]) obj;
                i4 = sArr[i2] & i6;
                if (z2) {
                    i5 = sArr[this.numColorComponents] & i6;
                    break;
                }
                break;
            case 2:
                short[] sArr2 = (short[]) obj;
                float f2 = (1 << i3) - 1;
                if (z2) {
                    short s2 = sArr2[this.numColorComponents];
                    if (s2 != 0) {
                        return (int) (((sArr2[i2] / s2) * f2) + 0.5f);
                    }
                    return 0;
                }
                return (int) (((sArr2[i2] / 32767.0f) * f2) + 0.5f);
            case 3:
                int[] iArr = (int[]) obj;
                i4 = iArr[i2];
                if (z2) {
                    i5 = iArr[this.numColorComponents];
                    break;
                }
                break;
            case 4:
                float[] fArr = (float[]) obj;
                float f3 = (1 << i3) - 1;
                if (z2) {
                    float f4 = fArr[this.numColorComponents];
                    if (f4 != 0.0f) {
                        return (int) (((fArr[i2] / f4) * f3) + 0.5f);
                    }
                    return 0;
                }
                return (int) ((fArr[i2] * f3) + 0.5f);
            case 5:
                double[] dArr = (double[]) obj;
                double d2 = (1 << i3) - 1;
                if (z2) {
                    double d3 = dArr[this.numColorComponents];
                    if (d3 != 0.0d) {
                        return (int) (((dArr[i2] / d3) * d2) + 0.5d);
                    }
                    return 0;
                }
                return (int) ((dArr[i2] * d2) + 0.5d);
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
        }
        if (z2) {
            if (i5 != 0) {
                return (int) (((i4 / i6) * (((1 << this.nBits[this.numColorComponents]) - 1) / i5) * ((1 << i3) - 1)) + 0.5f);
            }
            return 0;
        }
        if (this.nBits[i2] != i3) {
            return (int) (((i4 / i6) * ((1 << i3) - 1)) + 0.5f);
        }
        return i4;
    }

    private int getRGBComponent(Object obj, int i2) {
        if (this.needScaleInit) {
            initScale();
        }
        if (this.is_sRGB_stdScale) {
            return extractComponent(obj, i2, 8);
        }
        if (this.is_LinearRGB_stdScale) {
            return this.tosRGB8LUT[extractComponent(obj, i2, 16)] & 255;
        }
        if (this.is_ICCGray_stdScale) {
            return this.tosRGB8LUT[extractComponent(obj, 0, 16)] & 255;
        }
        return (int) ((this.colorSpace.toRGB(getNormalizedComponents(obj, null, 0))[i2] * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public int getRed(Object obj) {
        return getRGBComponent(obj, 0);
    }

    @Override // java.awt.image.ColorModel
    public int getGreen(Object obj) {
        return getRGBComponent(obj, 1);
    }

    @Override // java.awt.image.ColorModel
    public int getBlue(Object obj) {
        return getRGBComponent(obj, 2);
    }

    @Override // java.awt.image.ColorModel
    public int getAlpha(Object obj) {
        int i2;
        if (!this.supportsAlpha) {
            return 255;
        }
        int i3 = this.numColorComponents;
        int i4 = (1 << this.nBits[i3]) - 1;
        switch (this.transferType) {
            case 0:
                i2 = ((byte[]) obj)[i3] & i4;
                break;
            case 1:
                i2 = ((short[]) obj)[i3] & i4;
                break;
            case 2:
                return (int) (((((short[]) obj)[i3] / 32767.0f) * 255.0f) + 0.5f);
            case 3:
                i2 = ((int[]) obj)[i3];
                break;
            case 4:
                return (int) ((((float[]) obj)[i3] * 255.0f) + 0.5f);
            case 5:
                return (int) ((((double[]) obj)[i3] * 255.0d) + 0.5d);
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
        }
        if (this.nBits[i3] == 8) {
            return i2;
        }
        return (int) (((i2 / ((1 << this.nBits[i3]) - 1)) * 255.0f) + 0.5f);
    }

    @Override // java.awt.image.ColorModel
    public int getRGB(Object obj) {
        if (this.needScaleInit) {
            initScale();
        }
        if (this.is_sRGB_stdScale || this.is_LinearRGB_stdScale) {
            return (getAlpha(obj) << 24) | (getRed(obj) << 16) | (getGreen(obj) << 8) | getBlue(obj);
        }
        if (this.colorSpaceType == 6) {
            int red = getRed(obj);
            return (getAlpha(obj) << 24) | (red << 16) | (red << 8) | red;
        }
        float[] rgb = this.colorSpace.toRGB(getNormalizedComponents(obj, null, 0));
        return (getAlpha(obj) << 24) | (((int) ((rgb[0] * 255.0f) + 0.5f)) << 16) | (((int) ((rgb[1] * 255.0f) + 0.5f)) << 8) | (((int) ((rgb[2] * 255.0f) + 0.5f)) << 0);
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(int i2, Object obj) {
        int[] iArr;
        int i3;
        float f2;
        short[] sArr;
        byte[] bArr;
        double[] dArr;
        double d2;
        float[] fArr;
        float f3;
        short[] sArr2;
        int i4 = (i2 >> 16) & 255;
        int i5 = (i2 >> 8) & 255;
        int i6 = i2 & 255;
        if (this.needScaleInit) {
            initScale();
        }
        if (this.signed) {
            switch (this.transferType) {
                case 2:
                    if (obj == null) {
                        sArr2 = new short[this.numComponents];
                    } else {
                        sArr2 = (short[]) obj;
                    }
                    if (this.is_sRGB_stdScale || this.is_LinearRGB_stdScale) {
                        float f4 = 128.49803f;
                        if (this.is_LinearRGB_stdScale) {
                            i4 = this.fromsRGB8LUT16[i4] & 65535;
                            i5 = this.fromsRGB8LUT16[i5] & 65535;
                            i6 = this.fromsRGB8LUT16[i6] & 65535;
                            f4 = 0.49999237f;
                        }
                        if (this.supportsAlpha) {
                            int i7 = (i2 >> 24) & 255;
                            sArr2[3] = (short) ((i7 * 128.49803f) + 0.5f);
                            if (this.isAlphaPremultiplied) {
                                f4 = i7 * f4 * 0.003921569f;
                            }
                        }
                        sArr2[0] = (short) ((i4 * f4) + 0.5f);
                        sArr2[1] = (short) ((i5 * f4) + 0.5f);
                        sArr2[2] = (short) ((i6 * f4) + 0.5f);
                    } else if (this.is_LinearGray_stdScale) {
                        float f5 = (((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) / 65535.0f;
                        float f6 = 32767.0f;
                        if (this.supportsAlpha) {
                            int i8 = (i2 >> 24) & 255;
                            sArr2[1] = (short) ((i8 * 128.49803f) + 0.5f);
                            if (this.isAlphaPremultiplied) {
                                f6 = i8 * 32767.0f * 0.003921569f;
                            }
                        }
                        sArr2[0] = (short) ((f5 * f6) + 0.5f);
                    } else if (this.is_ICCGray_stdScale) {
                        int i9 = this.fromLinearGray16ToOtherGray16LUT[(int) ((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535)) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535)) + 0.5f)] & 65535;
                        float f7 = 0.49999237f;
                        if (this.supportsAlpha) {
                            int i10 = (i2 >> 24) & 255;
                            sArr2[1] = (short) ((i10 * 128.49803f) + 0.5f);
                            if (this.isAlphaPremultiplied) {
                                f7 = i10 * 0.49999237f * 0.003921569f;
                            }
                        }
                        sArr2[0] = (short) ((i9 * f7) + 0.5f);
                    } else {
                        float[] fArrFromRGB = this.colorSpace.fromRGB(new float[]{i4 * 0.003921569f, i5 * 0.003921569f, i6 * 0.003921569f});
                        if (this.nonStdScale) {
                            for (int i11 = 0; i11 < this.numColorComponents; i11++) {
                                fArrFromRGB[i11] = (fArrFromRGB[i11] - this.compOffset[i11]) * this.compScale[i11];
                                if (fArrFromRGB[i11] < 0.0f) {
                                    fArrFromRGB[i11] = 0.0f;
                                }
                                if (fArrFromRGB[i11] > 1.0f) {
                                    fArrFromRGB[i11] = 1.0f;
                                }
                            }
                        }
                        float f8 = 32767.0f;
                        if (this.supportsAlpha) {
                            int i12 = (i2 >> 24) & 255;
                            sArr2[this.numColorComponents] = (short) ((i12 * 128.49803f) + 0.5f);
                            if (this.isAlphaPremultiplied) {
                                f8 = 32767.0f * i12 * 0.003921569f;
                            }
                        }
                        for (int i13 = 0; i13 < this.numColorComponents; i13++) {
                            sArr2[i13] = (short) ((fArrFromRGB[i13] * f8) + 0.5f);
                        }
                    }
                    return sArr2;
                case 4:
                    if (obj == null) {
                        fArr = new float[this.numComponents];
                    } else {
                        fArr = (float[]) obj;
                    }
                    if (this.is_sRGB_stdScale || this.is_LinearRGB_stdScale) {
                        if (this.is_LinearRGB_stdScale) {
                            i4 = this.fromsRGB8LUT16[i4] & 65535;
                            i5 = this.fromsRGB8LUT16[i5] & 65535;
                            i6 = this.fromsRGB8LUT16[i6] & 65535;
                            f3 = 1.5259022E-5f;
                        } else {
                            f3 = 0.003921569f;
                        }
                        if (this.supportsAlpha) {
                            fArr[3] = ((i2 >> 24) & 255) * 0.003921569f;
                            if (this.isAlphaPremultiplied) {
                                f3 *= fArr[3];
                            }
                        }
                        fArr[0] = i4 * f3;
                        fArr[1] = i5 * f3;
                        fArr[2] = i6 * f3;
                    } else if (this.is_LinearGray_stdScale) {
                        fArr[0] = (((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) / 65535.0f;
                        if (this.supportsAlpha) {
                            fArr[1] = ((i2 >> 24) & 255) * 0.003921569f;
                            if (this.isAlphaPremultiplied) {
                                float[] fArr2 = fArr;
                                fArr2[0] = fArr2[0] * fArr[1];
                            }
                        }
                    } else if (this.is_ICCGray_stdScale) {
                        fArr[0] = (this.fromLinearGray16ToOtherGray16LUT[(int) ((((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) + 0.5f)] & 65535) / 65535.0f;
                        if (this.supportsAlpha) {
                            fArr[1] = ((i2 >> 24) & 255) * 0.003921569f;
                            if (this.isAlphaPremultiplied) {
                                float[] fArr3 = fArr;
                                fArr3[0] = fArr3[0] * fArr[1];
                            }
                        }
                    } else {
                        float[] fArrFromRGB2 = this.colorSpace.fromRGB(new float[]{i4 * 0.003921569f, i5 * 0.003921569f, i6 * 0.003921569f});
                        if (this.supportsAlpha) {
                            int i14 = (i2 >> 24) & 255;
                            fArr[this.numColorComponents] = i14 * 0.003921569f;
                            if (this.isAlphaPremultiplied) {
                                float f9 = 0.003921569f * i14;
                                for (int i15 = 0; i15 < this.numColorComponents; i15++) {
                                    int i16 = i15;
                                    fArrFromRGB2[i16] = fArrFromRGB2[i16] * f9;
                                }
                            }
                        }
                        for (int i17 = 0; i17 < this.numColorComponents; i17++) {
                            fArr[i17] = fArrFromRGB2[i17];
                        }
                    }
                    return fArr;
                case 5:
                    if (obj == null) {
                        dArr = new double[this.numComponents];
                    } else {
                        dArr = (double[]) obj;
                    }
                    if (this.is_sRGB_stdScale || this.is_LinearRGB_stdScale) {
                        if (this.is_LinearRGB_stdScale) {
                            i4 = this.fromsRGB8LUT16[i4] & 65535;
                            i5 = this.fromsRGB8LUT16[i5] & 65535;
                            i6 = this.fromsRGB8LUT16[i6] & 65535;
                            d2 = 1.5259021896696422E-5d;
                        } else {
                            d2 = 0.00392156862745098d;
                        }
                        if (this.supportsAlpha) {
                            dArr[3] = ((i2 >> 24) & 255) * 0.00392156862745098d;
                            if (this.isAlphaPremultiplied) {
                                d2 *= dArr[3];
                            }
                        }
                        dArr[0] = i4 * d2;
                        dArr[1] = i5 * d2;
                        dArr[2] = i6 * d2;
                    } else if (this.is_LinearGray_stdScale) {
                        dArr[0] = (((0.2125d * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154d * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721d * (this.fromsRGB8LUT16[i6] & 65535))) / 65535.0d;
                        if (this.supportsAlpha) {
                            dArr[1] = ((i2 >> 24) & 255) * 0.00392156862745098d;
                            if (this.isAlphaPremultiplied) {
                                double[] dArr2 = dArr;
                                dArr2[0] = dArr2[0] * dArr[1];
                            }
                        }
                    } else if (this.is_ICCGray_stdScale) {
                        dArr[0] = (this.fromLinearGray16ToOtherGray16LUT[(int) ((((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) + 0.5f)] & 65535) / 65535.0d;
                        if (this.supportsAlpha) {
                            dArr[1] = ((i2 >> 24) & 255) * 0.00392156862745098d;
                            if (this.isAlphaPremultiplied) {
                                double[] dArr3 = dArr;
                                dArr3[0] = dArr3[0] * dArr[1];
                            }
                        }
                    } else {
                        float[] fArrFromRGB3 = this.colorSpace.fromRGB(new float[]{i4 * 0.003921569f, i5 * 0.003921569f, i6 * 0.003921569f});
                        if (this.supportsAlpha) {
                            int i18 = (i2 >> 24) & 255;
                            dArr[this.numColorComponents] = i18 * 0.00392156862745098d;
                            if (this.isAlphaPremultiplied) {
                                float f10 = 0.003921569f * i18;
                                for (int i19 = 0; i19 < this.numColorComponents; i19++) {
                                    int i20 = i19;
                                    fArrFromRGB3[i20] = fArrFromRGB3[i20] * f10;
                                }
                            }
                        }
                        for (int i21 = 0; i21 < this.numColorComponents; i21++) {
                            dArr[i21] = fArrFromRGB3[i21];
                        }
                    }
                    return dArr;
            }
        }
        if (this.transferType == 3 && obj != null) {
            iArr = (int[]) obj;
        } else {
            iArr = new int[this.numComponents];
        }
        if (this.is_sRGB_stdScale || this.is_LinearRGB_stdScale) {
            if (this.is_LinearRGB_stdScale) {
                if (this.transferType == 0) {
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
                int i22 = (i2 >> 24) & 255;
                if (this.nBits[3] == 8) {
                    iArr[3] = i22;
                } else {
                    iArr[3] = (int) ((i22 * 0.003921569f * ((1 << this.nBits[3]) - 1)) + 0.5f);
                }
                if (this.isAlphaPremultiplied) {
                    f2 *= i22 * 0.003921569f;
                    i3 = -1;
                }
            }
            if (this.nBits[0] == i3) {
                iArr[0] = i4;
            } else {
                iArr[0] = (int) ((i4 * f2 * ((1 << this.nBits[0]) - 1)) + 0.5f);
            }
            if (this.nBits[1] == i3) {
                iArr[1] = i5;
            } else {
                iArr[1] = (int) ((i5 * f2 * ((1 << this.nBits[1]) - 1)) + 0.5f);
            }
            if (this.nBits[2] == i3) {
                iArr[2] = i6;
            } else {
                iArr[2] = (int) ((i6 * f2 * ((1 << this.nBits[2]) - 1)) + 0.5f);
            }
        } else if (this.is_LinearGray_stdScale) {
            float f11 = (((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) / 65535.0f;
            if (this.supportsAlpha) {
                int i23 = (i2 >> 24) & 255;
                if (this.nBits[1] == 8) {
                    iArr[1] = i23;
                } else {
                    iArr[1] = (int) ((i23 * 0.003921569f * ((1 << this.nBits[1]) - 1)) + 0.5f);
                }
                if (this.isAlphaPremultiplied) {
                    f11 *= i23 * 0.003921569f;
                }
            }
            iArr[0] = (int) ((f11 * ((1 << this.nBits[0]) - 1)) + 0.5f);
        } else if (this.is_ICCGray_stdScale) {
            float f12 = (this.fromLinearGray16ToOtherGray16LUT[(int) ((((0.2125f * (this.fromsRGB8LUT16[i4] & 65535)) + (0.7154f * (this.fromsRGB8LUT16[i5] & 65535))) + (0.0721f * (this.fromsRGB8LUT16[i6] & 65535))) + 0.5f)] & 65535) / 65535.0f;
            if (this.supportsAlpha) {
                int i24 = (i2 >> 24) & 255;
                if (this.nBits[1] == 8) {
                    iArr[1] = i24;
                } else {
                    iArr[1] = (int) ((i24 * 0.003921569f * ((1 << this.nBits[1]) - 1)) + 0.5f);
                }
                if (this.isAlphaPremultiplied) {
                    f12 *= i24 * 0.003921569f;
                }
            }
            iArr[0] = (int) ((f12 * ((1 << this.nBits[0]) - 1)) + 0.5f);
        } else {
            float[] fArrFromRGB4 = this.colorSpace.fromRGB(new float[]{i4 * 0.003921569f, i5 * 0.003921569f, i6 * 0.003921569f});
            if (this.nonStdScale) {
                for (int i25 = 0; i25 < this.numColorComponents; i25++) {
                    fArrFromRGB4[i25] = (fArrFromRGB4[i25] - this.compOffset[i25]) * this.compScale[i25];
                    if (fArrFromRGB4[i25] < 0.0f) {
                        fArrFromRGB4[i25] = 0.0f;
                    }
                    if (fArrFromRGB4[i25] > 1.0f) {
                        fArrFromRGB4[i25] = 1.0f;
                    }
                }
            }
            if (this.supportsAlpha) {
                int i26 = (i2 >> 24) & 255;
                if (this.nBits[this.numColorComponents] != 8) {
                    iArr[this.numColorComponents] = (int) ((i26 * 0.003921569f * ((1 << this.nBits[this.numColorComponents]) - 1)) + 0.5f);
                } else {
                    iArr[this.numColorComponents] = i26;
                }
                if (this.isAlphaPremultiplied) {
                    float f13 = 0.003921569f * i26;
                    for (int i27 = 0; i27 < this.numColorComponents; i27++) {
                        int i28 = i27;
                        fArrFromRGB4[i28] = fArrFromRGB4[i28] * f13;
                    }
                }
            }
            for (int i29 = 0; i29 < this.numColorComponents; i29++) {
                iArr[i29] = (int) ((fArrFromRGB4[i29] * ((1 << this.nBits[i29]) - 1)) + 0.5f);
            }
        }
        switch (this.transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[this.numComponents];
                } else {
                    bArr = (byte[]) obj;
                }
                for (int i30 = 0; i30 < this.numComponents; i30++) {
                    bArr[i30] = (byte) (255 & iArr[i30]);
                }
                return bArr;
            case 1:
                if (obj == null) {
                    sArr = new short[this.numComponents];
                } else {
                    sArr = (short[]) obj;
                }
                for (int i31 = 0; i31 < this.numComponents; i31++) {
                    sArr[i31] = (short) (iArr[i31] & 65535);
                }
                return sArr;
            case 2:
            default:
                throw new IllegalArgumentException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                if (this.maxBits > 23) {
                    for (int i32 = 0; i32 < this.numComponents; i32++) {
                        if (iArr[i32] > (1 << this.nBits[i32]) - 1) {
                            iArr[i32] = (1 << this.nBits[i32]) - 1;
                        }
                    }
                }
                return iArr;
        }
    }

    @Override // java.awt.image.ColorModel
    public int[] getComponents(int i2, int[] iArr, int i3) {
        if (this.numComponents > 1) {
            throw new IllegalArgumentException("More than one component per pixel");
        }
        if (this.needScaleInit) {
            initScale();
        }
        if (this.noUnnorm) {
            throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
        }
        if (iArr == null) {
            iArr = new int[i3 + 1];
        }
        iArr[i3 + 0] = i2 & ((1 << this.nBits[0]) - 1);
        return iArr;
    }

    @Override // java.awt.image.ColorModel
    public int[] getComponents(Object obj, int[] iArr, int i2) {
        int[] intArray;
        if (this.needScaleInit) {
            initScale();
        }
        if (this.noUnnorm) {
            throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
        }
        if (obj instanceof int[]) {
            intArray = (int[]) obj;
        } else {
            intArray = DataBuffer.toIntArray(obj);
            if (intArray == null) {
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            }
        }
        if (intArray.length < this.numComponents) {
            throw new IllegalArgumentException("Length of pixel array < number of components in model");
        }
        if (iArr == null) {
            iArr = new int[i2 + this.numComponents];
        } else if (iArr.length - i2 < this.numComponents) {
            throw new IllegalArgumentException("Length of components array < number of components in model");
        }
        System.arraycopy(intArray, 0, iArr, i2, this.numComponents);
        return iArr;
    }

    @Override // java.awt.image.ColorModel
    public int[] getUnnormalizedComponents(float[] fArr, int i2, int[] iArr, int i3) {
        if (this.needScaleInit) {
            initScale();
        }
        if (this.noUnnorm) {
            throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
        }
        return super.getUnnormalizedComponents(fArr, i2, iArr, i3);
    }

    @Override // java.awt.image.ColorModel
    public float[] getNormalizedComponents(int[] iArr, int i2, float[] fArr, int i3) {
        if (this.needScaleInit) {
            initScale();
        }
        if (this.noUnnorm) {
            throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
        }
        return super.getNormalizedComponents(iArr, i2, fArr, i3);
    }

    @Override // java.awt.image.ColorModel
    public int getDataElement(int[] iArr, int i2) {
        if (this.needScaleInit) {
            initScale();
        }
        if (this.numComponents == 1) {
            if (this.noUnnorm) {
                throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
            }
            return iArr[i2 + 0];
        }
        throw new IllegalArgumentException("This model returns " + this.numComponents + " elements in the pixel array.");
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(int[] iArr, int i2, Object obj) {
        short[] sArr;
        byte[] bArr;
        int[] iArr2;
        if (this.needScaleInit) {
            initScale();
        }
        if (this.noUnnorm) {
            throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
        }
        if (iArr.length - i2 < this.numComponents) {
            throw new IllegalArgumentException("Component array too small (should be " + this.numComponents);
        }
        switch (this.transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[this.numComponents];
                } else {
                    bArr = (byte[]) obj;
                }
                for (int i3 = 0; i3 < this.numComponents; i3++) {
                    bArr[i3] = (byte) (iArr[i2 + i3] & 255);
                }
                return bArr;
            case 1:
                if (obj == null) {
                    sArr = new short[this.numComponents];
                } else {
                    sArr = (short[]) obj;
                }
                for (int i4 = 0; i4 < this.numComponents; i4++) {
                    sArr[i4] = (short) (iArr[i2 + i4] & 65535);
                }
                return sArr;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                if (obj == null) {
                    iArr2 = new int[this.numComponents];
                } else {
                    iArr2 = (int[]) obj;
                }
                System.arraycopy(iArr, i2, iArr2, 0, this.numComponents);
                return iArr2;
        }
    }

    @Override // java.awt.image.ColorModel
    public int getDataElement(float[] fArr, int i2) {
        if (this.numComponents > 1) {
            throw new IllegalArgumentException("More than one component per pixel");
        }
        if (this.signed) {
            throw new IllegalArgumentException("Component value is signed");
        }
        if (this.needScaleInit) {
            initScale();
        }
        Object dataElements = getDataElements(fArr, i2, (Object) null);
        switch (this.transferType) {
            case 0:
                return ((byte[]) dataElements)[0] & 255;
            case 1:
                return ((short[]) dataElements)[0] & 65535;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                return ((int[]) dataElements)[0];
        }
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(float[] fArr, int i2, Object obj) {
        float[] fArr2;
        double[] dArr;
        float[] fArr3;
        short[] sArr;
        int[] iArr;
        short[] sArr2;
        byte[] bArr;
        boolean z2 = this.supportsAlpha && this.isAlphaPremultiplied;
        if (this.needScaleInit) {
            initScale();
        }
        if (this.nonStdScale) {
            fArr2 = new float[this.numComponents];
            int i3 = 0;
            int i4 = i2;
            while (i3 < this.numColorComponents) {
                fArr2[i3] = (fArr[i4] - this.compOffset[i3]) * this.compScale[i3];
                if (fArr2[i3] < 0.0f) {
                    fArr2[i3] = 0.0f;
                }
                if (fArr2[i3] > 1.0f) {
                    fArr2[i3] = 1.0f;
                }
                i3++;
                i4++;
            }
            if (this.supportsAlpha) {
                fArr2[this.numColorComponents] = fArr[this.numColorComponents + i2];
            }
            i2 = 0;
        } else {
            fArr2 = fArr;
        }
        switch (this.transferType) {
            case 0:
                if (obj == null) {
                    bArr = new byte[this.numComponents];
                } else {
                    bArr = (byte[]) obj;
                }
                if (z2) {
                    float f2 = fArr2[this.numColorComponents + i2];
                    int i5 = 0;
                    int i6 = i2;
                    while (i5 < this.numColorComponents) {
                        bArr[i5] = (byte) ((fArr2[i6] * f2 * ((1 << this.nBits[i5]) - 1)) + 0.5f);
                        i5++;
                        i6++;
                    }
                    bArr[this.numColorComponents] = (byte) ((f2 * ((1 << this.nBits[this.numColorComponents]) - 1)) + 0.5f);
                } else {
                    int i7 = 0;
                    int i8 = i2;
                    while (i7 < this.numComponents) {
                        bArr[i7] = (byte) ((fArr2[i8] * ((1 << this.nBits[i7]) - 1)) + 0.5f);
                        i7++;
                        i8++;
                    }
                }
                return bArr;
            case 1:
                if (obj == null) {
                    sArr2 = new short[this.numComponents];
                } else {
                    sArr2 = (short[]) obj;
                }
                if (z2) {
                    float f3 = fArr2[this.numColorComponents + i2];
                    int i9 = 0;
                    int i10 = i2;
                    while (i9 < this.numColorComponents) {
                        sArr2[i9] = (short) ((fArr2[i10] * f3 * ((1 << this.nBits[i9]) - 1)) + 0.5f);
                        i9++;
                        i10++;
                    }
                    sArr2[this.numColorComponents] = (short) ((f3 * ((1 << this.nBits[this.numColorComponents]) - 1)) + 0.5f);
                } else {
                    int i11 = 0;
                    int i12 = i2;
                    while (i11 < this.numComponents) {
                        sArr2[i11] = (short) ((fArr2[i12] * ((1 << this.nBits[i11]) - 1)) + 0.5f);
                        i11++;
                        i12++;
                    }
                }
                return sArr2;
            case 2:
                if (obj == null) {
                    sArr = new short[this.numComponents];
                } else {
                    sArr = (short[]) obj;
                }
                if (z2) {
                    float f4 = fArr2[this.numColorComponents + i2];
                    int i13 = 0;
                    int i14 = i2;
                    while (i13 < this.numColorComponents) {
                        sArr[i13] = (short) ((fArr2[i14] * f4 * 32767.0f) + 0.5f);
                        i13++;
                        i14++;
                    }
                    sArr[this.numColorComponents] = (short) ((f4 * 32767.0f) + 0.5f);
                } else {
                    int i15 = 0;
                    int i16 = i2;
                    while (i15 < this.numComponents) {
                        sArr[i15] = (short) ((fArr2[i16] * 32767.0f) + 0.5f);
                        i15++;
                        i16++;
                    }
                }
                return sArr;
            case 3:
                if (obj == null) {
                    iArr = new int[this.numComponents];
                } else {
                    iArr = (int[]) obj;
                }
                if (z2) {
                    float f5 = fArr2[this.numColorComponents + i2];
                    int i17 = 0;
                    int i18 = i2;
                    while (i17 < this.numColorComponents) {
                        iArr[i17] = (int) ((fArr2[i18] * f5 * ((1 << this.nBits[i17]) - 1)) + 0.5f);
                        i17++;
                        i18++;
                    }
                    iArr[this.numColorComponents] = (int) ((f5 * ((1 << this.nBits[this.numColorComponents]) - 1)) + 0.5f);
                } else {
                    int i19 = 0;
                    int i20 = i2;
                    while (i19 < this.numComponents) {
                        iArr[i19] = (int) ((fArr2[i20] * ((1 << this.nBits[i19]) - 1)) + 0.5f);
                        i19++;
                        i20++;
                    }
                }
                return iArr;
            case 4:
                if (obj == null) {
                    fArr3 = new float[this.numComponents];
                } else {
                    fArr3 = (float[]) obj;
                }
                if (z2) {
                    float f6 = fArr[this.numColorComponents + i2];
                    int i21 = 0;
                    int i22 = i2;
                    while (i21 < this.numColorComponents) {
                        fArr3[i21] = fArr[i22] * f6;
                        i21++;
                        i22++;
                    }
                    fArr3[this.numColorComponents] = f6;
                } else {
                    int i23 = 0;
                    int i24 = i2;
                    while (i23 < this.numComponents) {
                        fArr3[i23] = fArr[i24];
                        i23++;
                        i24++;
                    }
                }
                return fArr3;
            case 5:
                if (obj == null) {
                    dArr = new double[this.numComponents];
                } else {
                    dArr = (double[]) obj;
                }
                if (z2) {
                    double d2 = fArr[this.numColorComponents + i2];
                    int i25 = 0;
                    int i26 = i2;
                    while (i25 < this.numColorComponents) {
                        dArr[i25] = fArr[i26] * d2;
                        i25++;
                        i26++;
                    }
                    dArr[this.numColorComponents] = d2;
                } else {
                    int i27 = 0;
                    int i28 = i2;
                    while (i27 < this.numComponents) {
                        dArr[i27] = fArr[i28];
                        i27++;
                        i28++;
                    }
                }
                return dArr;
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
        }
    }

    @Override // java.awt.image.ColorModel
    public float[] getNormalizedComponents(Object obj, float[] fArr, int i2) {
        if (fArr == null) {
            fArr = new float[this.numComponents + i2];
        }
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                int i3 = 0;
                int i4 = i2;
                while (i3 < this.numComponents) {
                    fArr[i4] = (bArr[i3] & 255) / ((1 << this.nBits[i3]) - 1);
                    i3++;
                    i4++;
                }
                break;
            case 1:
                short[] sArr = (short[]) obj;
                int i5 = 0;
                int i6 = i2;
                while (i5 < this.numComponents) {
                    fArr[i6] = (sArr[i5] & 65535) / ((1 << this.nBits[i5]) - 1);
                    i5++;
                    i6++;
                }
                break;
            case 2:
                short[] sArr2 = (short[]) obj;
                int i7 = 0;
                int i8 = i2;
                while (i7 < this.numComponents) {
                    fArr[i8] = sArr2[i7] / 32767.0f;
                    i7++;
                    i8++;
                }
                break;
            case 3:
                int[] iArr = (int[]) obj;
                int i9 = 0;
                int i10 = i2;
                while (i9 < this.numComponents) {
                    fArr[i10] = iArr[i9] / ((1 << this.nBits[i9]) - 1);
                    i9++;
                    i10++;
                }
                break;
            case 4:
                float[] fArr2 = (float[]) obj;
                int i11 = 0;
                int i12 = i2;
                while (i11 < this.numComponents) {
                    fArr[i12] = fArr2[i11];
                    i11++;
                    i12++;
                }
                break;
            case 5:
                double[] dArr = (double[]) obj;
                int i13 = 0;
                int i14 = i2;
                while (i13 < this.numComponents) {
                    fArr[i14] = (float) dArr[i13];
                    i13++;
                    i14++;
                }
                break;
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
        }
        if (this.supportsAlpha && this.isAlphaPremultiplied) {
            float f2 = fArr[this.numColorComponents + i2];
            if (f2 != 0.0f) {
                float f3 = 1.0f / f2;
                for (int i15 = i2; i15 < this.numColorComponents + i2; i15++) {
                    float[] fArr3 = fArr;
                    int i16 = i15;
                    fArr3[i16] = fArr3[i16] * f3;
                }
            }
        }
        if (this.min != null) {
            for (int i17 = 0; i17 < this.numColorComponents; i17++) {
                fArr[i17 + i2] = this.min[i17] + (this.diffMinMax[i17] * fArr[i17 + i2]);
            }
        }
        return fArr;
    }

    @Override // java.awt.image.ColorModel
    public ColorModel coerceData(WritableRaster writableRaster, boolean z2) {
        if (!this.supportsAlpha || this.isAlphaPremultiplied == z2) {
            return this;
        }
        int width = writableRaster.getWidth();
        int height = writableRaster.getHeight();
        int numBands = writableRaster.getNumBands() - 1;
        int minX = writableRaster.getMinX();
        int minY = writableRaster.getMinY();
        if (z2) {
            switch (this.transferType) {
                case 0:
                    byte[] bArr = null;
                    byte[] bArr2 = null;
                    float f2 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i2 = 0;
                    while (i2 < height) {
                        int i3 = minX;
                        int i4 = 0;
                        while (i4 < width) {
                            bArr = (byte[]) writableRaster.getDataElements(i3, minY, bArr);
                            if ((bArr[numBands] & 255) * f2 != 0.0f) {
                                for (int i5 = 0; i5 < numBands; i5++) {
                                    bArr[i5] = (byte) (((bArr[i5] & 255) * r0) + 0.5f);
                                }
                                writableRaster.setDataElements(i3, minY, bArr);
                            } else {
                                if (bArr2 == null) {
                                    bArr2 = new byte[this.numComponents];
                                    Arrays.fill(bArr2, (byte) 0);
                                }
                                writableRaster.setDataElements(i3, minY, bArr2);
                            }
                            i4++;
                            i3++;
                        }
                        i2++;
                        minY++;
                    }
                    break;
                case 1:
                    short[] sArr = null;
                    short[] sArr2 = null;
                    float f3 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i6 = 0;
                    while (i6 < height) {
                        int i7 = minX;
                        int i8 = 0;
                        while (i8 < width) {
                            sArr = (short[]) writableRaster.getDataElements(i7, minY, sArr);
                            if ((sArr[numBands] & 65535) * f3 != 0.0f) {
                                for (int i9 = 0; i9 < numBands; i9++) {
                                    sArr[i9] = (short) (((sArr[i9] & 65535) * r0) + 0.5f);
                                }
                                writableRaster.setDataElements(i7, minY, sArr);
                            } else {
                                if (sArr2 == null) {
                                    sArr2 = new short[this.numComponents];
                                    Arrays.fill(sArr2, (short) 0);
                                }
                                writableRaster.setDataElements(i7, minY, sArr2);
                            }
                            i8++;
                            i7++;
                        }
                        i6++;
                        minY++;
                    }
                    break;
                case 2:
                    short[] sArr3 = null;
                    short[] sArr4 = null;
                    int i10 = 0;
                    while (i10 < height) {
                        int i11 = minX;
                        int i12 = 0;
                        while (i12 < width) {
                            sArr3 = (short[]) writableRaster.getDataElements(i11, minY, sArr3);
                            if (sArr3[numBands] * 3.051851E-5f != 0.0f) {
                                for (int i13 = 0; i13 < numBands; i13++) {
                                    sArr3[i13] = (short) ((sArr3[i13] * r0) + 0.5f);
                                }
                                writableRaster.setDataElements(i11, minY, sArr3);
                            } else {
                                if (sArr4 == null) {
                                    sArr4 = new short[this.numComponents];
                                    Arrays.fill(sArr4, (short) 0);
                                }
                                writableRaster.setDataElements(i11, minY, sArr4);
                            }
                            i12++;
                            i11++;
                        }
                        i10++;
                        minY++;
                    }
                    break;
                case 3:
                    int[] iArr = null;
                    int[] iArr2 = null;
                    float f4 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i14 = 0;
                    while (i14 < height) {
                        int i15 = minX;
                        int i16 = 0;
                        while (i16 < width) {
                            iArr = (int[]) writableRaster.getDataElements(i15, minY, iArr);
                            float f5 = iArr[numBands] * f4;
                            if (f5 != 0.0f) {
                                for (int i17 = 0; i17 < numBands; i17++) {
                                    iArr[i17] = (int) ((iArr[i17] * f5) + 0.5f);
                                }
                                writableRaster.setDataElements(i15, minY, iArr);
                            } else {
                                if (iArr2 == null) {
                                    iArr2 = new int[this.numComponents];
                                    Arrays.fill(iArr2, 0);
                                }
                                writableRaster.setDataElements(i15, minY, iArr2);
                            }
                            i16++;
                            i15++;
                        }
                        i14++;
                        minY++;
                    }
                    break;
                case 4:
                    float[] fArr = null;
                    float[] fArr2 = null;
                    int i18 = 0;
                    while (i18 < height) {
                        int i19 = minX;
                        int i20 = 0;
                        while (i20 < width) {
                            fArr = (float[]) writableRaster.getDataElements(i19, minY, fArr);
                            float f6 = fArr[numBands];
                            if (f6 != 0.0f) {
                                for (int i21 = 0; i21 < numBands; i21++) {
                                    int i22 = i21;
                                    fArr[i22] = fArr[i22] * f6;
                                }
                                writableRaster.setDataElements(i19, minY, fArr);
                            } else {
                                if (fArr2 == null) {
                                    fArr2 = new float[this.numComponents];
                                    Arrays.fill(fArr2, 0.0f);
                                }
                                writableRaster.setDataElements(i19, minY, fArr2);
                            }
                            i20++;
                            i19++;
                        }
                        i18++;
                        minY++;
                    }
                    break;
                case 5:
                    double[] dArr = null;
                    double[] dArr2 = null;
                    int i23 = 0;
                    while (i23 < height) {
                        int i24 = minX;
                        int i25 = 0;
                        while (i25 < width) {
                            dArr = (double[]) writableRaster.getDataElements(i24, minY, dArr);
                            double d2 = dArr[numBands];
                            if (d2 != 0.0d) {
                                for (int i26 = 0; i26 < numBands; i26++) {
                                    int i27 = i26;
                                    dArr[i27] = dArr[i27] * d2;
                                }
                                writableRaster.setDataElements(i24, minY, dArr);
                            } else {
                                if (dArr2 == null) {
                                    dArr2 = new double[this.numComponents];
                                    Arrays.fill(dArr2, 0.0d);
                                }
                                writableRaster.setDataElements(i24, minY, dArr2);
                            }
                            i25++;
                            i24++;
                        }
                        i23++;
                        minY++;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            }
        } else {
            switch (this.transferType) {
                case 0:
                    byte[] bArr3 = null;
                    float f7 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i28 = 0;
                    while (i28 < height) {
                        int i29 = minX;
                        int i30 = 0;
                        while (i30 < width) {
                            bArr3 = (byte[]) writableRaster.getDataElements(i29, minY, bArr3);
                            float f8 = (bArr3[numBands] & 255) * f7;
                            if (f8 != 0.0f) {
                                float f9 = 1.0f / f8;
                                for (int i31 = 0; i31 < numBands; i31++) {
                                    bArr3[i31] = (byte) (((bArr3[i31] & 255) * f9) + 0.5f);
                                }
                                writableRaster.setDataElements(i29, minY, bArr3);
                            }
                            i30++;
                            i29++;
                        }
                        i28++;
                        minY++;
                    }
                    break;
                case 1:
                    short[] sArr5 = null;
                    float f10 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i32 = 0;
                    while (i32 < height) {
                        int i33 = minX;
                        int i34 = 0;
                        while (i34 < width) {
                            sArr5 = (short[]) writableRaster.getDataElements(i33, minY, sArr5);
                            float f11 = (sArr5[numBands] & 65535) * f10;
                            if (f11 != 0.0f) {
                                float f12 = 1.0f / f11;
                                for (int i35 = 0; i35 < numBands; i35++) {
                                    sArr5[i35] = (short) (((sArr5[i35] & 65535) * f12) + 0.5f);
                                }
                                writableRaster.setDataElements(i33, minY, sArr5);
                            }
                            i34++;
                            i33++;
                        }
                        i32++;
                        minY++;
                    }
                    break;
                case 2:
                    short[] sArr6 = null;
                    int i36 = 0;
                    while (i36 < height) {
                        int i37 = minX;
                        int i38 = 0;
                        while (i38 < width) {
                            sArr6 = (short[]) writableRaster.getDataElements(i37, minY, sArr6);
                            float f13 = sArr6[numBands] * 3.051851E-5f;
                            if (f13 != 0.0f) {
                                float f14 = 1.0f / f13;
                                for (int i39 = 0; i39 < numBands; i39++) {
                                    sArr6[i39] = (short) ((sArr6[i39] * f14) + 0.5f);
                                }
                                writableRaster.setDataElements(i37, minY, sArr6);
                            }
                            i38++;
                            i37++;
                        }
                        i36++;
                        minY++;
                    }
                    break;
                case 3:
                    int[] iArr3 = null;
                    float f15 = 1.0f / ((1 << this.nBits[numBands]) - 1);
                    int i40 = 0;
                    while (i40 < height) {
                        int i41 = minX;
                        int i42 = 0;
                        while (i42 < width) {
                            iArr3 = (int[]) writableRaster.getDataElements(i41, minY, iArr3);
                            float f16 = iArr3[numBands] * f15;
                            if (f16 != 0.0f) {
                                float f17 = 1.0f / f16;
                                for (int i43 = 0; i43 < numBands; i43++) {
                                    iArr3[i43] = (int) ((iArr3[i43] * f17) + 0.5f);
                                }
                                writableRaster.setDataElements(i41, minY, iArr3);
                            }
                            i42++;
                            i41++;
                        }
                        i40++;
                        minY++;
                    }
                    break;
                case 4:
                    float[] fArr3 = null;
                    int i44 = 0;
                    while (i44 < height) {
                        int i45 = minX;
                        int i46 = 0;
                        while (i46 < width) {
                            fArr3 = (float[]) writableRaster.getDataElements(i45, minY, fArr3);
                            float f18 = fArr3[numBands];
                            if (f18 != 0.0f) {
                                float f19 = 1.0f / f18;
                                for (int i47 = 0; i47 < numBands; i47++) {
                                    int i48 = i47;
                                    fArr3[i48] = fArr3[i48] * f19;
                                }
                                writableRaster.setDataElements(i45, minY, fArr3);
                            }
                            i46++;
                            i45++;
                        }
                        i44++;
                        minY++;
                    }
                    break;
                case 5:
                    double[] dArr3 = null;
                    int i49 = 0;
                    while (i49 < height) {
                        int i50 = minX;
                        int i51 = 0;
                        while (i51 < width) {
                            dArr3 = (double[]) writableRaster.getDataElements(i50, minY, dArr3);
                            double d3 = dArr3[numBands];
                            if (d3 != 0.0d) {
                                double d4 = 1.0d / d3;
                                for (int i52 = 0; i52 < numBands; i52++) {
                                    int i53 = i52;
                                    dArr3[i53] = dArr3[i53] * d4;
                                }
                                writableRaster.setDataElements(i50, minY, dArr3);
                            }
                            i51++;
                            i50++;
                        }
                        i49++;
                        minY++;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            }
        }
        if (!this.signed) {
            return new ComponentColorModel(this.colorSpace, this.nBits, this.supportsAlpha, z2, this.transparency, this.transferType);
        }
        return new ComponentColorModel(this.colorSpace, this.supportsAlpha, z2, this.transparency, this.transferType);
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleRaster(Raster raster) {
        SampleModel sampleModel = raster.getSampleModel();
        if (!(sampleModel instanceof ComponentSampleModel) || sampleModel.getNumBands() != getNumComponents()) {
            return false;
        }
        for (int i2 = 0; i2 < this.nBits.length; i2++) {
            if (sampleModel.getSampleSize(i2) < this.nBits[i2]) {
                return false;
            }
        }
        return raster.getTransferType() == this.transferType;
    }

    @Override // java.awt.image.ColorModel
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        WritableRaster writableRasterCreateWritableRaster;
        int i4 = i2 * i3 * this.numComponents;
        switch (this.transferType) {
            case 0:
            case 1:
                writableRasterCreateWritableRaster = Raster.createInterleavedRaster(this.transferType, i2, i3, this.numComponents, null);
                break;
            default:
                SampleModel sampleModelCreateCompatibleSampleModel = createCompatibleSampleModel(i2, i3);
                writableRasterCreateWritableRaster = Raster.createWritableRaster(sampleModelCreateCompatibleSampleModel, sampleModelCreateCompatibleSampleModel.createDataBuffer(), null);
                break;
        }
        return writableRasterCreateWritableRaster;
    }

    @Override // java.awt.image.ColorModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        int[] iArr = new int[this.numComponents];
        for (int i4 = 0; i4 < this.numComponents; i4++) {
            iArr[i4] = i4;
        }
        switch (this.transferType) {
            case 0:
            case 1:
                return new PixelInterleavedSampleModel(this.transferType, i2, i3, this.numComponents, i2 * this.numComponents, iArr);
            default:
                return new ComponentSampleModel(this.transferType, i2, i3, this.numComponents, i2 * this.numComponents, iArr);
        }
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleSampleModel(SampleModel sampleModel) {
        if (!(sampleModel instanceof ComponentSampleModel) || this.numComponents != sampleModel.getNumBands() || sampleModel.getTransferType() != this.transferType) {
            return false;
        }
        return true;
    }

    @Override // java.awt.image.ColorModel
    public WritableRaster getAlphaRaster(WritableRaster writableRaster) {
        if (!hasAlpha()) {
            return null;
        }
        int minX = writableRaster.getMinX();
        int minY = writableRaster.getMinY();
        return writableRaster.createWritableChild(minX, minY, writableRaster.getWidth(), writableRaster.getHeight(), minX, minY, new int[]{writableRaster.getNumBands() - 1});
    }

    @Override // java.awt.image.ColorModel
    public boolean equals(Object obj) {
        if (!super.equals(obj) || obj.getClass() != getClass()) {
            return false;
        }
        return true;
    }
}
