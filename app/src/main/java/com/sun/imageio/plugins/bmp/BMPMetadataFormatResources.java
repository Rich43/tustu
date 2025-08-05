package com.sun.imageio.plugins.bmp;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ListResourceBundle;
import org.icepdf.core.pobjects.graphics.SoftMask;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPMetadataFormatResources.class */
public class BMPMetadataFormatResources extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"BMPVersion", "BMP version string"}, new Object[]{"Width", "The width of the image"}, new Object[]{"Height", "The height of the image"}, new Object[]{"BitsPerPixel", ""}, new Object[]{"PixelsPerMeter", "Resolution in pixels per unit distance"}, new Object[]{"X", "Pixels Per Meter along X"}, new Object[]{Constants._TAG_Y, "Pixels Per Meter along Y"}, new Object[]{"ColorsUsed", "Number of color indexes in the color table actually used"}, new Object[]{"ColorsImportant", "Number of color indexes considered important for display"}, new Object[]{"Mask", "Color masks; present for BI_BITFIELDS compression only"}, new Object[]{"Intent", "Rendering intent"}, new Object[]{"Palette", "The color palette"}, new Object[]{"Red", "Red Mask/Color Palette"}, new Object[]{"Green", "Green Mask/Color Palette/Gamma"}, new Object[]{"Blue", "Blue Mask/Color Palette/Gamma"}, new Object[]{SoftMask.SOFT_MASK_TYPE_ALPHA, "Alpha Mask/Color Palette/Gamma"}, new Object[]{"ColorSpaceType", "Color Space Type"}, new Object[]{"X", "The X coordinate of a point in XYZ color space"}, new Object[]{Constants._TAG_Y, "The Y coordinate of a point in XYZ color space"}, new Object[]{com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG, "The Z coordinate of a point in XYZ color space"}};
    }
}
