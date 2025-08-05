package bN;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bN/l.class */
public class l implements t {

    /* renamed from: a, reason: collision with root package name */
    d f7260a = new d(1);

    /* renamed from: b, reason: collision with root package name */
    d f7261b = new d(0);

    /* renamed from: c, reason: collision with root package name */
    private boolean f7262c = true;

    public void a(int i2) {
        this.f7260a.a(i2);
    }

    @Override // bN.t
    public int a() {
        return this.f7260a.a();
    }

    @Override // bN.t
    public int b() {
        return this.f7260a.b() + this.f7261b.b();
    }

    @Override // bN.t
    public byte[] c() {
        return this.f7261b.d();
    }

    @Override // bN.t
    public byte[] d() {
        return C0995c.a(this.f7260a.d(), this.f7261b.d());
    }

    @Override // bN.t
    public byte[] a(byte[] bArr) throws bS.a {
        if (bArr.length != b()) {
            throw new bS.a(b(), bArr.length);
        }
        System.arraycopy(this.f7260a.d(), 0, bArr, 0, this.f7260a.d().length);
        System.arraycopy(this.f7261b.d(), 0, bArr, 0 + this.f7260a.d().length, this.f7261b.d().length);
        return bArr;
    }

    public void b(byte[] bArr) {
        this.f7261b.a(bArr);
    }

    @Override // bN.t
    public void c(byte[] bArr) {
        System.arraycopy(bArr, 0, this.f7260a.d(), 0, this.f7260a.d().length);
        byte[] bArr2 = new byte[bArr.length - this.f7260a.d().length];
        System.arraycopy(bArr, this.f7260a.d().length, bArr2, 0, bArr2.length);
        this.f7261b.a(bArr2);
    }

    @Override // bN.t
    public boolean e() {
        return this.f7262c;
    }

    public String toString() {
        byte[] bArrD = d();
        return bArrD == null ? super.toString() : C0995c.d(bArrD);
    }
}
