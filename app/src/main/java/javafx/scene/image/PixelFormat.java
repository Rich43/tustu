package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import javafx.scene.image.WritablePixelFormat;

/* loaded from: jfxrt.jar:javafx/scene/image/PixelFormat.class */
public abstract class PixelFormat<T extends Buffer> {
    private Type type;

    /* loaded from: jfxrt.jar:javafx/scene/image/PixelFormat$Type.class */
    public enum Type {
        INT_ARGB_PRE,
        INT_ARGB,
        BYTE_BGRA_PRE,
        BYTE_BGRA,
        BYTE_RGB,
        BYTE_INDEXED
    }

    public abstract boolean isWritable();

    public abstract boolean isPremultiplied();

    public abstract int getArgb(T t2, int i2, int i3, int i4);

    PixelFormat(Type type) {
        this.type = type;
    }

    public static WritablePixelFormat<IntBuffer> getIntArgbInstance() {
        return WritablePixelFormat.IntArgb.INSTANCE;
    }

    public static WritablePixelFormat<IntBuffer> getIntArgbPreInstance() {
        return WritablePixelFormat.IntArgbPre.INSTANCE;
    }

    public static WritablePixelFormat<ByteBuffer> getByteBgraInstance() {
        return WritablePixelFormat.ByteBgra.INSTANCE;
    }

    public static WritablePixelFormat<ByteBuffer> getByteBgraPreInstance() {
        return WritablePixelFormat.ByteBgraPre.INSTANCE;
    }

    public static PixelFormat<ByteBuffer> getByteRgbInstance() {
        return ByteRgb.instance;
    }

    public static PixelFormat<ByteBuffer> createByteIndexedPremultipliedInstance(int[] colors) {
        return IndexedPixelFormat.createByte(colors, true);
    }

    public static PixelFormat<ByteBuffer> createByteIndexedInstance(int[] colors) {
        return IndexedPixelFormat.createByte(colors, false);
    }

    public Type getType() {
        return this.type;
    }

    static int NonPretoPre(int nonpre) {
        int a2 = nonpre >>> 24;
        if (a2 == 255) {
            return nonpre;
        }
        if (a2 == 0) {
            return 0;
        }
        int r2 = (nonpre >> 16) & 255;
        int g2 = (nonpre >> 8) & 255;
        int b2 = nonpre & 255;
        int r3 = ((r2 * a2) + 127) / 255;
        int g3 = ((g2 * a2) + 127) / 255;
        return (a2 << 24) | (r3 << 16) | (g3 << 8) | (((b2 * a2) + 127) / 255);
    }

    static int PretoNonPre(int pre) {
        int a2 = pre >>> 24;
        if (a2 == 255 || a2 == 0) {
            return pre;
        }
        int r2 = (pre >> 16) & 255;
        int g2 = (pre >> 8) & 255;
        int b2 = pre & 255;
        int halfa = a2 >> 1;
        return (a2 << 24) | ((r2 >= a2 ? 255 : ((r2 * 255) + halfa) / a2) << 16) | ((g2 >= a2 ? 255 : ((g2 * 255) + halfa) / a2) << 8) | (b2 >= a2 ? 255 : ((b2 * 255) + halfa) / a2);
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/PixelFormat$ByteRgb.class */
    static class ByteRgb extends PixelFormat<ByteBuffer> {
        static final ByteRgb instance = new ByteRgb();

        private ByteRgb() {
            super(Type.BYTE_RGB);
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isWritable() {
            return true;
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return false;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(ByteBuffer buf, int x2, int y2, int scanlineStride) {
            int index = (y2 * scanlineStride) + (x2 * 3);
            int r2 = buf.get(index) & 255;
            int g2 = buf.get(index + 1) & 255;
            int b2 = buf.get(index + 2) & 255;
            return (-16777216) | (r2 << 16) | (g2 << 8) | b2;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/PixelFormat$IndexedPixelFormat.class */
    static class IndexedPixelFormat extends PixelFormat<ByteBuffer> {
        int[] precolors;
        int[] nonprecolors;
        boolean premult;

        static PixelFormat createByte(int[] colors, boolean premult) {
            return new IndexedPixelFormat(Type.BYTE_INDEXED, premult, Arrays.copyOf(colors, 256));
        }

        private IndexedPixelFormat(Type type, boolean premult, int[] colors) {
            super(type);
            if (premult) {
                this.precolors = colors;
            } else {
                this.nonprecolors = colors;
            }
            this.premult = premult;
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isWritable() {
            return false;
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return this.premult;
        }

        int[] getPreColors() {
            if (this.precolors == null) {
                int[] colors = new int[this.nonprecolors.length];
                for (int i2 = 0; i2 < colors.length; i2++) {
                    colors[i2] = NonPretoPre(this.nonprecolors[i2]);
                }
                this.precolors = colors;
            }
            return this.precolors;
        }

        int[] getNonPreColors() {
            if (this.nonprecolors == null) {
                int[] colors = new int[this.precolors.length];
                for (int i2 = 0; i2 < colors.length; i2++) {
                    colors[i2] = PretoNonPre(this.precolors[i2]);
                }
                this.nonprecolors = colors;
            }
            return this.nonprecolors;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(ByteBuffer buf, int x2, int y2, int scanlineStride) {
            return getNonPreColors()[buf.get((y2 * scanlineStride) + x2) & 255];
        }
    }
}
