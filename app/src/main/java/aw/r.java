package aW;

import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:aW/r.class */
public class r {

    /* renamed from: b, reason: collision with root package name */
    private static r f3993b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f3994a = new HashMap();

    public static r a() {
        if (f3993b == null) {
            f3993b = new r();
        }
        return f3993b;
    }

    public boolean a(String str) {
        return this.f3994a.containsKey(str);
    }

    public void a(a aVar, String str) {
        q qVar = (q) this.f3994a.get(str);
        if (qVar != null) {
            qVar.a(aVar, str);
        }
    }

    public void a(String str, q qVar) {
        this.f3994a.put(str, qVar);
    }
}
