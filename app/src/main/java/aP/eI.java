package aP;

/* loaded from: TunerStudioMS.jar:aP/eI.class */
class eI implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ boolean f3289a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3290b;

    eI(C0308dx c0308dx, boolean z2) {
        this.f3290b = c0308dx;
        this.f3289a = z2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3290b.m().setEnabled(this.f3289a);
        if (this.f3290b.f3267e == null || this.f3290b.f3267e.getComponent() == null) {
            return;
        }
        this.f3290b.f3267e.getComponent().setEnabled(this.f3289a);
    }
}
