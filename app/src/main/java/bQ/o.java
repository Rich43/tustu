package bQ;

import G.C0123f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/o.class */
class o extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final List f7464a;

    /* renamed from: b, reason: collision with root package name */
    boolean f7465b;

    /* renamed from: c, reason: collision with root package name */
    C0123f f7466c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ l f7467d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(l lVar) {
        super("DAQ Publisher");
        this.f7467d = lVar;
        this.f7464a = new ArrayList();
        this.f7465b = false;
        this.f7466c = new C0123f();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (!this.f7465b && this.f7467d.f421F) {
            try {
                wait(50L);
                if (bN.k.v() - this.f7467d.f7454an > 3.0f) {
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            while (this.f7464a.size() > 0) {
                r rVar = (r) this.f7464a.remove(0);
                if (rVar != null) {
                    this.f7467d.f7449n = rVar.b();
                    this.f7467d.a(this.f7467d.e().u(), rVar.a());
                    this.f7466c.a(rVar.a());
                }
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e3) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
    }

    public void a() {
        this.f7465b = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(byte[] bArr, double d2) {
        byte[] bArrB = this.f7466c.b(bArr.length);
        System.arraycopy(bArr, 0, bArrB, 0, bArr.length);
        this.f7464a.add(new r(this.f7467d, bArrB, d2));
        notify();
    }
}
