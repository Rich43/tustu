package com.sun.javafx.iio.jpeg;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/jpeg/JPEGImageLoaderFactory.class */
public class JPEGImageLoaderFactory implements ImageLoaderFactory {
    private static final JPEGImageLoaderFactory theInstance = new JPEGImageLoaderFactory();

    private JPEGImageLoaderFactory() {
    }

    public static final ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageFormatDescription getFormatDescription() {
        return JPEGDescriptor.getInstance();
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageLoader createImageLoader(InputStream input) throws IOException {
        return new JPEGImageLoader(input);
    }
}
