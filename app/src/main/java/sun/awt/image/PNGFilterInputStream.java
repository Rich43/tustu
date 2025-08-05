package sun.awt.image;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* compiled from: PNGImageDecoder.java */
/* loaded from: rt.jar:sun/awt/image/PNGFilterInputStream.class */
class PNGFilterInputStream extends FilterInputStream {
    PNGImageDecoder owner;
    public InputStream underlyingInputStream;

    public PNGFilterInputStream(PNGImageDecoder pNGImageDecoder, InputStream inputStream) {
        super(inputStream);
        this.underlyingInputStream = this.in;
        this.owner = pNGImageDecoder;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return (this.owner.limit - this.owner.pos) + this.in.available();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.owner.chunkLength <= 0 && !this.owner.getData()) {
            return -1;
        }
        this.owner.chunkLength--;
        byte[] bArr = this.owner.inbuf;
        PNGImageDecoder pNGImageDecoder = this.owner;
        int i2 = pNGImageDecoder.chunkStart;
        pNGImageDecoder.chunkStart = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.owner.chunkLength <= 0 && !this.owner.getData()) {
            return -1;
        }
        if (this.owner.chunkLength < i3) {
            i3 = this.owner.chunkLength;
        }
        System.arraycopy(this.owner.inbuf, this.owner.chunkStart, bArr, i2, i3);
        this.owner.chunkLength -= i3;
        this.owner.chunkStart += i3;
        return i3;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        int i2 = 0;
        while (i2 < j2 && read() >= 0) {
            i2++;
        }
        return i2;
    }
}
