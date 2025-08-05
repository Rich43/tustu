package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PixelUtils.class */
class PixelUtils {
    private static ImageFormatDescription[] supportedFormats = ImageStorage.getSupportedDescriptions();

    private PixelUtils() {
    }

    protected static boolean supportedFormatType(String type) {
        for (ImageFormatDescription ifd : supportedFormats) {
            for (String ext : ifd.getExtensions()) {
                if (type.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Pixels imageToPixels(Image image) {
        PixelFormat.DataType pixelType = image.getDataType();
        Application app = Application.GetApplication();
        int nativeFormat = Pixels.getNativeFormat();
        if (pixelType == PixelFormat.DataType.BYTE) {
            ByteBuffer bytes = (ByteBuffer) image.getPixelBuffer();
            int w2 = image.getWidth();
            int h2 = image.getHeight();
            int scanBytes = image.getScanlineStride();
            if (image.getBytesPerPixelUnit() == 3) {
                switch (nativeFormat) {
                    case 1:
                        byte[] newbytes = new byte[w2 * h2 * 4];
                        ByteRgb.ToByteBgraPreConverter().convert(bytes, 0, scanBytes, newbytes, 0, w2 * 4, w2, h2);
                        bytes = ByteBuffer.wrap(newbytes);
                        break;
                    case 2:
                        byte[] newbytes2 = new byte[w2 * h2 * 4];
                        ByteRgb.ToByteArgbConverter().convert(bytes, 0, scanBytes, newbytes2, 0, w2 * 4, w2, h2);
                        bytes = ByteBuffer.wrap(newbytes2);
                        break;
                    default:
                        throw new IllegalArgumentException("unhandled native format: " + nativeFormat);
                }
            } else if (image.getPixelFormat() != PixelFormat.BYTE_BGRA_PRE) {
                throw new IllegalArgumentException("non-RGB image format");
            }
            Pixels pixels = app.createPixels(image.getWidth(), image.getHeight(), bytes);
            return pixels;
        }
        if (pixelType == PixelFormat.DataType.INT) {
            if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
                throw new UnsupportedOperationException("INT_ARGB_PRE only supported for LITTLE_ENDIAN machines");
            }
            IntBuffer ints = (IntBuffer) image.getPixelBuffer();
            Pixels pixels2 = app.createPixels(image.getWidth(), image.getHeight(), ints);
            return pixels2;
        }
        throw new IllegalArgumentException("unhandled image type: " + ((Object) pixelType));
    }

    public static Image pixelsToImage(Pixels pix) {
        Buffer pixbuf = pix.getPixels();
        if (pix.getBytesPerComponent() == 1) {
            ByteBuffer buf = ByteBuffer.allocateDirect(pixbuf.capacity());
            buf.put((ByteBuffer) pixbuf);
            buf.rewind();
            return Image.fromByteBgraPreData(buf, pix.getWidth(), pix.getHeight());
        }
        if (pix.getBytesPerComponent() == 4) {
            IntBuffer buf2 = IntBuffer.allocate(pixbuf.capacity());
            buf2.put((IntBuffer) pixbuf);
            buf2.rewind();
            return Image.fromIntArgbPreData((IntBuffer) pixbuf, pix.getWidth(), pix.getHeight());
        }
        throw new IllegalArgumentException("unhandled pixel buffer: " + pixbuf.getClass().getName());
    }
}
