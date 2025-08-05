package bK;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: TunerStudioMS.jar:bK/c.class */
public class c extends InputStream {

    /* renamed from: a, reason: collision with root package name */
    private InputStream f7110a;

    /* renamed from: b, reason: collision with root package name */
    private int f7111b;

    /* renamed from: c, reason: collision with root package name */
    private int f7112c;

    /* renamed from: d, reason: collision with root package name */
    private int f7113d;

    /* renamed from: e, reason: collision with root package name */
    private b f7114e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7115f;

    /* renamed from: g, reason: collision with root package name */
    private a f7116g;

    /* renamed from: h, reason: collision with root package name */
    private int f7117h;

    /* renamed from: i, reason: collision with root package name */
    private int f7118i;

    /* renamed from: j, reason: collision with root package name */
    private int f7119j;

    /* renamed from: k, reason: collision with root package name */
    private int f7120k;

    /* renamed from: l, reason: collision with root package name */
    private int f7121l;

    public c(InputStream inputStream) {
        this();
        this.f7110a = inputStream;
    }

    public c() {
        this.f7121l = 0;
        this.f7116g = new a(128);
        this.f7113d = -1;
        this.f7117h = 9;
        this.f7118i = -1;
    }

    private int a(int i2) throws IOException {
        while (this.f7112c < i2) {
            int i3 = this.f7110a.read();
            if (i3 < 0) {
                return -1;
            }
            this.f7111b |= i3 << this.f7112c;
            this.f7112c += 8;
        }
        int i4 = this.f7111b & ((1 << i2) - 1);
        this.f7111b >>>= i2;
        this.f7112c -= i2;
        return i4;
    }

    private void b() throws IOException {
        int i2 = (this.f7110a.read() << 8) | this.f7110a.read();
        if (i2 != 8093) {
            throw new RuntimeException("Bad magic number " + i2);
        }
        int i3 = this.f7110a.read();
        this.f7115f = (i3 & 128) != 0;
        this.f7113d = i3 & 31;
        if (this.f7113d > 16) {
            throw new RuntimeException("Cannot handle " + this.f7113d + " bits");
        }
        this.f7114e = new b(1 << this.f7113d);
    }

    private int c() throws IOException {
        int iA = a(this.f7117h);
        this.f7120k++;
        if (iA < 0) {
            return -1;
        }
        if (iA == 256) {
            while (this.f7115f && this.f7120k % 8 != 0) {
                a(this.f7117h);
                this.f7120k++;
            }
            this.f7114e.a();
            this.f7116g.a();
            this.f7118i = -1;
            this.f7117h = 9;
            return 0;
        }
        int iB = this.f7114e.b();
        if (iA < iB) {
            this.f7114e.a((char) iA, this.f7116g);
        } else {
            if (iA != iB) {
                throw new IOException("Invalid code " + iA);
            }
            this.f7116g.a(this.f7116g.c()[0]);
        }
        this.f7119j = 0;
        if (this.f7118i >= 0 && iB < (1 << this.f7113d)) {
            this.f7114e.a((char) this.f7118i, this.f7116g.c()[0]);
            if (iB + 1 >= (1 << this.f7117h) && this.f7117h < this.f7113d) {
                this.f7117h++;
                this.f7120k = 0;
            }
        }
        this.f7118i = (char) iA;
        return this.f7116g.b();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.f7113d < 0) {
            b();
        }
        while (this.f7119j >= this.f7116g.b()) {
            if (c() < 0) {
                return -1;
            }
        }
        byte[] bArrC = this.f7116g.c();
        int i2 = this.f7119j;
        this.f7119j = i2 + 1;
        return 255 & bArrC[i2];
    }

    public static int a(c cVar, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[128];
        while (true) {
            int i2 = cVar.read(bArr);
            if (i2 < 0) {
                outputStream.flush();
                return cVar.a();
            }
            outputStream.write(bArr, 0, i2);
            cVar.f7121l += i2;
        }
    }

    public int a() {
        return this.f7121l;
    }
}
