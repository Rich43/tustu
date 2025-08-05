package A;

import java.util.HashMap;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:A/q.class */
public class q {

    /* renamed from: b, reason: collision with root package name */
    private String f46b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f47c = null;

    /* renamed from: d, reason: collision with root package name */
    private Class f48d = null;

    /* renamed from: e, reason: collision with root package name */
    private i f49e = null;

    /* renamed from: a, reason: collision with root package name */
    Map f50a = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    private static String f51f = "DEFAULT_INSTANCE";

    public String a() {
        return this.f46b;
    }

    public void a(String str) {
        this.f46b = str;
    }

    public void b(String str) {
        this.f47c = str;
    }

    public void a(Class cls) {
        this.f48d = cls;
    }

    public f c(String str) {
        if (str == null) {
            str = f51f;
        }
        f fVarA = (f) this.f50a.get(str);
        if (fVarA == null) {
            fVarA = b() == null ? (f) this.f48d.newInstance() : b().a(this.f46b, str);
            this.f50a.put(str, fVarA);
        }
        return fVarA;
    }

    public i b() {
        return this.f49e;
    }

    public void a(i iVar) {
        this.f49e = iVar;
    }
}
