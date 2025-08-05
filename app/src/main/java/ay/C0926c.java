package ay;

import G.cX;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* renamed from: ay.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/c.class */
public class C0926c {

    /* renamed from: b, reason: collision with root package name */
    private String f6435b;

    /* renamed from: c, reason: collision with root package name */
    private String f6436c;

    /* renamed from: a, reason: collision with root package name */
    Map f6437a = new HashMap();

    public C0926c(String str, String str2) {
        this.f6435b = str;
        this.f6436c = str2;
    }

    public String a() {
        return this.f6435b;
    }

    public void a(String str, String str2) {
        this.f6437a.put(str, new C0927d(this, str2));
    }

    public String a(String str) {
        cX cXVar = (cX) this.f6437a.get(str);
        if (cXVar == null) {
            return null;
        }
        return cXVar.a();
    }

    public Set b() {
        return this.f6437a.keySet();
    }

    public String c() {
        return this.f6436c;
    }
}
