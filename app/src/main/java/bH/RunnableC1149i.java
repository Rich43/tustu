package bh;

/* renamed from: bh.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/i.class */
class RunnableC1149i implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1142b f8112a;

    RunnableC1149i(C1142b c1142b) {
        this.f8112a = c1142b;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.f8112a.getTreeLock()) {
            this.f8112a.f8098n.a().doLayout();
            this.f8112a.f8098n.a().validate();
        }
    }
}
