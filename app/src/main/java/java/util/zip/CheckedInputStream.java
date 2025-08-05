package java.util.zip;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:java/util/zip/CheckedInputStream.class */
public class CheckedInputStream extends FilterInputStream {
    private Checksum cksum;

    public CheckedInputStream(InputStream inputStream, Checksum checksum) {
        super(inputStream);
        this.cksum = checksum;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = this.in.read();
        if (i2 != -1) {
            this.cksum.update(i2);
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = this.in.read(bArr, i2, i3);
        if (i4 != -1) {
            this.cksum.update(bArr, i2, i4);
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        byte[] bArr = new byte[512];
        long j3 = 0;
        while (true) {
            long j4 = j3;
            if (j4 < j2) {
                long j5 = j2 - j4;
                long j6 = read(bArr, 0, j5 < ((long) bArr.length) ? (int) j5 : bArr.length);
                if (j6 == -1) {
                    return j4;
                }
                j3 = j4 + j6;
            } else {
                return j4;
            }
        }
    }

    public Checksum getChecksum() {
        return this.cksum;
    }
}
