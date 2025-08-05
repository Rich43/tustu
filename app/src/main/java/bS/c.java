package bS;

import G.C0123f;
import bH.C;
import bH.C0995c;
import bN.k;
import bN.l;
import bN.m;
import bN.t;
import java.util.zip.CRC32;

/* loaded from: TunerStudioMS.jar:bS/c.class */
public class c {

    /* renamed from: d, reason: collision with root package name */
    private b f7541d;

    /* renamed from: f, reason: collision with root package name */
    private e f7543f;

    /* renamed from: h, reason: collision with root package name */
    private m f7545h;

    /* renamed from: a, reason: collision with root package name */
    k f7546a;

    /* renamed from: e, reason: collision with root package name */
    private t f7542e = null;

    /* renamed from: b, reason: collision with root package name */
    C0123f f7547b = new C0123f();

    /* renamed from: c, reason: collision with root package name */
    CRC32 f7548c = new CRC32();

    /* renamed from: i, reason: collision with root package name */
    private boolean f7549i = true;

    /* renamed from: g, reason: collision with root package name */
    private l f7544g = new l();

    protected c(k kVar) {
        this.f7541d = null;
        this.f7543f = null;
        this.f7541d = new b(kVar);
        this.f7543f = new e(kVar);
        this.f7546a = kVar;
        this.f7545h = new m(kVar);
    }

    public b a() {
        return this.f7541d;
    }

    public t b() {
        return this.f7542e;
    }

    public e c() {
        return this.f7543f;
    }

    public void a(byte[] bArr) {
        if (b(bArr)) {
            this.f7542e = this.f7544g;
        } else {
            this.f7542e = this.f7545h;
        }
        this.f7542e.c(bArr);
    }

    private boolean b(byte[] bArr) {
        return bArr.length > 0 && C0995c.a(bArr[0]) > 192;
    }

    public boolean d() {
        if (this.f7543f.c() == -1) {
            return true;
        }
        int iG = g();
        if (iG != this.f7543f.c()) {
            C.b("Checksum Error: Expected: 0x" + Integer.toHexString(iG).toUpperCase() + ", found: 0x" + Integer.toHexString(this.f7543f.c()).toUpperCase());
        }
        return iG == this.f7543f.c();
    }

    public void e() {
        this.f7541d.a(this.f7542e.b() + this.f7543f.b());
    }

    public void f() {
        if (this.f7543f.c() == -1) {
            return;
        }
        this.f7543f.a(g());
    }

    public int g() {
        if (this.f7543f.c() == -1) {
            return -1;
        }
        if (this.f7546a.f() != 1) {
            if (this.f7546a.f() != 4) {
                C.b("Unsupported CRC Size: " + this.f7546a.f() + " skipping check, currently only 1 and 4 are supported.");
                return -1;
            }
            try {
                byte[] bArrB = this.f7541d.b(this.f7547b.b(this.f7541d.a()));
                byte[] bArrA = this.f7542e.a(this.f7547b.b(this.f7542e.b()));
                byte[] bArrB2 = this.f7543f.b(this.f7547b.b(this.f7543f.a() - this.f7543f.d()));
                this.f7548c.reset();
                this.f7548c.update(bArrB);
                this.f7548c.update(bArrA);
                this.f7548c.update(bArrB2);
                this.f7547b.a(bArrB);
                this.f7547b.a(bArrA);
                this.f7547b.a(bArrB2);
                return (int) this.f7548c.getValue();
            } catch (a e2) {
                C.b("CRC Validation failed with unexpected buffer size: " + e2.getLocalizedMessage());
                return -1;
            }
        }
        try {
            byte[] bArrB3 = this.f7541d.b(this.f7547b.b(this.f7541d.a()));
            byte[] bArrA2 = this.f7542e.a(this.f7547b.b(this.f7542e.b()));
            byte[] bArrB4 = this.f7543f.b(this.f7547b.b(this.f7543f.a() - this.f7543f.d()));
            int iA = 0;
            for (byte b2 : bArrB3) {
                iA += C0995c.a(b2);
            }
            for (byte b3 : bArrA2) {
                iA += C0995c.a(b3);
            }
            for (byte b4 : bArrB4) {
                iA += C0995c.a(b4);
            }
            int i2 = iA % 256;
            this.f7547b.a(bArrB3);
            this.f7547b.a(bArrA2);
            this.f7547b.a(bArrB4);
            return i2;
        } catch (a e3) {
            C.b("CRC Validation failed with unexpected buffer size: " + e3.getLocalizedMessage());
            return -1;
        }
    }

    public byte[] h() throws a {
        byte[] bArrB = this.f7541d.b(this.f7547b.b(this.f7541d.a()));
        byte[] bArrA = this.f7542e.a(this.f7547b.b(this.f7542e.b()));
        byte[] bArrA2 = this.f7543f.a(this.f7547b.b(this.f7543f.a()));
        byte[] bArr = new byte[bArrB.length + bArrA.length + bArrA2.length];
        System.arraycopy(bArrB, 0, bArr, 0, bArrB.length);
        int length = 0 + bArrB.length;
        System.arraycopy(bArrA, 0, bArr, length, bArrA.length);
        System.arraycopy(bArrA2, 0, bArr, length + bArrA.length, bArrA2.length);
        this.f7547b.a(bArrB);
        this.f7547b.a(bArrA);
        this.f7547b.a(bArrA2);
        return bArr;
    }

    public void i() {
        if (this.f7546a.m()) {
            int i2 = this.f7542e instanceof l ? this.f7546a.i() - this.f7542e.b() : this.f7546a.j() - this.f7542e.b();
            if (i2 != this.f7543f.b()) {
                this.f7547b.a(this.f7543f.e());
                this.f7543f.c(this.f7547b.b(i2));
            }
        }
    }

    public boolean j() {
        return this.f7549i;
    }

    public void a(boolean z2) {
        this.f7549i = z2;
    }
}
