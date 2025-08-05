package Z;

import java.util.HashMap;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:Z/b.class */
public class b {

    /* renamed from: c, reason: collision with root package name */
    private static b f2220c = null;

    /* renamed from: a, reason: collision with root package name */
    final HashMap f2221a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    final HashMap f2222b = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private c f2223d = null;

    private b() {
    }

    public static b a() {
        if (f2220c == null) {
            f2220c = new b();
        }
        return f2220c;
    }

    public void b() {
        this.f2221a.clear();
        this.f2222b.clear();
        for (e eVar : this.f2223d.a()) {
            this.f2221a.put(eVar.a(), eVar);
        }
    }

    public String a(String str) {
        if (this.f2222b.isEmpty() && !this.f2221a.isEmpty()) {
            for (String str2 : this.f2221a.keySet()) {
                Iterator it = ((e) this.f2221a.get(str2)).b().iterator();
                while (it.hasNext()) {
                    this.f2222b.put((String) it.next(), str2);
                }
            }
        }
        return (String) this.f2222b.get(str);
    }

    public String b(String str) {
        String strA = a(str);
        return strA != null ? strA : str;
    }

    public c c() {
        return this.f2223d;
    }

    public void a(c cVar) {
        this.f2223d = cVar;
    }
}
