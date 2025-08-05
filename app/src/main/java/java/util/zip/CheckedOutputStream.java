package java.util.zip;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/util/zip/CheckedOutputStream.class */
public class CheckedOutputStream extends FilterOutputStream {
    private Checksum cksum;

    public CheckedOutputStream(OutputStream outputStream, Checksum checksum) {
        super(outputStream);
        this.cksum = checksum;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.out.write(i2);
        this.cksum.update(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        this.out.write(bArr, i2, i3);
        this.cksum.update(bArr, i2, i3);
    }

    public Checksum getChecksum() {
        return this.cksum;
    }
}
