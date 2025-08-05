package ao;

/* renamed from: ao.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/z.class */
class RunnableC0828z implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0827y f6191a;

    RunnableC0828z(C0827y c0827y) {
        this.f6191a = c0827y;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6191a.getParent().invalidate();
        this.f6191a.getParent().validate();
    }
}
