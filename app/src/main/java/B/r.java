package B;

import bH.C;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* loaded from: TunerStudioMS.jar:B/r.class */
public class r extends OutputStream {

    /* renamed from: a, reason: collision with root package name */
    protected DatagramSocket f209a;

    /* renamed from: b, reason: collision with root package name */
    DatagramPacket f210b;

    /* renamed from: g, reason: collision with root package name */
    private InetAddress f211g;

    /* renamed from: h, reason: collision with root package name */
    private int f212h;

    /* renamed from: c, reason: collision with root package name */
    byte[] f213c;

    /* renamed from: d, reason: collision with root package name */
    byte[] f214d;

    /* renamed from: e, reason: collision with root package name */
    int f215e;

    /* renamed from: f, reason: collision with root package name */
    int f216f;

    public r() {
        this.f209a = null;
        this.f210b = null;
        this.f211g = null;
        this.f212h = 0;
        this.f213c = new byte[1024];
        this.f214d = null;
        this.f215e = 0;
        this.f216f = 8192;
    }

    public r(DatagramSocket datagramSocket) {
        this.f209a = null;
        this.f210b = null;
        this.f211g = null;
        this.f212h = 0;
        this.f213c = new byte[1024];
        this.f214d = null;
        this.f215e = 0;
        this.f216f = 8192;
        this.f209a = datagramSocket;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            flush();
        } catch (IOException e2) {
        }
        this.f215e = 0;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.f215e == 0) {
            return;
        }
        if (this.f215e == this.f213c.length) {
            this.f214d = this.f213c;
        } else {
            this.f214d = new byte[this.f215e];
            System.arraycopy(this.f213c, 0, this.f214d, 0, this.f215e);
        }
        this.f210b = new DatagramPacket(this.f214d, this.f215e, a(), b());
        this.f209a.send(this.f210b);
        this.f215e = 0;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        this.f213c[this.f215e] = (byte) (i2 & 255);
        this.f215e++;
        if (this.f215e >= this.f213c.length) {
            flush();
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        int length = i3;
        while (this.f213c.length - this.f215e <= length) {
            try {
                System.arraycopy(bArr, i2 + (i3 - length), this.f213c, this.f215e, this.f213c.length - this.f215e);
                length -= this.f213c.length - this.f215e;
                this.f215e = this.f213c.length;
                flush();
            } catch (ArrayIndexOutOfBoundsException e2) {
                C.c("len: " + i3);
                C.c("lenRemaining: " + length);
                C.c("idx: " + this.f215e);
                C.c("buffer.length: " + this.f213c.length);
                C.c("offset: " + i2);
                C.c("data.length: " + bArr.length);
                throw e2;
            }
        }
        if (length == 0) {
            return;
        }
        System.arraycopy(bArr, i2 + (i3 - length), this.f213c, this.f215e, length);
        this.f215e += length;
    }

    public InetAddress a() {
        return this.f211g;
    }

    public void a(InetAddress inetAddress) {
        this.f211g = inetAddress;
    }

    public int b() {
        return this.f212h;
    }

    public void a(int i2) {
        this.f212h = i2;
    }

    public void a(DatagramSocket datagramSocket) {
        this.f209a = datagramSocket;
    }
}
