package bs;

/* loaded from: TunerStudioMS.jar:bs/x.class */
class x extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8636a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ w f8637b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(w wVar) {
        super("WueFlasher");
        this.f8637b = wVar;
        this.f8636a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8636a) {
            this.f8637b.f8633a = !this.f8637b.f8633a;
            this.f8637b.setText(this.f8637b.a());
            this.f8637b.repaint();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
            }
        }
        this.f8637b.f8633a = false;
        this.f8637b.repaint();
    }

    public void a() {
        this.f8636a = false;
    }
}
