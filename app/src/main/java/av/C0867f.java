package av;

import G.T;
import W.C0200z;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/f.class */
public class C0867f extends AbstractC0868g {

    /* renamed from: a, reason: collision with root package name */
    protected static C0867f f6283a = null;

    public static C0867f f() {
        if (f6283a == null) {
            f6283a = new C0867f();
        }
        return f6283a;
    }

    private C0867f() {
        i();
    }

    private void i() {
        a("SingleBigMlvConfigInstance", "./inc/bsTables.ini");
    }

    public void a(String str) throws V.h {
        String strA = C0200z.a(str);
        if (T.a().c() == null || !T.a().c().i().equals(strA)) {
            i();
        }
        try {
            new C0862a().a(str, this);
            this.f6284b.h().g();
            g();
        } catch (V.a e2) {
            Logger.getLogger(C0867f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Unable to load tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    @Override // ao.hF
    public void j(String str) throws V.h {
        try {
            new C0862a().b(str, this);
            this.f6284b.h().g();
        } catch (IOException e2) {
            Logger.getLogger(C0867f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Failed to save tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    @Override // av.AbstractC0868g, ao.hF
    public boolean k(String str) {
        return false;
    }
}
