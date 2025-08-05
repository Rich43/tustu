package z;

import gnu.io.SerialPort;

/* loaded from: TunerStudioMS.jar:z/h.class */
class h extends Thread {

    /* renamed from: a, reason: collision with root package name */
    SerialPort f14117a;

    /* renamed from: b, reason: collision with root package name */
    boolean f14118b = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1901e f14119c;

    public h(C1901e c1901e, SerialPort serialPort) {
        this.f14119c = c1901e;
        this.f14117a = null;
        this.f14117a = serialPort;
        start();
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (!this.f14118b && System.currentTimeMillis() - jCurrentTimeMillis < 10000) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f14119c.a(this.f14117a);
        } finally {
            this.f14118b = true;
        }
    }
}
