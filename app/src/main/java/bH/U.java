package bH;

import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:bH/U.class */
class U extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f7021a;

    /* renamed from: b, reason: collision with root package name */
    T f7022b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ T f7023c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public U(T t2, T t3, long j2) {
        super("SpeedTest");
        this.f7023c = t2;
        this.f7021a = 0L;
        this.f7022b = null;
        this.f7022b = t3;
        this.f7021a = j2;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            while (System.currentTimeMillis() - this.f7021a < this.f7023c.f7020d) {
                arrayList.add("Value" + (i2 * Math.random()));
                i2++;
            }
            this.f7022b.a(arrayList.size());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
