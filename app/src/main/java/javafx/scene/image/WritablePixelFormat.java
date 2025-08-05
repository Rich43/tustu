package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

/* loaded from: jfxrt.jar:javafx/scene/image/WritablePixelFormat.class */
public abstract class WritablePixelFormat<T extends Buffer> extends PixelFormat<T> {
    public abstract void setArgb(T t2, int i2, int i3, int i4, int i5);

    WritablePixelFormat(PixelFormat.Type type) {
        super(type);
    }

    @Override // javafx.scene.image.PixelFormat
    public boolean isWritable() {
        return true;
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/WritablePixelFormat$IntArgb.class */
    static class IntArgb extends WritablePixelFormat<IntBuffer> {
        static final IntArgb INSTANCE = new IntArgb();

        private IntArgb() {
            super(PixelFormat.Type.INT_ARGB);
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return false;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(IntBuffer buf, int x2, int y2, int scanlineStride) {
            return buf.get((y2 * scanlineStride) + x2);
        }

        @Override // javafx.scene.image.WritablePixelFormat
        public void setArgb(IntBuffer buf, int x2, int y2, int scanlineStride, int argb) {
            buf.put((y2 * scanlineStride) + x2, argb);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/WritablePixelFormat$IntArgbPre.class */
    static class IntArgbPre extends WritablePixelFormat<IntBuffer> {
        static final IntArgbPre INSTANCE = new IntArgbPre();

        private IntArgbPre() {
            super(PixelFormat.Type.INT_ARGB_PRE);
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return true;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(IntBuffer buf, int x2, int y2, int scanlineStride) {
            return PretoNonPre(buf.get((y2 * scanlineStride) + x2));
        }

        @Override // javafx.scene.image.WritablePixelFormat
        public void setArgb(IntBuffer buf, int x2, int y2, int scanlineStride, int argb) {
            buf.put((y2 * scanlineStride) + x2, NonPretoPre(argb));
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/WritablePixelFormat$ByteBgra.class */
    static class ByteBgra extends WritablePixelFormat<ByteBuffer> {
        static final ByteBgra INSTANCE = new ByteBgra();

        private ByteBgra() {
            super(PixelFormat.Type.BYTE_BGRA);
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return false;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(ByteBuffer buf, int x2, int y2, int scanlineStride) {
            int index = (y2 * scanlineStride) + (x2 * 4);
            int b2 = buf.get(index) & 255;
            int g2 = buf.get(index + 1) & 255;
            int r2 = buf.get(index + 2) & 255;
            int a2 = buf.get(index + 3) & 255;
            return (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
        }

        @Override // javafx.scene.image.WritablePixelFormat
        public void setArgb(ByteBuffer buf, int x2, int y2, int scanlineStride, int argb) {
            int index = (y2 * scanlineStride) + (x2 * 4);
            buf.put(index, (byte) argb);
            buf.put(index + 1, (byte) (argb >> 8));
            buf.put(index + 2, (byte) (argb >> 16));
            buf.put(index + 3, (byte) (argb >> 24));
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/WritablePixelFormat$ByteBgraPre.class */
    static class ByteBgraPre extends WritablePixelFormat<ByteBuffer> {
        static final ByteBgraPre INSTANCE = new ByteBgraPre();

        private ByteBgraPre() {
            super(PixelFormat.Type.BYTE_BGRA_PRE);
        }

        @Override // javafx.scene.image.PixelFormat
        public boolean isPremultiplied() {
            return true;
        }

        @Override // javafx.scene.image.PixelFormat
        public int getArgb(ByteBuffer buf, int x2, int y2, int scanlineStride) {
            int index = (y2 * scanlineStride) + (x2 * 4);
            int b2 = buf.get(index) & 255;
            int g2 = buf.get(index + 1) & 255;
            int r2 = buf.get(index + 2) & 255;
            int a2 = buf.get(index + 3) & 255;
            if (a2 > 0 && a2 < 255) {
                int halfa = a2 >> 1;
                r2 = r2 >= a2 ? 255 : ((r2 * 255) + halfa) / a2;
                g2 = g2 >= a2 ? 255 : ((g2 * 255) + halfa) / a2;
                b2 = b2 >= a2 ? 255 : ((b2 * 255) + halfa) / a2;
            }
            return (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
        }

        @Override // javafx.scene.image.WritablePixelFormat
        public void setArgb(ByteBuffer buf, int x2, int y2, int scanlineStride, int argb) {
            int b2;
            int g2;
            int r2;
            int index = (y2 * scanlineStride) + (x2 * 4);
            int a2 = argb >>> 24;
            if (a2 > 0) {
                r2 = (argb >> 16) & 255;
                g2 = (argb >> 8) & 255;
                b2 = argb & 255;
                if (a2 < 255) {
                    r2 = ((r2 * a2) + 127) / 255;
                    g2 = ((g2 * a2) + 127) / 255;
                    b2 = ((b2 * a2) + 127) / 255;
                }
            } else {
                b2 = 0;
                g2 = 0;
                r2 = 0;
                a2 = 0;
            }
            buf.put(index, (byte) b2);
            buf.put(index + 1, (byte) g2);
            buf.put(index + 2, (byte) r2);
            buf.put(index + 3, (byte) a2);
        }
    }
}
