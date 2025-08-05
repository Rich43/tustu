package com.sun.imageio.plugins.jpeg;

import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriterSpi.class */
public class JPEGImageWriterSpi extends ImageWriterSpi {
    private static String[] readerSpiNames = {"com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi"};

    public JPEGImageWriterSpi() {
        super("Oracle Corporation", JPEG.version, JPEG.names, JPEG.suffixes, JPEG.MIMETypes, "com.sun.imageio.plugins.jpeg.JPEGImageWriter", new Class[]{ImageOutputStream.class}, readerSpiNames, true, JPEG.nativeStreamMetadataFormatName, JPEG.nativeStreamMetadataFormatClassName, null, null, true, JPEG.nativeImageMetadataFormatName, JPEG.nativeImageMetadataFormatClassName, null, null);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard JPEG Image Writer";
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public boolean isFormatLossless() {
        return false;
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier) {
        int[] sampleSize = imageTypeSpecifier.getSampleModel().getSampleSize();
        int i2 = sampleSize[0];
        for (int i3 = 1; i3 < sampleSize.length; i3++) {
            if (sampleSize[i3] > i2) {
                i2 = sampleSize[i3];
            }
        }
        if (i2 < 1 || i2 > 8) {
            return false;
        }
        return true;
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public ImageWriter createWriterInstance(Object obj) throws IIOException {
        return new JPEGImageWriter(this);
    }
}
