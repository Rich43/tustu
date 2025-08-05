package com.sun.imageio.plugins.gif;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFStreamMetadataFormatResources.class */
public class GIFStreamMetadataFormatResources extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"Version", "The file version, either 87a or 89a"}, new Object[]{"LogicalScreenDescriptor", "The logical screen descriptor, except for the global color table"}, new Object[]{"GlobalColorTable", "The global color table"}, new Object[]{"ColorTableEntry", "A global color table entry"}, new Object[]{"Version/value", "The version string"}, new Object[]{"LogicalScreenDescriptor/logicalScreenWidth", "The width in pixels of the whole picture"}, new Object[]{"LogicalScreenDescriptor/logicalScreenHeight", "The height in pixels of the whole picture"}, new Object[]{"LogicalScreenDescriptor/colorResolution", "The number of bits of color resolution, beteen 1 and 8"}, new Object[]{"LogicalScreenDescriptor/pixelAspectRatio", "If 0, indicates square pixels, else W/H = (value + 15)/64"}, new Object[]{"GlobalColorTable/sizeOfGlobalColorTable", "The number of entries in the global color table"}, new Object[]{"GlobalColorTable/backgroundColorIndex", "The index of the color table entry to be used as a background"}, new Object[]{"GlobalColorTable/sortFlag", "True if the global color table is sorted by frequency"}, new Object[]{"ColorTableEntry/index", "The index of the color table entry"}, new Object[]{"ColorTableEntry/red", "The red value for the color table entry"}, new Object[]{"ColorTableEntry/green", "The green value for the color table entry"}, new Object[]{"ColorTableEntry/blue", "The blue value for the color table entry"}};
    }
}
