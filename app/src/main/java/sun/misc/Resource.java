package sun.misc;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.jar.Manifest;
import sun.nio.ByteBuffered;

/* loaded from: rt.jar:sun/misc/Resource.class */
public abstract class Resource {
    private InputStream cis;

    public abstract String getName();

    public abstract URL getURL();

    public abstract URL getCodeSourceURL();

    public abstract InputStream getInputStream() throws IOException;

    public abstract int getContentLength() throws IOException;

    private synchronized InputStream cachedInputStream() throws IOException {
        if (this.cis == null) {
            this.cis = getInputStream();
        }
        return this.cis;
    }

    public byte[] getBytes() throws IOException {
        boolean z2;
        int contentLength;
        int length;
        InputStream inputStreamCachedInputStream = cachedInputStream();
        boolean zInterrupted = Thread.interrupted();
        while (true) {
            try {
                z2 = zInterrupted;
                contentLength = getContentLength();
                try {
                    break;
                } finally {
                    try {
                        inputStreamCachedInputStream.close();
                    } catch (InterruptedIOException e2) {
                        z2 = true;
                    } catch (IOException e3) {
                    }
                    if (z2) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (InterruptedIOException e4) {
                Thread.interrupted();
                zInterrupted = true;
            }
        }
        byte[] bArrCopyOf = new byte[0];
        if (contentLength == -1) {
            contentLength = Integer.MAX_VALUE;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= contentLength) {
                break;
            }
            if (i2 >= bArrCopyOf.length) {
                length = Math.min(contentLength - i2, bArrCopyOf.length + 1024);
                if (bArrCopyOf.length < i2 + length) {
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, i2 + length);
                }
            } else {
                length = bArrCopyOf.length - i2;
            }
            int i3 = 0;
            try {
                i3 = inputStreamCachedInputStream.read(bArrCopyOf, i2, length);
            } catch (InterruptedIOException e5) {
                Thread.interrupted();
                z2 = true;
            }
            if (i3 >= 0) {
                i2 += i3;
            } else {
                if (contentLength != Integer.MAX_VALUE) {
                    throw new EOFException("Detect premature EOF");
                }
                if (bArrCopyOf.length != i2) {
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, i2);
                }
            }
        }
        return bArrCopyOf;
    }

    public ByteBuffer getByteBuffer() throws IOException {
        Closeable closeableCachedInputStream = cachedInputStream();
        if (closeableCachedInputStream instanceof ByteBuffered) {
            return ((ByteBuffered) closeableCachedInputStream).getByteBuffer();
        }
        return null;
    }

    public Manifest getManifest() throws IOException {
        return null;
    }

    public Certificate[] getCertificates() {
        return null;
    }

    public CodeSigner[] getCodeSigners() {
        return null;
    }

    public Exception getDataError() {
        return null;
    }
}
