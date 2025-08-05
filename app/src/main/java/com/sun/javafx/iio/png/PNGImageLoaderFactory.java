package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/png/PNGImageLoaderFactory.class */
public class PNGImageLoaderFactory implements ImageLoaderFactory {
    private static final PNGImageLoaderFactory theInstance = new PNGImageLoaderFactory();

    private PNGImageLoaderFactory() {
    }

    public static final ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageFormatDescription getFormatDescription() {
        return PNGDescriptor.getInstance();
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageLoader createImageLoader(InputStream input) throws IOException {
        return new PNGImageLoader2(input);
    }
}
