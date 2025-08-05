package N;

import G.R;
import G.T;
import bH.C;
import bT.l;
import bT.o;
import bT.r;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:N/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    R f1739a;

    /* renamed from: b, reason: collision with root package name */
    l f1740b = new l();

    /* renamed from: c, reason: collision with root package name */
    List f1741c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    c f1742d = new c(this);

    /* renamed from: f, reason: collision with root package name */
    private boolean f1744f = false;

    /* renamed from: e, reason: collision with root package name */
    private static final Map f1743e = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private static d f1745g = null;

    private a(R r2) {
        this.f1739a = r2;
        g();
        this.f1740b.a(this.f1742d);
        if (f1745g == null) {
            f1745g = new d(this);
            T.a().a(f1745g);
        }
    }

    public synchronized void a() {
        this.f1740b.a();
        C.d("Slave Server Activated for: " + this.f1739a.c());
    }

    public int b() {
        return this.f1740b.d();
    }

    public static void c() {
        Iterator it = f1743e.values().iterator();
        while (it.hasNext()) {
            ((a) it.next()).d();
        }
    }

    public synchronized void d() {
        this.f1740b.b();
        for (b bVar : (b[]) this.f1741c.toArray(new b[this.f1741c.size()])) {
            bVar.f().b();
            bVar.g().g();
        }
        this.f1741c.clear();
        C.d("Slave Server Deactivated for: " + this.f1739a.c());
    }

    public static a a(R r2) {
        a aVar = (a) f1743e.get(r2.c());
        if (aVar == null) {
            aVar = new a(r2);
            f1743e.put(r2.c(), aVar);
        }
        return aVar;
    }

    private void g() {
    }

    public void a(r rVar) {
        this.f1740b.a(rVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(r rVar) {
        C.d("New Client Connection for Config: " + this.f1739a.c() + ", connection info: " + rVar.n());
        try {
            b bVar = new b(this, new o(this.f1739a, rVar), rVar);
            rVar.a(bVar);
            this.f1741c.add(bVar);
        } catch (IOException e2) {
            try {
                rVar.g();
            } catch (Exception e3) {
                Logger.getLogger(a.class.getName()).log(Level.WARNING, "Error Closing bad connection", (Throwable) e2);
            }
            Logger.getLogger(a.class.getName()).log(Level.WARNING, "Failed to create server for connection. Closing Conneciton.", (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(b bVar) {
        bVar.g().b(bVar);
        this.f1741c.remove(bVar);
    }

    public boolean e() {
        return this.f1744f;
    }
}
