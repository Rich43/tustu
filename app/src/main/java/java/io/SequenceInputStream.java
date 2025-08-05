package java.io;

import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:java/io/SequenceInputStream.class */
public class SequenceInputStream extends InputStream {

    /* renamed from: e, reason: collision with root package name */
    Enumeration<? extends InputStream> f12419e;
    InputStream in;

    public SequenceInputStream(Enumeration<? extends InputStream> enumeration) {
        this.f12419e = enumeration;
        try {
            nextStream();
        } catch (IOException e2) {
            throw new Error("panic");
        }
    }

    public SequenceInputStream(InputStream inputStream, InputStream inputStream2) {
        Vector vector = new Vector(2);
        vector.addElement(inputStream);
        vector.addElement(inputStream2);
        this.f12419e = vector.elements();
        try {
            nextStream();
        } catch (IOException e2) {
            throw new Error("panic");
        }
    }

    final void nextStream() throws IOException {
        if (this.in != null) {
            this.in.close();
        }
        if (this.f12419e.hasMoreElements()) {
            this.in = this.f12419e.nextElement();
            if (this.in == null) {
                throw new NullPointerException();
            }
            return;
        }
        this.in = null;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (this.in != null) {
            int i2 = this.in.read();
            if (i2 != -1) {
                return i2;
            }
            nextStream();
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.in == null) {
            return -1;
        }
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        do {
            int i4 = this.in.read(bArr, i2, i3);
            if (i4 > 0) {
                return i4;
            }
            nextStream();
        } while (this.in != null);
        return -1;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        do {
            nextStream();
        } while (this.in != null);
    }
}
