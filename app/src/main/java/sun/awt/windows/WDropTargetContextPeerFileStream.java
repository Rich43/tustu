package sun.awt.windows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/* compiled from: WDropTargetContextPeer.java */
/* loaded from: rt.jar:sun/awt/windows/WDropTargetContextPeerFileStream.class */
final class WDropTargetContextPeerFileStream extends FileInputStream {
    private long stgmedium;

    private native void freeStgMedium(long j2);

    WDropTargetContextPeerFileStream(String str, long j2) throws FileNotFoundException {
        super(str);
        this.stgmedium = j2;
    }

    @Override // java.io.FileInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.stgmedium != 0) {
            super.close();
            freeStgMedium(this.stgmedium);
            this.stgmedium = 0L;
        }
    }
}
