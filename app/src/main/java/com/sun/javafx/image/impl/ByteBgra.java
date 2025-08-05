package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgra.class */
public class ByteBgra {
    public static final BytePixelGetter getter = Accessor.instance;
    public static final BytePixelSetter setter = Accessor.instance;
    public static final BytePixelAccessor accessor = Accessor.instance;
    private static ByteToBytePixelConverter ToByteBgraConv;

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        if (ToByteBgraConv == null) {
            ToByteBgraConv = BaseByteToByteConverter.create(accessor);
        }
        return ToByteBgraConv;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgraPreConv.instance;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntArgbSameConv.nonpremul;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntArgbPreConv.instance;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgra$Accessor.class */
    static class Accessor implements BytePixelAccessor {
        static final BytePixelAccessor instance = new Accessor();

        private Accessor() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.NONPREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 4;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return (arr[offset] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset + 2] & 255) << 16) | (arr[offset + 3] << 24);
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return PixelUtils.NonPretoPre(getArgb(arr, offset));
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buf, int offset) {
            return (buf.get(offset) & 255) | ((buf.get(offset + 1) & 255) << 8) | ((buf.get(offset + 2) & 255) << 16) | (buf.get(offset + 3) << 24);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buf, int offset) {
            return PixelUtils.NonPretoPre(getArgb(buf, offset));
        }

        @Override // com.sun.javafx.image.BytePixelSetter
        public void setArgb(byte[] arr, int offset, int argb) {
            arr[offset] = (byte) argb;
            arr[offset + 1] = (byte) (argb >> 8);
            arr[offset + 2] = (byte) (argb >> 16);
            arr[offset + 3] = (byte) (argb >> 24);
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
            buf.put(offset + 3, (byte) (argb >> 24));
        }

        @Override // com.sun.javafx.image.PixelSetter
        public void setArgbPre(ByteBuffer buf, int offset, int argbpre) {
            setArgb(buf, offset, PixelUtils.PretoNonPre(argbpre));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgra$ToByteBgraPreConv.class */
    static class ToByteBgraPreConv extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter instance = new ToByteBgraPreConv();

        private ToByteBgraPreConv() {
            super(ByteBgra.getter, ByteBgraPre.setter);
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = (byte) ((((b2 & 255) * a2) + 127) / 255);
                                g2 = (byte) ((((g2 & 255) * a2) + 127) / 255);
                                r2 = (byte) ((((r2 & 255) * a2) + 127) / 255);
                            }
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = (byte) ((((b2 & 255) * a2) + 127) / 255);
                                g2 = (byte) ((((g2 & 255) * a2) + 127) / 255);
                                r2 = (byte) ((((r2 & 255) * a2) + 127) / 255);
                            }
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

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgra$ToIntArgbSameConv.class */
    static class ToIntArgbSameConv extends BaseByteToIntConverter {
        static final ByteToIntPixelConverter nonpremul = new ToIntArgbSameConv(false);
        static final ByteToIntPixelConverter premul = new ToIntArgbSameConv(true);

        private ToIntArgbSameConv(boolean isPremult) {
            super(isPremult ? ByteBgraPre.getter : ByteBgra.getter, isPremult ? IntArgbPre.setter : IntArgb.setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanints2 = dstscanints - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        dstoff++;
                        int i3 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int srcoff3 = srcoff2 + 1;
                        int i4 = (srcarr[i3] & 255) | ((srcarr[srcoff2] & 255) << 8);
                        int srcoff4 = srcoff3 + 1;
                        int i5 = i4 | ((srcarr[srcoff3] & 255) << 16);
                        srcoff = srcoff4 + 1;
                        dstarr[i2] = i5 | (srcarr[srcoff4] << 24);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints2;
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
                        dstbuf.put(dstoff + x2, (srcbuf.get(srcoff) & 255) | ((srcbuf.get(srcoff + 1) & 255) << 8) | ((srcbuf.get(srcoff + 2) & 255) << 16) | (srcbuf.get(srcoff + 3) << 24));
                        srcoff += 4;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteBgra$ToIntArgbPreConv.class */
    static class ToIntArgbPreConv extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter instance = new ToIntArgbPreConv();

        private ToIntArgbPreConv() {
            super(ByteBgra.getter, IntArgbPre.setter);
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = ((b2 * a2) + 127) / 255;
                                g2 = ((g2 * a2) + 127) / 255;
                                r2 = ((r2 * a2) + 127) / 255;
                            }
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
                        if (a2 < 255) {
                            if (a2 == 0) {
                                r2 = 0;
                                g2 = 0;
                                b2 = 0;
                            } else {
                                b2 = ((b2 * a2) + 127) / 255;
                                g2 = ((g2 * a2) + 127) / 255;
                                r2 = ((r2 * a2) + 127) / 255;
                            }
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
