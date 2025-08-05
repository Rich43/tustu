package G;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/aj.class */
public class C0050aj extends Q {

    /* renamed from: a, reason: collision with root package name */
    private cZ f764a = null;

    /* renamed from: b, reason: collision with root package name */
    private cZ f765b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f766c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f767d = null;

    public String a() {
        try {
            if (this.f764a == null) {
                return null;
            }
            return this.f764a.a();
        } catch (V.g e2) {
            Logger.getLogger(C0050aj.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public void a(cZ cZVar) {
        this.f764a = cZVar;
    }

    public String b() {
        try {
            if (this.f765b == null) {
                return null;
            }
            return this.f765b.a();
        } catch (V.g e2) {
            Logger.getLogger(C0050aj.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public void b(cZ cZVar) {
        this.f765b = cZVar;
    }

    public String c() {
        return this.f766c;
    }

    public void a(String str) {
        this.f766c += bH.W.b(bH.W.b(str, "\\n", "\n"), "\\t", "\t");
    }

    public String d() {
        return this.f767d;
    }

    public void b(String str) {
        this.f767d = str;
    }
}
