package R;

import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:R/d.class */
public class d {

    /* renamed from: b, reason: collision with root package name */
    private static d f1792b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f1793a = new HashMap();

    private d() {
        b();
    }

    private void b() {
        a(l.f1811a, new e(this));
        a(l.f1813c, new f(this));
    }

    public static d a() {
        if (f1792b == null) {
            f1792b = new d();
        }
        return f1792b;
    }

    public void a(String str, c cVar) {
        this.f1793a.put(str, cVar);
    }

    public c a(String str) {
        return (c) this.f1793a.get(str);
    }
}
