package br;

/* loaded from: TunerStudioMS.jar:br/ab.class */
class ab extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8407a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aa f8408b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ab(aa aaVar) {
        super("Flasher");
        this.f8408b = aaVar;
        this.f8407a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8407a) {
            this.f8408b.f8404a = !this.f8408b.f8404a;
            this.f8408b.setText(this.f8408b.a());
            this.f8408b.repaint();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
            }
        }
        this.f8408b.f8404a = false;
        this.f8408b.repaint();
    }

    public void a() {
        this.f8407a = false;
    }
}
