package az;

import bH.C;
import bH.I;
import bH.Z;
import f.C1720b;

/* loaded from: TunerStudioMS.jar:az/s.class */
class s extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private String f6514c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f6515d = false;

    /* renamed from: a, reason: collision with root package name */
    C1720b f6516a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ o f6517b;

    s(o oVar, C1720b c1720b) {
        this.f6517b = oVar;
        this.f6516a = c1720b;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Z z2 = null;
        if (this.f6517b.f6496c) {
            z2 = new Z();
            z2.a();
        }
        if (I.a()) {
            this.f6514c = f.f.b();
        }
        if (this.f6517b.f6496c) {
            C.c("MBID Time: " + z2.d());
        }
        this.f6515d = true;
        if (this.f6514c == null || this.f6514c.length() <= 5) {
            return;
        }
        this.f6516a.c(this.f6514c);
    }

    public boolean a() {
        return this.f6515d;
    }

    public String b() {
        return this.f6514c;
    }
}
