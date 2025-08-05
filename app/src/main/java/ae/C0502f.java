package ae;

import G.C0129l;
import bH.C;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ae.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ae/f.class */
public class C0502f {

    /* renamed from: d, reason: collision with root package name */
    private q f4353d;

    /* renamed from: e, reason: collision with root package name */
    private p f4354e;

    /* renamed from: f, reason: collision with root package name */
    private k f4355f;

    /* renamed from: g, reason: collision with root package name */
    private List f4356g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private final double f4357h = 0.15d;

    /* renamed from: i, reason: collision with root package name */
    private final double f4358i = 0.8d;

    /* renamed from: j, reason: collision with root package name */
    private final double f4359j = 0.05d;

    /* renamed from: a, reason: collision with root package name */
    u f4360a = new j(this, 0.0d, 1.0d);

    /* renamed from: b, reason: collision with root package name */
    u f4361b = new j(this, 0.0d, 1.0d);

    /* renamed from: c, reason: collision with root package name */
    u f4362c = new j(this, 0.0d, 1.0d);

    public C0502f(q qVar, k kVar, p pVar) {
        a(qVar);
        a(pVar);
        this.f4355f = kVar;
    }

    public void a(u uVar) {
        this.f4356g.add(uVar);
    }

    public void b(u uVar) {
        this.f4356g.remove(uVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(double d2) {
        Iterator it = this.f4356g.iterator();
        while (it.hasNext()) {
            ((u) it.next()).a(d2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        Iterator it = this.f4356g.iterator();
        while (it.hasNext()) {
            ((u) it.next()).a(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(String str) {
        Iterator it = this.f4356g.iterator();
        while (it.hasNext()) {
            if (!((u) it.next()).b(str)) {
                return false;
            }
        }
        return true;
    }

    private void a() throws IOException {
        if (this.f4354e.a().k() == 3) {
            try {
                Thread.sleep(50L);
                return;
            } catch (InterruptedException e2) {
                Logger.getLogger(C0502f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return;
            }
        }
        try {
            C.c("Opening: " + this.f4354e.a().n());
            this.f4354e.a().f();
            try {
                Thread.sleep(100 + this.f4354e.a().o());
            } catch (InterruptedException e3) {
                Logger.getLogger(C0502f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        } catch (C0129l e4) {
            Logger.getLogger(C0502f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new IOException("Failed to connect to " + this.f4354e.a().n());
        }
    }

    private void b() {
        C.c("Closing: " + this.f4354e.a().n());
        this.f4354e.a().g();
    }

    public void a(w wVar) {
        new C0503g(this, wVar).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List c() throws IOException {
        C0500d c0500dA;
        ArrayList arrayList = new ArrayList();
        a("Preparing to load firmware.");
        a();
        try {
            for (s sVar : this.f4353d.c()) {
                int i2 = 0;
                do {
                    c0500dA = sVar.a(this.f4355f, this.f4354e, this.f4360a);
                    i2++;
                    if (c0500dA.a() != C0500d.f4347b || !c0500dA.c()) {
                        break;
                    }
                } while (i2 < 5);
                arrayList.add(c0500dA);
                if (c0500dA.a() == C0500d.f4347b || c0500dA.d() > 0) {
                    return arrayList;
                }
            }
            b();
            return arrayList;
        } finally {
            b();
        }
    }

    public void b(w wVar) {
        new h(this, wVar).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized C0500d d() throws IOException {
        C0500d c0500dA;
        a("Loading firmware.");
        a();
        int i2 = 0;
        do {
            try {
                c0500dA = this.f4353d.a(this.f4355f, this.f4354e, this.f4361b);
                i2++;
                if (c0500dA.a() == C0500d.f4346a || !c0500dA.c()) {
                    break;
                }
            } finally {
                b();
            }
        } while (i2 < 3);
        return c0500dA;
    }

    public void c(w wVar) {
        new i(this, wVar).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List e() throws IOException {
        C0500d c0500dA;
        ArrayList arrayList = new ArrayList();
        a("Finalizing firmware options.");
        a();
        try {
            List listD = this.f4353d.d();
            for (int i2 = 0; i2 < listD.size(); i2++) {
                s sVar = (s) listD.get(i2);
                int i3 = 0;
                do {
                    c0500dA = sVar.a(this.f4354e, this.f4362c);
                    i3++;
                    if (c0500dA.a() != C0500d.f4347b || !c0500dA.c()) {
                        break;
                    }
                } while (i3 < 5);
                arrayList.add(c0500dA);
                if (c0500dA.a() == C0500d.f4347b || c0500dA.d() > 0) {
                    return arrayList;
                }
            }
            a("Post work completed.");
            b();
            return arrayList;
        } finally {
            b();
        }
    }

    public void a(p pVar) {
        this.f4354e = pVar;
        if (this.f4353d != null) {
            pVar.a(this.f4353d);
        }
    }

    public void a(q qVar) {
        this.f4353d = qVar;
        if (this.f4354e != null) {
            this.f4354e.a(qVar);
        }
    }

    public void a(k kVar) {
        this.f4355f = kVar;
    }
}
