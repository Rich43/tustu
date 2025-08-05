package B;

/* loaded from: TunerStudioMS.jar:B/q.class */
class q extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f207a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ p f208b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    q(p pVar) {
        super("UDP_ReceiveThread_" + Math.random());
        this.f208b = pVar;
        this.f207a = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f207a) {
            try {
                this.f208b.c();
            } catch (Exception e2) {
                this.f207a = false;
            }
        }
        this.f208b.f204f = null;
    }
}
