package com.sun.imageio.plugins.bmp;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPMetadataFormat.class */
public class BMPMetadataFormat extends IIOMetadataFormatImpl {
    private static IIOMetadataFormat instance = null;

    private BMPMetadataFormat() {
        super(BMPMetadata.nativeMetadataFormatName, 2);
        addElement("ImageDescriptor", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("ImageDescriptor", "bmpVersion", 0, true, null);
        addAttribute("ImageDescriptor", MetadataParser.WIDTH_TAG_NAME, 2, true, null, "0", "65535", true, true);
        addAttribute("ImageDescriptor", MetadataParser.HEIGHT_TAG_NAME, 2, true, null, "1", "65535", true, true);
        addAttribute("ImageDescriptor", "bitsPerPixel", 2, true, null, "1", "65535", true, true);
        addAttribute("ImageDescriptor", "compression", 2, false, null);
        addAttribute("ImageDescriptor", "imageSize", 2, true, null, "1", "65535", true, true);
        addElement("PixelsPerMeter", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("PixelsPerMeter", "X", 2, false, null, "1", "65535", true, true);
        addAttribute("PixelsPerMeter", Constants._TAG_Y, 2, false, null, "1", "65535", true, true);
        addElement("ColorsUsed", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("ColorsUsed", "value", 2, true, null, "0", "65535", true, true);
        addElement("ColorsImportant", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("ColorsImportant", "value", 2, false, null, "0", "65535", true, true);
        addElement("BI_BITFIELDS_Mask", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("BI_BITFIELDS_Mask", "red", 2, false, null, "0", "65535", true, true);
        addAttribute("BI_BITFIELDS_Mask", "green", 2, false, null, "0", "65535", true, true);
        addAttribute("BI_BITFIELDS_Mask", "blue", 2, false, null, "0", "65535", true, true);
        addElement(PdfOps.CS_NAME, BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute(PdfOps.CS_NAME, "value", 2, false, null, "0", "65535", true, true);
        addElement("LCS_CALIBRATED_RGB", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("LCS_CALIBRATED_RGB", "redX", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "redY", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "redZ", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "greenX", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "greenY", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "greenZ", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "blueX", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "blueY", 4, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB", "blueZ", 4, false, null, "0", "65535", true, true);
        addElement("LCS_CALIBRATED_RGB_GAMMA", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("LCS_CALIBRATED_RGB_GAMMA", "red", 2, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB_GAMMA", "green", 2, false, null, "0", "65535", true, true);
        addAttribute("LCS_CALIBRATED_RGB_GAMMA", "blue", 2, false, null, "0", "65535", true, true);
        addElement("Intent", BMPMetadata.nativeMetadataFormatName, 0);
        addAttribute("Intent", "value", 2, false, null, "0", "65535", true, true);
        addElement("Palette", BMPMetadata.nativeMetadataFormatName, 2, 256);
        addAttribute("Palette", "sizeOfPalette", 2, true, null);
        addBooleanAttribute("Palette", "sortFlag", false, false);
        addElement("PaletteEntry", "Palette", 0);
        addAttribute("PaletteEntry", "index", 2, true, null, "0", "255", true, true);
        addAttribute("PaletteEntry", "red", 2, true, null, "0", "255", true, true);
        addAttribute("PaletteEntry", "green", 2, true, null, "0", "255", true, true);
        addAttribute("PaletteEntry", "blue", 2, true, null, "0", "255", true, true);
        addElement("CommentExtensions", BMPMetadata.nativeMetadataFormatName, 1, Integer.MAX_VALUE);
        addElement("CommentExtension", "CommentExtensions", 0);
        addAttribute("CommentExtension", "value", 0, true, null);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormatImpl, javax.imageio.metadata.IIOMetadataFormat
    public boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier) {
        return true;
    }

    public static synchronized IIOMetadataFormat getInstance() {
        if (instance == null) {
            instance = new BMPMetadataFormat();
        }
        return instance;
    }
}
