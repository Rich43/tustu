package bN;

import G.J;
import bH.C0995c;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bN/r.class */
public class r {

    /* renamed from: b, reason: collision with root package name */
    k f7312b;

    /* renamed from: c, reason: collision with root package name */
    OutputStream f7313c;

    /* renamed from: a, reason: collision with root package name */
    s f7311a = null;

    /* renamed from: d, reason: collision with root package name */
    List f7314d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f7315e = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private final Object f7316g = new Object();

    /* renamed from: h, reason: collision with root package name */
    private final Object f7317h = new Object();

    /* renamed from: i, reason: collision with root package name */
    private int f7318i = 0;

    /* renamed from: j, reason: collision with root package name */
    private int f7319j = 0;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7320k = false;

    /* renamed from: f, reason: collision with root package name */
    String f7321f = " Slave TX:\n";

    /* renamed from: l, reason: collision with root package name */
    private boolean f7322l = false;

    /* renamed from: m, reason: collision with root package name */
    private J.j f7323m = null;

    public r(OutputStream outputStream, k kVar) {
        this.f7313c = outputStream;
        this.f7312b = kVar;
    }

    public int a(List list) {
        int iE = -1;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            t tVar = (t) it.next();
            bS.c cVarA = bS.d.a().a(this.f7312b);
            iE = e();
            cVarA.a().b(iE);
            cVarA.a(tVar.d());
            this.f7314d.add(cVarA);
        }
        synchronized (this.f7316g) {
            this.f7316g.notifyAll();
        }
        f();
        return iE;
    }

    public int a(t tVar) {
        bS.c cVarA = bS.d.a().a(this.f7312b);
        int iE = e();
        cVarA.a().b(iE);
        cVarA.a(tVar.d());
        this.f7314d.add(cVarA);
        synchronized (this.f7316g) {
            this.f7316g.notifyAll();
        }
        f();
        return iE;
    }

    private int e() {
        int i2 = this.f7318i;
        this.f7318i = i2 + 1;
        return (int) (i2 % Math.pow(2.0d, this.f7312b.d() * 8));
    }

    private void f() {
        if (this.f7311a == null || !this.f7311a.isAlive()) {
            a();
        }
    }

    public void a() {
        if (this.f7311a != null) {
            this.f7311a.f7324a = false;
        }
        this.f7311a = new s(this);
        this.f7311a.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        while (!this.f7314d.isEmpty()) {
            bS.c cVar = (bS.c) this.f7314d.remove(0);
            try {
                a(cVar);
                b(cVar);
            } catch (o e2) {
                a(e2, cVar);
            }
            bS.d.a().a(this.f7312b, cVar);
        }
    }

    private void a(bS.c cVar) {
        cVar.i();
        cVar.e();
        cVar.f();
        try {
            this.f7313c.write(cVar.h());
            this.f7313c.flush();
            if (this.f7323m != null) {
                if (h.a(cVar)) {
                    this.f7323m.e();
                } else {
                    this.f7323m.j();
                }
            }
            if (h()) {
                a(cVar.h());
            }
            if (this.f7320k && h.a(cVar) && cVar.b().e()) {
                this.f7319j++;
            }
        } catch (bS.a e2) {
            throw new o("Failed to get Message bytes, size mismatch: " + e2.getMessage());
        }
    }

    private boolean h() {
        return J.I() || this.f7322l;
    }

    private void a(byte[] bArr) {
        System.out.println(k.v() + this.f7321f + C0995c.d(bArr));
    }

    public void a(g gVar) {
        this.f7315e.add(gVar);
    }

    public void b(g gVar) {
        this.f7315e.remove(gVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IOException iOException) {
        Iterator it = this.f7315e.iterator();
        while (it.hasNext()) {
            ((g) it.next()).b(iOException);
        }
    }

    private void a(o oVar, bS.c cVar) {
        Iterator it = this.f7315e.iterator();
        while (it.hasNext()) {
            ((g) it.next()).a(oVar, cVar);
        }
    }

    private void b(bS.c cVar) {
        Iterator it = this.f7315e.iterator();
        while (it.hasNext()) {
            ((g) it.next()).a(cVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        try {
            synchronized (this.f7316g) {
                this.f7316g.wait(i2);
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public void b() {
        this.f7319j = 0;
        synchronized (this.f7317h) {
            this.f7317h.notifyAll();
        }
    }

    public void c() {
        if (this.f7311a != null) {
            this.f7311a.f7324a = false;
        }
    }

    public void a(boolean z2) {
        this.f7320k = z2;
        if (z2) {
            this.f7321f = ": Master TX:\n";
        } else {
            this.f7321f = ": Slave TX:\n";
        }
    }

    public k d() {
        return this.f7312b;
    }

    public void a(J.j jVar) {
        this.f7323m = jVar;
    }
}
