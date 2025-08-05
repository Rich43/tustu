package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.tk.Toolkit;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteIndexed.class */
public class ByteIndexed {
    public static BytePixelGetter createGetter(PixelFormat<ByteBuffer> pf) {
        return new Getter(pf);
    }

    public static ByteToBytePixelConverter createToByteBgraAny(BytePixelGetter src, BytePixelSetter dst) {
        return new ToByteBgraAnyConverter(src, dst);
    }

    public static ByteToIntPixelConverter createToIntArgbAny(BytePixelGetter src, IntPixelSetter dst) {
        return new ToIntArgbAnyConverter(src, dst);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteIndexed$Getter.class */
    public static class Getter implements BytePixelGetter {
        PixelFormat<ByteBuffer> theFormat;
        private int[] precolors;
        private int[] nonprecolors;

        Getter(PixelFormat<ByteBuffer> pf) {
            this.theFormat = pf;
        }

        int[] getPreColors() {
            if (this.precolors == null) {
                this.precolors = Toolkit.getImageAccessor().getPreColors(this.theFormat);
            }
            return this.precolors;
        }

        int[] getNonPreColors() {
            if (this.nonprecolors == null) {
                this.nonprecolors = Toolkit.getImageAccessor().getNonPreColors(this.theFormat);
            }
            return this.nonprecolors;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return this.theFormat.isPremultiplied() ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 1;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return getNonPreColors()[arr[offset] & 255];
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return getPreColors()[arr[offset] & 255];
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buf, int offset) {
            return getNonPreColors()[buf.get(offset) & 255];
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buf, int offset) {
            return getPreColors()[buf.get(offset) & 255];
        }
    }

    static int[] getColors(BytePixelGetter getter, PixelSetter setter) {
        Getter big = (Getter) getter;
        if (setter.getAlphaType() == AlphaType.PREMULTIPLIED) {
            return big.getPreColors();
        }
        return big.getNonPreColors();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteIndexed$ToByteBgraAnyConverter.class */
    public static class ToByteBgraAnyConverter extends BaseByteToByteConverter {
        public ToByteBgraAnyConverter(BytePixelGetter getter, BytePixelSetter setter) {
            super(getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int[] colors = ByteIndexed.getColors(getGetter(), getSetter());
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int argb = colors[srcarr[srcoff + x2] & 255];
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = (byte) argb;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = (byte) (argb >> 8);
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = (byte) (argb >> 16);
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = (byte) (argb >> 24);
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
            int[] colors = ByteIndexed.getColors(getGetter(), getSetter());
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int argb = colors[srcbuf.get(srcoff + x2) & 255];
                        dstbuf.put(dstoff, (byte) argb);
                        dstbuf.put(dstoff + 1, (byte) (argb >> 8));
                        dstbuf.put(dstoff + 2, (byte) (argb >> 16));
                        dstbuf.put(dstoff + 3, (byte) (argb >> 24));
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

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteIndexed$ToIntArgbAnyConverter.class */
    public static class ToIntArgbAnyConverter extends BaseByteToIntConverter {
        public ToIntArgbAnyConverter(BytePixelGetter getter, IntPixelSetter setter) {
            super(getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int[] colors = ByteIndexed.getColors(getGetter(), getSetter());
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        dstarr[dstoff + x2] = colors[srcarr[srcoff + x2] & 255];
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
            int[] colors = ByteIndexed.getColors(getGetter(), getSetter());
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        dstbuf.put(dstoff + x2, colors[srcbuf.get(srcoff + x2) & 255]);
                    }
                    srcoff += srcscanbytes;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }
}
