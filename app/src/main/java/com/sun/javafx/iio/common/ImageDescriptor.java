package com.sun.javafx.iio.common;

import com.sun.javafx.iio.ImageFormatDescription;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/ImageDescriptor.class */
public class ImageDescriptor implements ImageFormatDescription {
    private final String formatName;
    private final List<String> extensions;
    private final List<ImageFormatDescription.Signature> signatures;

    public ImageDescriptor(String formatName, String[] extensions, ImageFormatDescription.Signature[] signatures) {
        this.formatName = formatName;
        this.extensions = Collections.unmodifiableList(Arrays.asList(extensions));
        this.signatures = Collections.unmodifiableList(Arrays.asList(signatures));
    }

    @Override // com.sun.javafx.iio.ImageFormatDescription
    public String getFormatName() {
        return this.formatName;
    }

    @Override // com.sun.javafx.iio.ImageFormatDescription
    public List<String> getExtensions() {
        return this.extensions;
    }

    @Override // com.sun.javafx.iio.ImageFormatDescription
    public List<ImageFormatDescription.Signature> getSignatures() {
        return this.signatures;
    }
}
