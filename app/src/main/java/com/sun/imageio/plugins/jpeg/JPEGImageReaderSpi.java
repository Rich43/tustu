package com.sun.imageio.plugins.jpeg;

import java.io.IOException;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReaderSpi.class */
public class JPEGImageReaderSpi extends ImageReaderSpi {
    private static String[] writerSpiNames = {"com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi"};

    public JPEGImageReaderSpi() {
        super("Oracle Corporation", JPEG.version, JPEG.names, JPEG.suffixes, JPEG.MIMETypes, "com.sun.imageio.plugins.jpeg.JPEGImageReader", new Class[]{ImageInputStream.class}, writerSpiNames, true, JPEG.nativeStreamMetadataFormatName, JPEG.nativeStreamMetadataFormatClassName, null, null, true, JPEG.nativeImageMetadataFormatName, JPEG.nativeImageMetadataFormatClassName, null, null);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard JPEG Image Reader";
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public boolean canDecodeInput(Object obj) throws IOException {
        if (!(obj instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream imageInputStream = (ImageInputStream) obj;
        imageInputStream.mark();
        int i2 = imageInputStream.read();
        int i3 = imageInputStream.read();
        imageInputStream.reset();
        if (i2 == 255 && i3 == 216) {
            return true;
        }
        return false;
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public ImageReader createReaderInstance(Object obj) throws IIOException {
        return new JPEGImageReader(this);
    }
}
