package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgbPre.class */
public class IntArgbPre {
    public static final IntPixelGetter getter = Accessor.instance;
    public static final IntPixelSetter setter = Accessor.instance;
    public static final IntPixelAccessor accessor = Accessor.instance;
    private static IntToBytePixelConverter ToByteBgraPreObj;
    private static IntToIntPixelConverter ToIntArgbPreObj;

    public static IntToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgraConv.instance;
    }

    public static IntToBytePixelConverter ToByteBgraPreConverter() {
        if (ToByteBgraPreObj == null) {
            ToByteBgraPreObj = new IntTo4ByteSameConverter(getter, ByteBgraPre.setter);
        }
        return ToByteBgraPreObj;
    }

    public static IntToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbConv.instance;
    }

    public static IntToIntPixelConverter ToIntArgbPreConverter() {
        if (ToIntArgbPreObj == null) {
            ToIntArgbPreObj = BaseIntToIntConverter.create(accessor);
        }
        return ToIntArgbPreObj;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgbPre$Accessor.class */
    static class Accessor implements IntPixelAccessor {
        static final IntPixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.PREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 1;
        }

        @Override // com.sun.javafx.image.IntPixelGetter
        public int getArgb(int[] arr, int offset) {
            return PixelUtils.PretoNonPre(arr[offset]);
        }

        @Override // com.sun.javafx.image.IntPixelGetter
        public int getArgbPre(int[] arr, int offset) {
            return arr[offset];
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(IntBuffer buffer, int offset) {
            return PixelUtils.PretoNonPre(buffer.get(offset));
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(IntBuffer buffer, int offset) {
            return buffer.get(offset);
        }

        @Override // com.sun.javafx.image.IntPixelSetter
        public void setArgb(int[] arr, int offset, int argb) {
            arr[offset] = PixelUtils.NonPretoPre(argb);
        }

        @Override // com.sun.javafx.image.IntPixelSetter
        public void setArgbPre(int[] arr, int offset, int argbpre) {
            arr[offset] = argbpre;
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(IntBuffer buffer, int offset, int argb) {
            buffer.put(offset, PixelUtils.NonPretoPre(argb));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(IntBuffer buffer, int offset, int argbpre) {
            buffer.put(offset, argbpre);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgbPre$ToIntArgbConv.class */
    public static class ToIntArgbConv extends BaseIntToIntConverter {
        public static final IntToIntPixelConverter instance = new ToIntArgbConv();

        private ToIntArgbConv() {
            super(IntArgbPre.getter, IntArgb.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(int[] srcarr, int srcoff, int srcscanints, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanints2 = srcscanints - w2;
            int dstscanints2 = dstscanints - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        srcoff++;
                        int pixel = srcarr[i2];
                        int a2 = pixel >>> 24;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            int r2 = ((((pixel >> 16) & 255) * 255) + halfa) / a2;
                            int g2 = ((((pixel >> 8) & 255) * 255) + halfa) / a2;
                            int b2 = (((pixel & 255) * 255) + halfa) / a2;
                            pixel = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                        }
                        int i3 = dstoff;
                        dstoff++;
                        dstarr[i3] = pixel;
                    }
                    srcoff += srcscanints2;
                    dstoff += dstscanints2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(IntBuffer srcbuf, int srcoff, int srcscanints, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int pixel = srcbuf.get(srcoff + x2);
                        int a2 = pixel >>> 24;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            int r2 = ((((pixel >> 16) & 255) * 255) + halfa) / a2;
                            int g2 = ((((pixel >> 8) & 255) * 255) + halfa) / a2;
                            int b2 = (((pixel & 255) * 255) + halfa) / a2;
                            pixel = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                        }
                        dstbuf.put(dstoff + x2, pixel);
                    }
                    srcoff += srcscanints;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgbPre$ToByteBgraConv.class */
    static class ToByteBgraConv extends BaseIntToByteConverter {
        public static final IntToBytePixelConverter instance = new ToByteBgraConv();

        private ToByteBgraConv() {
            super(IntArgbPre.getter, ByteBgra.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
        void doConvert(int[] srcarr, int srcoff, int srcscanints, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanints2 = srcscanints - w2;
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        srcoff++;
                        int pixel = srcarr[i2];
                        int a2 = pixel >>> 24;
                        int r2 = (pixel >> 16) & 255;
                        int g2 = (pixel >> 8) & 255;
                        int b2 = pixel & 255;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            r2 = ((r2 * 255) + halfa) / a2;
                            g2 = ((g2 * 255) + halfa) / a2;
                            b2 = ((b2 * 255) + halfa) / a2;
                        }
                        int i3 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i3] = (byte) b2;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = (byte) g2;
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = (byte) r2;
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = (byte) a2;
                    }
                    srcoff += srcscanints2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
        void doConvert(IntBuffer srcbuf, int srcoff, int srcscanints, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int pixel = srcbuf.get(srcoff + x2);
                        int a2 = pixel >>> 24;
                        int r2 = (pixel >> 16) & 255;
                        int g2 = (pixel >> 8) & 255;
                        int b2 = pixel & 255;
                        if (a2 > 0 && a2 < 255) {
                            int halfa = a2 >> 1;
                            r2 = ((r2 * 255) + halfa) / a2;
                            g2 = ((g2 * 255) + halfa) / a2;
                            b2 = ((b2 * 255) + halfa) / a2;
                        }
                        dstbuf.put(dstoff, (byte) b2);
                        dstbuf.put(dstoff + 1, (byte) g2);
                        dstbuf.put(dstoff + 2, (byte) r2);
                        dstbuf.put(dstoff + 3, (byte) a2);
                        dstoff += 4;
                    }
                    srcoff += srcscanints;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }
}
