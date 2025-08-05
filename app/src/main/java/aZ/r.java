package az;

import bH.C;
import bH.Z;
import f.C1720b;

/* loaded from: TunerStudioMS.jar:az/r.class */
class r extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private String f6510c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f6511d = false;

    /* renamed from: a, reason: collision with root package name */
    C1720b f6512a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ o f6513b;

    r(o oVar, C1720b c1720b) {
        this.f6513b = oVar;
        this.f6512a = c1720b;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Z z2 = null;
        if (this.f6513b.f6496c) {
            z2 = new Z();
            z2.a();
        }
        try {
            this.f6510c = f.f.k();
            if (this.f6510c != null) {
                this.f6512a.a(this.f6510c);
            }
        } catch (Exception e2) {
        }
        if (this.f6513b.f6496c) {
            C.c("MAC Time: " + z2.d());
        }
        this.f6511d = true;
    }

    public boolean a() {
        return this.f6511d;
    }

    public String b() {
        return this.f6510c;
    }
}
