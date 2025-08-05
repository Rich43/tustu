package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import sun.awt.image.ByteInterleavedRaster;
import sun.awt.image.IntegerInterleavedRaster;
import sun.awt.image.SunWritableRaster;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/TexturePaintContext.class */
abstract class TexturePaintContext implements PaintContext {
    public static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
    public static ColorModel argbmodel = ColorModel.getRGBdefault();
    ColorModel colorModel;
    int bWidth;
    int bHeight;
    int maxWidth;
    WritableRaster outRas;
    double xOrg;
    double yOrg;
    double incXAcross;
    double incYAcross;
    double incXDown;
    double incYDown;
    int colincx;
    int colincy;
    int colincxerr;
    int colincyerr;
    int rowincx;
    int rowincy;
    int rowincxerr;
    int rowincyerr;
    private static WeakReference<Raster> xrgbRasRef;
    private static WeakReference<Raster> argbRasRef;
    private static WeakReference<Raster> byteRasRef;

    public abstract WritableRaster makeRaster(int i2, int i3);

    public abstract void setRaster(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17);

    public static PaintContext getContext(BufferedImage bufferedImage, AffineTransform affineTransform, RenderingHints renderingHints, Rectangle rectangle) {
        boolean z2;
        WritableRaster raster = bufferedImage.getRaster();
        ColorModel colorModel = bufferedImage.getColorModel();
        int i2 = rectangle.width;
        Object obj = renderingHints.get(RenderingHints.KEY_INTERPOLATION);
        if (obj == null) {
            z2 = renderingHints.get(RenderingHints.KEY_RENDERING) == RenderingHints.VALUE_RENDER_QUALITY;
        } else {
            z2 = obj != RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        }
        boolean z3 = z2;
        if ((raster instanceof IntegerInterleavedRaster) && (!z3 || isFilterableDCM(colorModel))) {
            IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster) raster;
            if (integerInterleavedRaster.getNumDataElements() == 1 && integerInterleavedRaster.getPixelStride() == 1) {
                return new Int(integerInterleavedRaster, colorModel, affineTransform, i2, z3);
            }
        } else if (raster instanceof ByteInterleavedRaster) {
            ByteInterleavedRaster byteInterleavedRaster = (ByteInterleavedRaster) raster;
            if (byteInterleavedRaster.getNumDataElements() == 1 && byteInterleavedRaster.getPixelStride() == 1) {
                if (z3) {
                    if (isFilterableICM(colorModel)) {
                        return new ByteFilter(byteInterleavedRaster, colorModel, affineTransform, i2);
                    }
                } else {
                    return new Byte(byteInterleavedRaster, colorModel, affineTransform, i2);
                }
            }
        }
        return new Any(raster, colorModel, affineTransform, i2, z3);
    }

    public static boolean isFilterableICM(ColorModel colorModel) {
        if ((colorModel instanceof IndexColorModel) && ((IndexColorModel) colorModel).getMapSize() <= 256) {
            return true;
        }
        return false;
    }

    public static boolean isFilterableDCM(ColorModel colorModel) {
        if (colorModel instanceof DirectColorModel) {
            DirectColorModel directColorModel = (DirectColorModel) colorModel;
            return isMaskOK(directColorModel.getAlphaMask(), true) && isMaskOK(directColorModel.getRedMask(), false) && isMaskOK(directColorModel.getGreenMask(), false) && isMaskOK(directColorModel.getBlueMask(), false);
        }
        return false;
    }

    public static boolean isMaskOK(int i2, boolean z2) {
        return (z2 && i2 == 0) || i2 == 255 || i2 == 65280 || i2 == 16711680 || i2 == -16777216;
    }

    public static ColorModel getInternedColorModel(ColorModel colorModel) {
        if (xrgbmodel == colorModel || xrgbmodel.equals(colorModel)) {
            return xrgbmodel;
        }
        if (argbmodel == colorModel || argbmodel.equals(colorModel)) {
            return argbmodel;
        }
        return colorModel;
    }

    TexturePaintContext(ColorModel colorModel, AffineTransform affineTransform, int i2, int i3, int i4) {
        this.colorModel = getInternedColorModel(colorModel);
        this.bWidth = i2;
        this.bHeight = i3;
        this.maxWidth = i4;
        try {
            affineTransform = affineTransform.createInverse();
        } catch (NoninvertibleTransformException e2) {
            affineTransform.setToScale(0.0d, 0.0d);
        }
        this.incXAcross = mod(affineTransform.getScaleX(), i2);
        this.incYAcross = mod(affineTransform.getShearY(), i3);
        this.incXDown = mod(affineTransform.getShearX(), i2);
        this.incYDown = mod(affineTransform.getScaleY(), i3);
        this.xOrg = affineTransform.getTranslateX();
        this.yOrg = affineTransform.getTranslateY();
        this.colincx = (int) this.incXAcross;
        this.colincy = (int) this.incYAcross;
        this.colincxerr = fractAsInt(this.incXAcross);
        this.colincyerr = fractAsInt(this.incYAcross);
        this.rowincx = (int) this.incXDown;
        this.rowincy = (int) this.incYDown;
        this.rowincxerr = fractAsInt(this.incXDown);
        this.rowincyerr = fractAsInt(this.incYDown);
    }

    static int fractAsInt(double d2) {
        return (int) ((d2 % 1.0d) * 2.147483647E9d);
    }

    static double mod(double d2, double d3) {
        double d4 = d2 % d3;
        if (d4 < 0.0d) {
            d4 += d3;
            if (d4 >= d3) {
                d4 = 0.0d;
            }
        }
        return d4;
    }

    @Override // java.awt.PaintContext
    public void dispose() {
        dropRaster(this.colorModel, this.outRas);
    }

    @Override // java.awt.PaintContext
    public ColorModel getColorModel() {
        return this.colorModel;
    }

    @Override // java.awt.PaintContext
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        if (this.outRas == null || this.outRas.getWidth() < i4 || this.outRas.getHeight() < i5) {
            this.outRas = makeRaster(i5 == 1 ? Math.max(i4, this.maxWidth) : i4, i5);
        }
        double dMod = mod(this.xOrg + (i2 * this.incXAcross) + (i3 * this.incXDown), this.bWidth);
        double dMod2 = mod(this.yOrg + (i2 * this.incYAcross) + (i3 * this.incYDown), this.bHeight);
        setRaster((int) dMod, (int) dMod2, fractAsInt(dMod), fractAsInt(dMod2), i4, i5, this.bWidth, this.bHeight, this.colincx, this.colincxerr, this.colincy, this.colincyerr, this.rowincx, this.rowincxerr, this.rowincy, this.rowincyerr);
        SunWritableRaster.markDirty(this.outRas);
        return this.outRas;
    }

    static synchronized WritableRaster makeRaster(ColorModel colorModel, Raster raster, int i2, int i3) {
        WritableRaster writableRaster;
        WritableRaster writableRaster2;
        if (xrgbmodel == colorModel) {
            if (xrgbRasRef != null && (writableRaster2 = (WritableRaster) xrgbRasRef.get()) != null && writableRaster2.getWidth() >= i2 && writableRaster2.getHeight() >= i3) {
                xrgbRasRef = null;
                return writableRaster2;
            }
            if (i2 <= 32 && i3 <= 32) {
                i3 = 32;
                i2 = 32;
            }
        } else if (argbmodel == colorModel) {
            if (argbRasRef != null && (writableRaster = (WritableRaster) argbRasRef.get()) != null && writableRaster.getWidth() >= i2 && writableRaster.getHeight() >= i3) {
                argbRasRef = null;
                return writableRaster;
            }
            if (i2 <= 32 && i3 <= 32) {
                i3 = 32;
                i2 = 32;
            }
        }
        if (raster != null) {
            return raster.createCompatibleWritableRaster(i2, i3);
        }
        return colorModel.createCompatibleWritableRaster(i2, i3);
    }

    static synchronized void dropRaster(ColorModel colorModel, Raster raster) {
        if (raster == null) {
            return;
        }
        if (xrgbmodel == colorModel) {
            xrgbRasRef = new WeakReference<>(raster);
        } else if (argbmodel == colorModel) {
            argbRasRef = new WeakReference<>(raster);
        }
    }

    static synchronized WritableRaster makeByteRaster(Raster raster, int i2, int i3) {
        WritableRaster writableRaster;
        if (byteRasRef != null && (writableRaster = (WritableRaster) byteRasRef.get()) != null && writableRaster.getWidth() >= i2 && writableRaster.getHeight() >= i3) {
            byteRasRef = null;
            return writableRaster;
        }
        if (i2 <= 32 && i3 <= 32) {
            i3 = 32;
            i2 = 32;
        }
        return raster.createCompatibleWritableRaster(i2, i3);
    }

    static synchronized void dropByteRaster(Raster raster) {
        if (raster == null) {
            return;
        }
        byteRasRef = new WeakReference<>(raster);
    }

    public static int blend(int[] iArr, int i2, int i3) {
        int i4 = i2 >>> 19;
        int i5 = i3 >>> 19;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        for (int i10 = 0; i10 < 4; i10++) {
            int i11 = iArr[i10];
            i4 = 4096 - i4;
            if ((i10 & 1) == 0) {
                i5 = 4096 - i5;
            }
            int i12 = i4 * i5;
            if (i12 != 0) {
                i9 += (i11 >>> 24) * i12;
                i8 += ((i11 >>> 16) & 255) * i12;
                i7 += ((i11 >>> 8) & 255) * i12;
                i6 += (i11 & 255) * i12;
            }
        }
        return (((i9 + 8388608) >>> 24) << 24) | (((i8 + 8388608) >>> 24) << 16) | (((i7 + 8388608) >>> 24) << 8) | ((i6 + 8388608) >>> 24);
    }

    /* loaded from: rt.jar:java/awt/TexturePaintContext$Int.class */
    static class Int extends TexturePaintContext {
        IntegerInterleavedRaster srcRas;
        int[] inData;
        int inOff;
        int inSpan;
        int[] outData;
        int outOff;
        int outSpan;
        boolean filter;

        public Int(IntegerInterleavedRaster integerInterleavedRaster, ColorModel colorModel, AffineTransform affineTransform, int i2, boolean z2) {
            super(colorModel, affineTransform, integerInterleavedRaster.getWidth(), integerInterleavedRaster.getHeight(), i2);
            this.srcRas = integerInterleavedRaster;
            this.inData = integerInterleavedRaster.getDataStorage();
            this.inSpan = integerInterleavedRaster.getScanlineStride();
            this.inOff = integerInterleavedRaster.getDataOffset(0);
            this.filter = z2;
        }

        @Override // java.awt.TexturePaintContext
        public WritableRaster makeRaster(int i2, int i3) {
            WritableRaster writableRasterMakeRaster = makeRaster(this.colorModel, this.srcRas, i2, i3);
            IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster) writableRasterMakeRaster;
            this.outData = integerInterleavedRaster.getDataStorage();
            this.outSpan = integerInterleavedRaster.getScanlineStride();
            this.outOff = integerInterleavedRaster.getDataOffset(0);
            return writableRasterMakeRaster;
        }

        @Override // java.awt.TexturePaintContext
        public void setRaster(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            int[] iArr = this.inData;
            int[] iArr2 = this.outData;
            int i18 = this.outOff;
            int i19 = this.inSpan;
            int i20 = this.inOff;
            int i21 = this.outSpan;
            boolean z2 = this.filter;
            boolean z3 = i10 == 1 && i11 == 0 && i12 == 0 && i13 == 0 && !z2;
            int i22 = i2;
            int i23 = i3;
            int i24 = i4;
            int i25 = i5;
            if (z3) {
                i21 -= i6;
            }
            int[] iArr3 = z2 ? new int[4] : null;
            for (int i26 = 0; i26 < i7; i26++) {
                if (z3) {
                    int i27 = i20 + (i23 * i19) + i8;
                    int i28 = i8 - i22;
                    i18 += i6;
                    if (i8 >= 32) {
                        int i29 = i6;
                        while (i29 > 0) {
                            int i30 = i29 < i28 ? i29 : i28;
                            System.arraycopy(iArr, i27 - i28, iArr2, i18 - i29, i30);
                            i29 -= i30;
                            int i31 = i28 - i30;
                            i28 = i31;
                            if (i31 == 0) {
                                i28 = i8;
                            }
                        }
                    } else {
                        for (int i32 = i6; i32 > 0; i32--) {
                            iArr2[i18 - i32] = iArr[i27 - i28];
                            i28--;
                            if (i28 == 0) {
                                i28 = i8;
                            }
                        }
                    }
                } else {
                    int i33 = i22;
                    int i34 = i23;
                    int i35 = i24;
                    int i36 = i25;
                    for (int i37 = 0; i37 < i6; i37++) {
                        if (z2) {
                            int i38 = i33 + 1;
                            int i39 = i38;
                            if (i38 >= i8) {
                                i39 = 0;
                            }
                            int i40 = i34 + 1;
                            int i41 = i40;
                            if (i40 >= i9) {
                                i41 = 0;
                            }
                            iArr3[0] = iArr[i20 + (i34 * i19) + i33];
                            iArr3[1] = iArr[i20 + (i34 * i19) + i39];
                            iArr3[2] = iArr[i20 + (i41 * i19) + i33];
                            iArr3[3] = iArr[i20 + (i41 * i19) + i39];
                            iArr2[i18 + i37] = TexturePaintContext.blend(iArr3, i35, i36);
                        } else {
                            iArr2[i18 + i37] = iArr[i20 + (i34 * i19) + i33];
                        }
                        int i42 = i35 + i11;
                        i35 = i42;
                        if (i42 < 0) {
                            i35 &= Integer.MAX_VALUE;
                            i33++;
                        }
                        int i43 = i33 + i10;
                        i33 = i43;
                        if (i43 >= i8) {
                            i33 -= i8;
                        }
                        int i44 = i36 + i13;
                        i36 = i44;
                        if (i44 < 0) {
                            i36 &= Integer.MAX_VALUE;
                            i34++;
                        }
                        int i45 = i34 + i12;
                        i34 = i45;
                        if (i45 >= i9) {
                            i34 -= i9;
                        }
                    }
                }
                int i46 = i24 + i15;
                i24 = i46;
                if (i46 < 0) {
                    i24 &= Integer.MAX_VALUE;
                    i22++;
                }
                int i47 = i22 + i14;
                i22 = i47;
                if (i47 >= i8) {
                    i22 -= i8;
                }
                int i48 = i25 + i17;
                i25 = i48;
                if (i48 < 0) {
                    i25 &= Integer.MAX_VALUE;
                    i23++;
                }
                int i49 = i23 + i16;
                i23 = i49;
                if (i49 >= i9) {
                    i23 -= i9;
                }
                i18 += i21;
            }
        }
    }

    /* loaded from: rt.jar:java/awt/TexturePaintContext$Byte.class */
    static class Byte extends TexturePaintContext {
        ByteInterleavedRaster srcRas;
        byte[] inData;
        int inOff;
        int inSpan;
        byte[] outData;
        int outOff;
        int outSpan;

        public Byte(ByteInterleavedRaster byteInterleavedRaster, ColorModel colorModel, AffineTransform affineTransform, int i2) {
            super(colorModel, affineTransform, byteInterleavedRaster.getWidth(), byteInterleavedRaster.getHeight(), i2);
            this.srcRas = byteInterleavedRaster;
            this.inData = byteInterleavedRaster.getDataStorage();
            this.inSpan = byteInterleavedRaster.getScanlineStride();
            this.inOff = byteInterleavedRaster.getDataOffset(0);
        }

        @Override // java.awt.TexturePaintContext
        public WritableRaster makeRaster(int i2, int i3) {
            WritableRaster writableRasterMakeByteRaster = makeByteRaster(this.srcRas, i2, i3);
            ByteInterleavedRaster byteInterleavedRaster = (ByteInterleavedRaster) writableRasterMakeByteRaster;
            this.outData = byteInterleavedRaster.getDataStorage();
            this.outSpan = byteInterleavedRaster.getScanlineStride();
            this.outOff = byteInterleavedRaster.getDataOffset(0);
            return writableRasterMakeByteRaster;
        }

        @Override // java.awt.TexturePaintContext, java.awt.PaintContext
        public void dispose() {
            dropByteRaster(this.outRas);
        }

        @Override // java.awt.TexturePaintContext
        public void setRaster(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            byte[] bArr = this.inData;
            byte[] bArr2 = this.outData;
            int i18 = this.outOff;
            int i19 = this.inSpan;
            int i20 = this.inOff;
            int i21 = this.outSpan;
            boolean z2 = i10 == 1 && i11 == 0 && i12 == 0 && i13 == 0;
            int i22 = i2;
            int i23 = i3;
            int i24 = i4;
            int i25 = i5;
            if (z2) {
                i21 -= i6;
            }
            for (int i26 = 0; i26 < i7; i26++) {
                if (z2) {
                    int i27 = i20 + (i23 * i19) + i8;
                    int i28 = i8 - i22;
                    i18 += i6;
                    if (i8 >= 32) {
                        int i29 = i6;
                        while (i29 > 0) {
                            int i30 = i29 < i28 ? i29 : i28;
                            System.arraycopy(bArr, i27 - i28, bArr2, i18 - i29, i30);
                            i29 -= i30;
                            int i31 = i28 - i30;
                            i28 = i31;
                            if (i31 == 0) {
                                i28 = i8;
                            }
                        }
                    } else {
                        for (int i32 = i6; i32 > 0; i32--) {
                            bArr2[i18 - i32] = bArr[i27 - i28];
                            i28--;
                            if (i28 == 0) {
                                i28 = i8;
                            }
                        }
                    }
                } else {
                    int i33 = i22;
                    int i34 = i23;
                    int i35 = i24;
                    int i36 = i25;
                    for (int i37 = 0; i37 < i6; i37++) {
                        bArr2[i18 + i37] = bArr[i20 + (i34 * i19) + i33];
                        int i38 = i35 + i11;
                        i35 = i38;
                        if (i38 < 0) {
                            i35 &= Integer.MAX_VALUE;
                            i33++;
                        }
                        int i39 = i33 + i10;
                        i33 = i39;
                        if (i39 >= i8) {
                            i33 -= i8;
                        }
                        int i40 = i36 + i13;
                        i36 = i40;
                        if (i40 < 0) {
                            i36 &= Integer.MAX_VALUE;
                            i34++;
                        }
                        int i41 = i34 + i12;
                        i34 = i41;
                        if (i41 >= i9) {
                            i34 -= i9;
                        }
                    }
                }
                int i42 = i24 + i15;
                i24 = i42;
                if (i42 < 0) {
                    i24 &= Integer.MAX_VALUE;
                    i22++;
                }
                int i43 = i22 + i14;
                i22 = i43;
                if (i43 >= i8) {
                    i22 -= i8;
                }
                int i44 = i25 + i17;
                i25 = i44;
                if (i44 < 0) {
                    i25 &= Integer.MAX_VALUE;
                    i23++;
                }
                int i45 = i23 + i16;
                i23 = i45;
                if (i45 >= i9) {
                    i23 -= i9;
                }
                i18 += i21;
            }
        }
    }

    /* loaded from: rt.jar:java/awt/TexturePaintContext$ByteFilter.class */
    static class ByteFilter extends TexturePaintContext {
        ByteInterleavedRaster srcRas;
        int[] inPalette;
        byte[] inData;
        int inOff;
        int inSpan;
        int[] outData;
        int outOff;
        int outSpan;

        public ByteFilter(ByteInterleavedRaster byteInterleavedRaster, ColorModel colorModel, AffineTransform affineTransform, int i2) {
            super(colorModel.getTransparency() == 1 ? xrgbmodel : argbmodel, affineTransform, byteInterleavedRaster.getWidth(), byteInterleavedRaster.getHeight(), i2);
            this.inPalette = new int[256];
            ((IndexColorModel) colorModel).getRGBs(this.inPalette);
            this.srcRas = byteInterleavedRaster;
            this.inData = byteInterleavedRaster.getDataStorage();
            this.inSpan = byteInterleavedRaster.getScanlineStride();
            this.inOff = byteInterleavedRaster.getDataOffset(0);
        }

        @Override // java.awt.TexturePaintContext
        public WritableRaster makeRaster(int i2, int i3) {
            WritableRaster writableRasterMakeRaster = makeRaster(this.colorModel, null, i2, i3);
            IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster) writableRasterMakeRaster;
            this.outData = integerInterleavedRaster.getDataStorage();
            this.outSpan = integerInterleavedRaster.getScanlineStride();
            this.outOff = integerInterleavedRaster.getDataOffset(0);
            return writableRasterMakeRaster;
        }

        @Override // java.awt.TexturePaintContext
        public void setRaster(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            byte[] bArr = this.inData;
            int[] iArr = this.outData;
            int i18 = this.outOff;
            int i19 = this.inSpan;
            int i20 = this.inOff;
            int i21 = this.outSpan;
            int i22 = i2;
            int i23 = i3;
            int i24 = i4;
            int i25 = i5;
            int[] iArr2 = new int[4];
            for (int i26 = 0; i26 < i7; i26++) {
                int i27 = i22;
                int i28 = i23;
                int i29 = i24;
                int i30 = i25;
                for (int i31 = 0; i31 < i6; i31++) {
                    int i32 = i27 + 1;
                    int i33 = i32;
                    if (i32 >= i8) {
                        i33 = 0;
                    }
                    int i34 = i28 + 1;
                    int i35 = i34;
                    if (i34 >= i9) {
                        i35 = 0;
                    }
                    iArr2[0] = this.inPalette[255 & bArr[i20 + i27 + (i19 * i28)]];
                    iArr2[1] = this.inPalette[255 & bArr[i20 + i33 + (i19 * i28)]];
                    iArr2[2] = this.inPalette[255 & bArr[i20 + i27 + (i19 * i35)]];
                    iArr2[3] = this.inPalette[255 & bArr[i20 + i33 + (i19 * i35)]];
                    iArr[i18 + i31] = TexturePaintContext.blend(iArr2, i29, i30);
                    int i36 = i29 + i11;
                    i29 = i36;
                    if (i36 < 0) {
                        i29 &= Integer.MAX_VALUE;
                        i27++;
                    }
                    int i37 = i27 + i10;
                    i27 = i37;
                    if (i37 >= i8) {
                        i27 -= i8;
                    }
                    int i38 = i30 + i13;
                    i30 = i38;
                    if (i38 < 0) {
                        i30 &= Integer.MAX_VALUE;
                        i28++;
                    }
                    int i39 = i28 + i12;
                    i28 = i39;
                    if (i39 >= i9) {
                        i28 -= i9;
                    }
                }
                int i40 = i24 + i15;
                i24 = i40;
                if (i40 < 0) {
                    i24 &= Integer.MAX_VALUE;
                    i22++;
                }
                int i41 = i22 + i14;
                i22 = i41;
                if (i41 >= i8) {
                    i22 -= i8;
                }
                int i42 = i25 + i17;
                i25 = i42;
                if (i42 < 0) {
                    i25 &= Integer.MAX_VALUE;
                    i23++;
                }
                int i43 = i23 + i16;
                i23 = i43;
                if (i43 >= i9) {
                    i23 -= i9;
                }
                i18 += i21;
            }
        }
    }

    /* loaded from: rt.jar:java/awt/TexturePaintContext$Any.class */
    static class Any extends TexturePaintContext {
        WritableRaster srcRas;
        boolean filter;

        public Any(WritableRaster writableRaster, ColorModel colorModel, AffineTransform affineTransform, int i2, boolean z2) {
            super(colorModel, affineTransform, writableRaster.getWidth(), writableRaster.getHeight(), i2);
            this.srcRas = writableRaster;
            this.filter = z2;
        }

        @Override // java.awt.TexturePaintContext
        public WritableRaster makeRaster(int i2, int i3) {
            return makeRaster(this.colorModel, this.srcRas, i2, i3);
        }

        @Override // java.awt.TexturePaintContext
        public void setRaster(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            Object dataElements = null;
            int i18 = i2;
            int i19 = i3;
            int i20 = i4;
            int i21 = i5;
            WritableRaster writableRaster = this.srcRas;
            WritableRaster writableRaster2 = this.outRas;
            int[] iArr = this.filter ? new int[4] : null;
            for (int i22 = 0; i22 < i7; i22++) {
                int i23 = i18;
                int i24 = i19;
                int i25 = i20;
                int i26 = i21;
                for (int i27 = 0; i27 < i6; i27++) {
                    dataElements = writableRaster.getDataElements(i23, i24, dataElements);
                    if (this.filter) {
                        int i28 = i23 + 1;
                        int i29 = i28;
                        if (i28 >= i8) {
                            i29 = 0;
                        }
                        int i30 = i24 + 1;
                        int i31 = i30;
                        if (i30 >= i9) {
                            i31 = 0;
                        }
                        iArr[0] = this.colorModel.getRGB(dataElements);
                        Object dataElements2 = writableRaster.getDataElements(i29, i24, dataElements);
                        iArr[1] = this.colorModel.getRGB(dataElements2);
                        Object dataElements3 = writableRaster.getDataElements(i23, i31, dataElements2);
                        iArr[2] = this.colorModel.getRGB(dataElements3);
                        Object dataElements4 = writableRaster.getDataElements(i29, i31, dataElements3);
                        iArr[3] = this.colorModel.getRGB(dataElements4);
                        dataElements = this.colorModel.getDataElements(TexturePaintContext.blend(iArr, i25, i26), dataElements4);
                    }
                    writableRaster2.setDataElements(i27, i22, dataElements);
                    int i32 = i25 + i11;
                    i25 = i32;
                    if (i32 < 0) {
                        i25 &= Integer.MAX_VALUE;
                        i23++;
                    }
                    int i33 = i23 + i10;
                    i23 = i33;
                    if (i33 >= i8) {
                        i23 -= i8;
                    }
                    int i34 = i26 + i13;
                    i26 = i34;
                    if (i34 < 0) {
                        i26 &= Integer.MAX_VALUE;
                        i24++;
                    }
                    int i35 = i24 + i12;
                    i24 = i35;
                    if (i35 >= i9) {
                        i24 -= i9;
                    }
                }
                int i36 = i20 + i15;
                i20 = i36;
                if (i36 < 0) {
                    i20 &= Integer.MAX_VALUE;
                    i18++;
                }
                int i37 = i18 + i14;
                i18 = i37;
                if (i37 >= i8) {
                    i18 -= i8;
                }
                int i38 = i21 + i17;
                i21 = i38;
                if (i38 < 0) {
                    i21 &= Integer.MAX_VALUE;
                    i19++;
                }
                int i39 = i19 + i16;
                i19 = i39;
                if (i39 >= i9) {
                    i19 -= i9;
                }
            }
        }
    }
}
