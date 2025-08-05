package G;

import java.util.HashMap;
import java.util.Map;

/* renamed from: G.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/h.class */
public class C0125h {

    /* renamed from: b, reason: collision with root package name */
    private static C0125h f1242b = null;

    /* renamed from: a, reason: collision with root package name */
    Map f1243a = new HashMap();

    private C0125h() {
    }

    public static C0125h a() {
        if (f1242b == null) {
            f1242b = new C0125h();
        }
        return f1242b;
    }

    public void a(aI aIVar) {
        this.f1243a.put(aIVar.ac(), aIVar);
    }

    public aI a(String str) {
        return (aI) this.f1243a.get(str);
    }

    public void b(String str) {
        this.f1243a.remove(str);
    }

    public void b() {
        this.f1243a.clear();
    }
}
