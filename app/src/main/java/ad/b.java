package aD;

import java.io.InputStream;
import jssc.SerialPort;

/* loaded from: TunerStudioMS.jar:aD/b.class */
class b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    SerialPort f2319a;

    /* renamed from: b, reason: collision with root package name */
    InputStream f2320b;

    /* renamed from: c, reason: collision with root package name */
    e f2321c;

    /* renamed from: d, reason: collision with root package name */
    boolean f2322d = false;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ a f2323e;

    public b(a aVar, SerialPort serialPort, InputStream inputStream, e eVar) {
        this.f2323e = aVar;
        this.f2319a = null;
        this.f2319a = serialPort;
        this.f2320b = inputStream;
        this.f2321c = eVar;
        start();
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (!this.f2322d && System.currentTimeMillis() - jCurrentTimeMillis < 10000) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        aVar.f2312r = 0;
        aVar.a();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f2323e.a(this.f2319a, this.f2320b, this.f2321c);
        } finally {
            this.f2322d = true;
        }
    }
}
