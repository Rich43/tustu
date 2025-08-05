package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGrayAlpha.class */
public class ByteGrayAlpha {
    public static final BytePixelGetter getter = Accessor.nonpremul;
    public static final BytePixelSetter setter = Accessor.nonpremul;
    public static final BytePixelAccessor accessor = Accessor.nonpremul;

    public static ByteToBytePixelConverter ToByteGrayAlphaPreConverter() {
        return ToByteGrayAlphaPreConv.instance;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraSameConv.nonpremul;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGrayAlpha$Accessor.class */
    static class Accessor implements BytePixelAccessor {
        static final BytePixelAccessor nonpremul = new Accessor(false);
        static final BytePixelAccessor premul = new Accessor(true);
        private boolean isPremult;

        private Accessor(boolean isPremult) {
            this.isPremult = isPremult;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return this.isPremult ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 2;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            int g2 = arr[offset] & 255;
            int a2 = arr[offset + 1] & 255;
            if (this.isPremult) {
                g2 = PixelUtils.PreToNonPre(g2, a2);
            }
            return (a2 << 24) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            int g2 = arr[offset] & 255;
            int a2 = arr[offset + 1] & 255;
            if (!this.isPremult) {
                g2 = PixelUtils.NonPretoPre(g2, a2);
            }
            return (a2 << 24) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buf, int offset) {
            int g2 = buf.get(offset) & 255;
            int a2 = buf.get(offset + 1) & 255;
            if (this.isPremult) {
                g2 = PixelUtils.PreToNonPre(g2, a2);
            }
            return (a2 << 24) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buf, int offset) {
            int g2 = buf.get(offset) & 255;
            int a2 = buf.get(offset + 1) & 255;
            if (!this.isPremult) {
                g2 = PixelUtils.NonPretoPre(g2, a2);
            }
            return (a2 << 24) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            int g2 = PixelUtils.RgbToGray(argb);
            int a2 = argb >>> 24;
            if (this.isPremult) {
                g2 = PixelUtils.NonPretoPre(g2, a2);
            }
            arr[offset] = (byte) g2;
            arr[offset + 1] = (byte) a2;
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgbPre(byte[] arr, int offset, int argbpre) {
            int g2 = PixelUtils.RgbToGray(argbpre);
            int a2 = argbpre >>> 24;
            if (!this.isPremult) {
                g2 = PixelUtils.PreToNonPre(g2, a2);
            }
            arr[offset] = (byte) g2;
            arr[offset + 1] = (byte) a2;
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(ByteBuffer buf, int offset, int argb) {
            int g2 = PixelUtils.RgbToGray(argb);
            int a2 = argb >>> 24;
            if (this.isPremult) {
                g2 = PixelUtils.NonPretoPre(g2, a2);
            }
            buf.put(offset, (byte) g2);
            buf.put(offset + 1, (byte) a2);
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buf, int offset, int argbpre) {
            int g2 = PixelUtils.RgbToGray(argbpre);
            int a2 = argbpre >>> 24;
            if (!this.isPremult) {
                g2 = PixelUtils.PreToNonPre(g2, a2);
            }
            buf.put(offset, (byte) g2);
            buf.put(offset + 1, (byte) a2);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGrayAlpha$ToByteGrayAlphaPreConv.class */
    static class ToByteGrayAlphaPreConv extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteGrayAlphaPreConv();

        private ToByteGrayAlphaPreConv() {
            super(ByteGrayAlpha.getter, ByteGrayAlphaPre.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 2);
            int dstscanbytes2 = dstscanbytes - (w2 * 2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int g2 = srcarr[i2] & 255;
                        srcoff = srcoff2 + 1;
                        byte b2 = srcarr[srcoff2];
                        if (b2 != -1) {
                            if (b2 == 0) {
                                g2 = 0;
                            } else {
                                g2 = ((g2 * (b2 & 255)) + 127) / 255;
                            }
                        }
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i3] = (byte) g2;
                        dstoff = dstoff2 + 1;
                        dstarr[dstoff2] = b2;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 2);
            int dstscanbytes2 = dstscanbytes - (w2 * 2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int g2 = srcbuf.get(i2) & 255;
                        srcoff = srcoff2 + 1;
                        int a2 = srcbuf.get(srcoff2);
                        if (a2 != -1) {
                            if (a2 == 0) {
                                g2 = 0;
                            } else {
                                g2 = ((g2 * (a2 & 255)) + 127) / 255;
                            }
                        }
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i3, (byte) g2);
                        dstoff = dstoff2 + 1;
                        dstbuf.put(dstoff2, (byte) a2);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGrayAlpha$ToByteBgraSameConv.class */
    static class ToByteBgraSameConv extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremul = new ToByteBgraSameConv(false);
        static final ByteToBytePixelConverter premul = new ToByteBgraSameConv(true);

        private ToByteBgraSameConv(boolean isPremult) {
            super(isPremult ? ByteGrayAlphaPre.getter : ByteGrayAlpha.getter, isPremult ? ByteBgraPre.setter : ByteBgra.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 2);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        byte g2 = srcarr[i2];
                        srcoff = srcoff2 + 1;
                        byte a2 = srcarr[srcoff2];
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i3] = g2;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = g2;
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = g2;
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = a2;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 2);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        byte g2 = srcbuf.get(i2);
                        srcoff = srcoff2 + 1;
                        byte a2 = srcbuf.get(srcoff2);
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i3, g2);
                        int dstoff3 = dstoff2 + 1;
                        dstbuf.put(dstoff2, g2);
                        int dstoff4 = dstoff3 + 1;
                        dstbuf.put(dstoff3, g2);
                        dstoff = dstoff4 + 1;
                        dstbuf.put(dstoff4, a2);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }
}
