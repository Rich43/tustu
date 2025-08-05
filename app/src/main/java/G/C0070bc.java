package G;

import java.io.Serializable;

/* renamed from: G.bc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bc.class */
public class C0070bc extends C0088bu implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f889a = null;

    /* renamed from: f, reason: collision with root package name */
    private String f890f = "52";

    /* renamed from: g, reason: collision with root package name */
    private int f891g = 255;

    public String a() {
        return this.f890f;
    }

    public boolean b() {
        try {
            Integer.parseInt(this.f890f);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    public void a(String str) {
        this.f890f = str;
    }

    public void a(int i2) {
        this.f891g = i2;
    }

    public int c() {
        return this.f891g;
    }

    public String d() {
        return this.f889a;
    }

    public void b(String str) {
        this.f889a = str;
    }
}
