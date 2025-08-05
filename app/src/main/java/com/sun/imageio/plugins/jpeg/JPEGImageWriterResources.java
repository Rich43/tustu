package com.sun.imageio.plugins.jpeg;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriterResources.class */
public class JPEGImageWriterResources extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{Integer.toString(0), "Only Rasters or band subsets may be written with a destination type. Destination type ignored."}, new Object[]{Integer.toString(1), "Stream metadata ignored on write"}, new Object[]{Integer.toString(2), "Metadata component ids incompatible with destination type. Metadata modified."}, new Object[]{Integer.toString(3), "Metadata JFIF settings incompatible with destination type. Metadata modified."}, new Object[]{Integer.toString(4), "Metadata Adobe settings incompatible with destination type. Metadata modified."}, new Object[]{Integer.toString(5), "Metadata JFIF settings incompatible with image type. Metadata modified."}, new Object[]{Integer.toString(6), "Metadata Adobe settings incompatible with image type. Metadata modified."}, new Object[]{Integer.toString(7), "Metadata must be JPEGMetadata when writing a Raster. Metadata ignored."}, new Object[]{Integer.toString(8), "Band subset not allowed for an IndexColorModel image.  Band subset ignored."}, new Object[]{Integer.toString(9), "Thumbnails must be simple (possibly index) RGB or grayscale.  Incompatible thumbnail ignored."}, new Object[]{Integer.toString(10), "Thumbnails ignored for non-JFIF-compatible image."}, new Object[]{Integer.toString(11), "Thumbnails require JFIF marker segment.  Missing node added to metadata."}, new Object[]{Integer.toString(12), "Thumbnail clipped."}, new Object[]{Integer.toString(13), "Metadata adjusted (made JFIF-compatible) for thumbnail."}, new Object[]{Integer.toString(14), "RGB thumbnail can't be written as indexed.  Written as RGB"}, new Object[]{Integer.toString(15), "Grayscale thumbnail can't be written as indexed.  Written as JPEG"}};
    }
}
