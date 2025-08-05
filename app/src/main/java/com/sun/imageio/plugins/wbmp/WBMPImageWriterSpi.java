package com.sun.imageio.plugins.wbmp;

import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.SampleModel;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/wbmp/WBMPImageWriterSpi.class */
public class WBMPImageWriterSpi extends ImageWriterSpi {
    private static String[] readerSpiNames = {"com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi"};
    private static String[] formatNames = {"wbmp", "WBMP"};
    private static String[] entensions = {"wbmp"};
    private static String[] mimeType = {"image/vnd.wap.wbmp"};
    private boolean registered;

    public WBMPImageWriterSpi() {
        super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.wbmp.WBMPImageWriter", new Class[]{ImageOutputStream.class}, readerSpiNames, true, null, null, null, null, true, null, null, null, null);
        this.registered = false;
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard WBMP Image Writer";
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
        SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
        if (!(sampleModel instanceof MultiPixelPackedSampleModel) || sampleModel.getSampleSize(0) != 1) {
            return false;
        }
        return true;
    }

    @Override // javax.imageio.spi.ImageWriterSpi
    public ImageWriter createWriterInstance(Object obj) throws IIOException {
        return new WBMPImageWriter(this);
    }
}
