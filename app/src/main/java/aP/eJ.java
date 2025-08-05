package aP;

/* loaded from: TunerStudioMS.jar:aP/eJ.class */
class eJ extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3291a;

    eJ(C0308dx c0308dx) {
        this.f3291a = c0308dx;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e2) {
        }
        this.f3291a.b(true);
    }
}
