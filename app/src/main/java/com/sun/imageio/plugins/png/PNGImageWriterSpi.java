package com.sun.imageio.plugins.png;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageWriterSpi.class */
public class PNGImageWriterSpi extends ImageWriterSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final String writerClassName = "com.sun.imageio.plugins.png.PNGImageWriter";
    private static final String[] names = {"png", "PNG"};
    private static final String[] suffixes = {"png"};
    private static final String[] MIMETypes = {"image/png", "image/x-png"};
    private static final String[] readerSpiNames = {"com.sun.imageio.plugins.png.PNGImageReaderSpi"};

    public PNGImageWriterSpi() {
        super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, writerClassName, new Class[]{ImageOutputStream.class}, readerSpiNames, false, null, null, null, null, true, PNGMetadata.nativeMetadataFormatName, "com.sun.imageio.plugins.png.PNGMetadataFormat", null, null);
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier) {
        int numBands;
        SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
        ColorModel colorModel = imageTypeSpecifier.getColorModel();
        int[] sampleSize = sampleModel.getSampleSize();
        int i2 = sampleSize[0];
        for (int i3 = 1; i3 < sampleSize.length; i3++) {
            if (sampleSize[i3] > i2) {
                i2 = sampleSize[i3];
            }
        }
        if (i2 < 1 || i2 > 16 || (numBands = sampleModel.getNumBands()) < 1 || numBands > 4) {
            return false;
        }
        boolean zHasAlpha = colorModel.hasAlpha();
        if (colorModel instanceof IndexColorModel) {
            return true;
        }
        if ((numBands == 1 || numBands == 3) && zHasAlpha) {
            return false;
        }
        if ((numBands == 2 || numBands == 4) && !zHasAlpha) {
            return false;
        }
        return true;
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard PNG image writer";
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public ImageWriter createWriterInstance(Object obj) {
        return new PNGImageWriter(this);
    }
}
