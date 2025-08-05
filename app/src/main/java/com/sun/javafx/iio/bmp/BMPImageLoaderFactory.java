package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/BMPImageLoaderFactory.class */
public final class BMPImageLoaderFactory implements ImageLoaderFactory {
    private static final BMPImageLoaderFactory theInstance = new BMPImageLoaderFactory();

    public static ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageFormatDescription getFormatDescription() {
        return BMPDescriptor.theInstance;
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageLoader createImageLoader(InputStream input) throws IOException {
        return new BMPImageLoader(input);
    }
}
