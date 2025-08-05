package com.sun.javafx.iio.ios;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageDescriptor;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ios/IosImageLoader.class */
public class IosImageLoader extends ImageLoaderImpl {
    public static final int GRAY = 0;
    public static final int GRAY_ALPHA = 1;
    public static final int GRAY_ALPHA_PRE = 2;
    public static final int PALETTE = 3;
    public static final int PALETTE_ALPHA = 4;
    public static final int PALETTE_ALPHA_PRE = 5;
    public static final int PALETTE_TRANS = 6;
    public static final int RGB = 7;
    public static final int RGBA = 8;
    public static final int RGBA_PRE = 9;
    private static final Map<Integer, ImageStorage.ImageType> colorSpaceMapping = new HashMap();
    private long structPointer;
    private int inWidth;
    private int inHeight;
    private int nImages;
    private boolean isDisposed;
    private int delayTime;
    private int loopCount;

    private static native void initNativeLoading();

    private native long loadImage(InputStream inputStream, boolean z2) throws IOException;

    private native long loadImageFromURL(String str, boolean z2) throws IOException;

    private native void resizeImage(long j2, int i2, int i3);

    private native byte[] getImageBuffer(long j2, int i2);

    private native int getNumberOfComponents(long j2);

    private native int getColorSpaceCode(long j2);

    private native int getDelayTime(long j2);

    private static native void disposeLoader(long j2);

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("nativeiio");
            return null;
        });
        colorSpaceMapping.put(0, ImageStorage.ImageType.GRAY);
        colorSpaceMapping.put(1, ImageStorage.ImageType.GRAY_ALPHA);
        colorSpaceMapping.put(2, ImageStorage.ImageType.GRAY_ALPHA_PRE);
        colorSpaceMapping.put(3, ImageStorage.ImageType.PALETTE);
        colorSpaceMapping.put(4, ImageStorage.ImageType.PALETTE_ALPHA);
        colorSpaceMapping.put(5, ImageStorage.ImageType.PALETTE_ALPHA_PRE);
        colorSpaceMapping.put(6, ImageStorage.ImageType.PALETTE_TRANS);
        colorSpaceMapping.put(7, ImageStorage.ImageType.RGB);
        colorSpaceMapping.put(8, ImageStorage.ImageType.RGBA);
        colorSpaceMapping.put(9, ImageStorage.ImageType.RGBA_PRE);
        initNativeLoading();
    }

    private void setInputParameters(int width, int height, int imageCount, int loopCount) {
        this.inWidth = width;
        this.inHeight = height;
        this.nImages = imageCount;
        this.loopCount = loopCount;
    }

    private void updateProgress(float progressPercentage) {
        updateImageProgress(progressPercentage);
    }

    private boolean shouldReportProgress() {
        return (this.listeners == null || this.listeners.isEmpty()) ? false : true;
    }

    private void checkNativePointer() throws IOException {
        if (this.structPointer == 0) {
            throw new IOException("Unable to initialize image native loader!");
        }
    }

    private void retrieveDelayTime() {
        if (this.nImages > 1) {
            this.delayTime = getDelayTime(this.structPointer);
        }
    }

    public IosImageLoader(String urlString, ImageDescriptor desc) throws IOException {
        super(desc);
        this.isDisposed = false;
        try {
            new URL(urlString);
            try {
                this.structPointer = loadImageFromURL(urlString, shouldReportProgress());
                checkNativePointer();
                retrieveDelayTime();
            } catch (IOException e2) {
                dispose();
                throw e2;
            }
        } catch (MalformedURLException e3) {
            throw new IllegalArgumentException("Image loader: Malformed URL!");
        }
    }

    public IosImageLoader(InputStream inputStream, ImageDescriptor desc) throws IOException {
        super(desc);
        this.isDisposed = false;
        if (inputStream == null) {
            throw new IllegalArgumentException("Image loader: input stream == null");
        }
        try {
            this.structPointer = loadImage(inputStream, shouldReportProgress());
            checkNativePointer();
            retrieveDelayTime();
        } catch (IOException e2) {
            dispose();
            throw e2;
        }
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public synchronized void dispose() {
        if (!this.isDisposed && this.structPointer != 0) {
            this.isDisposed = true;
            disposeLoader(this.structPointer);
            this.structPointer = 0L;
        }
    }

    protected void finalize() {
        dispose();
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public ImageFrame load(int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth) throws IOException {
        if (imageIndex >= this.nImages) {
            dispose();
            return null;
        }
        int[] widthHeight = ImageTools.computeDimensions(this.inWidth, this.inHeight, width, height, preserveAspectRatio);
        int width2 = widthHeight[0];
        int height2 = widthHeight[1];
        ImageMetadata md = new ImageMetadata(null, true, null, null, null, this.delayTime == 0 ? null : Integer.valueOf(this.delayTime), this.nImages > 1 ? Integer.valueOf(this.loopCount) : null, Integer.valueOf(width2), Integer.valueOf(height2), null, null, null);
        updateImageMetadata(md);
        resizeImage(this.structPointer, width2, height2);
        int nComponents = getNumberOfComponents(this.structPointer);
        int colorSpaceCode = getColorSpaceCode(this.structPointer);
        ImageStorage.ImageType imageType = colorSpaceMapping.get(Integer.valueOf(colorSpaceCode));
        byte[] pixels = getImageBuffer(this.structPointer, imageIndex);
        return new ImageFrame(imageType, ByteBuffer.wrap(pixels), width2, height2, width2 * nComponents, (byte[][]) null, md);
    }
}
