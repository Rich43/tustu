package G;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:G/bH.class */
public class bH extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f845a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f846b = "";

    public String a() {
        return this.f845a;
    }

    public void a(String str) {
        this.f845a = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof bH)) {
            return super.equals(obj);
        }
        bH bHVar = (bH) obj;
        return bHVar.aJ().equals(aJ()) && bHVar.a().equals(a());
    }

    public String b() {
        return this.f846b;
    }

    public void b(String str) {
        this.f846b = str;
    }
}
