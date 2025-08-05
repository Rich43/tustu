package aP;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/jD.class */
class jD extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f3770a;

    /* renamed from: b, reason: collision with root package name */
    long f3771b;

    /* renamed from: c, reason: collision with root package name */
    int f3772c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ jv f3773d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    jD(jv jvVar) {
        super("OfflineTuneSaver");
        this.f3773d = jvVar;
        this.f3770a = new ArrayList();
        this.f3771b = 0L;
        this.f3772c = 1000;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.f3772c);
            } catch (Exception e2) {
                Logger.getLogger(jv.class.getName()).log(Level.SEVERE, "Failed to save offline tune.", (Throwable) e2);
            }
            if (!this.f3770a.isEmpty() && System.currentTimeMillis() - this.f3771b > this.f3772c) {
                ArrayList<G.R> arrayList = new ArrayList();
                arrayList.addAll(this.f3770a);
                for (G.R r2 : arrayList) {
                    C0338f.a().e(r2);
                    this.f3770a.remove(r2);
                }
            }
        }
    }

    public void a(G.R r2) {
        if (!this.f3770a.contains(r2)) {
            this.f3770a.add(r2);
        }
        this.f3771b = System.currentTimeMillis();
    }
}
