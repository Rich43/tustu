package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgra;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgraPre.class */
public class ByteBgraPre {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteBgraPreObj;

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraConv.instance;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        if (ToByteBgraPreObj == null) {
            ToByteBgraPreObj = BaseByteToByteConverter.create(accessor);
        }
        return ToByteBgraPreObj;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbConv.instance;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ByteBgra.ToIntArgbSameConv.premul;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgraPre$Accessor.class */
    static class Accessor implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.PREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 4;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return PixelUtils.PretoNonPre(getArgbPre(arr, offset));
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return (arr[offset] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset + 2] & 255) << 16) | (arr[offset + 3] << 24);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buffer, int offset) {
            return PixelUtils.PretoNonPre(getArgbPre(buffer, offset));
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buffer, int offset) {
            return (buffer.get(offset) & 255) | ((buffer.get(offset + 1) & 255) << 8) | ((buffer.get(offset + 2) & 255) << 16) | (buffer.get(offset + 3) << 24);
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            setArgbPre(arr, offset, PixelUtils.NonPretoPre(argb));
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgbPre(byte[] arr, int offset, int argbpre) {
            arr[offset] = (byte) argbpre;
            arr[offset + 1] = (byte) (argbpre >> 8);
            arr[offset + 2] = (byte) (argbpre >> 16);
            arr[offset + 3] = (byte) (argbpre >> 24);
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(ByteBuffer buffer, int offset, int argb) {
            setArgbPre(buffer, offset, PixelUtils.NonPretoPre(argb));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buffer, int offset, int argbpre) {
            buffer.put(offset, (byte) argbpre);
            buffer.put(offset + 1, (byte) (argbpre >> 8));
            buffer.put(offset + 2, (byte) (argbpre >> 16));
            buffer.put(offset + 3, (byte) (argbpre >> 24));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgraPre$ToByteBgraConv.class */
    public static class ToByteBgraConv extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter instance = new ToByteBgraConv();

        private ToByteBgraConv() {
            super(ByteBgraPre.getter, ByteBgra.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        byte b2 = srcarr[i2];
                        int srcoff3 = srcoff2 + 1;
                        byte g2 = srcarr[srcoff2];
                        int srcoff4 = srcoff3 + 1;
                        byte r2 = srcarr[srcoff3];
                        srcoff = srcoff4 + 1;
                        int a2 = srcarr[srcoff4] & 255;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            b2 = (byte) ((((b2 & 255) * 255) + halfa) / a2);
                            g2 = (byte) ((((g2 & 255) * 255) + halfa) / a2);
                            r2 = (byte) ((((r2 & 255) * 255) + halfa) / a2);
                        }
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i3] = b2;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = g2;
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = r2;
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = (byte) a2;
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
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        byte b2 = srcbuf.get(srcoff);
                        byte g2 = srcbuf.get(srcoff + 1);
                        byte r2 = srcbuf.get(srcoff + 2);
                        int a2 = srcbuf.get(srcoff + 3) & 255;
                        srcoff += 4;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            b2 = (byte) ((((b2 & 255) * 255) + halfa) / a2);
                            g2 = (byte) ((((g2 & 255) * 255) + halfa) / a2);
                            r2 = (byte) ((((r2 & 255) * 255) + halfa) / a2);
                        }
                        dstbuf.put(dstoff, b2);
                        dstbuf.put(dstoff + 1, g2);
                        dstbuf.put(dstoff + 2, r2);
                        dstbuf.put(dstoff + 3, (byte) a2);
                        dstoff += 4;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgraPre$ToIntArgbConv.class */
    public static class ToIntArgbConv extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter instance = new ToIntArgbConv();

        private ToIntArgbConv() {
            super(ByteBgraPre.getter, IntArgb.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanints2 = dstscanints - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int b2 = srcarr[i2] & 255;
                        int srcoff3 = srcoff2 + 1;
                        int g2 = srcarr[srcoff2] & 255;
                        int srcoff4 = srcoff3 + 1;
                        int r2 = srcarr[srcoff3] & 255;
                        srcoff = srcoff4 + 1;
                        int a2 = srcarr[srcoff4] & 255;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            r2 = ((r2 * 255) + halfa) / a2;
                            g2 = ((g2 * 255) + halfa) / a2;
                            b2 = ((b2 * 255) + halfa) / a2;
                        }
                        int i3 = dstoff;
                        dstoff++;
                        dstarr[i3] = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                    }
                    dstoff += dstscanints2;
                    srcoff += srcscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int b2 = srcbuf.get(srcoff) & 255;
                        int g2 = srcbuf.get(srcoff + 1) & 255;
                        int r2 = srcbuf.get(srcoff + 2) & 255;
                        int a2 = srcbuf.get(srcoff + 3) & 255;
                        srcoff += 4;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            r2 = ((r2 * 255) + halfa) / a2;
                            g2 = ((g2 * 255) + halfa) / a2;
                            b2 = ((b2 * 255) + halfa) / a2;
                        }
                        dstbuf.put(dstoff + x2, (a2 << 24) | (r2 << 16) | (g2 << 8) | b2);
                    }
                    dstoff += dstscanints;
                    srcoff += srcscanbytes2;
                } else {
                    return;
                }
            }
        }
    }
}
