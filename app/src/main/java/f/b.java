package F;

import E.g;
import E.h;
import E.j;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:F/b.class */
public class b implements h {

    /* renamed from: a, reason: collision with root package name */
    private static Logger f275a = Logger.getLogger(b.class.getName());

    /* renamed from: b, reason: collision with root package name */
    private Map f276b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private boolean f277c = false;

    /* renamed from: d, reason: collision with root package name */
    private String f278d;

    /* renamed from: e, reason: collision with root package name */
    private String f279e;

    /* renamed from: f, reason: collision with root package name */
    private String f280f;

    /* renamed from: g, reason: collision with root package name */
    private String f281g;

    /* renamed from: h, reason: collision with root package name */
    private int f282h;

    /* renamed from: i, reason: collision with root package name */
    private String f283i;

    @Override // E.h
    public String e() {
        return this.f278d;
    }

    public void g(String str) {
        this.f278d = str;
    }

    public void h(String str) {
        this.f279e = str;
    }

    @Override // E.h
    public String a() {
        return this.f280f;
    }

    public void i(String str) {
        this.f280f = str;
    }

    @Override // E.h
    public String b() {
        return this.f281g;
    }

    public void j(String str) {
        this.f281g = str;
    }

    @Override // E.h
    public int c() {
        return this.f282h;
    }

    public void a(int i2) {
        this.f282h = i2;
    }

    @Override // E.h
    public String d() {
        return this.f283i;
    }

    public void k(String str) {
        this.f283i = str;
    }

    public Map f() {
        if (!this.f277c) {
            g();
            this.f277c = true;
            f275a.info("Initialize IP Range.");
        }
        return this.f276b;
    }

    private void g() {
        for (String str : j.a(this.f278d, this.f279e)) {
            this.f276b.put(str, new c(this));
        }
    }

    @Override // E.h
    public boolean e(String str) {
        boolean z2 = false;
        synchronized (this) {
            Iterator it = f().values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                c cVar = (c) it.next();
                if (cVar.d().equals(str) && cVar.c()) {
                    z2 = true;
                    break;
                }
            }
        }
        return z2;
    }

    @Override // E.h
    public g b(String str) {
        c cVar = null;
        String str2 = null;
        synchronized (this) {
            Iterator it = f().entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry entry = (Map.Entry) it.next();
                if (((c) entry.getValue()).d().equals(str) && ((c) entry.getValue()).c()) {
                    cVar = (c) entry.getValue();
                    str2 = (String) entry.getKey();
                    break;
                }
            }
        }
        if (cVar == null) {
            return null;
        }
        cVar.a(d.LEASE);
        cVar.a(System.currentTimeMillis());
        return f(str2);
    }

    @Override // E.h
    public g a(String str) {
        String str2 = null;
        h();
        synchronized (this) {
            for (Map.Entry entry : f().entrySet()) {
                if (((c) entry.getValue()).d().equals(str)) {
                    ((c) entry.getValue()).a(d.INOFFERING);
                    str2 = (String) entry.getKey();
                }
            }
            if (str2 == null) {
                Iterator it = f().entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Map.Entry entry2 = (Map.Entry) it.next();
                    if (((c) entry2.getValue()).b()) {
                        c cVar = (c) entry2.getValue();
                        str2 = (String) entry2.getKey();
                        cVar.a(d.INOFFERING);
                        cVar.a(str);
                        break;
                    }
                }
            }
        }
        if (str2 != null) {
            return f(str2);
        }
        return null;
    }

    @Override // E.h
    public void c(String str) {
        synchronized (this) {
            Iterator it = f().entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry entry = (Map.Entry) it.next();
                if (((c) entry.getValue()).d().equals(str)) {
                    ((c) entry.getValue()).a(d.IDLE);
                    break;
                }
            }
        }
    }

    @Override // E.h
    public boolean d(String str) {
        synchronized (this) {
            c cVar = (c) f().get(str);
            if (cVar == null) {
                return false;
            }
            cVar.a(d.LEASE);
            cVar.a(System.currentTimeMillis());
            return true;
        }
    }

    private void h() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        synchronized (this) {
            for (c cVar : f().values()) {
                if (cVar.a() && jCurrentTimeMillis - cVar.e() > this.f282h * 1000) {
                    cVar.a("");
                    cVar.a(d.IDLE);
                }
            }
        }
    }

    private g f(String str) {
        g gVar = new g();
        gVar.d(this.f283i);
        gVar.c(this.f281g);
        gVar.a(str);
        gVar.b(this.f280f);
        gVar.a(this.f282h);
        return gVar;
    }
}
