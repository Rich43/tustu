package org.jpedal.jbig2.jai;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/jai/JBIG2ImageReaderSpi.class */
public class JBIG2ImageReaderSpi extends ImageReaderSpi {
    public JBIG2ImageReaderSpi() {
        super("JPedal", "1.0", new String[]{"JBIG2"}, new String[]{"jb2", "jbig2"}, new String[]{"image/x-jbig2"}, "org.jpedal.jbig2.jai.JBIG2ImageReader", new Class[]{ImageInputStream.class}, null, false, null, null, null, null, false, null, null, null, null);
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public boolean canDecodeInput(Object input) throws IOException {
        if (!(input instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream stream = (ImageInputStream) input;
        byte[] header = new byte[8];
        try {
            stream.mark();
            stream.read(header);
            stream.reset();
            byte[] controlHeader = {-105, 74, 66, 50, 13, 10, 26, 10};
            return Arrays.equals(controlHeader, header);
        } catch (IOException e2) {
            return false;
        }
    }

    @Override // javax.imageio.spi.ImageReaderSpi
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new JBIG2ImageReader(this);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "JPedal JBIG2 Image Decoder provided by IDRsolutions.  See http://www.jpedal.org/jbig.php";
    }
}
