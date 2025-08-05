package aI;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import G.S;
import G.T;
import bH.C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/t.class */
public class t implements S, InterfaceC0109co {

    /* renamed from: b, reason: collision with root package name */
    private static HashMap f2507b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private R f2508c;

    /* renamed from: d, reason: collision with root package name */
    private List f2509d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private boolean f2510e = true;

    /* renamed from: a, reason: collision with root package name */
    boolean f2511a = false;

    private t(R r2) {
        this.f2508c = null;
        this.f2508c = r2;
        b();
    }

    private void b() {
        try {
            C0113cs.a().a(this.f2508c.c(), d.f2436a, this);
        } catch (V.a e2) {
            Logger.getLogger(t.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        T.a().a(this);
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (this.f2508c.R() && !this.f2511a) {
            if (System.currentTimeMillis() - jCurrentTimeMillis > 500) {
                C.b("SdCardInMonitor::Init timeout waiting for channel publish");
                return;
            } else {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e3) {
                    Logger.getLogger(t.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
    }

    private void c() {
        C0113cs.a().a(this);
        f2507b.remove(this.f2508c.c());
    }

    public static t d(R r2) {
        t tVar = (t) f2507b.get(r2.c());
        if (tVar == null) {
            tVar = new t(r2);
            f2507b.put(r2.c(), tVar);
        }
        return tVar;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        boolean z2 = (((int) d2) & d.f2437b) == d.f2437b;
        if (this.f2510e != z2) {
            if (z2) {
                e();
            } else {
                d();
            }
        }
        this.f2510e = z2;
        this.f2511a = true;
    }

    public synchronized void a(s sVar) {
        this.f2509d.add(sVar);
        if (this.f2510e) {
            sVar.d();
        } else {
            sVar.e();
        }
    }

    public void b(s sVar) {
        this.f2509d.add(sVar);
    }

    private void d() {
        Iterator it = this.f2509d.iterator();
        while (it.hasNext()) {
            ((s) it.next()).e();
        }
    }

    private void e() {
        Iterator it = this.f2509d.iterator();
        while (it.hasNext()) {
            ((s) it.next()).d();
        }
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        c();
    }

    @Override // G.S
    public void c(R r2) {
    }

    public boolean a() {
        return this.f2510e;
    }
}
