package com.sun.javafx.iio;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageLoaderFactory.class */
public interface ImageLoaderFactory {
    ImageFormatDescription getFormatDescription();

    ImageLoader createImageLoader(InputStream inputStream) throws IOException;
}
