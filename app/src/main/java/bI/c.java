package bI;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TunerStudioMS.jar:bI/c.class */
public class c extends FilterOutputStream {

    /* renamed from: a, reason: collision with root package name */
    private boolean f7086a;

    /* renamed from: b, reason: collision with root package name */
    private int f7087b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f7088c;

    /* renamed from: d, reason: collision with root package name */
    private int f7089d;

    /* renamed from: e, reason: collision with root package name */
    private int f7090e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7091f;

    /* renamed from: g, reason: collision with root package name */
    private byte[] f7092g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f7093h;

    /* renamed from: i, reason: collision with root package name */
    private int f7094i;

    /* renamed from: j, reason: collision with root package name */
    private byte[] f7095j;

    public c(OutputStream outputStream, int i2) {
        super(outputStream);
        this.f7091f = (i2 & 8) != 0;
        this.f7086a = (i2 & 1) != 0;
        this.f7089d = this.f7086a ? 3 : 4;
        this.f7088c = new byte[this.f7089d];
        this.f7087b = 0;
        this.f7090e = 0;
        this.f7093h = false;
        this.f7092g = new byte[4];
        this.f7094i = i2;
        this.f7095j = a.c(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.f7093h) {
            this.out.write(i2);
            return;
        }
        if (this.f7086a) {
            byte[] bArr = this.f7088c;
            int i3 = this.f7087b;
            this.f7087b = i3 + 1;
            bArr[i3] = (byte) i2;
            if (this.f7087b >= this.f7089d) {
                this.out.write(a.b(this.f7092g, this.f7088c, this.f7089d, this.f7094i));
                this.f7090e += 4;
                if (this.f7091f && this.f7090e >= 76) {
                    this.out.write(10);
                    this.f7090e = 0;
                }
                this.f7087b = 0;
                return;
            }
            return;
        }
        if (this.f7095j[i2 & 127] <= -5) {
            if (this.f7095j[i2 & 127] != -5) {
                throw new IOException("Invalid character in Base64 data.");
            }
            return;
        }
        byte[] bArr2 = this.f7088c;
        int i4 = this.f7087b;
        this.f7087b = i4 + 1;
        bArr2[i4] = (byte) i2;
        if (this.f7087b >= this.f7089d) {
            this.out.write(this.f7092g, 0, a.b(this.f7088c, 0, this.f7092g, 0, this.f7094i));
            this.f7087b = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.f7093h) {
            this.out.write(bArr, i2, i3);
            return;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write(bArr[i2 + i4]);
        }
    }

    public void a() throws IOException {
        if (this.f7087b > 0) {
            if (!this.f7086a) {
                throw new IOException("Base64 input not properly padded.");
            }
            this.out.write(a.b(this.f7092g, this.f7088c, this.f7087b, this.f7094i));
            this.f7087b = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        a();
        super.close();
        this.f7088c = null;
        this.out = null;
    }
}
