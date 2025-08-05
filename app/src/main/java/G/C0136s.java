package G;

import java.util.HashMap;
import java.util.Iterator;

/* renamed from: G.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/s.class */
public class C0136s extends Q {

    /* renamed from: a, reason: collision with root package name */
    private HashMap f1320a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private String f1321b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f1322c = "";

    public C0135r a(String str) {
        return (C0135r) this.f1320a.get(str);
    }

    public void a(C0135r c0135r) {
        this.f1320a.put(c0135r.aJ(), c0135r);
    }

    public Iterator a() {
        return this.f1320a.values().iterator();
    }

    public C0135r b() {
        C0135r c0135r = null;
        Iterator itA = a();
        while (itA.hasNext()) {
            C0135r c0135r2 = (C0135r) itA.next();
            if (c0135r2.b()) {
                return c0135r2;
            }
            if (c0135r == null) {
                c0135r = c0135r2;
            }
        }
        return c0135r;
    }

    public String c() {
        return this.f1321b;
    }

    public void b(String str) {
        this.f1321b = str;
    }

    public String d() {
        return this.f1322c;
    }

    public void c(String str) {
        this.f1322c = str;
    }

    public boolean d(String str) {
        return this.f1320a.containsKey(str);
    }

    public boolean equals(Object obj) {
        return obj instanceof C0136s ? ((C0136s) obj).c().equals(c()) : super.equals(obj);
    }
}
