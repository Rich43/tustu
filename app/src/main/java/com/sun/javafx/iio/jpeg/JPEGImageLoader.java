package com.sun.javafx.iio.jpeg;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/jpeg/JPEGImageLoader.class */
public class JPEGImageLoader extends ImageLoaderImpl {
    public static final int JCS_UNKNOWN = 0;
    public static final int JCS_GRAYSCALE = 1;
    public static final int JCS_RGB = 2;
    public static final int JCS_YCbCr = 3;
    public static final int JCS_CMYK = 4;
    public static final int JCS_YCC = 5;
    public static final int JCS_RGBA = 6;
    public static final int JCS_YCbCrA = 7;
    public static final int JCS_YCCA = 10;
    public static final int JCS_YCCK = 11;
    private long structPointer;
    private int inWidth;
    private int inHeight;
    private int inColorSpaceCode;
    private int outColorSpaceCode;
    private byte[] iccData;
    private int outWidth;
    private int outHeight;
    private ImageStorage.ImageType outImageType;
    private boolean isDisposed;
    private Lock accessLock;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initJPEGMethodIDs(Class cls);

    private static native void disposeNative(long j2);

    private native long initDecompressor(InputStream inputStream) throws IOException;

    private native int startDecompression(long j2, int i2, int i3, int i4);

    private native boolean decompressIndirect(long j2, boolean z2, byte[] bArr) throws IOException;

    static {
        $assertionsDisabled = !JPEGImageLoader.class.desiredAssertionStatus();
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_iio");
            return null;
        });
        initJPEGMethodIDs(InputStream.class);
    }

    private void setInputAttributes(int width, int height, int colorSpaceCode, int outColorSpaceCode, int numComponents, byte[] iccData) {
        this.inWidth = width;
        this.inHeight = height;
        this.inColorSpaceCode = colorSpaceCode;
        this.outColorSpaceCode = outColorSpaceCode;
        this.iccData = iccData;
        switch (outColorSpaceCode) {
            case 0:
                switch (numComponents) {
                    case 1:
                        this.outImageType = ImageStorage.ImageType.GRAY;
                        return;
                    case 2:
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        return;
                    case 3:
                        this.outImageType = ImageStorage.ImageType.RGB;
                        return;
                    case 4:
                        this.outImageType = ImageStorage.ImageType.RGBA_PRE;
                        return;
                }
            case 1:
                this.outImageType = ImageStorage.ImageType.GRAY;
                return;
            case 2:
            case 3:
            case 5:
                this.outImageType = ImageStorage.ImageType.RGB;
                return;
            case 4:
            case 6:
            case 7:
            case 10:
            case 11:
                this.outImageType = ImageStorage.ImageType.RGBA_PRE;
                return;
            case 8:
            case 9:
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }

    private void setOutputAttributes(int width, int height) {
        this.outWidth = width;
        this.outHeight = height;
    }

    private void updateImageProgress(int outLinesDecoded) {
        updateImageProgress((100.0f * outLinesDecoded) / this.outHeight);
    }

    JPEGImageLoader(InputStream input) throws IOException {
        super(JPEGDescriptor.getInstance());
        this.structPointer = 0L;
        this.isDisposed = false;
        this.accessLock = new Lock();
        if (input == null) {
            throw new IllegalArgumentException("input == null!");
        }
        try {
            this.structPointer = initDecompressor(input);
            if (this.structPointer == 0) {
                throw new IOException("Unable to initialize JPEG decompressor");
            }
        } catch (IOException e2) {
            dispose();
            throw e2;
        }
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public synchronized void dispose() {
        if (!this.accessLock.isLocked() && !this.isDisposed && this.structPointer != 0) {
            this.isDisposed = true;
            disposeNative(this.structPointer);
            this.structPointer = 0L;
        }
    }

    protected void finalize() {
        dispose();
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public ImageFrame load(int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth) throws IOException {
        if (imageIndex != 0) {
            return null;
        }
        this.accessLock.lock();
        int[] widthHeight = ImageTools.computeDimensions(this.inWidth, this.inHeight, width, height, preserveAspectRatio);
        int width2 = widthHeight[0];
        int height2 = widthHeight[1];
        ImageMetadata md = new ImageMetadata(null, true, null, null, null, null, null, Integer.valueOf(width2), Integer.valueOf(height2), null, null, null);
        updateImageMetadata(md);
        try {
            try {
                int outNumComponents = startDecompression(this.structPointer, this.outColorSpaceCode, width2, height2);
                if (this.outWidth < 0 || this.outHeight < 0 || outNumComponents < 0) {
                    throw new IOException("negative dimension.");
                }
                if (this.outWidth > Integer.MAX_VALUE / outNumComponents) {
                    throw new IOException("bad width.");
                }
                int scanlineStride = this.outWidth * outNumComponents;
                if (scanlineStride > Integer.MAX_VALUE / this.outHeight) {
                    throw new IOException("bad height.");
                }
                byte[] array = new byte[scanlineStride * this.outHeight];
                ByteBuffer buffer = ByteBuffer.wrap(array);
                decompressIndirect(this.structPointer, (this.listeners == null || this.listeners.isEmpty()) ? false : true, buffer.array());
                this.accessLock.unlock();
                dispose();
                if (buffer == null) {
                    throw new IOException("Error decompressing JPEG stream!");
                }
                if (this.outWidth != width2 || this.outHeight != height2) {
                    buffer = ImageTools.scaleImage(buffer, this.outWidth, this.outHeight, outNumComponents, width2, height2, smooth);
                }
                return new ImageFrame(this.outImageType, buffer, width2, height2, width2 * outNumComponents, (byte[][]) null, md);
            } catch (IOException e2) {
                throw e2;
            } catch (Throwable t2) {
                throw new IOException(t2);
            }
        } catch (Throwable th) {
            this.accessLock.unlock();
            dispose();
            throw th;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/iio/jpeg/JPEGImageLoader$Lock.class */
    private static class Lock {
        private boolean locked = false;

        public synchronized boolean isLocked() {
            return this.locked;
        }

        public synchronized void lock() {
            if (this.locked) {
                throw new IllegalStateException("Recursive loading is not allowed.");
            }
            this.locked = true;
        }

        public synchronized void unlock() {
            if (!this.locked) {
                throw new IllegalStateException("Invalid loader state.");
            }
            this.locked = false;
        }
    }
}
