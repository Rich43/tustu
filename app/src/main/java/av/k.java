package aV;

import ac.InterfaceC0487C;

/* loaded from: TunerStudioMS.jar:aV/k.class */
final class k implements InterfaceC0487C {

    /* renamed from: a, reason: collision with root package name */
    int f3948a = -1;

    k() {
    }

    @Override // ac.InterfaceC0487C
    public double a() {
        int iD = x.a().g().d();
        if (iD == this.f3948a) {
            return 0.0d;
        }
        this.f3948a = iD;
        return 1.0d;
    }
}
