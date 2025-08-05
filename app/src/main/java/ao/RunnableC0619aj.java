package ao;

/* renamed from: ao.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/aj.class */
class RunnableC0619aj implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0618ai f5199a;

    RunnableC0619aj(C0618ai c0618ai) {
        this.f5199a = c0618ai;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f5199a.getParent().invalidate();
        this.f5199a.getParent().doLayout();
        try {
            this.f5199a.getParent().getParent().doLayout();
        } catch (Exception e2) {
        }
    }
}
