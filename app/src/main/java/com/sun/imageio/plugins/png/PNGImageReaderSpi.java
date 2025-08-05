package com.sun.imageio.plugins.png;

import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageReaderSpi.class */
public class PNGImageReaderSpi extends ImageReaderSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final String readerClassName = "com.sun.imageio.plugins.png.PNGImageReader";
    private static final String[] names = {"png", "PNG"};
    private static final String[] suffixes = {"png"};
    private static final String[] MIMETypes = {"image/png", "image/x-png"};
    private static final String[] writerSpiNames = {"com.sun.imageio.plugins.png.PNGImageWriterSpi"};

    public PNGImageReaderSpi() {
        super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, readerClassName, new Class[]{ImageInputStream.class}, writerSpiNames, false, null, null, null, null, true, PNGMetadata.nativeMetadataFormatName, "com.sun.imageio.plugins.png.PNGMetadataFormat", null, null);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard PNG image reader";
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public boolean canDecodeInput(Object obj) throws IOException {
        if (!(obj instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream imageInputStream = (ImageInputStream) obj;
        byte[] bArr = new byte[8];
        imageInputStream.mark();
        imageInputStream.readFully(bArr);
        imageInputStream.reset();
        return bArr[0] == -119 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71 && bArr[4] == 13 && bArr[5] == 10 && bArr[6] == 26 && bArr[7] == 10;
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public ImageReader createReaderInstance(Object obj) {
        return new PNGImageReader(this);
    }
}
