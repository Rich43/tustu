package aP;

import bH.C1007o;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aP/gO.class */
class gO extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f3427a;

    /* renamed from: b, reason: collision with root package name */
    int f3428b;

    /* renamed from: c, reason: collision with root package name */
    int f3429c;

    /* renamed from: d, reason: collision with root package name */
    boolean f3430d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0308dx f3431e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    gO(C0308dx c0308dx) {
        super("TopEnableDelay");
        this.f3431e = c0308dx;
        this.f3427a = Long.MAX_VALUE;
        this.f3428b = 100;
        this.f3429c = 100;
        this.f3430d = false;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f3430d) {
            try {
                sleep(this.f3429c);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0308dx.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            try {
                if (!this.f3430d && System.currentTimeMillis() > this.f3427a) {
                    this.f3427a = Long.MAX_VALUE;
                    for (String str : G.T.a().d()) {
                        for (int i2 = 0; i2 < this.f3431e.m().getMenuCount(); i2++) {
                            bA.f fVar = (bA.f) this.f3431e.m().getMenu(i2);
                            if (fVar != null && fVar.d() != null) {
                                try {
                                    SwingUtilities.invokeLater(this.f3431e.a(fVar, C1007o.a(fVar.d(), G.T.a().c(str))));
                                } catch (Exception e3) {
                                }
                            }
                            if (fVar != null) {
                                try {
                                    SwingUtilities.invokeLater(this.f3431e.b(fVar, fVar.i() == null || fVar.i().a()));
                                } catch (Exception e4) {
                                }
                            }
                        }
                    }
                    for (String str2 : G.T.a().d()) {
                        for (int i3 = 0; !this.f3430d && i3 < this.f3431e.f3267e.a(str2); i3++) {
                            bA.f fVarA = this.f3431e.f3267e.a(str2, i3);
                            if (fVarA != null && fVarA.d() != null) {
                                try {
                                    SwingUtilities.invokeLater(this.f3431e.a(fVarA, C1007o.a(fVarA.d(), G.T.a().c(str2))));
                                } catch (Exception e5) {
                                }
                            }
                            if (fVarA != null && fVarA.i() != null) {
                                try {
                                    SwingUtilities.invokeLater(this.f3431e.b(fVarA, fVarA.i() == null || fVarA.i().a()));
                                } catch (Exception e6) {
                                }
                            }
                        }
                    }
                }
            } catch (Exception e7) {
            }
        }
    }

    public void a() {
        this.f3427a = System.currentTimeMillis() + this.f3428b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f3430d = true;
    }
}
