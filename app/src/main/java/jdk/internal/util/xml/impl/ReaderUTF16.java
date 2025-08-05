package jdk.internal.util.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/ReaderUTF16.class */
public class ReaderUTF16 extends Reader {
    private InputStream is;

    /* renamed from: bo, reason: collision with root package name */
    private char f12867bo;

    public ReaderUTF16(InputStream inputStream, char c2) {
        switch (c2) {
            case 'b':
            case 'l':
                this.f12867bo = c2;
                this.is = inputStream;
                return;
            default:
                throw new IllegalArgumentException("");
        }
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        int i4 = 0;
        if (this.f12867bo == 'b') {
            while (i4 < i3) {
                int i5 = this.is.read();
                if (i5 < 0) {
                    if (i4 != 0) {
                        return i4;
                    }
                    return -1;
                }
                int i6 = i2;
                i2++;
                cArr[i6] = (char) ((i5 << 8) | (this.is.read() & 255));
                i4++;
            }
        } else {
            while (i4 < i3) {
                int i7 = this.is.read();
                if (i7 < 0) {
                    if (i4 != 0) {
                        return i4;
                    }
                    return -1;
                }
                int i8 = i2;
                i2++;
                cArr[i8] = (char) ((this.is.read() << 8) | (i7 & 255));
                i4++;
            }
        }
        return i4;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        char c2;
        int i2 = this.is.read();
        if (i2 < 0) {
            return -1;
        }
        if (this.f12867bo == 'b') {
            c2 = (char) ((i2 << 8) | (this.is.read() & 255));
        } else {
            c2 = (char) ((this.is.read() << 8) | (i2 & 255));
        }
        return c2;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.is.close();
    }
}
