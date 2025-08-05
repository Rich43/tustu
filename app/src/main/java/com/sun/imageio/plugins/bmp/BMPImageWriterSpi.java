package com.sun.imageio.plugins.bmp;

import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPImageWriterSpi.class */
public class BMPImageWriterSpi extends ImageWriterSpi {
    private static String[] readerSpiNames = {"com.sun.imageio.plugins.bmp.BMPImageReaderSpi"};
    private static String[] formatNames = {"bmp", "BMP"};
    private static String[] entensions = {"bmp"};
    private static String[] mimeType = {"image/bmp"};
    private boolean registered;

    public BMPImageWriterSpi() {
        super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.bmp.BMPImageWriter", new Class[]{ImageOutputStream.class}, readerSpiNames, false, null, null, null, null, true, BMPMetadata.nativeMetadataFormatName, "com.sun.imageio.plugins.bmp.BMPMetadataFormat", null, null);
        this.registered = false;
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard BMP Image Writer";
    }

    @Override // javax.imageio.spi.IIOServiceProvider, javax.imageio.spi.RegisterableService
    public void onRegistration(ServiceRegistry serviceRegistry, Class<?> cls) {
        if (this.registered) {
            return;
        }
        this.registered = true;
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier) {
        int dataType = imageTypeSpecifier.getSampleModel().getDataType();
        if (dataType < 0 || dataType > 3) {
            return false;
        }
        SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
        int numBands = sampleModel.getNumBands();
        if (numBands != 1 && numBands != 3) {
            return false;
        }
        if (numBands == 1 && dataType != 0) {
            return false;
        }
        if (dataType > 0 && !(sampleModel instanceof SinglePixelPackedSampleModel)) {
            return false;
        }
        return true;
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public ImageWriter createWriterInstance(Object obj) throws IIOException {
        return new BMPImageWriter(this);
    }
}
