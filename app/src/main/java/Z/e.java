package Z;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:Z/e.class */
public class e {

    /* renamed from: a, reason: collision with root package name */
    private String f2224a;

    /* renamed from: b, reason: collision with root package name */
    private List f2225b = new ArrayList();

    public e(String str) {
        this.f2224a = "";
        this.f2224a = str;
    }

    public String a() {
        return this.f2224a;
    }

    public List b() {
        return this.f2225b;
    }

    public void a(String str) {
        this.f2225b.add(str);
    }

    public boolean b(String str) {
        return this.f2225b.contains(str);
    }
}
