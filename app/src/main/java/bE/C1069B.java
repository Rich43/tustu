package be;

import G.C0043ac;
import G.C0048ah;
import G.aH;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: be.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/B.class */
public class C1069B {

    /* renamed from: b, reason: collision with root package name */
    private G.R f7882b;

    /* renamed from: a, reason: collision with root package name */
    List f7883a = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC1101q f7884c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f7885d = true;

    /* renamed from: e, reason: collision with root package name */
    private boolean f7886e = false;

    public C1069B(G.R r2) {
        this.f7882b = r2;
    }

    public void a(aH aHVar) {
        aH aHVarG = d().g(aHVar.aJ());
        aHVar.q(this.f7885d);
        d().a(aHVar);
        if (aHVarG == null || !aHVarG.aL()) {
            c(aHVar);
        } else {
            d(aHVarG);
        }
    }

    public void b(aH aHVar) {
        d().z(aHVar.aJ());
        e(aHVar);
    }

    private void c(G.Q q2) {
        Iterator it = this.f7883a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1100p) it.next()).a(q2);
        }
        a();
    }

    private void d(G.Q q2) {
        Iterator it = this.f7883a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1100p) it.next()).b(q2);
        }
        a();
    }

    private void e(G.Q q2) {
        Iterator it = this.f7883a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1100p) it.next()).c(q2);
        }
        a();
    }

    private void b(boolean z2) {
        Iterator it = this.f7883a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1100p) it.next()).a(z2);
        }
    }

    public void a() {
        boolean z2 = !c();
        this.f7886e = true;
        if (z2) {
            b(c());
        }
    }

    public void b() {
        boolean zC = c();
        this.f7886e = false;
        if (zC) {
            b(c());
        }
    }

    public void a(InterfaceC1100p interfaceC1100p) {
        this.f7883a.add(interfaceC1100p);
    }

    public void b(InterfaceC1100p interfaceC1100p) {
        this.f7883a.remove(interfaceC1100p);
    }

    public boolean a(String str) {
        Iterator itB = d().B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (f(c0048ah) && c0048ah.i().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean f(G.Q q2) {
        return this.f7884c == null || this.f7884c.a(q2);
    }

    public void b(String str) {
        Iterator itB = d().B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (f(c0048ah) && c0048ah.i().equals(str)) {
                e(c0048ah);
                itB.remove();
            }
        }
    }

    public void a(InterfaceC1101q interfaceC1101q) {
        this.f7884c = interfaceC1101q;
    }

    public void a(G.Q q2) {
        q2.q(this.f7885d);
        if (q2 instanceof C0043ac) {
            if (d().a((C0043ac) q2)) {
                d(q2);
                return;
            } else {
                c(q2);
                return;
            }
        }
        if (q2 instanceof C0048ah) {
            C0048ah c0048ahK = d().k(q2.aJ());
            d().a((C0048ah) q2);
            if (c0048ahK == null || !c0048ahK.aL()) {
                c(q2);
            } else {
                d(q2);
            }
        }
    }

    public void a(boolean z2) {
        this.f7885d = z2;
    }

    public boolean c() {
        return this.f7886e;
    }

    public void b(G.Q q2) {
        if (q2 instanceof C0043ac) {
            d().b((C0043ac) q2);
        } else if (q2 instanceof C0048ah) {
            d().b((C0048ah) q2);
        } else if (q2 instanceof aH) {
            d().z(q2.aJ());
        }
        e(q2);
    }

    public G.R d() {
        return this.f7882b;
    }

    public void a(G.R r2) {
        this.f7882b = r2;
    }
}
