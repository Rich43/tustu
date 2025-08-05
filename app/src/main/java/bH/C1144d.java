package bh;

import i.InterfaceC1749i;

/* renamed from: bh.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/d.class */
class C1144d implements InterfaceC1749i {

    /* renamed from: a, reason: collision with root package name */
    long f8105a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1142b f8106b;

    C1144d(C1142b c1142b) {
        this.f8106b = c1142b;
    }

    @Override // i.InterfaceC1749i
    public void a() {
        this.f8105a = System.currentTimeMillis();
    }

    @Override // i.InterfaceC1749i
    public void b() {
        if (System.currentTimeMillis() - 3000 > 0) {
            this.f8106b.f(false);
        }
    }
}
