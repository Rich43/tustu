package G;

import java.io.Serializable;
import java.util.jar.Pack200;

/* renamed from: G.bz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bz.class */
public abstract class AbstractC0093bz extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private cZ f1026a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f1027b = null;

    public String l() {
        try {
            if (this.f1026a == null) {
                return null;
            }
            return this.f1026a.a();
        } catch (V.g e2) {
            return Pack200.Packer.ERROR;
        }
    }

    public void b(cZ cZVar) {
        this.f1026a = cZVar;
    }

    public void e(String str) {
        this.f1026a = new C0094c(str);
    }

    public String m() {
        return this.f1027b;
    }

    public void f(String str) {
        this.f1027b = str;
    }

    public abstract String b();
}
