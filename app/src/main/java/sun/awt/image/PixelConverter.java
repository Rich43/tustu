package sun.awt.image;

import com.sun.org.apache.xpath.internal.axes.WalkerFactory;
import java.awt.image.ColorModel;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/image/PixelConverter.class */
public class PixelConverter {
    public static final PixelConverter instance = new PixelConverter();
    protected int alphaMask = 0;

    protected PixelConverter() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int rgbToPixel(int i2, ColorModel colorModel) {
        Object dataElements = colorModel.getDataElements(i2, null);
        switch (colorModel.getTransferType()) {
            case 0:
                byte[] bArr = (byte[]) dataElements;
                int i3 = 0;
                switch (bArr.length) {
                    case 1:
                        break;
                    case 2:
                        i3 |= (bArr[1] & 255) << 8;
                        break;
                    case 3:
                        i3 |= (bArr[2] & 255) << 16;
                        i3 |= (bArr[1] & 255) << 8;
                        break;
                    default:
                        i3 = bArr[3] << 24;
                        i3 |= (bArr[2] & 255) << 16;
                        i3 |= (bArr[1] & 255) << 8;
                        break;
                }
                return i3 | (bArr[0] & 255);
            case 1:
            case 2:
                short[] sArr = (short[]) dataElements;
                return (sArr.length > 1 ? sArr[1] << 16 : 0) | (sArr[0] & 65535);
            case 3:
                return ((int[]) dataElements)[0];
            default:
                return i2;
        }
    }

    public int pixelToRgb(int i2, ColorModel colorModel) {
        return i2;
    }

    public final int getAlphaMask() {
        return this.alphaMask;
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Rgbx.class */
    public static class Rgbx extends PixelConverter {
        public static final PixelConverter instance = new Rgbx();

        private Rgbx() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return i2 << 8;
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (-16777216) | (i2 >> 8);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Xrgb.class */
    public static class Xrgb extends PixelConverter {
        public static final PixelConverter instance = new Xrgb();

        private Xrgb() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return i2;
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (-16777216) | i2;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Argb.class */
    public static class Argb extends PixelConverter {
        public static final PixelConverter instance = new Argb();

        private Argb() {
            this.alphaMask = -16777216;
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return i2;
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return i2;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Ushort565Rgb.class */
    public static class Ushort565Rgb extends PixelConverter {
        public static final PixelConverter instance = new Ushort565Rgb();

        private Ushort565Rgb() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return ((i2 >> 8) & 63488) | ((i2 >> 5) & 2016) | ((i2 >> 3) & 31);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = (i2 >> 11) & 31;
            int i4 = (i3 << 3) | (i3 >> 2);
            int i5 = (i2 >> 5) & 63;
            int i6 = (i5 << 2) | (i5 >> 4);
            int i7 = i2 & 31;
            return (-16777216) | (i4 << 16) | (i6 << 8) | (i7 << 3) | (i7 >> 2);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Ushort555Rgbx.class */
    public static class Ushort555Rgbx extends PixelConverter {
        public static final PixelConverter instance = new Ushort555Rgbx();

        private Ushort555Rgbx() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return ((i2 >> 8) & 63488) | ((i2 >> 5) & 1984) | ((i2 >> 2) & 62);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = (i2 >> 11) & 31;
            int i4 = (i3 << 3) | (i3 >> 2);
            int i5 = (i2 >> 6) & 31;
            int i6 = (i5 << 3) | (i5 >> 2);
            int i7 = (i2 >> 1) & 31;
            return (-16777216) | (i4 << 16) | (i6 << 8) | (i7 << 3) | (i7 >> 2);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Ushort555Rgb.class */
    public static class Ushort555Rgb extends PixelConverter {
        public static final PixelConverter instance = new Ushort555Rgb();

        private Ushort555Rgb() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return ((i2 >> 9) & 31744) | ((i2 >> 6) & 992) | ((i2 >> 3) & 31);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = (i2 >> 10) & 31;
            int i4 = (i3 << 3) | (i3 >> 2);
            int i5 = (i2 >> 5) & 31;
            int i6 = (i5 << 3) | (i5 >> 2);
            int i7 = i2 & 31;
            return (-16777216) | (i4 << 16) | (i6 << 8) | (i7 << 3) | (i7 >> 2);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Ushort4444Argb.class */
    public static class Ushort4444Argb extends PixelConverter {
        public static final PixelConverter instance = new Ushort4444Argb();

        private Ushort4444Argb() {
            this.alphaMask = 61440;
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return ((i2 >> 16) & 61440) | ((i2 >> 12) & WalkerFactory.BITS_RESERVED) | ((i2 >> 8) & 240) | ((i2 >> 4) & 15);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = i2 & 61440;
            int i4 = ((i2 << 16) | (i2 << 12)) & (-16777216);
            int i5 = i2 & WalkerFactory.BITS_RESERVED;
            int i6 = ((i2 << 12) | (i2 << 8)) & 16711680;
            int i7 = i2 & 240;
            int i8 = i2 & 15;
            return i4 | i6 | (((i2 << 8) | (i2 << 4)) & NormalizerImpl.CC_MASK) | (((i2 << 4) | (i2 << 0)) & 255);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Xbgr.class */
    public static class Xbgr extends PixelConverter {
        public static final PixelConverter instance = new Xbgr();

        private Xbgr() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return ((i2 & 255) << 16) | (i2 & NormalizerImpl.CC_MASK) | ((i2 >> 16) & 255);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (-16777216) | ((i2 & 255) << 16) | (i2 & NormalizerImpl.CC_MASK) | ((i2 >> 16) & 255);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Bgrx.class */
    public static class Bgrx extends PixelConverter {
        public static final PixelConverter instance = new Bgrx();

        private Bgrx() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return (i2 << 24) | ((i2 & NormalizerImpl.CC_MASK) << 8) | ((i2 >> 8) & NormalizerImpl.CC_MASK);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (-16777216) | ((i2 & NormalizerImpl.CC_MASK) << 8) | ((i2 >> 8) & NormalizerImpl.CC_MASK) | (i2 >>> 24);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$Rgba.class */
    public static class Rgba extends PixelConverter {
        public static final PixelConverter instance = new Rgba();

        private Rgba() {
            this.alphaMask = 255;
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return (i2 << 8) | (i2 >>> 24);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (i2 << 24) | (i2 >>> 8);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$RgbaPre.class */
    public static class RgbaPre extends PixelConverter {
        public static final PixelConverter instance = new RgbaPre();

        private RgbaPre() {
            this.alphaMask = 255;
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            if ((i2 >> 24) == -1) {
                return (i2 << 8) | (i2 >>> 24);
            }
            int i3 = i2 >>> 24;
            int i4 = i3 + (i3 >> 7);
            return (((((i2 >> 16) & 255) * i4) >> 8) << 24) | (((((i2 >> 8) & 255) * i4) >> 8) << 16) | ((((i2 & 255) * i4) >> 8) << 8) | i3;
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = i2 & 255;
            if (i3 == 255 || i3 == 0) {
                return (i2 >>> 8) | (i2 << 24);
            }
            int i4 = i2 >>> 24;
            int i5 = (i2 >> 16) & 255;
            int i6 = (i2 >> 8) & 255;
            return ((((i4 << 8) - i4) / i3) << 24) | ((((i5 << 8) - i5) / i3) << 16) | ((((i6 << 8) - i6) / i3) << 8) | i3;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$ArgbPre.class */
    public static class ArgbPre extends PixelConverter {
        public static final PixelConverter instance = new ArgbPre();

        private ArgbPre() {
            this.alphaMask = -16777216;
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            if ((i2 >> 24) == -1) {
                return i2;
            }
            int i3 = i2 >>> 24;
            int i4 = i3 + (i3 >> 7);
            int i5 = (((i2 >> 16) & 255) * i4) >> 8;
            int i6 = (((i2 >> 8) & 255) * i4) >> 8;
            return (i3 << 24) | (i5 << 16) | (i6 << 8) | (((i2 & 255) * i4) >> 8);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = i2 >>> 24;
            if (i3 == 255 || i3 == 0) {
                return i2;
            }
            int i4 = (i2 >> 16) & 255;
            int i5 = (i2 >> 8) & 255;
            int i6 = i2 & 255;
            int i7 = ((i4 << 8) - i4) / i3;
            int i8 = ((i5 << 8) - i5) / i3;
            return (i3 << 24) | (i7 << 16) | (i8 << 8) | (((i6 << 8) - i6) / i3);
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$ArgbBm.class */
    public static class ArgbBm extends PixelConverter {
        public static final PixelConverter instance = new ArgbBm();

        private ArgbBm() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return i2 | ((i2 >> 31) << 24);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return (i2 << 7) >> 7;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$ByteGray.class */
    public static class ByteGray extends PixelConverter {
        static final double RED_MULT = 0.299d;
        static final double GRN_MULT = 0.587d;
        static final double BLU_MULT = 0.114d;
        public static final PixelConverter instance = new ByteGray();

        private ByteGray() {
        }

        @Override // sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return (int) ((((i2 >> 16) & 255) * RED_MULT) + (((i2 >> 8) & 255) * GRN_MULT) + ((i2 & 255) * BLU_MULT) + 0.5d);
        }

        @Override // sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            return ((((65280 | i2) << 8) | i2) << 8) | i2;
        }
    }

    /* loaded from: rt.jar:sun/awt/image/PixelConverter$UshortGray.class */
    public static class UshortGray extends ByteGray {
        static final double SHORT_MULT = 257.0d;
        static final double USHORT_RED_MULT = 76.843d;
        static final double USHORT_GRN_MULT = 150.85899999999998d;
        static final double USHORT_BLU_MULT = 29.298000000000002d;
        public static final PixelConverter instance = new UshortGray();

        private UshortGray() {
            super();
        }

        @Override // sun.awt.image.PixelConverter.ByteGray, sun.awt.image.PixelConverter
        public int rgbToPixel(int i2, ColorModel colorModel) {
            return (int) ((((i2 >> 16) & 255) * USHORT_RED_MULT) + (((i2 >> 8) & 255) * USHORT_GRN_MULT) + ((i2 & 255) * USHORT_BLU_MULT) + 0.5d);
        }

        @Override // sun.awt.image.PixelConverter.ByteGray, sun.awt.image.PixelConverter
        public int pixelToRgb(int i2, ColorModel colorModel) {
            int i3 = i2 >> 8;
            return ((((65280 | i3) << 8) | i3) << 8) | i3;
        }
    }
}
