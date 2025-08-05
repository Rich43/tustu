package U;

import S.g;
import S.j;
import W.ap;

/* loaded from: TunerStudioMS.jar:U/a.class */
public class a implements g {

    /* renamed from: a, reason: collision with root package name */
    public static String f1885a = "Data log";

    /* renamed from: b, reason: collision with root package name */
    public static String f1886b = "Stop Data log";

    /* renamed from: e, reason: collision with root package name */
    private ap f1887e = null;

    /* renamed from: c, reason: collision with root package name */
    j f1888c = null;

    /* renamed from: d, reason: collision with root package name */
    j f1889d = null;

    @Override // S.g
    public S.a a(String str, String str2) {
        if (str2.equals(f1885a)) {
            if (this.f1888c != null) {
                return this.f1888c;
            }
            this.f1888c = new j();
            this.f1888c.g(str2);
            this.f1888c.e("0");
            this.f1888c.f("0");
            if (this.f1887e != null) {
                this.f1888c.h(str);
            }
            return this.f1888c;
        }
        if (!str2.equals(f1886b)) {
            return null;
        }
        if (this.f1889d != null) {
            return this.f1889d;
        }
        this.f1889d = new j();
        this.f1889d.g(str2);
        this.f1889d.e("0");
        this.f1889d.f("0");
        if (this.f1887e != null) {
            this.f1889d.h(str);
        }
        return this.f1889d;
    }

    public void a(ap apVar) {
        this.f1887e = apVar;
    }
}
