package defpackage;

import ao.C0658bv;
import ao.bP;
import i.C1743c;
import java.io.File;

/* loaded from: TunerStudioMS.jar:b.class */
final class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0658bv f6518a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f6519b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ File f6520c;

    b(C0658bv c0658bv, bP bPVar, File file) {
        this.f6518a = c0658bv;
        this.f6519b = bPVar;
        this.f6520c = file;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6518a.d()) {
            C1743c.a().a(new c(this));
        }
        this.f6519b.b(this.f6520c.getAbsolutePath(), this.f6518a.c());
        if (this.f6518a.b() == null || this.f6518a.b().isEmpty()) {
            return;
        }
        this.f6519b.d(this.f6518a.b());
    }
}
