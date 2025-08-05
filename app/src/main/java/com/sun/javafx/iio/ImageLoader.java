package com.sun.javafx.iio;

import java.io.IOException;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageLoader.class */
public interface ImageLoader {
    ImageFormatDescription getFormatDescription();

    void dispose();

    void addListener(ImageLoadListener imageLoadListener);

    void removeListener(ImageLoadListener imageLoadListener);

    ImageFrame load(int i2, int i3, int i4, boolean z2, boolean z3) throws IOException;
}
