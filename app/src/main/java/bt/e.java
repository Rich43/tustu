package bT;

import G.InterfaceC0042ab;
import G.InterfaceC0124g;
import G.R;
import G.aG;
import G.bS;
import bH.C0995c;
import bN.u;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/e.class */
public class e implements aG, InterfaceC0042ab, InterfaceC0124g {

    /* renamed from: a, reason: collision with root package name */
    R f7574a;

    /* renamed from: e, reason: collision with root package name */
    o f7578e;

    /* renamed from: b, reason: collision with root package name */
    Map f7575b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    long f7576c = 0;

    /* renamed from: d, reason: collision with root package name */
    final Object f7577d = new Object();

    /* renamed from: f, reason: collision with root package name */
    g f7579f = null;

    /* renamed from: g, reason: collision with root package name */
    boolean f7580g = false;

    public e(o oVar, R r2) {
        this.f7578e = oVar;
        this.f7574a = r2;
        r2.C().a((aG) this);
        r2.C().a((InterfaceC0124g) this);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        a(i2).a(i3, iArr.length);
        this.f7576c = System.currentTimeMillis();
        c();
        synchronized (this.f7577d) {
            this.f7577d.notify();
        }
    }

    private f a(int i2) {
        f fVar = (f) this.f7575b.get(Integer.valueOf(i2));
        if (fVar == null) {
            fVar = new f(this);
            fVar.a(i2);
            this.f7575b.put(Integer.valueOf(i2), fVar);
        }
        return fVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (this.f7580g) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this.f7575b) {
            arrayList.addAll(this.f7575b.values());
            this.f7575b.clear();
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            a((f) it.next());
        }
    }

    private void a(f fVar) {
        bN.l lVarB = u.a().b();
        lVarB.a(252);
        lVarB.b(C0995c.a(C0995c.a(new byte[]{-32, (byte) this.f7574a.O().x(), (byte) fVar.a(), 0, 0, 0, 0}, fVar.b(), 3, 2, this.f7578e.d().g()), fVar.c(), 5, 2, this.f7578e.d().g()));
        this.f7578e.e().a(lVarB);
    }

    public void a() {
        this.f7580g = true;
        if (this.f7579f != null) {
            this.f7579f.a();
        }
    }

    private void c() {
        if (this.f7579f == null || !this.f7579f.isAlive() || this.f7579f.f7586b) {
            this.f7579f = new g(this);
            this.f7579f.start();
        }
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        try {
            a(true);
            return true;
        } catch (bN.o e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    @Override // G.aG
    public void a(String str) {
        try {
            a(false);
        } catch (bN.o e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void a(boolean z2) {
        bN.l lVarB = u.a().b();
        lVarB.a(252);
        byte[] bArr = new byte[3];
        bArr[0] = -31;
        bArr[1] = (byte) (z2 ? 1 : 0);
        lVarB.b(bArr);
        this.f7578e.e().a(lVarB);
    }

    @Override // G.InterfaceC0124g
    public void a(String str, int i2) {
    }

    @Override // G.InterfaceC0124g
    public void b(String str, int i2) {
    }

    @Override // G.InterfaceC0124g
    public void a(String str, boolean z2) {
        if (z2) {
            bN.l lVarB = u.a().b();
            lVarB.a(252);
            lVarB.b(new byte[]{-30, -1, 0});
            try {
                this.f7578e.e().a(lVarB);
            } catch (bN.o e2) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, "Failed to send client all pages burned message.", (Throwable) e2);
            }
        }
    }
}
