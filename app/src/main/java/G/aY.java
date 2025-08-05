package G;

import java.io.Serializable;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:G/aY.class */
public class aY implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    private String f677b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f678c = "";

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aX f679a;

    public aY(aX aXVar, String str, String str2) {
        this.f679a = aXVar;
        a(str);
        b(str2);
    }

    public String a() {
        return this.f677b;
    }

    public void a(String str) {
        this.f677b = str;
    }

    public String b() {
        return bH.W.b(this.f678c, PdfOps.DOUBLE_QUOTE__TOKEN, "");
    }

    public void b(String str) {
        this.f678c = str;
    }

    public boolean equals(Object obj) {
        aY aYVar;
        return (!(obj instanceof aY) || (aYVar = (aY) obj) == null || aYVar.a() == null || a() == null) ? super.equals(obj) : aYVar.a().equals(a());
    }

    public String toString() {
        return (this.f678c == null || this.f678c.equals("")) ? a() : b();
    }
}
