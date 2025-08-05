package bQ;

import G.F;
import G.InterfaceC0131n;
import G.aV;
import bH.C;
import bH.C0995c;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/* loaded from: TunerStudioMS.jar:bQ/f.class */
public class f implements A.e {

    /* renamed from: c, reason: collision with root package name */
    bN.k f7408c;

    /* renamed from: a, reason: collision with root package name */
    bN.r f7406a = null;

    /* renamed from: b, reason: collision with root package name */
    bN.p f7407b = null;

    /* renamed from: k, reason: collision with root package name */
    private F f7409k = null;

    /* renamed from: d, reason: collision with root package name */
    h f7410d = new h(this);

    /* renamed from: e, reason: collision with root package name */
    g f7411e = new g(this);

    /* renamed from: f, reason: collision with root package name */
    ArrayList f7412f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    A.f f7413g = null;

    /* renamed from: l, reason: collision with root package name */
    private final List f7414l = new CopyOnWriteArrayList();

    /* renamed from: h, reason: collision with root package name */
    List f7415h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    final Object f7416i = new Object();

    /* renamed from: j, reason: collision with root package name */
    Map f7417j = new HashMap();

    /* renamed from: m, reason: collision with root package name */
    private int f7418m = 0;

    /* renamed from: n, reason: collision with root package name */
    private bR.c f7419n = null;

    /* renamed from: o, reason: collision with root package name */
    private final bR.d f7420o = new bR.d();

    /* renamed from: p, reason: collision with root package name */
    private J.j f7421p = null;

    protected f(bN.k kVar) {
        this.f7408c = kVar;
        this.f7417j.put(252, new bR.n());
        this.f7417j.put(253, this.f7420o);
    }

    public void f() {
        a(this.f7409k != null ? this.f7409k.u() : null);
    }

    public void g() {
        b(this.f7409k != null ? this.f7409k.u() : null);
    }

    public void a(aV aVVar) {
        this.f7412f.add(aVVar);
    }

    private void a(String str) {
        Iterator it = this.f7412f.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).b(str);
        }
    }

    private void b(String str) {
        Iterator it = this.f7412f.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).c(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f7412f.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).d(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f7412f.iterator();
        while (it.hasNext()) {
            ((aV) it.next()).e(str);
        }
    }

    public void a(k kVar) {
        f();
        this.f7406a.a(kVar.a());
        g();
    }

    public y b(k kVar) {
        f();
        y yVar = new y();
        bN.t tVarA = kVar.a();
        yVar.a(kVar);
        if (kVar.b() == 0) {
            c(tVarA, 0);
            yVar.a(1);
        } else {
            List listC = c(tVarA, kVar.b());
            if (listC != null) {
                yVar.a(listC);
                if (b(listC) || listC.size() != kVar.b()) {
                    yVar.a(3);
                } else {
                    yVar.a(1);
                }
                yVar.a(listC);
            } else {
                yVar.a("No Response within the timeout period");
                yVar.a(3);
            }
        }
        g();
        return yVar;
    }

    public bN.t a(bN.t tVar) {
        return a(tVar, this.f7408c.q());
    }

    public bN.t a(bN.t tVar, int i2) {
        this.f7415h.clear();
        a(tVar, this.f7415h, 1, null, i2);
        if (this.f7415h.isEmpty()) {
            return null;
        }
        return (bN.t) this.f7415h.get(0);
    }

    public bN.t b(bN.t tVar, int i2) {
        bN.t tVar2 = null;
        int i3 = 0;
        do {
            if (i3 > 0) {
                if (tVar2 != null) {
                    C.c("Packet failed! " + i3 + " of " + i2 + ", response: " + C0995c.d(tVar2.d()));
                } else {
                    C.c("Packet failed! " + i3 + " of " + i2 + ", response: " + ((Object) null));
                }
            }
            i3++;
            try {
                this.f7415h.clear();
                a(tVar, this.f7415h, 1, null, this.f7408c.q());
            } catch (V.b e2) {
                if (i3 >= i2) {
                    throw e2;
                }
                try {
                    C.c("Waiting for retry: " + this.f7408c.q());
                    Thread.sleep(this.f7408c.q());
                } catch (InterruptedException e3) {
                    Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
            tVar2 = this.f7415h.isEmpty() ? null : (bN.t) this.f7415h.get(0);
            if (tVar2 != null && tVar2.a() == 255) {
                break;
            }
        } while (i3 < i2);
        return tVar2;
    }

    public List c(bN.t tVar, int i2) {
        return a(tVar, new ArrayList(), i2, null, this.f7408c.q());
    }

    public List a(bN.t tVar, List list, int i2, InterfaceC0131n interfaceC0131n, int i3) throws V.b {
        f();
        this.f7406a.a(tVar);
        if (i2 == 0) {
            g();
            return list;
        }
        if (interfaceC0131n != null) {
            interfaceC0131n.e();
        }
        int i4 = 0;
        while (true) {
            long jCurrentTimeMillis = System.currentTimeMillis() + i3;
            try {
                synchronized (this.f7416i) {
                    this.f7416i.wait(i3);
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            i4++;
            if (interfaceC0131n != null) {
                interfaceC0131n.a(i4 / i2);
            }
            if (System.currentTimeMillis() >= jCurrentTimeMillis || (this.f7414l.size() >= i2 && i2 > 0)) {
                break;
            }
        }
        if (!b(this.f7414l)) {
        }
        if (this.f7414l.isEmpty()) {
            this.f7418m++;
            if (this.f7418m > 3) {
            }
            if (this.f7421p != null) {
                this.f7421p.g();
            }
            throw new V.b("Timeout on non-interleaved message response. Timeout set to: " + i3);
        }
        this.f7418m = 0;
        if (this.f7414l.size() == i2) {
            list.addAll(this.f7414l);
            this.f7414l.clear();
        } else {
            if (this.f7414l.size() < i2) {
                this.f7421p.g();
                throw new V.b("Did not receive all response packets within timeout. Timeout set to: " + i3);
            }
            for (int i5 = 0; i5 < i2; i5++) {
                list.add(this.f7414l.remove(0));
            }
            String str = "Extra packet(s) Received!";
            synchronized (this.f7414l) {
                Iterator it = this.f7414l.iterator();
                while (it.hasNext()) {
                    str = str + "\nMSG:\n" + C0995c.a(((bN.t) it.next()).d(), 16);
                }
                C.b(str);
                this.f7414l.clear();
            }
        }
        if (interfaceC0131n != null) {
            interfaceC0131n.a(1.0d);
        }
        g();
        return list;
    }

    private boolean b(List list) {
        synchronized (list) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (((bN.t) it.next()).a() == 254) {
                    return true;
                }
            }
            return false;
        }
    }

    public bN.t a(List list) throws V.b {
        f();
        for (int i2 = 0; i2 < list.size(); i2++) {
            bN.t tVar = (bN.t) list.get(i2);
            if (i2 > 0 && this.f7408c.k() > 0) {
                try {
                    Thread.sleep(r0 / 1000000, (this.f7408c.k() * Config.MAX_REPEAT_NUM) % 1000000);
                } catch (Exception e2) {
                    Logger.getLogger(f.class.getName()).log(Level.SEVERE, "MIN_ST Failed.", (Throwable) e2);
                }
            }
            this.f7406a.a(tVar);
        }
        Object objL = l();
        synchronized (objL) {
            try {
                objL.wait(this.f7408c.q());
            } catch (InterruptedException e3) {
                Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (this.f7414l.isEmpty()) {
            this.f7418m++;
            if (this.f7418m > 2) {
                this.f7413g.g();
            }
            g();
            this.f7421p.g();
            throw new V.b("Timeout on non-interleaved message response. Timeout set to: " + this.f7408c.q());
        }
        this.f7418m = 0;
        bN.t tVar2 = (bN.t) this.f7414l.remove(0);
        if (!this.f7414l.isEmpty()) {
            String str = "Extra packets Received!";
            Iterator it = this.f7414l.iterator();
            while (it.hasNext()) {
                str = str + "\nMSG:\n" + C0995c.a(((bN.t) it.next()).d(), 16);
            }
            C.b(str);
            this.f7414l.clear();
        }
        g();
        return tVar2;
    }

    private Object l() {
        return this.f7416i;
    }

    protected void a(A.f fVar) {
        this.f7413g = fVar;
    }

    protected void h() {
        if (this.f7407b != null) {
            this.f7407b.b(this.f7411e);
            this.f7407b.c();
        }
        if (this.f7406a != null) {
            this.f7406a.c();
        }
        this.f7406a = null;
        this.f7407b = null;
    }

    @Override // A.e
    public void a() {
        g();
        h();
    }

    @Override // A.e
    public void b() {
    }

    @Override // A.e
    public void c() {
        try {
            e();
            this.f7407b = new bN.p(this.f7413g.i(), this.f7408c);
            this.f7407b.a(bN.p.f7295i);
            this.f7407b.a(this.f7411e);
            this.f7407b.a(this.f7421p);
            this.f7406a = new bN.r(this.f7413g.j(), this.f7408c);
            this.f7406a.a(true);
            this.f7406a.a(this.f7421p);
        } catch (Exception e2) {
            C.d("Error getting Streams from connection. Disconnect issued.");
            this.f7413g.g();
        }
    }

    @Override // A.e
    public void d() {
    }

    @Override // A.e
    public void e() {
        if (this.f7407b != null) {
            this.f7407b.b(this.f7411e);
            this.f7407b.c();
        }
        if (this.f7406a != null) {
            this.f7406a.c();
        }
    }

    public void a(F f2) {
        this.f7409k = f2;
    }

    public void i() {
        this.f7407b.b();
    }

    public void a(bR.c cVar) {
        this.f7419n = cVar;
    }

    public bR.d j() {
        return this.f7420o;
    }

    public long k() {
        if (this.f7407b == null) {
            return 0L;
        }
        return this.f7407b.a();
    }

    public void a(J.j jVar) {
        this.f7421p = jVar;
    }
}
