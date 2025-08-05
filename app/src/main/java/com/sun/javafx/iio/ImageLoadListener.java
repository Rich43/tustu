package com.sun.javafx.iio;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageLoadListener.class */
public interface ImageLoadListener {
    void imageLoadProgress(ImageLoader imageLoader, float f2);

    void imageLoadWarning(ImageLoader imageLoader, String str);

    void imageLoadMetaData(ImageLoader imageLoader, ImageMetadata imageMetadata);
}
