package ao;

import W.C0184j;
import java.util.Iterator;
import javax.swing.SwingUtilities;

/* renamed from: ao.fe, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fe.class */
public class C0748fe extends Thread {

    /* renamed from: a, reason: collision with root package name */
    C0804hg f5796a;

    /* renamed from: b, reason: collision with root package name */
    C0184j f5797b;

    /* renamed from: c, reason: collision with root package name */
    boolean f5798c;

    /* renamed from: g, reason: collision with root package name */
    private boolean f5799g;

    /* renamed from: d, reason: collision with root package name */
    long f5800d;

    /* renamed from: e, reason: collision with root package name */
    long f5801e;

    /* renamed from: f, reason: collision with root package name */
    double f5802f;

    public C0748fe(C0804hg c0804hg, double d2) {
        this(c0804hg);
        a(d2);
    }

    public C0748fe(C0804hg c0804hg) {
        super("LogPlayBackThread_" + Math.random());
        this.f5796a = null;
        this.f5797b = null;
        this.f5798c = true;
        this.f5799g = false;
        this.f5800d = 0L;
        this.f5801e = 0L;
        this.f5802f = 1.0d;
        this.f5796a = c0804hg;
        a();
    }

    public void a() {
        Iterator it = this.f5796a.r().iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.a().equalsIgnoreCase(h.g.a().a("Time"))) {
                this.f5797b = c0184j;
                return;
            }
        }
    }

    public void a(double d2) {
        this.f5802f = d2;
    }

    @Override // java.lang.Thread
    public void start() {
        this.f5799g = false;
        if (this.f5797b != null) {
            super.start();
            this.f5796a.c();
            return;
        }
        bP bPVarB = C0645bi.a().b();
        C0736et c0736et = new C0736et(bPVarB, "No Time Column detected in log. This input is required for log playback.\nWould you like to add mapping now", true);
        if (c0736et.f5662a) {
            SwingUtilities.invokeLater(new RunnableC0749ff(this, C0636b.a().a(bPVarB, "Time")));
        }
        c0736et.dispose();
    }

    public synchronized void b() {
        this.f5798c = false;
        this.f5796a.d();
    }

    public synchronized void c() {
        this.f5800d = System.currentTimeMillis();
        this.f5801e = (long) (1000.0f * this.f5797b.c(this.f5796a.p()));
    }

    public synchronized void d() {
        this.f5799g = !this.f5799g;
        b();
    }

    public synchronized void e() {
        this.f5800d = System.currentTimeMillis();
        this.f5801e = (long) (1000.0f * this.f5797b.c(this.f5796a.p()));
        this.f5799g = false;
        this.f5796a.c();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x008f, code lost:
    
        if ((r6.f5797b.i() - 1) <= r6.f5796a.p()) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0092, code lost:
    
        r6.f5796a.l();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00cb, code lost:
    
        bH.C.c("PlayBackThread break.");
        r6.f5796a.d();
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 568
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ao.C0748fe.run():void");
    }

    public boolean f() {
        return this.f5799g;
    }
}
