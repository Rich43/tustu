package aj;

import java.io.IOException;

/* loaded from: TunerStudioMS.jar:aj/i.class */
class i extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f4562a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f4563b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    i(d dVar) {
        super("ReadTimer");
        this.f4563b = dVar;
        this.f4562a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws NumberFormatException, IOException {
        while (this.f4562a) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.f4563b.e();
            try {
                long jCurrentTimeMillis2 = this.f4563b.f4547x - (System.currentTimeMillis() - jCurrentTimeMillis);
                if (jCurrentTimeMillis2 > 0) {
                    Thread.sleep(jCurrentTimeMillis2);
                }
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        this.f4563b.k();
    }

    public void a() {
        this.f4562a = false;
    }
}
