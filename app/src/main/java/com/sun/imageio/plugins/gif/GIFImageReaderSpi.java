package com.sun.imageio.plugins.gif;

import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageReaderSpi.class */
public class GIFImageReaderSpi extends ImageReaderSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final String readerClassName = "com.sun.imageio.plugins.gif.GIFImageReader";
    private static final String[] names = {"gif", "GIF"};
    private static final String[] suffixes = {"gif"};
    private static final String[] MIMETypes = {"image/gif"};
    private static final String[] writerSpiNames = {"com.sun.imageio.plugins.gif.GIFImageWriterSpi"};

    public GIFImageReaderSpi() {
        super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, readerClassName, new Class[]{ImageInputStream.class}, writerSpiNames, true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null, true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard GIF image reader";
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public boolean canDecodeInput(Object obj) throws IOException {
        if (!(obj instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream imageInputStream = (ImageInputStream) obj;
        byte[] bArr = new byte[6];
        imageInputStream.mark();
        imageInputStream.readFully(bArr);
        imageInputStream.reset();
        return bArr[0] == 71 && bArr[1] == 73 && bArr[2] == 70 && bArr[3] == 56 && (bArr[4] == 55 || bArr[4] == 57) && bArr[5] == 97;
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public ImageReader createReaderInstance(Object obj) {
        return new GIFImageReader(this);
    }
}
