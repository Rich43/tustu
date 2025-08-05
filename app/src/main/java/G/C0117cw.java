package G;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: G.cw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cw.class */
public class C0117cw implements aG {

    /* renamed from: a, reason: collision with root package name */
    private static final Map f1165a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final R f1166b;

    /* renamed from: c, reason: collision with root package name */
    private C0118cx f1167c = null;

    /* renamed from: d, reason: collision with root package name */
    private final List f1168d = new ArrayList();

    private C0117cw(R r2) {
        this.f1166b = r2;
    }

    public static C0117cw a(R r2) {
        if (f1165a.get(r2.c()) == null) {
            f1165a.put(r2.c(), new C0117cw(r2));
        }
        return (C0117cw) f1165a.get(r2.c());
    }

    public static boolean b(String str) {
        return f1165a.get(str) != null;
    }

    public void a() {
        this.f1166b.C().a(this);
        Iterator it = this.f1168d.iterator();
        while (it.hasNext()) {
            b((bZ) it.next());
        }
    }

    public static void c(String str) {
        f1165a.remove(str);
    }

    public void a(bZ bZVar) {
        if (bZVar.i().equals(bZ.f883d) || bZVar.i().equals(bZ.f884e)) {
            this.f1168d.add(bZVar);
        }
    }

    private void b(bZ bZVar) {
        if (bZVar.i().equals(bZ.f883d) || bZVar.i().equals(bZ.f884e)) {
            try {
                C0113cs.a().a(this.f1166b.c(), bZVar.R(), new C0119cy(this, bZVar));
            } catch (Exception e2) {
                bH.C.b("Unable to update '" + bZVar.aJ() + "' from Channel: " + e2.getLocalizedMessage());
            }
        }
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        for (bZ bZVar : this.f1168d) {
            if (bZVar.i().equals(bZ.f883d)) {
                b(bZVar);
            }
        }
    }

    public void a(InterfaceC0109co interfaceC0109co) {
        if (this.f1167c == null || !this.f1167c.isAlive()) {
            this.f1167c = new C0118cx(this);
            this.f1167c.start();
        }
        this.f1167c.a(interfaceC0109co);
    }
}
