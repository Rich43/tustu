package com.sun.imageio.spi;

import java.io.File;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/spi/FileImageInputStreamSpi.class */
public class FileImageInputStreamSpi extends ImageInputStreamSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final Class inputClass = File.class;

    public FileImageInputStreamSpi() {
        super("Oracle Corporation", "1.0", inputClass);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Service provider that instantiates a FileImageInputStream from a File";
    }

    @Override // javax.imageio.spi.ImageInputStreamSpi
    public ImageInputStream createInputStreamInstance(Object obj, boolean z2, File file) {
        if (obj instanceof File) {
            try {
                return new FileImageInputStream((File) obj);
            } catch (Exception e2) {
                return null;
            }
        }
        throw new IllegalArgumentException();
    }
}
