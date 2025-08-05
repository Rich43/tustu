package bt;

/* renamed from: bt.bh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bh.class */
class RunnableC1326bh implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1324bf f9015a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f9016b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1324bf f9017c;

    RunnableC1326bh(C1324bf c1324bf, C1324bf c1324bf2, int i2) {
        this.f9017c = c1324bf;
        this.f9015a = c1324bf2;
        this.f9016b = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f9015a.getComponentCount() > this.f9016b) {
            this.f9015a.getComponent(this.f9016b).requestFocus();
        }
    }
}
