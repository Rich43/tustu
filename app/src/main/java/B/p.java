package B;

import G.C0123f;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:B/p.class */
public class p extends InputStream {

    /* renamed from: i, reason: collision with root package name */
    private DatagramSocket f197i;

    /* renamed from: e, reason: collision with root package name */
    r f203e;

    /* renamed from: g, reason: collision with root package name */
    int f205g;

    /* renamed from: j, reason: collision with root package name */
    private final List f198j = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    byte[] f200b = new byte[TFTP.DEFAULT_TIMEOUT];

    /* renamed from: c, reason: collision with root package name */
    int f201c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f202d = 0;

    /* renamed from: f, reason: collision with root package name */
    q f204f = null;

    /* renamed from: h, reason: collision with root package name */
    C0123f f206h = new C0123f();

    /* renamed from: a, reason: collision with root package name */
    DatagramPacket f199a = new DatagramPacket(this.f200b, this.f200b.length);

    public p(DatagramSocket datagramSocket, r rVar) {
        this.f197i = datagramSocket;
        this.f203e = rVar;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            a().close();
        } catch (Exception e2) {
        }
        a((DatagramSocket) null);
        this.f201c = 0;
        this.f202d = 0;
    }

    @Override // java.io.InputStream
    public int available() {
        if (this.f201c == this.f202d) {
            try {
                b();
            } catch (IOException e2) {
                return 0;
            }
        }
        return this.f201c - this.f202d;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.f202d == this.f201c) {
            b();
        }
        this.f205g = this.f200b[this.f202d] & 255;
        this.f202d++;
        return this.f205g;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.f202d == this.f201c) {
            b();
        }
        if (this.f202d > this.f201c) {
            C.c("No UDP bytes left.");
            this.f202d = 0;
            this.f201c = 0;
            return -1;
        }
        if (this.f202d == this.f201c) {
            C.c("No UDP bytes left.");
            return 0;
        }
        try {
            System.arraycopy(this.f200b, this.f202d, bArr, i2 + (i3 - i3), i3);
            this.f202d += i3;
            return i3;
        } catch (Exception e2) {
            C.c("UDPInputStream error: packetIndex:" + this.f202d + ", off:" + i2 + ", lenRemaining:" + i3 + ", buffLen:" + bArr.length);
            return 0;
        }
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        if (this.f202d == this.f201c) {
            b();
        }
        long jAvailable = j2;
        while (available() < jAvailable) {
            jAvailable -= available();
            b();
        }
        this.f202d += (int) jAvailable;
        return j2;
    }

    private void b() throws IOException {
        if (a() == null) {
            throw new IOException("Socket closed");
        }
        if (this.f204f == null) {
            this.f204f = new q(this);
            this.f204f.start();
        }
        if (this.f198j.isEmpty()) {
            synchronized (this.f198j) {
                try {
                    this.f198j.wait(700L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(p.class.getName()).log(Level.WARNING, "Error waiting for UDP Packet", (Throwable) e2);
                }
            }
        }
        if (this.f198j.isEmpty()) {
            throw new IOException("Read Socket Timeout");
        }
        if (this.f202d != this.f201c) {
            C.c("Were bytes added back?");
            return;
        }
        this.f199a = (DatagramPacket) this.f198j.remove(0);
        byte[] bArr = this.f200b;
        this.f200b = this.f199a.getData();
        this.f206h.a(bArr);
        if (this.f199a.getAddress() != null) {
            this.f203e.a(this.f199a.getAddress());
            this.f203e.a(this.f199a.getPort());
        }
        this.f202d = 0;
        this.f201c = this.f199a.getLength();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() throws IOException {
        if (a() == null) {
            throw new IOException("Socket closed");
        }
        DatagramPacket datagramPacket = new DatagramPacket(this.f206h.b(TFTP.DEFAULT_TIMEOUT), TFTP.DEFAULT_TIMEOUT);
        a().receive(datagramPacket);
        this.f198j.add(datagramPacket);
        synchronized (this.f198j) {
            this.f198j.notify();
        }
    }

    @Override // java.io.InputStream
    public void mark(int i2) {
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("Marks not supported by UdpInputStream, add a BufferedInputStream.");
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    public DatagramSocket a() {
        return this.f197i;
    }

    public void a(DatagramSocket datagramSocket) {
        this.f197i = datagramSocket;
    }

    public void a(byte[] bArr) {
        C.c("Adding to UDP InputStream: \n" + C0995c.d(bArr));
        if (bArr.length + this.f201c > this.f200b.length) {
            byte[] bArr2 = new byte[bArr.length + this.f201c];
            System.arraycopy(this.f200b, 0, bArr2, 0, this.f200b.length);
            byte[] bArr3 = this.f200b;
            this.f200b = bArr2;
            this.f206h.a(bArr3);
        }
        System.arraycopy(bArr, 0, this.f200b, this.f202d, bArr.length);
        this.f201c += bArr.length;
    }

    public void a(r rVar) {
        this.f203e = rVar;
    }
}
