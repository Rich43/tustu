package bs;

/* loaded from: TunerStudioMS.jar:bs/y.class */
class y extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f8638a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8639b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ k f8640c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y(k kVar, int i2) {
        super("WUE ApplyThread");
        this.f8640c = kVar;
        this.f8638a = -1;
        this.f8639b = true;
        this.f8638a = i2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8639b) {
            try {
                Thread.sleep(this.f8638a * 1000);
                this.f8640c.h();
            } catch (Exception e2) {
                bH.C.c("Exception in the apply Thread, handled but here is the stack:");
                e2.printStackTrace();
            }
        }
        while (this.f8640c.f8605b.a()) {
            this.f8640c.h();
        }
    }

    public void a() {
        this.f8639b = false;
    }
}
