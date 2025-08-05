package bT;

import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/l.class */
public class l {

    /* renamed from: e, reason: collision with root package name */
    private static int f7597e = 0;

    /* renamed from: d, reason: collision with root package name */
    private boolean f7593d = false;

    /* renamed from: a, reason: collision with root package name */
    List f7594a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f7595b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f7596c = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private int f7598f = 0;

    public void a(r rVar) {
        if (this.f7595b.contains(rVar)) {
            return;
        }
        this.f7595b.add(rVar);
        if (c()) {
            b(rVar);
        }
    }

    public synchronized void a() {
        this.f7593d = true;
        Iterator it = this.f7595b.iterator();
        while (it.hasNext()) {
            b((r) it.next());
        }
    }

    public synchronized void b() {
        this.f7593d = false;
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f7595b);
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            ((r) it.next()).w();
        }
        g();
    }

    private void b(r rVar) {
        n nVar = new n(this, rVar);
        this.f7596c.add(nVar);
        nVar.start();
    }

    public void a(q qVar) {
        if (this.f7594a.contains(qVar)) {
            return;
        }
        this.f7594a.add(qVar);
    }

    private void g() {
        for (n nVar : new ArrayList(this.f7596c)) {
            try {
                nVar.a();
                nVar.b().g();
            } catch (Exception e2) {
                Logger.getLogger(l.class.getName()).log(Level.INFO, "Error closing connection", (Throwable) e2);
            }
        }
        this.f7596c.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(r rVar) {
        Iterator it = this.f7594a.iterator();
        while (it.hasNext()) {
            ((q) it.next()).a(rVar);
        }
        rVar.a(new m(this, rVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(r rVar) {
        this.f7595b.remove(rVar);
        if (!c() || a(rVar.h()) >= rVar.x()) {
            return;
        }
        b(rVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(String str) {
        int i2 = 0;
        Iterator it = this.f7596c.iterator();
        while (it.hasNext()) {
            if (((n) it.next()).b().h().equals(str)) {
                i2++;
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(r rVar, Exception exc) {
        C.a("Failed to start connection: " + exc.getLocalizedMessage());
        a(100);
    }

    public void a(int i2) {
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public boolean c() {
        return this.f7593d;
    }

    public int d() {
        return this.f7595b.size();
    }

    public int e() {
        return this.f7598f;
    }

    public int f() {
        return f7597e;
    }

    static /* synthetic */ int a(l lVar) {
        int i2 = lVar.f7598f;
        lVar.f7598f = i2 + 1;
        return i2;
    }

    static /* synthetic */ int b(l lVar) {
        int i2 = lVar.f7598f;
        lVar.f7598f = i2 - 1;
        return i2;
    }
}
