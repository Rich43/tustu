package com.sun.imageio.plugins.gif;

import com.sun.imageio.plugins.common.PaletteBuilder;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageWriterSpi.class */
public class GIFImageWriterSpi extends ImageWriterSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final String writerClassName = "com.sun.imageio.plugins.gif.GIFImageWriter";
    private static final String[] names = {"gif", "GIF"};
    private static final String[] suffixes = {"gif"};
    private static final String[] MIMETypes = {"image/gif"};
    private static final String[] readerSpiNames = {"com.sun.imageio.plugins.gif.GIFImageReaderSpi"};

    public GIFImageWriterSpi() {
        super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, writerClassName, new Class[]{ImageOutputStream.class}, readerSpiNames, true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null, true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier) {
        if (imageTypeSpecifier == null) {
            throw new IllegalArgumentException("type == null!");
        }
        SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
        ColorModel colorModel = imageTypeSpecifier.getColorModel();
        if (sampleModel.getNumBands() == 1 && sampleModel.getSampleSize(0) <= 8 && sampleModel.getWidth() <= 65535 && sampleModel.getHeight() <= 65535 && (colorModel == null || colorModel.getComponentSize()[0] <= 8)) {
            return true;
        }
        return PaletteBuilder.canCreatePalette(imageTypeSpecifier);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard GIF image writer";
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public ImageWriter createWriterInstance(Object obj) {
        return new GIFImageWriter(this);
    }
}
