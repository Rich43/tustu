package com.sun.imageio.plugins.jpeg;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGStreamMetadataFormatResources.class */
public class JPEGStreamMetadataFormatResources extends JPEGMetadataFormatResources {
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        Object[][] objArr = new Object[commonContents.length][2];
        for (int i2 = 0; i2 < commonContents.length; i2++) {
            objArr[i2][0] = commonContents[i2][0];
            objArr[i2][1] = commonContents[i2][1];
        }
        return objArr;
    }
}
