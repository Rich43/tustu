package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.common.ImageTools;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* compiled from: BMPImageLoaderFactory.java */
/* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/LEInputStream.class */
final class LEInputStream {
    public final InputStream in;

    LEInputStream(InputStream is) {
        this.in = is;
    }

    public final short readShort() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch2 << 8) + ch1);
    }

    public final int readInt() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        int ch3 = this.in.read();
        int ch4 = this.in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    public final void skipBytes(int n2) throws IOException {
        ImageTools.skipFully(this.in, n2);
    }
}
