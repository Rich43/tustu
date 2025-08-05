package bS;

import bN.k;

/* loaded from: TunerStudioMS.jar:bS/e.class */
public class e {

    /* renamed from: a, reason: collision with root package name */
    bN.d f7553a;

    /* renamed from: b, reason: collision with root package name */
    bN.d f7554b;

    protected e(k kVar) {
        this.f7553a = new bN.d(kVar.e());
        this.f7554b = new bN.d(kVar.f());
    }

    public int a() {
        return this.f7553a.b() + this.f7554b.b();
    }

    public int b() {
        return this.f7553a.b();
    }

    public int c() {
        if (this.f7554b.b() > 0) {
            return this.f7554b.a();
        }
        return -1;
    }

    public void a(int i2) {
        this.f7554b.a(i2);
    }

    public void a(byte[] bArr, int i2) {
        System.arraycopy(bArr, i2, this.f7553a.d(), 0, this.f7553a.d().length);
        System.arraycopy(bArr, i2 + this.f7553a.d().length, this.f7554b.d(), 0, this.f7554b.d().length);
    }

    public byte[] a(byte[] bArr) throws a {
        int iA = a();
        if (bArr.length != iA) {
            throw new a(iA, bArr.length);
        }
        System.arraycopy(this.f7553a.d(), 0, bArr, 0, this.f7553a.d().length);
        System.arraycopy(this.f7554b.d(), 0 + this.f7553a.d().length, bArr, 0, this.f7554b.d().length);
        return bArr;
    }

    public byte[] b(byte[] bArr) throws a {
        int iA = a() - this.f7554b.b();
        if (bArr.length != iA) {
            throw new a(iA, bArr.length);
        }
        System.arraycopy(this.f7553a.d(), 0, bArr, 0, bArr.length);
        return bArr;
    }

    public int d() {
        return this.f7554b.b();
    }

    public byte[] e() {
        return this.f7553a.d();
    }

    public void c(byte[] bArr) {
        this.f7553a.a(bArr);
    }
}
