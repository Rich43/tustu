package br;

/* renamed from: br.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/E.class */
class C1227E extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8341a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1226D f8342b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1227E(C1226D c1226d) {
        super("Flasher");
        this.f8342b = c1226d;
        this.f8341a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8341a) {
            this.f8342b.f8338a = !this.f8342b.f8338a;
            this.f8342b.setText(this.f8342b.a());
            this.f8342b.repaint();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
            }
        }
        this.f8342b.f8338a = false;
        this.f8342b.repaint();
    }

    public void a() {
        this.f8341a = false;
    }
}
