package G;

import java.io.Serializable;

/* renamed from: G.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/c.class */
public class C0094c implements cZ, Serializable {

    /* renamed from: a, reason: collision with root package name */
    String f1028a = "";

    public C0094c() {
    }

    public C0094c(String str) {
        a(str);
    }

    public void a(String str) {
        this.f1028a = str;
    }

    @Override // G.cZ
    public String a() {
        return this.f1028a;
    }

    @Override // G.cZ
    public String[] b() {
        return null;
    }

    public String toString() {
        return this.f1028a;
    }
}
