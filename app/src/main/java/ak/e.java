package aK;

/* loaded from: TunerStudioMS.jar:aK/e.class */
class e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f2574a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f2575b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    e(d dVar) {
        super("GPSPublisher");
        this.f2575b = dVar;
        this.f2574a = false;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        this.f2574a = true;
        while (this.f2574a) {
            try {
                wait();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.f2575b.u();
        }
    }

    public synchronized void a() {
        notify();
    }
}
