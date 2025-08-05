package com.sun.imageio.spi;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/spi/RAFImageInputStreamSpi.class */
public class RAFImageInputStreamSpi extends ImageInputStreamSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final Class inputClass = RandomAccessFile.class;

    public RAFImageInputStreamSpi() {
        super("Oracle Corporation", "1.0", inputClass);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Service provider that instantiates a FileImageInputStream from a RandomAccessFile";
    }

    @Override // javax.imageio.spi.ImageInputStreamSpi
    public ImageInputStream createInputStreamInstance(Object obj, boolean z2, File file) {
        if (obj instanceof RandomAccessFile) {
            try {
                return new FileImageInputStream((RandomAccessFile) obj);
            } catch (Exception e2) {
                return null;
            }
        }
        throw new IllegalArgumentException("input not a RandomAccessFile!");
    }
}
