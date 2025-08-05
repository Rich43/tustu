package aP;

import java.io.File;

/* renamed from: aP.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/z.class */
class RunnableC0470z implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File[] f3844a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0338f f3845b;

    RunnableC0470z(C0338f c0338f, File[] fileArr) {
        this.f3845b = c0338f;
        this.f3844a = fileArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        cZ.a().i().a().g(gW.f3467r);
        new E(this.f3845b, this.f3844a).start();
    }
}
