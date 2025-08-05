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

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgr.class */
public class ByteBgr {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteBgrObj;

    public static ByteToBytePixelConverter ToByteBgrConverter() {
        if (ToByteBgrObj == null) {
            ToByteBgrObj = BaseByteToByteConverter.create(accessor);
        }
        return ToByteBgrObj;
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

    public static ByteToBytePixelConverter ToByteArgbConverter() {
        return ToByteFrgbConv.nonpremult;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgr$Accessor.class */
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
            return 3;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return (arr[offset] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset + 2] & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return (arr[offset] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset + 2] & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buffer, int offset) {
            return (buffer.get(offset) & 255) | ((buffer.get(offset + 1) & 255) << 8) | ((buffer.get(offset + 2) & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buffer, int offset) {
            return (buffer.get(offset) & 255) | ((buffer.get(offset + 1) & 255) << 8) | ((buffer.get(offset + 2) & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            arr[offset] = (byte) argb;
            arr[offset + 1] = (byte) (argb >> 8);
            arr[offset + 2] = (byte) (argb >> 16);
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgbPre(byte[] arr, int offset, int argbpre) {
            setArgb(arr, offset, PixelUtils.PretoNonPre(argbpre));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(ByteBuffer buf, int offset, int argb) {
            buf.put(offset, (byte) argb);
            buf.put(offset + 1, (byte) (argb >> 8));
            buf.put(offset + 2, (byte) (argb >> 16));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buf, int offset, int argbpre) {
            setArgb(buf, offset, PixelUtils.PretoNonPre(argbpre));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgr$ToByteBgrfConv.class */
    static class ToByteBgrfConv extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter nonpremult = new ToByteBgrfConv(ByteBgra.setter);
        public static final ByteToBytePixelConverter premult = new ToByteBgrfConv(ByteBgraPre.setter);

        private ToByteBgrfConv(BytePixelSetter setter) {
            super(ByteBgr.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        int i3 = srcoff;
                        int srcoff2 = srcoff + 1;
                        dstarr[i2] = srcarr[i3];
                        int dstoff3 = dstoff2 + 1;
                        int srcoff3 = srcoff2 + 1;
                        dstarr[dstoff2] = srcarr[srcoff2];
                        int dstoff4 = dstoff3 + 1;
                        srcoff = srcoff3 + 1;
                        dstarr[dstoff3] = srcarr[srcoff3];
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = -1;
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
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        int i3 = srcoff;
                        int srcoff2 = srcoff + 1;
                        dstbuf.put(i2, srcbuf.get(i3));
                        int dstoff3 = dstoff2 + 1;
                        int srcoff3 = srcoff2 + 1;
                        dstbuf.put(dstoff2, srcbuf.get(srcoff2));
                        int dstoff4 = dstoff3 + 1;
                        srcoff = srcoff3 + 1;
                        dstbuf.put(dstoff3, srcbuf.get(srcoff3));
                        dstoff = dstoff4 + 1;
                        dstbuf.put(dstoff4, (byte) -1);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgr$ToIntFrgbConv.class */
    static class ToIntFrgbConv extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter nonpremult = new ToIntFrgbConv(IntArgb.setter);
        public static final ByteToIntPixelConverter premult = new ToIntFrgbConv(IntArgbPre.setter);

        private ToIntFrgbConv(IntPixelSetter setter) {
            super(ByteBgr.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int b2 = srcarr[i2] & 255;
                        int srcoff3 = srcoff2 + 1;
                        int g2 = srcarr[srcoff2] & 255;
                        srcoff = srcoff3 + 1;
                        int r2 = srcarr[srcoff3] & 255;
                        dstarr[dstoff + x2] = (-16777216) | (r2 << 16) | (g2 << 8) | b2;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int b2 = srcbuf.get(i2) & 255;
                        int srcoff3 = srcoff2 + 1;
                        int g2 = srcbuf.get(srcoff2) & 255;
                        srcoff = srcoff3 + 1;
                        int r2 = srcbuf.get(srcoff3) & 255;
                        dstbuf.put(dstoff + x2, (-16777216) | (r2 << 16) | (g2 << 8) | b2);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgr$ToByteFrgbConv.class */
    static class ToByteFrgbConv extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremult = new ToByteFrgbConv(ByteArgb.setter);

        private ToByteFrgbConv(BytePixelSetter setter) {
            super(ByteBgr.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = -1;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = srcarr[srcoff + 2];
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = srcarr[srcoff + 1];
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = srcarr[srcoff];
                        srcoff += 3;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i2, (byte) -1);
                        int dstoff3 = dstoff2 + 1;
                        dstbuf.put(dstoff2, srcbuf.get(srcoff + 2));
                        int dstoff4 = dstoff3 + 1;
                        dstbuf.put(dstoff3, srcbuf.get(srcoff + 1));
                        dstoff = dstoff4 + 1;
                        dstbuf.put(dstoff4, srcbuf.get(srcoff));
                        srcoff += 3;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }
    }
}
