package ao;

/* renamed from: ao.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/w.class */
class RunnableC0825w implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0823u f6186a;

    RunnableC0825w(C0823u c0823u) {
        this.f6186a = c0823u;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6186a.f6180l = this.f6186a.e();
        this.f6186a.doLayout();
    }
}
