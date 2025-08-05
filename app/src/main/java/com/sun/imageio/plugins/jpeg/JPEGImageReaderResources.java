package com.sun.imageio.plugins.jpeg;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReaderResources.class */
public class JPEGImageReaderResources extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{Integer.toString(0), "Truncated File - Missing EOI marker"}, new Object[]{Integer.toString(1), "JFIF markers not allowed in JFIF JPEG thumbnail; ignored"}, new Object[]{Integer.toString(2), "Embedded color profile is invalid; ignored"}};
    }
}
