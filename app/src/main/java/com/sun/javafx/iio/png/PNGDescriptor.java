package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/png/PNGDescriptor.class */
public class PNGDescriptor extends ImageDescriptor {
    private static final String formatName = "PNG";
    private static final String[] extensions = {"png"};
    private static final ImageFormatDescription.Signature[] signatures = {new ImageFormatDescription.Signature(-119, 80, 78, 71, 13, 10, 26, 10)};
    private static ImageDescriptor theInstance = null;

    private PNGDescriptor() {
        super(formatName, extensions, signatures);
    }

    public static synchronized ImageDescriptor getInstance() {
        if (theInstance == null) {
            theInstance = new PNGDescriptor();
        }
        return theInstance;
    }
}
