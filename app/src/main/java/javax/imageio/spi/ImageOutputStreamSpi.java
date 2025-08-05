package javax.imageio.spi;

import java.io.File;
import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:javax/imageio/spi/ImageOutputStreamSpi.class */
public abstract class ImageOutputStreamSpi extends IIOServiceProvider {
    protected Class<?> outputClass;

    public abstract ImageOutputStream createOutputStreamInstance(Object obj, boolean z2, File file) throws IOException;

    protected ImageOutputStreamSpi() {
    }

    public ImageOutputStreamSpi(String str, String str2, Class<?> cls) {
        super(str, str2);
        this.outputClass = cls;
    }

    public Class<?> getOutputClass() {
        return this.outputClass;
    }

    public boolean canUseCacheFile() {
        return false;
    }

    public boolean needsCacheFile() {
        return false;
    }

    public ImageOutputStream createOutputStreamInstance(Object obj) throws IOException {
        return createOutputStreamInstance(obj, true, null);
    }
}
