package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

/* compiled from: BMPImageLoaderFactory.java */
/* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/BMPDescriptor.class */
final class BMPDescriptor extends ImageDescriptor {
    static final String formatName = "BMP";
    static final String[] extensions = {"bmp"};
    static final ImageFormatDescription.Signature[] signatures = {new ImageFormatDescription.Signature(66, 77)};
    static final ImageDescriptor theInstance = new BMPDescriptor();

    private BMPDescriptor() {
        super(formatName, extensions, signatures);
    }
}
