package sun.awt.windows;

import java.io.IOException;
import java.io.InputStream;

/* compiled from: WDropTargetContextPeer.java */
/* loaded from: rt.jar:sun/awt/windows/WDropTargetContextPeerIStream.class */
final class WDropTargetContextPeerIStream extends InputStream {
    private long istream;

    private native int Available(long j2);

    private native int Read(long j2) throws IOException;

    private native int ReadBytes(long j2, byte[] bArr, int i2, int i3) throws IOException;

    private native void Close(long j2);

    WDropTargetContextPeerIStream(long j2) throws IOException {
        if (j2 == 0) {
            throw new IOException("No IStream");
        }
        this.istream = j2;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.istream == 0) {
            throw new IOException("No IStream");
        }
        return Available(this.istream);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.istream == 0) {
            throw new IOException("No IStream");
        }
        return Read(this.istream);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.istream == 0) {
            throw new IOException("No IStream");
        }
        return ReadBytes(this.istream, bArr, i2, i3);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.istream != 0) {
            super.close();
            Close(this.istream);
            this.istream = 0L;
        }
    }
}
