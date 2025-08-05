package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGray.class */
public class ByteGray {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteGrayObj;

    public static ByteToBytePixelConverter ToByteGrayConverter() {
        if (ToByteGrayObj == null) {
            ToByteGrayObj = BaseByteToByteConverter.create(accessor);
        }
        return ToByteGrayObj;
    }

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgrfConv.nonpremult;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgrfConv.premult;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntFrgbConv.nonpremult;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntFrgbConv.premult;
    }

    public static ByteToBytePixelConverter ToByteBgrConverter() {
        return ToByteRgbAnyConv.bgr;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGray$Accessor.class */
    static class Accessor implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.OPAQUE;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 1;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            int g2 = arr[offset] & 255;
            return (-16777216) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            int g2 = arr[offset] & 255;
            return (-16777216) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buf, int offset) {
            int g2 = buf.get(offset) & 255;
            return (-16777216) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buf, int offset) {
            int g2 = buf.get(offset) & 255;
            return (-16777216) | (g2 << 16) | (g2 << 8) | g2;
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            arr[offset] = (byte) PixelUtils.RgbToGray(argb);
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgbPre(byte[] arr, int offset, int argbpre) {
            setArgb(arr, offset, PixelUtils.PretoNonPre(argbpre));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(ByteBuffer buf, int offset, int argb) {
            buf.put(offset, (byte) PixelUtils.RgbToGray(argb));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buf, int offset, int argbpre) {
            setArgb(buf, offset, PixelUtils.PretoNonPre(argbpre));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGray$ToByteBgrfConv.class */
    static class ToByteBgrfConv extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter nonpremult = new ToByteBgrfConv(ByteBgra.setter);
        public static final ByteToBytePixelConverter premult = new ToByteBgrfConv(ByteBgraPre.setter);

        ToByteBgrfConv(BytePixelSetter setter) {
            super(ByteGray.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        byte g2 = srcarr[srcoff + x2];
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = g2;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = g2;
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = g2;
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = -1;
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        byte g2 = srcbuf.get(srcoff + x2);
                        dstbuf.put(dstoff, g2);
                        dstbuf.put(dstoff + 1, g2);
                        dstbuf.put(dstoff + 2, g2);
                        dstbuf.put(dstoff + 3, (byte) -1);
                        dstoff += 4;
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGray$ToIntFrgbConv.class */
    static class ToIntFrgbConv extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter nonpremult = new ToIntFrgbConv(IntArgb.setter);
        public static final ByteToIntPixelConverter premult = new ToIntFrgbConv(IntArgbPre.setter);

        private ToIntFrgbConv(IntPixelSetter setter) {
            super(ByteGray.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int g2 = srcarr[srcoff + x2] & 255;
                        dstarr[dstoff + x2] = (-16777216) | (g2 << 16) | (g2 << 8) | g2;
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int g2 = srcbuf.get(srcoff + x2) & 255;
                        dstbuf.put(dstoff + x2, (-16777216) | (g2 << 16) | (g2 << 8) | g2);
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGray$ToByteRgbAnyConv.class */
    static class ToByteRgbAnyConv extends BaseByteToByteConverter {
        static ToByteRgbAnyConv bgr = new ToByteRgbAnyConv(ByteBgr.setter);

        private ToByteRgbAnyConv(BytePixelSetter setter) {
            super(ByteGray.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int dstscanbytes2 = dstscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int g2 = srcarr[srcoff + x2] & 255;
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = (byte) g2;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = (byte) g2;
                        dstoff = dstoff3 + 1;
                        dstarr[dstoff3] = (byte) g2;
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int dstscanbytes2 = dstscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int g2 = srcbuf.get(srcoff + x2) & 255;
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i2, (byte) g2);
                        int dstoff3 = dstoff2 + 1;
                        dstbuf.put(dstoff2, (byte) g2);
                        dstoff = dstoff3 + 1;
                        dstbuf.put(dstoff3, (byte) g2);
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }
}
