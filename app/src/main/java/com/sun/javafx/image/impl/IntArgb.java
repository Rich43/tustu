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

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgb.class */
public class IntArgb {
    public static final IntPixelGetter getter = Accessor.instance;
    public static final IntPixelSetter setter = Accessor.instance;
    public static final IntPixelAccessor accessor = Accessor.instance;
    private static IntToBytePixelConverter ToByteBgraObj;
    private static IntToIntPixelConverter ToIntArgbObj;

    public static IntToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraObj == null) {
            ToByteBgraObj = new IntTo4ByteSameConverter(getter, ByteBgra.setter);
        }
        return ToByteBgraObj;
    }

    public static IntToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static IntToIntPixelConverter ToIntArgbConverter() {
        if (ToIntArgbObj == null) {
            ToIntArgbObj = BaseIntToIntConverter.create(accessor);
        }
        return ToIntArgbObj;
    }

    public static IntToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgb$Accessor.class */
    static class Accessor implements IntPixelAccessor {
        static final IntPixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 1;
        }

        @Override // com.sun.javafx.image.IntPixelGetter
        public int getArgb(int[] arr, int offset) {
            return arr[offset];
        }

        @Override // com.sun.javafx.image.IntPixelGetter
        public int getArgbPre(int[] arr, int offset) {
            return PixelUtils.NonPretoPre(arr[offset]);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(IntBuffer buffer, int offset) {
            return buffer.get(offset);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(IntBuffer buffer, int offset) {
            return PixelUtils.NonPretoPre(buffer.get(offset));
        }

        @Override // com.sun.javafx.image.IntPixelSetter
        public void setArgb(int[] arr, int offset, int argb) {
            arr[offset] = argb;
        }

        @Override // com.sun.javafx.image.IntPixelSetter
        public void setArgbPre(int[] arr, int offset, int argbpre) {
            arr[offset] = PixelUtils.PretoNonPre(argbpre);
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgb(IntBuffer buffer, int offset, int argb) {
            buffer.put(offset, argb);
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(IntBuffer buffer, int offset, int argbpre) {
            buffer.put(offset, PixelUtils.PretoNonPre(argbpre));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgb$ToIntArgbPreConv.class */
    public static class ToIntArgbPreConv extends BaseIntToIntConverter {
        public static final IntToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(IntArgb.getter, IntArgbPre.setter);
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                pixel = 0;
                            } else {
                                int r2 = ((((pixel >> 16) & 255) * a2) + 127) / 255;
                                int g2 = ((((pixel >> 8) & 255) * a2) + 127) / 255;
                                int b2 = (((pixel & 255) * a2) + 127) / 255;
                                pixel = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                            }
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                pixel = 0;
                            } else {
                                int r2 = ((((pixel >> 16) & 255) * a2) + 127) / 255;
                                int g2 = ((((pixel >> 8) & 255) * a2) + 127) / 255;
                                int b2 = (((pixel & 255) * a2) + 127) / 255;
                                pixel = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                            }
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

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntArgb$ToByteBgraPreConv.class */
    static class ToByteBgraPreConv extends BaseIntToByteConverter {
        public static final IntToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(IntArgb.getter, ByteBgraPre.setter);
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
                        int r2 = pixel >> 16;
                        int g2 = pixel >> 8;
                        int b2 = pixel;
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = (((b2 & 255) * a2) + 127) / 255;
                                g2 = (((g2 & 255) * a2) + 127) / 255;
                                r2 = (((r2 & 255) * a2) + 127) / 255;
                            }
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
                        int r2 = pixel >> 16;
                        int g2 = pixel >> 8;
                        int b2 = pixel;
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = (((b2 & 255) * a2) + 127) / 255;
                                g2 = (((g2 & 255) * a2) + 127) / 255;
                                r2 = (((r2 & 255) * a2) + 127) / 255;
                            }
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
