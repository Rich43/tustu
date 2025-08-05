package bL;

import bH.C;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bL/o.class */
class o extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private boolean f7172b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f7173c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ n f7174a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    o(n nVar) {
        super("VE Analyze Processor");
        this.f7174a = nVar;
        this.f7172b = true;
        this.f7173c = false;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f7173c = true;
        while (a()) {
            if (this.f7174a.f7166b.isEmpty()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } else {
                while (this.f7174a.f7166b.size() > 0) {
                    p pVar = (p) this.f7174a.f7166b.get(0);
                    this.f7174a.f7166b.remove(pVar);
                    try {
                        this.f7174a.f7165a.a(pVar);
                        this.f7174a.f7169e++;
                        if (pVar.e()) {
                            this.f7174a.f7170f++;
                        }
                        if (this.f7174a.f7166b.isEmpty()) {
                            this.f7174a.g();
                        }
                    } catch (Exception e3) {
                        C.a("VE Analyze Error:\n" + e3.getMessage() + "\nCheck log for more detail.");
                        e3.printStackTrace();
                    }
                }
            }
        }
        this.f7173c = false;
    }

    public boolean a() {
        return this.f7172b;
    }

    public void a(boolean z2) {
        this.f7172b = z2;
    }

    public boolean b() {
        return this.f7173c;
    }
}
