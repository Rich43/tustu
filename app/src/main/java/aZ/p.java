package az;

import bH.C;
import bH.I;
import bH.Z;
import f.C1720b;

/* loaded from: TunerStudioMS.jar:az/p.class */
class p extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private String f6502c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f6503d = false;

    /* renamed from: a, reason: collision with root package name */
    C1720b f6504a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ o f6505b;

    p(o oVar, C1720b c1720b) {
        this.f6505b = oVar;
        this.f6504a = c1720b;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Z z2 = null;
        if (this.f6505b.f6496c) {
            z2 = new Z();
            z2.a();
        }
        if (I.a()) {
            this.f6502c = f.f.c();
        } else if (I.d()) {
            this.f6502c = f.f.e();
        } else if (I.b()) {
            this.f6502c = f.f.g();
        } else {
            this.f6502c = null;
        }
        if (this.f6502c != null && !this.f6502c.isEmpty()) {
            this.f6504a.i(this.f6502c);
        }
        if (this.f6505b.f6496c) {
            C.c("cpu Time: " + z2.d());
        }
        this.f6503d = true;
    }

    public boolean a() {
        return this.f6503d;
    }

    public String b() {
        return this.f6502c;
    }
}
