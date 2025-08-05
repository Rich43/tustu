package jdk.internal.util.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/ReaderUTF8.class */
public class ReaderUTF8 extends Reader {
    private InputStream is;

    public ReaderUTF8(InputStream inputStream) {
        this.is = inputStream;
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        int i4 = 0;
        while (i4 < i3) {
            int i5 = this.is.read();
            if (i5 < 0) {
                if (i4 != 0) {
                    return i4;
                }
                return -1;
            }
            switch (i5 & 240) {
                case 192:
                case 208:
                    int i6 = i2;
                    i2++;
                    cArr[i6] = (char) (((i5 & 31) << 6) | (this.is.read() & 63));
                    break;
                case 224:
                    int i7 = i2;
                    i2++;
                    cArr[i7] = (char) (((i5 & 15) << 12) | ((this.is.read() & 63) << 6) | (this.is.read() & 63));
                    break;
                case 240:
                    throw new UnsupportedEncodingException("UTF-32 (or UCS-4) encoding not supported.");
                default:
                    int i8 = i2;
                    i2++;
                    cArr[i8] = (char) i5;
                    break;
            }
            i4++;
        }
        return i4;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        int i2 = this.is.read();
        int i3 = i2;
        if (i2 < 0) {
            return -1;
        }
        switch (i3 & 240) {
            case 192:
            case 208:
                i3 = ((i3 & 31) << 6) | (this.is.read() & 63);
                break;
            case 224:
                i3 = ((i3 & 15) << 12) | ((this.is.read() & 63) << 6) | (this.is.read() & 63);
                break;
            case 240:
                throw new UnsupportedEncodingException();
        }
        return i3;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.is.close();
    }
}
