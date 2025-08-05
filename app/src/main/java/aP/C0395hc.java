package aP;

/* renamed from: aP.hc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hc.class */
class C0395hc extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0394hb f3569a;

    C0395hc(C0394hb c0394hb) {
        this.f3569a = c0394hb;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(250L);
        } catch (InterruptedException e2) {
        }
        this.f3569a.f3556a.I();
    }
}
