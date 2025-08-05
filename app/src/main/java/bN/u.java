package bN;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bN/u.class */
public class u {

    /* renamed from: a, reason: collision with root package name */
    private static u f7326a = null;

    /* renamed from: b, reason: collision with root package name */
    private final List f7327b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private final List f7328c = new ArrayList();

    private u() {
    }

    public static u a() {
        if (f7326a == null) {
            f7326a = new u();
        }
        return f7326a;
    }

    public l b() {
        return !this.f7327b.isEmpty() ? (l) this.f7327b.remove(0) : new l();
    }

    public m a(k kVar) {
        if (this.f7328c.isEmpty()) {
            return new m(kVar);
        }
        m mVar = (m) this.f7328c.remove(0);
        mVar.a(kVar.a());
        return mVar;
    }
}
