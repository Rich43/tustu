package G;

import L.C0157n;

/* loaded from: TunerStudioMS.jar:G/U.class */
class U implements aF {

    /* renamed from: a, reason: collision with root package name */
    int f499a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ T f500b;

    U(T t2) {
        this.f500b = t2;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (this.f500b.c() == null || !str.equals(this.f500b.c().c())) {
            return;
        }
        C0157n c0157nA = C0157n.a();
        int i2 = this.f499a;
        this.f499a = i2 + 1;
        c0157nA.a(i2);
    }
}
