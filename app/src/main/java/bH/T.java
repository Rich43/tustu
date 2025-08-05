package bH;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bH/T.class */
public class T {

    /* renamed from: a, reason: collision with root package name */
    int f7017a = 8;

    /* renamed from: b, reason: collision with root package name */
    int f7018b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f7019c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f7020d = 380;

    public int a() {
        this.f7018b = 0;
        this.f7019c = 0;
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (int i2 = 0; i2 < this.f7017a; i2++) {
            new U(this, this, jCurrentTimeMillis).start();
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e2) {
                Logger.getLogger(T.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        while (true) {
            if (this.f7017a <= this.f7018b) {
                break;
            }
            if (System.currentTimeMillis() - jCurrentTimeMillis > this.f7020d + 500) {
                C.c("Speed test timed out, exiting after:" + (System.currentTimeMillis() - jCurrentTimeMillis) + "\n" + this.f7018b + " completed out of expected " + this.f7017a);
                break;
            }
            try {
                Thread.currentThread();
                Thread.sleep(50L);
            } catch (Exception e3) {
            }
        }
        return this.f7019c;
    }

    public synchronized void a(int i2) {
        this.f7019c += i2;
        this.f7018b++;
    }
}
