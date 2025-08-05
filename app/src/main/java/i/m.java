package I;

/* loaded from: TunerStudioMS.jar:I/m.class */
class m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f1391a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    m(k kVar) {
        super("TimedStoreAccumulated");
        this.f1391a = kVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000L);
                this.f1391a.c();
            } catch (Exception e2) {
            }
        }
    }
}
