package java.awt.image;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import sun.java2d.cmm.CMSManager;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.PCMM;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/image/ColorModel.class */
public abstract class ColorModel implements Transparency {
    private long pData;
    protected int pixel_bits;
    int[] nBits;
    int transparency;
    boolean supportsAlpha;
    boolean isAlphaPremultiplied;
    int numComponents;
    int numColorComponents;
    ColorSpace colorSpace;
    int colorSpaceType;
    int maxBits;
    boolean is_sRGB;
    protected int transferType;
    private static boolean loaded = false;
    private static ColorModel RGBdefault;
    static byte[] l8Tos8;
    static byte[] s8Tol8;
    static byte[] l16Tos8;
    static short[] s8Tol16;
    static Map<ICC_ColorSpace, byte[]> g8Tos8Map;
    static Map<ICC_ColorSpace, byte[]> lg16Toog8Map;
    static Map<ICC_ColorSpace, byte[]> g16Tos8Map;
    static Map<ICC_ColorSpace, short[]> lg16Toog16Map;

    private static native void initIDs();

    public abstract int getRed(int i2);

    public abstract int getGreen(int i2);

    public abstract int getBlue(int i2);

    public abstract int getAlpha(int i2);

    static {
        loadLibraries();
        initIDs();
        l8Tos8 = null;
        s8Tol8 = null;
        l16Tos8 = null;
        s8Tol16 = null;
        g8Tos8Map = null;
        lg16Toog8Map = null;
        g16Tos8Map = null;
        lg16Toog16Map = null;
    }

    static void loadLibraries() {
        if (!loaded) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.image.ColorModel.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    System.loadLibrary("awt");
                    return null;
                }
            });
            loaded = true;
        }
    }

    public static ColorModel getRGBdefault() {
        if (RGBdefault == null) {
            RGBdefault = new DirectColorModel(32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216);
        }
        return RGBdefault;
    }

    public ColorModel(int i2) {
        this.transparency = 3;
        this.supportsAlpha = true;
        this.isAlphaPremultiplied = false;
        this.numComponents = -1;
        this.numColorComponents = -1;
        this.colorSpace = ColorSpace.getInstance(1000);
        this.colorSpaceType = 5;
        this.is_sRGB = true;
        this.pixel_bits = i2;
        if (i2 < 1) {
            throw new IllegalArgumentException("Number of bits must be > 0");
        }
        this.numComponents = 4;
        this.numColorComponents = 3;
        this.maxBits = i2;
        this.transferType = getDefaultTransferType(i2);
    }

    protected ColorModel(int i2, int[] iArr, ColorSpace colorSpace, boolean z2, boolean z3, int i3, int i4) {
        this.transparency = 3;
        this.supportsAlpha = true;
        this.isAlphaPremultiplied = false;
        this.numComponents = -1;
        this.numColorComponents = -1;
        this.colorSpace = ColorSpace.getInstance(1000);
        this.colorSpaceType = 5;
        this.is_sRGB = true;
        this.colorSpace = colorSpace;
        this.colorSpaceType = colorSpace.getType();
        this.numColorComponents = colorSpace.getNumComponents();
        this.numComponents = this.numColorComponents + (z2 ? 1 : 0);
        this.supportsAlpha = z2;
        if (iArr.length < this.numComponents) {
            throw new IllegalArgumentException("Number of color/alpha components should be " + this.numComponents + " but length of bits array is " + iArr.length);
        }
        if (i3 < 1 || i3 > 3) {
            throw new IllegalArgumentException("Unknown transparency: " + i3);
        }
        if (!this.supportsAlpha) {
            this.isAlphaPremultiplied = false;
            this.transparency = 1;
        } else {
            this.isAlphaPremultiplied = z3;
            this.transparency = i3;
        }
        this.nBits = (int[]) iArr.clone();
        this.pixel_bits = i2;
        if (i2 <= 0) {
            throw new IllegalArgumentException("Number of pixel bits must be > 0");
        }
        this.maxBits = 0;
        for (int i5 = 0; i5 < iArr.length; i5++) {
            if (iArr[i5] < 0) {
                throw new IllegalArgumentException("Number of bits must be >= 0");
            }
            if (this.maxBits < iArr[i5]) {
                this.maxBits = iArr[i5];
            }
        }
        if (this.maxBits == 0) {
            throw new IllegalArgumentException("There must be at least one component with > 0 pixel bits.");
        }
        if (colorSpace != ColorSpace.getInstance(1000)) {
            this.is_sRGB = false;
        }
        this.transferType = i4;
    }

    public final boolean hasAlpha() {
        return this.supportsAlpha;
    }

    public final boolean isAlphaPremultiplied() {
        return this.isAlphaPremultiplied;
    }

    public final int getTransferType() {
        return this.transferType;
    }

    public int getPixelSize() {
        return this.pixel_bits;
    }

    public int getComponentSize(int i2) {
        if (this.nBits == null) {
            throw new NullPointerException("Number of bits array is null.");
        }
        return this.nBits[i2];
    }

    public int[] getComponentSize() {
        if (this.nBits != null) {
            return (int[]) this.nBits.clone();
        }
        return null;
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return this.transparency;
    }

    public int getNumComponents() {
        return this.numComponents;
    }

    public int getNumColorComponents() {
        return this.numColorComponents;
    }

    public int getRGB(int i2) {
        return (getAlpha(i2) << 24) | (getRed(i2) << 16) | (getGreen(i2) << 8) | (getBlue(i2) << 0);
    }

    public int getRed(Object obj) {
        int i2;
        int length;
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                i2 = bArr[0] & 255;
                length = bArr.length;
                break;
            case 1:
                short[] sArr = (short[]) obj;
                i2 = sArr[0] & 65535;
                length = sArr.length;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                int[] iArr = (int[]) obj;
                i2 = iArr[0];
                length = iArr.length;
                break;
        }
        if (length == 1) {
            return getRed(i2);
        }
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public int getGreen(Object obj) {
        int i2;
        int length;
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                i2 = bArr[0] & 255;
                length = bArr.length;
                break;
            case 1:
                short[] sArr = (short[]) obj;
                i2 = sArr[0] & 65535;
                length = sArr.length;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                int[] iArr = (int[]) obj;
                i2 = iArr[0];
                length = iArr.length;
                break;
        }
        if (length == 1) {
            return getGreen(i2);
        }
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public int getBlue(Object obj) {
        int i2;
        int length;
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                i2 = bArr[0] & 255;
                length = bArr.length;
                break;
            case 1:
                short[] sArr = (short[]) obj;
                i2 = sArr[0] & 65535;
                length = sArr.length;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                int[] iArr = (int[]) obj;
                i2 = iArr[0];
                length = iArr.length;
                break;
        }
        if (length == 1) {
            return getBlue(i2);
        }
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public int getAlpha(Object obj) {
        int i2;
        int length;
        switch (this.transferType) {
            case 0:
                byte[] bArr = (byte[]) obj;
                i2 = bArr[0] & 255;
                length = bArr.length;
                break;
            case 1:
                short[] sArr = (short[]) obj;
                i2 = sArr[0] & 65535;
                length = sArr.length;
                break;
            case 2:
            default:
                throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
            case 3:
                int[] iArr = (int[]) obj;
                i2 = iArr[0];
                length = iArr.length;
                break;
        }
        if (length == 1) {
            return getAlpha(i2);
        }
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public int getRGB(Object obj) {
        return (getAlpha(obj) << 24) | (getRed(obj) << 16) | (getGreen(obj) << 8) | (getBlue(obj) << 0);
    }

    public Object getDataElements(int i2, Object obj) {
        throw new UnsupportedOperationException("This method is not supported by this color model.");
    }

    public int[] getComponents(int i2, int[] iArr, int i3) {
        throw new UnsupportedOperationException("This method is not supported by this color model.");
    }

    public int[] getComponents(Object obj, int[] iArr, int i2) {
        throw new UnsupportedOperationException("This method is not supported by this color model.");
    }

    public int[] getUnnormalizedComponents(float[] fArr, int i2, int[] iArr, int i3) {
        if (this.colorSpace == null) {
            throw new UnsupportedOperationException("This method is not supported by this color model.");
        }
        if (this.nBits == null) {
            throw new UnsupportedOperationException("This method is not supported.  Unable to determine #bits per component.");
        }
        if (fArr.length - i2 < this.numComponents) {
            throw new IllegalArgumentException("Incorrect number of components.  Expecting " + this.numComponents);
        }
        if (iArr == null) {
            iArr = new int[i3 + this.numComponents];
        }
        if (this.supportsAlpha && this.isAlphaPremultiplied) {
            float f2 = fArr[i2 + this.numColorComponents];
            for (int i4 = 0; i4 < this.numColorComponents; i4++) {
                iArr[i3 + i4] = (int) ((fArr[i2 + i4] * ((1 << this.nBits[i4]) - 1) * f2) + 0.5f);
            }
            iArr[i3 + this.numColorComponents] = (int) ((f2 * ((1 << this.nBits[this.numColorComponents]) - 1)) + 0.5f);
        } else {
            for (int i5 = 0; i5 < this.numComponents; i5++) {
                iArr[i3 + i5] = (int) ((fArr[i2 + i5] * ((1 << this.nBits[i5]) - 1)) + 0.5f);
            }
        }
        return iArr;
    }

    public float[] getNormalizedComponents(int[] iArr, int i2, float[] fArr, int i3) {
        if (this.colorSpace == null) {
            throw new UnsupportedOperationException("This method is not supported by this color model.");
        }
        if (this.nBits == null) {
            throw new UnsupportedOperationException("This method is not supported.  Unable to determine #bits per component.");
        }
        if (iArr.length - i2 < this.numComponents) {
            throw new IllegalArgumentException("Incorrect number of components.  Expecting " + this.numComponents);
        }
        if (fArr == null) {
            fArr = new float[this.numComponents + i3];
        }
        if (this.supportsAlpha && this.isAlphaPremultiplied) {
            float f2 = iArr[i2 + this.numColorComponents] / ((1 << this.nBits[this.numColorComponents]) - 1);
            if (f2 != 0.0f) {
                for (int i4 = 0; i4 < this.numColorComponents; i4++) {
                    fArr[i3 + i4] = iArr[i2 + i4] / (f2 * ((1 << this.nBits[i4]) - 1));
                }
            } else {
                for (int i5 = 0; i5 < this.numColorComponents; i5++) {
                    fArr[i3 + i5] = 0.0f;
                }
            }
            fArr[i3 + this.numColorComponents] = f2;
        } else {
            for (int i6 = 0; i6 < this.numComponents; i6++) {
                fArr[i3 + i6] = iArr[i2 + i6] / ((1 << this.nBits[i6]) - 1);
            }
        }
        return fArr;
    }

    public int getDataElement(int[] iArr, int i2) {
        throw new UnsupportedOperationException("This method is not supported by this color model.");
    }

    public Object getDataElements(int[] iArr, int i2, Object obj) {
        throw new UnsupportedOperationException("This method has not been implemented for this color model.");
    }

    public int getDataElement(float[] fArr, int i2) {
        return getDataElement(getUnnormalizedComponents(fArr, i2, null, 0), 0);
    }

    public Object getDataElements(float[] fArr, int i2, Object obj) {
        return getDataElements(getUnnormalizedComponents(fArr, i2, null, 0), 0, obj);
    }

    public float[] getNormalizedComponents(Object obj, float[] fArr, int i2) {
        return getNormalizedComponents(getComponents(obj, (int[]) null, 0), 0, fArr, i2);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ColorModel)) {
            return false;
        }
        ColorModel colorModel = (ColorModel) obj;
        if (this == colorModel) {
            return true;
        }
        if (this.supportsAlpha != colorModel.hasAlpha() || this.isAlphaPremultiplied != colorModel.isAlphaPremultiplied() || this.pixel_bits != colorModel.getPixelSize() || this.transparency != colorModel.getTransparency() || this.numComponents != colorModel.getNumComponents()) {
            return false;
        }
        int[] componentSize = colorModel.getComponentSize();
        if (this.nBits == null || componentSize == null) {
            return this.nBits == null && componentSize == null;
        }
        for (int i2 = 0; i2 < this.numComponents; i2++) {
            if (this.nBits[i2] != componentSize[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i2 = (this.supportsAlpha ? 2 : 3) + (this.isAlphaPremultiplied ? 4 : 5) + (this.pixel_bits * 6) + (this.transparency * 7) + (this.numComponents * 8);
        if (this.nBits != null) {
            for (int i3 = 0; i3 < this.numComponents; i3++) {
                i2 += this.nBits[i3] * (i3 + 9);
            }
        }
        return i2;
    }

    public final ColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public ColorModel coerceData(WritableRaster writableRaster, boolean z2) {
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public boolean isCompatibleRaster(Raster raster) {
        throw new UnsupportedOperationException("This method has not been implemented for this ColorModel.");
    }

    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public boolean isCompatibleSampleModel(SampleModel sampleModel) {
        throw new UnsupportedOperationException("This method is not supported by this color model");
    }

    public void finalize() {
    }

    public WritableRaster getAlphaRaster(WritableRaster writableRaster) {
        return null;
    }

    public String toString() {
        return new String("ColorModel: #pixelBits = " + this.pixel_bits + " numComponents = " + this.numComponents + " color space = " + ((Object) this.colorSpace) + " transparency = " + this.transparency + " has alpha = " + this.supportsAlpha + " isAlphaPre = " + this.isAlphaPremultiplied);
    }

    static int getDefaultTransferType(int i2) {
        if (i2 <= 8) {
            return 0;
        }
        if (i2 <= 16) {
            return 1;
        }
        if (i2 <= 32) {
            return 3;
        }
        return 32;
    }

    static boolean isLinearRGBspace(ColorSpace colorSpace) {
        return colorSpace == CMSManager.LINEAR_RGBspace;
    }

    static boolean isLinearGRAYspace(ColorSpace colorSpace) {
        return colorSpace == CMSManager.GRAYspace;
    }

    static byte[] getLinearRGB8TosRGB8LUT() {
        float fPow;
        if (l8Tos8 == null) {
            l8Tos8 = new byte[256];
            for (int i2 = 0; i2 <= 255; i2++) {
                float f2 = i2 / 255.0f;
                if (f2 <= 0.0031308f) {
                    fPow = f2 * 12.92f;
                } else {
                    fPow = (1.055f * ((float) Math.pow(f2, 0.4166666666666667d))) - 0.055f;
                }
                l8Tos8[i2] = (byte) Math.round(fPow * 255.0f);
            }
        }
        return l8Tos8;
    }

    static byte[] getsRGB8ToLinearRGB8LUT() {
        float fPow;
        if (s8Tol8 == null) {
            s8Tol8 = new byte[256];
            for (int i2 = 0; i2 <= 255; i2++) {
                float f2 = i2 / 255.0f;
                if (f2 <= 0.04045f) {
                    fPow = f2 / 12.92f;
                } else {
                    fPow = (float) Math.pow((f2 + 0.055f) / 1.055f, 2.4d);
                }
                s8Tol8[i2] = (byte) Math.round(fPow * 255.0f);
            }
        }
        return s8Tol8;
    }

    static byte[] getLinearRGB16TosRGB8LUT() {
        float fPow;
        if (l16Tos8 == null) {
            l16Tos8 = new byte[65536];
            for (int i2 = 0; i2 <= 65535; i2++) {
                float f2 = i2 / 65535.0f;
                if (f2 <= 0.0031308f) {
                    fPow = f2 * 12.92f;
                } else {
                    fPow = (1.055f * ((float) Math.pow(f2, 0.4166666666666667d))) - 0.055f;
                }
                l16Tos8[i2] = (byte) Math.round(fPow * 255.0f);
            }
        }
        return l16Tos8;
    }

    static short[] getsRGB8ToLinearRGB16LUT() {
        float fPow;
        if (s8Tol16 == null) {
            s8Tol16 = new short[256];
            for (int i2 = 0; i2 <= 255; i2++) {
                float f2 = i2 / 255.0f;
                if (f2 <= 0.04045f) {
                    fPow = f2 / 12.92f;
                } else {
                    fPow = (float) Math.pow((f2 + 0.055f) / 1.055f, 2.4d);
                }
                s8Tol16[i2] = (short) Math.round(fPow * 65535.0f);
            }
        }
        return s8Tol16;
    }

    static byte[] getGray8TosRGB8LUT(ICC_ColorSpace iCC_ColorSpace) {
        byte[] bArr;
        if (isLinearGRAYspace(iCC_ColorSpace)) {
            return getLinearRGB8TosRGB8LUT();
        }
        if (g8Tos8Map != null && (bArr = g8Tos8Map.get(iCC_ColorSpace)) != null) {
            return bArr;
        }
        byte[] bArr2 = new byte[256];
        for (int i2 = 0; i2 <= 255; i2++) {
            bArr2[i2] = (byte) i2;
        }
        PCMM module = CMSManager.getModule();
        byte[] bArrColorConvert = module.createTransform(new ColorTransform[]{module.createTransform(iCC_ColorSpace.getProfile(), -1, 1), module.createTransform(((ICC_ColorSpace) ColorSpace.getInstance(1000)).getProfile(), -1, 2)}).colorConvert(bArr2, (byte[]) null);
        int i3 = 0;
        int i4 = 2;
        while (i3 <= 255) {
            bArr2[i3] = bArrColorConvert[i4];
            i3++;
            i4 += 3;
        }
        if (g8Tos8Map == null) {
            g8Tos8Map = Collections.synchronizedMap(new WeakHashMap(2));
        }
        g8Tos8Map.put(iCC_ColorSpace, bArr2);
        return bArr2;
    }

    static byte[] getLinearGray16ToOtherGray8LUT(ICC_ColorSpace iCC_ColorSpace) {
        byte[] bArr;
        if (lg16Toog8Map != null && (bArr = lg16Toog8Map.get(iCC_ColorSpace)) != null) {
            return bArr;
        }
        short[] sArr = new short[65536];
        for (int i2 = 0; i2 <= 65535; i2++) {
            sArr[i2] = (short) i2;
        }
        PCMM module = CMSManager.getModule();
        short[] sArrColorConvert = module.createTransform(new ColorTransform[]{module.createTransform(((ICC_ColorSpace) ColorSpace.getInstance(1003)).getProfile(), -1, 1), module.createTransform(iCC_ColorSpace.getProfile(), -1, 2)}).colorConvert(sArr, (short[]) null);
        byte[] bArr2 = new byte[65536];
        for (int i3 = 0; i3 <= 65535; i3++) {
            bArr2[i3] = (byte) (((sArrColorConvert[i3] & 65535) * 0.0038910506f) + 0.5f);
        }
        if (lg16Toog8Map == null) {
            lg16Toog8Map = Collections.synchronizedMap(new WeakHashMap(2));
        }
        lg16Toog8Map.put(iCC_ColorSpace, bArr2);
        return bArr2;
    }

    static byte[] getGray16TosRGB8LUT(ICC_ColorSpace iCC_ColorSpace) {
        byte[] bArr;
        if (isLinearGRAYspace(iCC_ColorSpace)) {
            return getLinearRGB16TosRGB8LUT();
        }
        if (g16Tos8Map != null && (bArr = g16Tos8Map.get(iCC_ColorSpace)) != null) {
            return bArr;
        }
        short[] sArr = new short[65536];
        for (int i2 = 0; i2 <= 65535; i2++) {
            sArr[i2] = (short) i2;
        }
        PCMM module = CMSManager.getModule();
        short[] sArrColorConvert = module.createTransform(new ColorTransform[]{module.createTransform(iCC_ColorSpace.getProfile(), -1, 1), module.createTransform(((ICC_ColorSpace) ColorSpace.getInstance(1000)).getProfile(), -1, 2)}).colorConvert(sArr, (short[]) null);
        byte[] bArr2 = new byte[65536];
        int i3 = 0;
        int i4 = 2;
        while (i3 <= 65535) {
            bArr2[i3] = (byte) (((sArrColorConvert[i4] & 65535) * 0.0038910506f) + 0.5f);
            i3++;
            i4 += 3;
        }
        if (g16Tos8Map == null) {
            g16Tos8Map = Collections.synchronizedMap(new WeakHashMap(2));
        }
        g16Tos8Map.put(iCC_ColorSpace, bArr2);
        return bArr2;
    }

    static short[] getLinearGray16ToOtherGray16LUT(ICC_ColorSpace iCC_ColorSpace) {
        short[] sArr;
        if (lg16Toog16Map != null && (sArr = lg16Toog16Map.get(iCC_ColorSpace)) != null) {
            return sArr;
        }
        short[] sArr2 = new short[65536];
        for (int i2 = 0; i2 <= 65535; i2++) {
            sArr2[i2] = (short) i2;
        }
        PCMM module = CMSManager.getModule();
        short[] sArrColorConvert = module.createTransform(new ColorTransform[]{module.createTransform(((ICC_ColorSpace) ColorSpace.getInstance(1003)).getProfile(), -1, 1), module.createTransform(iCC_ColorSpace.getProfile(), -1, 2)}).colorConvert(sArr2, (short[]) null);
        if (lg16Toog16Map == null) {
            lg16Toog16Map = Collections.synchronizedMap(new WeakHashMap(2));
        }
        lg16Toog16Map.put(iCC_ColorSpace, sArrColorConvert);
        return sArrColorConvert;
    }
}
