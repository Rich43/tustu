package com.sun.imageio.plugins.wbmp;

import com.sun.imageio.plugins.common.ReaderUtil;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/wbmp/WBMPImageReaderSpi.class */
public class WBMPImageReaderSpi extends ImageReaderSpi {
    private static final int MAX_WBMP_WIDTH = 1024;
    private static final int MAX_WBMP_HEIGHT = 768;
    private static String[] writerSpiNames = {"com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi"};
    private static String[] formatNames = {"wbmp", "WBMP"};
    private static String[] entensions = {"wbmp"};
    private static String[] mimeType = {"image/vnd.wap.wbmp"};
    private boolean registered;

    public WBMPImageReaderSpi() {
        super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.wbmp.WBMPImageReader", new Class[]{ImageInputStream.class}, writerSpiNames, true, null, null, null, null, true, "javax_imageio_wbmp_1.0", "com.sun.imageio.plugins.wbmp.WBMPMetadataFormat", null, null);
        this.registered = false;
    }

    @Override // javax.imageio.spi.IIOServiceProvider, javax.imageio.spi.RegisterableService
    public void onRegistration(ServiceRegistry serviceRegistry, Class<?> cls) {
        if (this.registered) {
            return;
        }
        this.registered = true;
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Standard WBMP Image Reader";
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public boolean canDecodeInput(Object obj) throws IOException {
        if (!(obj instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream imageInputStream = (ImageInputStream) obj;
        imageInputStream.mark();
        try {
            byte b2 = imageInputStream.readByte();
            byte b3 = imageInputStream.readByte();
            if (b2 != 0 || b3 != 0) {
                return false;
            }
            int multiByteInteger = ReaderUtil.readMultiByteInteger(imageInputStream);
            int multiByteInteger2 = ReaderUtil.readMultiByteInteger(imageInputStream);
            if (multiByteInteger <= 0 || multiByteInteger2 <= 0) {
                imageInputStream.reset();
                return false;
            }
            long length = imageInputStream.length();
            if (length == -1) {
                boolean z2 = multiByteInteger < 1024 && multiByteInteger2 < 768;
                imageInputStream.reset();
                return z2;
            }
            boolean z3 = length - imageInputStream.getStreamPosition() == ((long) ((multiByteInteger / 8) + (multiByteInteger % 8 == 0 ? 0 : 1))) * ((long) multiByteInteger2);
            imageInputStream.reset();
            return z3;
        } finally {
            imageInputStream.reset();
        }
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public ImageReader createReaderInstance(Object obj) throws IIOException {
        return new WBMPImageReader(this);
    }
}
