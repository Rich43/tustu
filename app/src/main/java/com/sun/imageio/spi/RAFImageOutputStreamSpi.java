package com.sun.imageio.spi;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Locale;
import javax.imageio.spi.ImageOutputStreamSpi;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/spi/RAFImageOutputStreamSpi.class */
public class RAFImageOutputStreamSpi extends ImageOutputStreamSpi {
    private static final String vendorName = "Oracle Corporation";
    private static final String version = "1.0";
    private static final Class outputClass = RandomAccessFile.class;

    public RAFImageOutputStreamSpi() {
        super("Oracle Corporation", "1.0", outputClass);
    }

    @Override // javax.imageio.spi.IIOServiceProvider
    public String getDescription(Locale locale) {
        return "Service provider that instantiates a FileImageOutputStream from a RandomAccessFile";
    }

    @Override // javax.imageio.spi.ImageOutputStreamSpi
    public ImageOutputStream createOutputStreamInstance(Object obj, boolean z2, File file) {
        if (obj instanceof RandomAccessFile) {
            try {
                return new FileImageOutputStream((RandomAccessFile) obj);
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        throw new IllegalArgumentException("input not a RandomAccessFile!");
    }
}
