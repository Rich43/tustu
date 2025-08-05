package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: G.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/am.class */
public class C0053am implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    R f778a;

    /* renamed from: c, reason: collision with root package name */
    private String f779c = "showPanel";

    /* renamed from: b, reason: collision with root package name */
    List f780b = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private String f781d = null;

    public C0053am(R r2) {
        this.f778a = r2;
    }

    public R a() {
        return this.f778a;
    }

    public void a(String str) {
        this.f780b.add(str);
    }

    public String a(int i2) {
        return (String) this.f780b.get(i2);
    }

    public String b() {
        return this.f781d;
    }

    public void b(String str) {
        this.f781d = str;
    }

    public String c() {
        return this.f779c;
    }

    public void c(String str) {
        this.f779c = str;
    }
}
