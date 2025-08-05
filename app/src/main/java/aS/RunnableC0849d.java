package as;

import ao.C0645bi;
import java.io.File;

/* renamed from: as.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/d.class */
class RunnableC0849d implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File f6247a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String[] f6248b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0848c f6249c;

    RunnableC0849d(C0848c c0848c, File file, String[] strArr) {
        this.f6249c = c0848c;
        this.f6247a = file;
        this.f6248b = strArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6249c.f6246a.f6244d != null) {
            this.f6249c.f6246a.f6244d.a(this.f6247a);
        } else {
            C0645bi.a().b().a(this.f6248b);
        }
    }
}
