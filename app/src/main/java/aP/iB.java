package aP;

/* loaded from: TunerStudioMS.jar:aP/iB.class */
class iB implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ boolean f3635a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0440iv f3636b;

    iB(C0440iv c0440iv, boolean z2) {
        this.f3636b = c0440iv;
        this.f3635a = z2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3636b.pack();
        this.f3636b.setVisible(this.f3635a);
    }
}
