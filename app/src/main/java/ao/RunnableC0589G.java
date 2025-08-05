package ao;

/* renamed from: ao.G, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/G.class */
class RunnableC0589G implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0588F f5083a;

    RunnableC0589G(C0588F c0588f) {
        this.f5083a = c0588f;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f5083a.f5082d = true;
        this.f5083a.f5079a.invalidate();
        this.f5083a.f5079a.repaint();
    }
}
