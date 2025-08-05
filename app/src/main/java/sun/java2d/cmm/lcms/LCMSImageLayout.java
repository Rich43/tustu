package sun.java2d.cmm.lcms;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.Raster;
import sun.awt.image.ByteComponentRaster;
import sun.awt.image.IntegerComponentRaster;
import sun.awt.image.ShortComponentRaster;

/* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSImageLayout.class */
class LCMSImageLayout {
    public static final int SWAPFIRST = 16384;
    public static final int DOSWAP = 1024;
    public static final int PT_RGB_8 = CHANNELS_SH(3) | BYTES_SH(1);
    public static final int PT_GRAY_8 = CHANNELS_SH(1) | BYTES_SH(1);
    public static final int PT_GRAY_16 = CHANNELS_SH(1) | BYTES_SH(2);
    public static final int PT_RGBA_8 = (EXTRA_SH(1) | CHANNELS_SH(3)) | BYTES_SH(1);
    public static final int PT_ARGB_8 = ((EXTRA_SH(1) | CHANNELS_SH(3)) | BYTES_SH(1)) | 16384;
    public static final int PT_BGR_8 = (1024 | CHANNELS_SH(3)) | BYTES_SH(1);
    public static final int PT_ABGR_8 = ((1024 | EXTRA_SH(1)) | CHANNELS_SH(3)) | BYTES_SH(1);
    public static final int PT_BGRA_8 = (((EXTRA_SH(1) | CHANNELS_SH(3)) | BYTES_SH(1)) | 1024) | 16384;
    public static final int DT_BYTE = 0;
    public static final int DT_SHORT = 1;
    public static final int DT_INT = 2;
    public static final int DT_DOUBLE = 3;
    boolean isIntPacked;
    int pixelType;
    int dataType;
    int width;
    int height;
    int nextRowOffset;
    private int nextPixelOffset;
    int offset;
    private boolean imageAtOnce;
    Object dataArray;
    private int dataArrayLength;

    public static int BYTES_SH(int i2) {
        return i2;
    }

    public static int EXTRA_SH(int i2) {
        return i2 << 7;
    }

    public static int CHANNELS_SH(int i2) {
        return i2 << 3;
    }

    private LCMSImageLayout(int i2, int i3, int i4) throws ImageLayoutException {
        this.isIntPacked = false;
        this.imageAtOnce = false;
        this.pixelType = i3;
        this.width = i2;
        this.height = 1;
        this.nextPixelOffset = i4;
        this.nextRowOffset = safeMult(i4, i2);
        this.offset = 0;
    }

    private LCMSImageLayout(int i2, int i3, int i4, int i5) throws ImageLayoutException {
        this.isIntPacked = false;
        this.imageAtOnce = false;
        this.pixelType = i4;
        this.width = i2;
        this.height = i3;
        this.nextPixelOffset = i5;
        this.nextRowOffset = safeMult(i5, i2);
        this.offset = 0;
    }

    public LCMSImageLayout(byte[] bArr, int i2, int i3, int i4) throws ImageLayoutException {
        this(i2, i3, i4);
        this.dataType = 0;
        this.dataArray = bArr;
        this.dataArrayLength = bArr.length;
        verify();
    }

    public LCMSImageLayout(short[] sArr, int i2, int i3, int i4) throws ImageLayoutException {
        this(i2, i3, i4);
        this.dataType = 1;
        this.dataArray = sArr;
        this.dataArrayLength = 2 * sArr.length;
        verify();
    }

    public LCMSImageLayout(int[] iArr, int i2, int i3, int i4) throws ImageLayoutException {
        this(i2, i3, i4);
        this.dataType = 2;
        this.dataArray = iArr;
        this.dataArrayLength = 4 * iArr.length;
        verify();
    }

    public LCMSImageLayout(double[] dArr, int i2, int i3, int i4) throws ImageLayoutException {
        this(i2, i3, i4);
        this.dataType = 3;
        this.dataArray = dArr;
        this.dataArrayLength = 8 * dArr.length;
        verify();
    }

    private LCMSImageLayout() {
        this.isIntPacked = false;
        this.imageAtOnce = false;
    }

    public static LCMSImageLayout createImageLayout(BufferedImage bufferedImage) throws ImageLayoutException {
        LCMSImageLayout lCMSImageLayout = new LCMSImageLayout();
        switch (bufferedImage.getType()) {
            case 1:
                lCMSImageLayout.pixelType = PT_ARGB_8;
                lCMSImageLayout.isIntPacked = true;
                break;
            case 2:
                lCMSImageLayout.pixelType = PT_ARGB_8;
                lCMSImageLayout.isIntPacked = true;
                break;
            case 3:
            case 7:
            case 8:
            case 9:
            default:
                ColorModel colorModel = bufferedImage.getColorModel();
                if (colorModel instanceof ComponentColorModel) {
                    for (int i2 : ((ComponentColorModel) colorModel).getComponentSize()) {
                        if (i2 != 8) {
                            return null;
                        }
                    }
                    return createImageLayout(bufferedImage.getRaster());
                }
                return null;
            case 4:
                lCMSImageLayout.pixelType = PT_ABGR_8;
                lCMSImageLayout.isIntPacked = true;
                break;
            case 5:
                lCMSImageLayout.pixelType = PT_BGR_8;
                break;
            case 6:
                lCMSImageLayout.pixelType = PT_ABGR_8;
                break;
            case 10:
                lCMSImageLayout.pixelType = PT_GRAY_8;
                break;
            case 11:
                lCMSImageLayout.pixelType = PT_GRAY_16;
                break;
        }
        lCMSImageLayout.width = bufferedImage.getWidth();
        lCMSImageLayout.height = bufferedImage.getHeight();
        switch (bufferedImage.getType()) {
            case 1:
            case 2:
            case 4:
                IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) bufferedImage.getRaster();
                lCMSImageLayout.nextRowOffset = safeMult(4, integerComponentRaster.getScanlineStride());
                lCMSImageLayout.nextPixelOffset = safeMult(4, integerComponentRaster.getPixelStride());
                lCMSImageLayout.offset = safeMult(4, integerComponentRaster.getDataOffset(0));
                lCMSImageLayout.dataArray = integerComponentRaster.getDataStorage();
                lCMSImageLayout.dataArrayLength = 4 * integerComponentRaster.getDataStorage().length;
                lCMSImageLayout.dataType = 2;
                if (lCMSImageLayout.nextRowOffset == lCMSImageLayout.width * 4 * integerComponentRaster.getPixelStride()) {
                    lCMSImageLayout.imageAtOnce = true;
                    break;
                }
                break;
            case 3:
            case 7:
            case 8:
            case 9:
            default:
                return null;
            case 5:
            case 6:
                ByteComponentRaster byteComponentRaster = (ByteComponentRaster) bufferedImage.getRaster();
                lCMSImageLayout.nextRowOffset = byteComponentRaster.getScanlineStride();
                lCMSImageLayout.nextPixelOffset = byteComponentRaster.getPixelStride();
                lCMSImageLayout.offset = byteComponentRaster.getDataOffset(bufferedImage.getSampleModel().getNumBands() - 1);
                lCMSImageLayout.dataArray = byteComponentRaster.getDataStorage();
                lCMSImageLayout.dataArrayLength = byteComponentRaster.getDataStorage().length;
                lCMSImageLayout.dataType = 0;
                if (lCMSImageLayout.nextRowOffset == lCMSImageLayout.width * byteComponentRaster.getPixelStride()) {
                    lCMSImageLayout.imageAtOnce = true;
                    break;
                }
                break;
            case 10:
                ByteComponentRaster byteComponentRaster2 = (ByteComponentRaster) bufferedImage.getRaster();
                lCMSImageLayout.nextRowOffset = byteComponentRaster2.getScanlineStride();
                lCMSImageLayout.nextPixelOffset = byteComponentRaster2.getPixelStride();
                lCMSImageLayout.dataArrayLength = byteComponentRaster2.getDataStorage().length;
                lCMSImageLayout.offset = byteComponentRaster2.getDataOffset(0);
                lCMSImageLayout.dataArray = byteComponentRaster2.getDataStorage();
                lCMSImageLayout.dataType = 0;
                if (lCMSImageLayout.nextRowOffset == lCMSImageLayout.width * byteComponentRaster2.getPixelStride()) {
                    lCMSImageLayout.imageAtOnce = true;
                    break;
                }
                break;
            case 11:
                ShortComponentRaster shortComponentRaster = (ShortComponentRaster) bufferedImage.getRaster();
                lCMSImageLayout.nextRowOffset = safeMult(2, shortComponentRaster.getScanlineStride());
                lCMSImageLayout.nextPixelOffset = safeMult(2, shortComponentRaster.getPixelStride());
                lCMSImageLayout.offset = safeMult(2, shortComponentRaster.getDataOffset(0));
                lCMSImageLayout.dataArray = shortComponentRaster.getDataStorage();
                lCMSImageLayout.dataArrayLength = 2 * shortComponentRaster.getDataStorage().length;
                lCMSImageLayout.dataType = 1;
                if (lCMSImageLayout.nextRowOffset == lCMSImageLayout.width * 2 * shortComponentRaster.getPixelStride()) {
                    lCMSImageLayout.imageAtOnce = true;
                    break;
                }
                break;
        }
        lCMSImageLayout.verify();
        return lCMSImageLayout;
    }

    /* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSImageLayout$BandOrder.class */
    private enum BandOrder {
        DIRECT,
        INVERTED,
        ARBITRARY,
        UNKNOWN;

        public static BandOrder getBandOrder(int[] iArr) {
            BandOrder bandOrder = UNKNOWN;
            int length = iArr.length;
            for (int i2 = 0; bandOrder != ARBITRARY && i2 < iArr.length; i2++) {
                switch (bandOrder) {
                    case UNKNOWN:
                        if (iArr[i2] == i2) {
                            bandOrder = DIRECT;
                            break;
                        } else if (iArr[i2] == (length - 1) - i2) {
                            bandOrder = INVERTED;
                            break;
                        } else {
                            bandOrder = ARBITRARY;
                            break;
                        }
                    case DIRECT:
                        if (iArr[i2] != i2) {
                            bandOrder = ARBITRARY;
                            break;
                        } else {
                            break;
                        }
                    case INVERTED:
                        if (iArr[i2] != (length - 1) - i2) {
                            bandOrder = ARBITRARY;
                            break;
                        } else {
                            break;
                        }
                }
            }
            return bandOrder;
        }
    }

    private void verify() throws ImageLayoutException {
        if (this.offset < 0 || this.offset >= this.dataArrayLength) {
            throw new ImageLayoutException("Invalid image layout");
        }
        if (this.nextPixelOffset != getBytesPerPixel(this.pixelType)) {
            throw new ImageLayoutException("Invalid image layout");
        }
        int iSafeAdd = safeAdd(this.offset, safeAdd(safeMult(this.nextPixelOffset, this.width - 1), safeMult(this.nextRowOffset, this.height - 1)));
        if (iSafeAdd < 0 || iSafeAdd >= this.dataArrayLength) {
            throw new ImageLayoutException("Invalid image layout");
        }
    }

    static int safeAdd(int i2, int i3) throws ImageLayoutException {
        long j2 = i2 + i3;
        if (j2 < -2147483648L || j2 > 2147483647L) {
            throw new ImageLayoutException("Invalid image layout");
        }
        return (int) j2;
    }

    static int safeMult(int i2, int i3) throws ImageLayoutException {
        long j2 = i2 * i3;
        if (j2 < -2147483648L || j2 > 2147483647L) {
            throw new ImageLayoutException("Invalid image layout");
        }
        return (int) j2;
    }

    /* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSImageLayout$ImageLayoutException.class */
    public static class ImageLayoutException extends Exception {
        public ImageLayoutException(String str) {
            super(str);
        }
    }

    public static LCMSImageLayout createImageLayout(Raster raster) {
        LCMSImageLayout lCMSImageLayout = new LCMSImageLayout();
        if ((raster instanceof ByteComponentRaster) && (raster.getSampleModel() instanceof ComponentSampleModel)) {
            ByteComponentRaster byteComponentRaster = (ByteComponentRaster) raster;
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) raster.getSampleModel();
            lCMSImageLayout.pixelType = CHANNELS_SH(byteComponentRaster.getNumBands()) | BYTES_SH(1);
            int numBands = 0;
            switch (BandOrder.getBandOrder(componentSampleModel.getBandOffsets())) {
                case DIRECT:
                    break;
                case INVERTED:
                    lCMSImageLayout.pixelType |= 1024;
                    numBands = componentSampleModel.getNumBands() - 1;
                    break;
                default:
                    return null;
            }
            lCMSImageLayout.nextRowOffset = byteComponentRaster.getScanlineStride();
            lCMSImageLayout.nextPixelOffset = byteComponentRaster.getPixelStride();
            lCMSImageLayout.offset = byteComponentRaster.getDataOffset(numBands);
            lCMSImageLayout.dataArray = byteComponentRaster.getDataStorage();
            lCMSImageLayout.dataType = 0;
            lCMSImageLayout.width = byteComponentRaster.getWidth();
            lCMSImageLayout.height = byteComponentRaster.getHeight();
            if (lCMSImageLayout.nextRowOffset == lCMSImageLayout.width * byteComponentRaster.getPixelStride()) {
                lCMSImageLayout.imageAtOnce = true;
            }
            return lCMSImageLayout;
        }
        return null;
    }

    private static int getBytesPerPixel(int i2) {
        return (7 & i2) * ((15 & (i2 >> 3)) + (7 & (i2 >> 7)));
    }
}
