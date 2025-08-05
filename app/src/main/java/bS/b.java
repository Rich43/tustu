package bS;

import bH.C;
import bN.k;

/* loaded from: TunerStudioMS.jar:bS/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    bN.d f7539a;

    /* renamed from: b, reason: collision with root package name */
    bN.d f7540b;

    protected b(k kVar) {
        this.f7539a = new bN.d(kVar.c());
        this.f7539a.a(false);
        this.f7540b = new bN.d(kVar.d());
        this.f7540b.a(false);
    }

    public int a() {
        return this.f7539a.b() + this.f7540b.b();
    }

    public int b() {
        if (this.f7539a.b() > 0) {
            return this.f7539a.a();
        }
        return -1;
    }

    public void a(int i2) {
        this.f7539a.a(i2);
    }

    public int c() {
        if (this.f7540b.b() > 0) {
            return this.f7540b.a();
        }
        return -1;
    }

    public void b(int i2) {
        this.f7540b.a(i2);
    }

    public void a(byte[] bArr) {
        if (bArr.length != a()) {
            C.a("wrong size header buffer!!! Len: " + bArr.length + ", expected: " + a());
        }
        System.arraycopy(bArr, 0, this.f7539a.d(), 0, this.f7539a.d().length);
        System.arraycopy(bArr, 0 + this.f7539a.d().length, this.f7540b.d(), 0, this.f7540b.d().length);
    }

    public byte[] b(byte[] bArr) throws a {
        int iA = a();
        if (bArr.length != iA) {
            throw new a(iA, bArr.length);
        }
        System.arraycopy(this.f7539a.d(), 0, bArr, 0, this.f7539a.d().length);
        System.arraycopy(this.f7540b.d(), 0, bArr, 0 + this.f7539a.d().length, this.f7540b.d().length);
        return bArr;
    }
}
