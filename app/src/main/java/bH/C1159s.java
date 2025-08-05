package bh;

/* renamed from: bh.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/s.class */
class C1159s extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1158r f8131a;

    C1159s(C1158r c1158r) {
        this.f8131a = c1158r;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(250L);
        } catch (InterruptedException e2) {
        }
        this.f8131a.f8129a.I();
    }
}
