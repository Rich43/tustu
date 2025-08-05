package aH;

import G.R;

/* loaded from: TunerStudioMS.jar:aH/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ R f2416a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f2417b;

    c(b bVar, R r2) {
        this.f2417b = bVar;
        this.f2416a = r2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2417b.b(this.f2416a);
    }
}
