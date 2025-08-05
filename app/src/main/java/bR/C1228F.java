package br;

/* renamed from: br.F, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/F.class */
class C1228F extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f8343a;

    /* renamed from: b, reason: collision with root package name */
    boolean f8344b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1255s f8345c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1228F(C1255s c1255s, int i2) {
        super("VE ApplyThread");
        this.f8345c = c1255s;
        this.f8343a = -1;
        this.f8344b = true;
        this.f8343a = i2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8344b) {
            try {
                Thread.sleep(this.f8343a * 1000);
                this.f8345c.b();
            } catch (Exception e2) {
                bH.C.c("Exception in the apply Thread, handled but here is the stack:");
                e2.printStackTrace();
            }
        }
        while (this.f8345c.f8519h.b()) {
            this.f8345c.b();
        }
    }

    public void a() {
        this.f8344b = false;
    }
}
