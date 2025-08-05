package az;

import bH.C;
import bH.I;
import bH.Z;
import f.C1720b;
import java.io.File;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:az/q.class */
class q extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private String f6506c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f6507d = false;

    /* renamed from: a, reason: collision with root package name */
    C1720b f6508a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ o f6509b;

    q(o oVar, C1720b c1720b) {
        this.f6509b = oVar;
        this.f6508a = c1720b;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Z z2 = null;
        if (this.f6509b.f6496c) {
            z2 = new Z();
            z2.a();
        }
        if (I.a()) {
            File file = new File(".");
            if (file.getAbsolutePath().contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                this.f6506c = f.f.a(file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(CallSiteDescriptor.TOKEN_DELIMITER)));
            }
        } else if (I.d()) {
            this.f6506c = f.f.h();
        } else if (I.b()) {
            this.f6506c = f.f.j();
        } else {
            this.f6506c = null;
        }
        if (this.f6509b.f6496c) {
            C.c("HDID Time: " + z2.d());
        }
        this.f6507d = true;
        if (this.f6506c == null || this.f6506c.length() <= 5) {
            return;
        }
        this.f6508a.b(this.f6506c);
    }

    public boolean a() {
        return this.f6507d;
    }

    public String b() {
        return this.f6506c;
    }
}
