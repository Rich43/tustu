package bN;

import bH.C;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:bN/q.class */
class q extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7307a = true;

    /* renamed from: b, reason: collision with root package name */
    int f7308b = 0;

    /* renamed from: c, reason: collision with root package name */
    boolean f7309c = false;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ p f7310d;

    q(p pVar) {
        this.f7310d = pVar;
        if (pVar.f7296j == p.f7294h) {
            super.setName("XCP Reader_Slave_" + Math.random());
        } else if (pVar.f7296j == p.f7295i) {
            super.setName("XCP Reader_Master_" + Math.random());
        } else {
            super.setName("XCP Reader_" + Math.random());
        }
        try {
            if (this.f7309c) {
                setPriority(10);
            }
        } catch (Exception e2) {
            C.a("Failed to set XCP Reader Thread Priority");
        }
        C.d("Started Thread: " + getName());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f7307a) {
            try {
                if (this.f7310d.f7298l) {
                    this.f7310d.g();
                    this.f7310d.f7298l = false;
                    synchronized (this.f7310d.f7303o) {
                        this.f7310d.f7303o.notifyAll();
                    }
                } else {
                    this.f7310d.h();
                    this.f7308b = 0;
                }
                if (this.f7307a && this.f7310d.f7287a.available() <= 0) {
                    this.f7310d.a(5, 0);
                }
            } catch (IOException e2) {
                if (this.f7307a) {
                    this.f7310d.a(e2);
                }
                int i2 = this.f7308b;
                this.f7308b = i2 + 1;
                if (i2 > 4) {
                    this.f7307a = false;
                    e2.printStackTrace();
                    C.d("Read Thread exceeded 4 concecutive errors, stopping.");
                }
                this.f7310d.f7298l = false;
                this.f7310d.a(40, 0);
            } catch (Exception e3) {
                C.b("Read Thread Exception: " + e3.getMessage());
                this.f7310d.a(20, 0);
            }
        }
    }
}
