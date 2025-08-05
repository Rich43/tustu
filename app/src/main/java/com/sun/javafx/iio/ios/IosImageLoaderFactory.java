package com.sun.javafx.iio.ios;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ios/IosImageLoaderFactory.class */
public class IosImageLoaderFactory implements ImageLoaderFactory {
    private static IosImageLoaderFactory theInstance;

    private IosImageLoaderFactory() {
    }

    public static final synchronized IosImageLoaderFactory getInstance() {
        if (theInstance == null) {
            theInstance = new IosImageLoaderFactory();
        }
        return theInstance;
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageFormatDescription getFormatDescription() {
        return IosDescriptor.getInstance();
    }

    @Override // com.sun.javafx.iio.ImageLoaderFactory
    public ImageLoader createImageLoader(InputStream input) throws IOException {
        return new IosImageLoader(input, IosDescriptor.getInstance());
    }

    public ImageLoader createImageLoader(String input) throws IOException {
        return new IosImageLoader(input, IosDescriptor.getInstance());
    }
}
