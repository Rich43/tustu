package java.awt.image;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Hashtable;
import sun.awt.image.BufImgSurfaceData;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/image/IndexColorModel.class */
public class IndexColorModel extends ColorModel {
    private int[] rgb;
    private int map_size;
    private int pixel_mask;
    private int transparent_index;
    private boolean allgrayopaque;
    private BigInteger validBits;
    private BufImgSurfaceData.ICMColorData colorData;
    private static int[] opaqueBits = {8, 8, 8};
    private static int[] alphaBits = {8, 8, 8, 8};
    private static final int CACHESIZE = 40;
    private int[] lookupcache;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public IndexColorModel(int i2, int i3, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        super(i2, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(i2));
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        setRGBs(i3, bArr, bArr2, bArr3, null);
        calculatePixelMask();
    }

    public IndexColorModel(int i2, int i3, byte[] bArr, byte[] bArr2, byte[] bArr3, int i4) {
        super(i2, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(i2));
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        setRGBs(i3, bArr, bArr2, bArr3, null);
        setTransparentPixel(i4);
        calculatePixelMask();
    }

    public IndexColorModel(int i2, int i3, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        super(i2, alphaBits, ColorSpace.getInstance(1000), true, false, 3, ColorModel.getDefaultTransferType(i2));
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        setRGBs(i3, bArr, bArr2, bArr3, bArr4);
        calculatePixelMask();
    }

    public IndexColorModel(int i2, int i3, byte[] bArr, int i4, boolean z2) {
        this(i2, i3, bArr, i4, z2, -1);
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
    }

    public IndexColorModel(int i2, int i3, byte[] bArr, int i4, boolean z2, int i5) {
        super(i2, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(i2));
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("Map size (" + i3 + ") must be >= 1");
        }
        this.map_size = i3;
        this.rgb = new int[calcRealMapSize(i2, i3)];
        int i6 = i4;
        int i7 = 255;
        boolean z3 = true;
        int i8 = 1;
        for (int i9 = 0; i9 < i3; i9++) {
            int i10 = i6;
            int i11 = i6 + 1;
            int i12 = bArr[i10] & 255;
            int i13 = i11 + 1;
            int i14 = bArr[i11] & 255;
            i6 = i13 + 1;
            int i15 = bArr[i13] & 255;
            z3 = z3 && i12 == i14 && i14 == i15;
            if (z2) {
                i6++;
                i7 = bArr[i6] & 255;
                if (i7 != 255) {
                    if (i7 == 0) {
                        i8 = i8 == 1 ? 2 : i8;
                        if (this.transparent_index < 0) {
                            this.transparent_index = i9;
                        }
                    } else {
                        i8 = 3;
                    }
                    z3 = false;
                }
            }
            this.rgb[i9] = (i7 << 24) | (i12 << 16) | (i14 << 8) | i15;
        }
        this.allgrayopaque = z3;
        setTransparency(i8);
        setTransparentPixel(i5);
        calculatePixelMask();
    }

    public IndexColorModel(int i2, int i3, int[] iArr, int i4, boolean z2, int i5, int i6) {
        super(i2, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, i6);
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("Map size (" + i3 + ") must be >= 1");
        }
        if (i6 != 0 && i6 != 1) {
            throw new IllegalArgumentException("transferType must be eitherDataBuffer.TYPE_BYTE or DataBuffer.TYPE_USHORT");
        }
        setRGBs(i3, iArr, i4, z2);
        setTransparentPixel(i5);
        calculatePixelMask();
    }

    public IndexColorModel(int i2, int i3, int[] iArr, int i4, int i5, BigInteger bigInteger) {
        super(i2, alphaBits, ColorSpace.getInstance(1000), true, false, 3, i5);
        this.transparent_index = -1;
        this.colorData = null;
        this.lookupcache = new int[40];
        if (i2 < 1 || i2 > 16) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("Map size (" + i3 + ") must be >= 1");
        }
        if (i5 != 0 && i5 != 1) {
            throw new IllegalArgumentException("transferType must be eitherDataBuffer.TYPE_BYTE or DataBuffer.TYPE_USHORT");
        }
        if (bigInteger != null) {
            int i6 = 0;
            while (true) {
                if (i6 >= i3) {
                    break;
                }
                if (bigInteger.testBit(i6)) {
                    i6++;
                } else {
                    this.validBits = bigInteger;
                    break;
                }
            }
        }
        setRGBs(i3, iArr, i4, true);
        calculatePixelMask();
    }

    private void setRGBs(int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        if (i2 < 1) {
            throw new IllegalArgumentException("Map size (" + i2 + ") must be >= 1");
        }
        this.map_size = i2;
        this.rgb = new int[calcRealMapSize(this.pixel_bits, i2)];
        int i3 = 255;
        int i4 = 1;
        boolean z2 = true;
        for (int i5 = 0; i5 < i2; i5++) {
            int i6 = bArr[i5] & 255;
            int i7 = bArr2[i5] & 255;
            int i8 = bArr3[i5] & 255;
            z2 = z2 && i6 == i7 && i7 == i8;
            if (bArr4 != null) {
                i3 = bArr4[i5] & 255;
                if (i3 != 255) {
                    if (i3 == 0) {
                        if (i4 == 1) {
                            i4 = 2;
                        }
                        if (this.transparent_index < 0) {
                            this.transparent_index = i5;
                        }
                    } else {
                        i4 = 3;
                    }
                    z2 = false;
                }
            }
            this.rgb[i5] = (i3 << 24) | (i6 << 16) | (i7 << 8) | i8;
        }
        this.allgrayopaque = z2;
        setTransparency(i4);
    }

    private void setRGBs(int i2, int[] iArr, int i3, boolean z2) {
        this.map_size = i2;
        this.rgb = new int[calcRealMapSize(this.pixel_bits, i2)];
        int i4 = i3;
        int i5 = 1;
        boolean z3 = true;
        BigInteger bigInteger = this.validBits;
        int i6 = 0;
        while (i6 < i2) {
            if (bigInteger == null || bigInteger.testBit(i6)) {
                int i7 = iArr[i4];
                int i8 = (i7 >> 8) & 255;
                z3 = z3 && ((i7 >> 16) & 255) == i8 && i8 == (i7 & 255);
                if (z2) {
                    int i9 = i7 >>> 24;
                    if (i9 != 255) {
                        if (i9 == 0) {
                            if (i5 == 1) {
                                i5 = 2;
                            }
                            if (this.transparent_index < 0) {
                                this.transparent_index = i6;
                            }
                        } else {
                            i5 = 3;
                        }
                        z3 = false;
                    }
                } else {
                    i7 |= -16777216;
                }
                this.rgb[i6] = i7;
            }
            i6++;
            i4++;
        }
        this.allgrayopaque = z3;
        setTransparency(i5);
    }

    private int calcRealMapSize(int i2, int i3) {
        return Math.max(Math.max(1 << i2, i3), 256);
    }

    private BigInteger getAllValid() {
        int i2 = (this.map_size + 7) / 8;
        byte[] bArr = new byte[i2];
        Arrays.fill(bArr, (byte) -1);
        bArr[0] = (byte) (255 >>> ((i2 * 8) - this.map_size));
        return new BigInteger(1, bArr);
    }

    @Override // java.awt.image.ColorModel, java.awt.Transparency
    public int getTransparency() {
        return this.transparency;
    }

    @Override // java.awt.image.ColorModel
    public int[] getComponentSize() {
        if (this.nBits == null) {
            if (this.supportsAlpha) {
                this.nBits = new int[4];
                this.nBits[3] = 8;
            } else {
                this.nBits = new int[3];
            }
            int[] iArr = this.nBits;
            int[] iArr2 = this.nBits;
            this.nBits[2] = 8;
            iArr2[1] = 8;
            iArr[0] = 8;
        }
        return (int[]) this.nBits.clone();
    }

    public final int getMapSize() {
        return this.map_size;
    }

    public final int getTransparentPixel() {
        return this.transparent_index;
    }

    public final void getReds(byte[] bArr) {
        for (int i2 = 0; i2 < this.map_size; i2++) {
            bArr[i2] = (byte) (this.rgb[i2] >> 16);
        }
    }

    public final void getGreens(byte[] bArr) {
        for (int i2 = 0; i2 < this.map_size; i2++) {
            bArr[i2] = (byte) (this.rgb[i2] >> 8);
        }
    }

    public final void getBlues(byte[] bArr) {
        for (int i2 = 0; i2 < this.map_size; i2++) {
            bArr[i2] = (byte) this.rgb[i2];
        }
    }

    public final void getAlphas(byte[] bArr) {
        for (int i2 = 0; i2 < this.map_size; i2++) {
            bArr[i2] = (byte) (this.rgb[i2] >> 24);
        }
    }

    public final void getRGBs(int[] iArr) {
        System.arraycopy(this.rgb, 0, iArr, 0, this.map_size);
    }

    private void setTransparentPixel(int i2) {
        if (i2 >= 0 && i2 < this.map_size) {
            int[] iArr = this.rgb;
            iArr[i2] = iArr[i2] & 16777215;
            this.transparent_index = i2;
            this.allgrayopaque = false;
            if (this.transparency == 1) {
                setTransparency(2);
            }
        }
    }

    private void setTransparency(int i2) {
        if (this.transparency != i2) {
            this.transparency = i2;
            if (i2 == 1) {
                this.supportsAlpha = false;
                this.numComponents = 3;
                this.nBits = opaqueBits;
            } else {
                this.supportsAlpha = true;
                this.numComponents = 4;
                this.nBits = alphaBits;
            }
        }
    }

    private final void calculatePixelMask() {
        int i2 = this.pixel_bits;
        if (i2 == 3) {
            i2 = 4;
        } else if (i2 > 4 && i2 < 8) {
            i2 = 8;
        }
        this.pixel_mask = (1 << i2) - 1;
    }

    @Override // java.awt.image.ColorModel
    public final int getRed(int i2) {
        return (this.rgb[i2 & this.pixel_mask] >> 16) & 255;
    }

    @Override // java.awt.image.ColorModel
    public final int getGreen(int i2) {
        return (this.rgb[i2 & this.pixel_mask] >> 8) & 255;
    }

    @Override // java.awt.image.ColorModel
    public final int getBlue(int i2) {
        return this.rgb[i2 & this.pixel_mask] & 255;
    }

    @Override // java.awt.image.ColorModel
    public final int getAlpha(int i2) {
        return (this.rgb[i2 & this.pixel_mask] >> 24) & 255;
    }

    @Override // java.awt.image.ColorModel
    public final int getRGB(int i2) {
        return this.rgb[i2 & this.pixel_mask];
    }

    @Override // java.awt.image.ColorModel
    public synchronized Object getDataElements(int i2, Object obj) {
        int i3;
        int i4;
        int i5 = (i2 >> 16) & 255;
        int i6 = (i2 >> 8) & 255;
        int i7 = i2 & 255;
        int i8 = i2 >>> 24;
        int i9 = 0;
        for (int i10 = 38; i10 >= 0; i10 -= 2) {
            int i11 = this.lookupcache[i10];
            i9 = i11;
            if (i11 == 0) {
                break;
            }
            if (i2 == this.lookupcache[i10 + 1]) {
                return installpixel(obj, i9 ^ (-1));
            }
        }
        if (this.allgrayopaque) {
            int i12 = 256;
            int i13 = ((((i5 * 77) + (i6 * 150)) + (i7 * 29)) + 128) / 256;
            for (int i14 = 0; i14 < this.map_size; i14++) {
                if (this.rgb[i14] != 0) {
                    int i15 = (this.rgb[i14] & 255) - i13;
                    if (i15 < 0) {
                        i15 = -i15;
                    }
                    if (i15 < i12) {
                        i9 = i14;
                        if (i15 == 0) {
                            break;
                        }
                        i12 = i15;
                    } else {
                        continue;
                    }
                }
            }
        } else if (this.transparency == 1) {
            int i16 = Integer.MAX_VALUE;
            int[] iArr = this.rgb;
            int i17 = 0;
            while (true) {
                if (i17 >= this.map_size) {
                    break;
                }
                int i18 = iArr[i17];
                if (i18 != i2 || i18 == 0) {
                    i17++;
                } else {
                    i9 = i17;
                    i16 = 0;
                    break;
                }
            }
            if (i16 != 0) {
                for (int i19 = 0; i19 < this.map_size; i19++) {
                    int i20 = iArr[i19];
                    if (i20 != 0 && (i4 = (i3 = ((i20 >> 16) & 255) - i5) * i3) < i16) {
                        int i21 = ((i20 >> 8) & 255) - i6;
                        int i22 = i4 + (i21 * i21);
                        if (i22 < i16) {
                            int i23 = (i20 & 255) - i7;
                            int i24 = i22 + (i23 * i23);
                            if (i24 < i16) {
                                i9 = i19;
                                i16 = i24;
                            }
                        }
                    }
                }
            }
        } else if (i8 == 0 && this.transparent_index >= 0) {
            i9 = this.transparent_index;
        } else {
            int i25 = Integer.MAX_VALUE;
            int[] iArr2 = this.rgb;
            for (int i26 = 0; i26 < this.map_size; i26++) {
                int i27 = iArr2[i26];
                if (i27 == i2) {
                    if (this.validBits == null || this.validBits.testBit(i26)) {
                        i9 = i26;
                        break;
                    }
                } else {
                    int i28 = ((i27 >> 16) & 255) - i5;
                    int i29 = i28 * i28;
                    if (i29 < i25) {
                        int i30 = ((i27 >> 8) & 255) - i6;
                        int i31 = i29 + (i30 * i30);
                        if (i31 < i25) {
                            int i32 = (i27 & 255) - i7;
                            int i33 = i31 + (i32 * i32);
                            if (i33 < i25) {
                                int i34 = (i27 >>> 24) - i8;
                                int i35 = i33 + (i34 * i34);
                                if (i35 < i25 && (this.validBits == null || this.validBits.testBit(i26))) {
                                    i9 = i26;
                                    i25 = i35;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.arraycopy(this.lookupcache, 2, this.lookupcache, 0, 38);
        this.lookupcache[39] = i2;
        this.lookupcache[38] = i9 ^ (-1);
        return installpixel(obj, i9);
    }

    private Object installpixel(Object obj, int i2) {
        short[] sArr;
        byte[] bArr;
        int[] iArr;
        switch (this.transferType) {
            case 0:
                if (obj == null) {
                    byte[] bArr2 = new byte[1];
                    bArr = bArr2;
                    obj = bArr2;
                } else {
                    bArr = (byte[]) obj;
                }
                bArr[0] = (byte) i2;
                break;
            case 1:
                if (obj == null) {
                    short[] sArr2 = new short[1];
                    sArr = sArr2;
                    obj = sArr2;
                } else {
                    sArr = (short[]) obj;
                }
                sArr[0] = (short) i2;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                if (obj == null) {
                    int[] iArr2 = new int[1];
                    iArr = iArr2;
                    obj = iArr2;
                } else {
                    iArr = (int[]) obj;
                }
                iArr[0] = i2;
                break;
        }
        return obj;
    }

    @Override // java.awt.image.ColorModel
    public int[] getComponents(int i2, int[] iArr, int i3) {
        if (iArr == null) {
            iArr = new int[i3 + this.numComponents];
        }
        iArr[i3 + 0] = getRed(i2);
        iArr[i3 + 1] = getGreen(i2);
        iArr[i3 + 2] = getBlue(i2);
        if (this.supportsAlpha && iArr.length - i3 > 3) {
            iArr[i3 + 3] = getAlpha(i2);
        }
        return iArr;
    }

    @Override // java.awt.image.ColorModel
    public int[] getComponents(Object obj, int[] iArr, int i2) {
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.awt.image.ColorModel
    public int getDataElement(int[] iArr, int i2) {
        int i3;
        int i4;
        int i5 = (iArr[i2 + 0] << 16) | (iArr[i2 + 1] << 8) | iArr[i2 + 2];
        if (this.supportsAlpha) {
            i3 = i5 | (iArr[i2 + 3] << 24);
        } else {
            i3 = i5 | (-16777216);
        }
        Object dataElements = getDataElements(i3, null);
        switch (this.transferType) {
            case 0:
                i4 = (((byte[]) dataElements)[0] & 255) == true ? 1 : 0;
                break;
            case 1:
                i4 = ((short[]) dataElements)[0];
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                i4 = ((int[]) dataElements)[0];
                break;
        }
        return i4;
    }

    @Override // java.awt.image.ColorModel
    public Object getDataElements(int[] iArr, int i2, Object obj) {
        int i3;
        int i4 = (iArr[i2 + 0] << 16) | (iArr[i2 + 1] << 8) | iArr[i2 + 2];
        if (this.supportsAlpha) {
            i3 = i4 | (iArr[i2 + 3] << 24);
        } else {
            i3 = i4 & (-16777216);
        }
        return getDataElements(i3, obj);
    }

    @Override // java.awt.image.ColorModel
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        WritableRaster writableRasterCreatePackedRaster;
        if (this.pixel_bits == 1 || this.pixel_bits == 2 || this.pixel_bits == 4) {
            writableRasterCreatePackedRaster = Raster.createPackedRaster(0, i2, i3, 1, this.pixel_bits, (Point) null);
        } else if (this.pixel_bits <= 8) {
            writableRasterCreatePackedRaster = Raster.createInterleavedRaster(0, i2, i3, 1, null);
        } else if (this.pixel_bits <= 16) {
            writableRasterCreatePackedRaster = Raster.createInterleavedRaster(1, i2, i3, 1, null);
        } else {
            throw new UnsupportedOperationException("This method is not supported  for pixel bits > 16.");
        }
        return writableRasterCreatePackedRaster;
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleRaster(Raster raster) {
        return raster.getTransferType() == this.transferType && raster.getNumBands() == 1 && (1 << raster.getSampleModel().getSampleSize(0)) >= this.map_size;
    }

    @Override // java.awt.image.ColorModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        int[] iArr = {0};
        if (this.pixel_bits == 1 || this.pixel_bits == 2 || this.pixel_bits == 4) {
            return new MultiPixelPackedSampleModel(this.transferType, i2, i3, this.pixel_bits);
        }
        return new ComponentSampleModel(this.transferType, i2, i3, 1, i2, iArr);
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleSampleModel(SampleModel sampleModel) {
        if ((!(sampleModel instanceof ComponentSampleModel) && !(sampleModel instanceof MultiPixelPackedSampleModel)) || sampleModel.getTransferType() != this.transferType || sampleModel.getNumBands() != 1) {
            return false;
        }
        return true;
    }

    public BufferedImage convertToIntDiscrete(Raster raster, boolean z2) {
        ColorModel rGBdefault;
        int[] intArray;
        if (!isCompatibleRaster(raster)) {
            throw new IllegalArgumentException("This raster is not compatiblewith this IndexColorModel.");
        }
        if (z2 || this.transparency == 3) {
            rGBdefault = ColorModel.getRGBdefault();
        } else if (this.transparency == 2) {
            rGBdefault = new DirectColorModel(25, 16711680, NormalizerImpl.CC_MASK, 255, 16777216);
        } else {
            rGBdefault = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
        }
        int width = raster.getWidth();
        int height = raster.getHeight();
        WritableRaster writableRasterCreateCompatibleWritableRaster = rGBdefault.createCompatibleWritableRaster(width, height);
        Object dataElements = null;
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        int i2 = 0;
        while (i2 < height) {
            dataElements = raster.getDataElements(minX, minY, width, 1, dataElements);
            if (dataElements instanceof int[]) {
                intArray = (int[]) dataElements;
            } else {
                intArray = DataBuffer.toIntArray(dataElements);
            }
            for (int i3 = 0; i3 < width; i3++) {
                intArray[i3] = this.rgb[intArray[i3] & this.pixel_mask];
            }
            writableRasterCreateCompatibleWritableRaster.setDataElements(0, i2, width, 1, intArray);
            i2++;
            minY++;
        }
        return new BufferedImage(rGBdefault, writableRasterCreateCompatibleWritableRaster, false, (Hashtable<?, ?>) null);
    }

    public boolean isValid(int i2) {
        return i2 >= 0 && i2 < this.map_size && (this.validBits == null || this.validBits.testBit(i2));
    }

    public boolean isValid() {
        return this.validBits == null;
    }

    public BigInteger getValidPixels() {
        if (this.validBits == null) {
            return getAllValid();
        }
        return this.validBits;
    }

    @Override // java.awt.image.ColorModel
    public void finalize() {
    }

    @Override // java.awt.image.ColorModel
    public String toString() {
        return new String("IndexColorModel: #pixelBits = " + this.pixel_bits + " numComponents = " + this.numComponents + " color space = " + ((Object) this.colorSpace) + " transparency = " + this.transparency + " transIndex   = " + this.transparent_index + " has alpha = " + this.supportsAlpha + " isAlphaPre = " + this.isAlphaPremultiplied);
    }
}
