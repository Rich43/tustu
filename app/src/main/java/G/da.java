package G;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/da.class */
public class da implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    R f1181a = null;

    /* renamed from: b, reason: collision with root package name */
    double f1182b = 0.0d;

    /* renamed from: c, reason: collision with root package name */
    boolean f1183c = false;

    /* renamed from: d, reason: collision with root package name */
    bH.Z f1184d = null;

    /* renamed from: e, reason: collision with root package name */
    final db f1185e = new db(this);

    public C0132o a(R r2, C0130m c0130m, int i2) {
        if (this.f1183c) {
            if (this.f1184d == null) {
                this.f1184d = new bH.Z();
                this.f1184d.a();
            }
            bH.C.c("SynchronousCommInstructionProcessor: Instruction entering '" + c0130m.aJ() + "' Time:" + this.f1184d.d());
        }
        this.f1181a = r2;
        this.f1185e.f1186a = null;
        c0130m.b(this);
        if (!r2.R()) {
            this.f1185e.f1186a = new C0132o();
            this.f1185e.f1186a.a(2);
            this.f1185e.f1186a.a("Controller not online.");
            return this.f1185e.f1186a;
        }
        if (this.f1183c) {
            bH.C.c("SynchronousCommInstructionProcessor: Dispatch Instruction to Comm Manager Time:" + this.f1184d.d());
        }
        r2.C().b(c0130m);
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.f1185e.f1187b = jCurrentTimeMillis;
        while (this.f1185e.f1186a == null) {
            if (this.f1185e.f1186a == null && System.currentTimeMillis() - this.f1185e.f1187b > i2) {
                this.f1185e.f1186a = new C0132o();
                this.f1185e.f1186a.a(3);
                this.f1185e.f1186a.a("Timeout of " + i2 + " ms. exceeded. time:" + (System.currentTimeMillis() - jCurrentTimeMillis));
                return this.f1185e.f1186a;
            }
            try {
                if (this.f1183c) {
                    bH.C.c("SynchronousCommInstructionProcessor: Thread waiting for result:" + this.f1184d.d());
                }
                synchronized (this.f1185e) {
                    this.f1185e.wait(i2 + 1);
                    this.f1185e.notifyAll();
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(da.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (this.f1183c) {
            bH.C.c("SynchronousCommInstructionProcessor: returning result:" + this.f1184d.d());
        }
        return this.f1185e.f1186a;
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        if (this.f1182b != d2) {
            this.f1185e.f1187b = System.currentTimeMillis();
        }
        this.f1182b = d2;
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (this.f1183c) {
            bH.C.c("SynchronousCommInstructionProcessor: notified result complete:" + this.f1184d.d());
        }
        this.f1185e.f1187b = System.currentTimeMillis();
        this.f1185e.f1186a = c0132o;
        synchronized (this.f1185e) {
            this.f1185e.notifyAll();
        }
    }
}
