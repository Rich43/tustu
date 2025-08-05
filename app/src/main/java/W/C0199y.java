package W;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* renamed from: W.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/y.class */
public class C0199y extends OutputStream {

    /* renamed from: a, reason: collision with root package name */
    OutputStream f2190a;

    /* renamed from: d, reason: collision with root package name */
    private String f2191d = "";

    /* renamed from: b, reason: collision with root package name */
    ByteArrayOutputStream f2192b = new ByteArrayOutputStream();

    /* renamed from: c, reason: collision with root package name */
    boolean f2193c = false;

    public C0199y(OutputStream outputStream) {
        this.f2190a = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.f2193c) {
            throw new IOException("Can not write to after flushed. Please write all data then do 1 flush at end.");
        }
        this.f2192b.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        if (this.f2193c) {
            throw new IOException("Can not write to after flushed. Please write all data then do 1 flush at end.");
        }
        this.f2192b.write(bArr);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.f2193c) {
            throw new IOException("Can not write to after flushed. Please write all data then do 1 flush at end.");
        }
        this.f2192b.write(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.f2193c) {
            return;
        }
        this.f2190a.write(new ak().a(this.f2192b.toByteArray(), this.f2191d));
        this.f2190a.flush();
        this.f2193c = true;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.f2190a.close();
    }

    public void a(String str) {
        this.f2191d = str;
    }
}
