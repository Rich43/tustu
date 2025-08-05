package bN;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bN/m.class */
public class m implements t {

    /* renamed from: a, reason: collision with root package name */
    d f7263a;

    /* renamed from: b, reason: collision with root package name */
    d f7264b;

    /* renamed from: c, reason: collision with root package name */
    d f7265c;

    /* renamed from: d, reason: collision with root package name */
    d f7266d;

    /* renamed from: e, reason: collision with root package name */
    d f7267e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7268f;

    public m() {
        this.f7263a = new d(1);
        this.f7264b = new d(0);
        this.f7265c = new d(0);
        this.f7266d = new d(0);
        this.f7267e = new d(0);
        this.f7268f = false;
    }

    public m(k kVar) {
        this.f7263a = new d(1);
        this.f7264b = new d(0);
        this.f7265c = new d(0);
        this.f7266d = new d(0);
        this.f7267e = new d(0);
        this.f7268f = false;
        this.f7266d = new d(kVar.a());
    }

    @Override // bN.t
    public int a() {
        return this.f7263a.a();
    }

    @Override // bN.t
    public int b() {
        return this.f7263a.b() + this.f7264b.b() + this.f7265c.b() + this.f7266d.b() + this.f7267e.b();
    }

    public void a(int i2) {
        if (this.f7266d.b() != i2) {
            this.f7266d.a(new byte[i2]);
        }
    }

    @Override // bN.t
    public byte[] c() {
        return this.f7267e.d();
    }

    public void b(byte[] bArr) {
        this.f7267e.a(bArr);
    }

    @Override // bN.t
    public byte[] d() {
        return C0995c.a(C0995c.a(C0995c.a(C0995c.a(this.f7263a.d(), this.f7264b.d()), this.f7265c.d()), this.f7266d.d()), this.f7267e.d());
    }

    @Override // bN.t
    public void c(byte[] bArr) throws o {
        try {
            System.arraycopy(bArr, 0, this.f7263a.d(), 0, this.f7263a.d().length);
            int length = 0 + this.f7263a.d().length;
            System.arraycopy(bArr, length, this.f7264b.d(), 0, this.f7264b.d().length);
            int length2 = length + this.f7264b.d().length;
            System.arraycopy(bArr, length2, this.f7265c.d(), 0, this.f7265c.d().length);
            int length3 = length2 + this.f7265c.d().length;
            System.arraycopy(bArr, length3, this.f7266d.d(), 0, this.f7266d.d().length);
            int length4 = length3 + this.f7266d.d().length;
            byte[] bArr2 = new byte[bArr.length - length4];
            System.arraycopy(bArr, length4, bArr2, 0, bArr2.length);
            this.f7267e.a(bArr2);
        } catch (Exception e2) {
            throw new o("Invalid data for DTO Packet: \n" + C0995c.d(bArr));
        }
    }

    @Override // bN.t
    public byte[] a(byte[] bArr) throws bS.a {
        if (bArr.length != b()) {
            throw new bS.a(b(), bArr.length);
        }
        System.arraycopy(this.f7263a.d(), 0, bArr, 0, this.f7263a.d().length);
        int length = 0 + this.f7263a.d().length;
        System.arraycopy(this.f7264b.d(), 0, bArr, length, this.f7264b.d().length);
        int length2 = length + this.f7264b.d().length;
        System.arraycopy(this.f7265c.d(), 0, bArr, length2, this.f7265c.d().length);
        int length3 = length2 + this.f7265c.d().length;
        System.arraycopy(this.f7266d.d(), 0, bArr, length3, this.f7266d.d().length);
        System.arraycopy(this.f7267e.d(), 0, bArr, length3 + this.f7266d.d().length, this.f7267e.d().length);
        return bArr;
    }

    @Override // bN.t
    public boolean e() {
        return this.f7268f;
    }

    public byte[] f() {
        return this.f7266d.d();
    }

    public void d(byte[] bArr) throws bS.a {
        if (bArr.length != 0 && bArr.length != this.f7266d.b()) {
            throw new bS.a(this.f7266d.b(), bArr.length);
        }
        this.f7266d.a(bArr);
    }
}
