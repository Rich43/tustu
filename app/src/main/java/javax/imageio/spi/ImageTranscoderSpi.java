package javax.imageio.spi;

import javax.imageio.ImageTranscoder;

/* loaded from: rt.jar:javax/imageio/spi/ImageTranscoderSpi.class */
public abstract class ImageTranscoderSpi extends IIOServiceProvider {
    public abstract String getReaderServiceProviderName();

    public abstract String getWriterServiceProviderName();

    public abstract ImageTranscoder createTranscoderInstance();

    protected ImageTranscoderSpi() {
    }

    public ImageTranscoderSpi(String str, String str2) {
        super(str, str2);
    }
}
