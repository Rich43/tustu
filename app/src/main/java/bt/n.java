package bT;

import G.C0129l;
import bH.C;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/n.class */
class n extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private r f7601b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f7602c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f7603a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    n(l lVar, r rVar) {
        super("SlaveConnection: " + rVar.h());
        this.f7603a = lVar;
        this.f7602c = true;
        this.f7601b = rVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        do {
            if (this.f7603a.a(this.f7601b.h()) >= this.f7601b.x() || this.f7603a.f() <= this.f7603a.e()) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } else {
                try {
                    C.c("Server listening on: " + this.f7601b.n());
                    r rVarV = this.f7601b.v();
                    if (rVarV != null) {
                        this.f7603a.c(rVarV);
                    } else {
                        this.f7603a.d(this.f7601b);
                    }
                } catch (C0129l e3) {
                    this.f7603a.a(this.f7601b, e3);
                }
            }
        } while (this.f7602c);
    }

    public void a() {
        this.f7602c = false;
    }

    public r b() {
        return this.f7601b;
    }
}
