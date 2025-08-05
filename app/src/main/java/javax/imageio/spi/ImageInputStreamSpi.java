package javax.imageio.spi;

import java.io.File;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:javax/imageio/spi/ImageInputStreamSpi.class */
public abstract class ImageInputStreamSpi extends IIOServiceProvider {
    protected Class<?> inputClass;

    public abstract ImageInputStream createInputStreamInstance(Object obj, boolean z2, File file) throws IOException;

    protected ImageInputStreamSpi() {
    }

    public ImageInputStreamSpi(String str, String str2, Class<?> cls) {
        super(str, str2);
        this.inputClass = cls;
    }

    public Class<?> getInputClass() {
        return this.inputClass;
    }

    public boolean canUseCacheFile() {
        return false;
    }

    public boolean needsCacheFile() {
        return false;
    }

    public ImageInputStream createInputStreamInstance(Object obj) throws IOException {
        return createInputStreamInstance(obj, true, null);
    }
}
