package com.sun.imageio.plugins.jpeg;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGStreamMetadataFormat.class */
public class JPEGStreamMetadataFormat extends JPEGMetadataFormat {
    private static JPEGStreamMetadataFormat theInstance = null;

    @Override // com.sun.imageio.plugins.jpeg.JPEGMetadataFormat, javax.imageio.metadata.IIOMetadataFormatImpl, javax.imageio.metadata.IIOMetadataFormat
    public /* bridge */ /* synthetic */ boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier) {
        return super.canNodeAppear(str, imageTypeSpecifier);
    }

    private JPEGStreamMetadataFormat() {
        super(JPEG.nativeStreamMetadataFormatName, 4);
        addStreamElements(getRootName());
    }

    public static synchronized IIOMetadataFormat getInstance() {
        if (theInstance == null) {
            theInstance = new JPEGStreamMetadataFormat();
        }
        return theInstance;
    }
}
