package Z;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:Z/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private static f f2226a = null;

    /* renamed from: b, reason: collision with root package name */
    private final List f2227b = new ArrayList();

    private f() {
    }

    public static f a() {
        if (f2226a == null) {
            f2226a = new f();
        }
        return f2226a;
    }

    public void a(d dVar) {
        this.f2227b.add(dVar);
    }

    public List b() {
        return this.f2227b;
    }
}
