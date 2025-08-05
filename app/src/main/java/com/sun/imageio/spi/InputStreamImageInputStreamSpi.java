package com.sun.imageio.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/spi/InputStreamImageInputStreamSpi.class */
public class InputStreamImageInputStreamSpi extends ImageInputStreamSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final Class inputClass = InputStream.class;

    public InputStreamImageInputStreamSpi() {
        super("Oracle Corporation", "1.0", inputClass);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Service provider that instantiates a FileCacheImageInputStream or MemoryCacheImageInputStream from an InputStream";
    }

    @Override // javax.imageio.spi.ImageInputStreamSpi
    public boolean canUseCacheFile() {
        return true;
    }

    @Override // javax.imageio.spi.ImageInputStreamSpi
    public boolean needsCacheFile() {
        return false;
    }

    @Override // javax.imageio.spi.ImageInputStreamSpi
    public ImageInputStream createInputStreamInstance(Object obj, boolean z2, File file) throws IOException {
        if (obj instanceof InputStream) {
            InputStream inputStream = (InputStream) obj;
            if (z2) {
                return new FileCacheImageInputStream(inputStream, file);
            }
            return new MemoryCacheImageInputStream(inputStream);
        }
        throw new IllegalArgumentException();
    }
}
