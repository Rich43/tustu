package aU;

import G.R;

/* loaded from: TunerStudioMS.jar:aU/b.class */
class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ R f3933a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ a f3934b;

    b(a aVar, R r2) {
        this.f3934b = aVar;
        this.f3933a = r2;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e2) {
        }
        this.f3933a.I();
    }
}
