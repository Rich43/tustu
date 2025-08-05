package av;

import G.C0135r;
import G.T;
import W.C0171aa;
import W.C0200z;
import W.aj;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/l.class */
public class C0873l extends AbstractC0868g {

    /* renamed from: a, reason: collision with root package name */
    protected static C0873l f6302a = null;

    /* renamed from: e, reason: collision with root package name */
    String f6303e = null;

    /* renamed from: f, reason: collision with root package name */
    String f6304f = null;

    public static C0873l f() {
        if (f6302a == null) {
            f6302a = new C0873l();
            f6302a.a(new C0874m());
        }
        return f6302a;
    }

    private C0873l() {
    }

    public void a(File file, C0135r[] c0135rArr) {
        a("SingleIniMlvConfigInstance", file.getAbsolutePath(), c0135rArr);
    }

    public void a(String str) throws V.h {
        String strA = C0200z.a(str);
        if (T.a().c() == null || !T.a().c().i().equals(strA)) {
            throw new V.h("Proper Configuration has not been loaded.");
        }
        try {
            C0171aa c0171aa = new C0171aa();
            c0171aa.a(h(), str, (List) null);
            this.f6304f = c0171aa.d(new File(str));
            new C0875n(this).start();
            g();
            this.f6303e = str;
        } catch (V.g e2) {
            throw new V.h("Error loading tune: " + e2.getMessage());
        } catch (aj e3) {
            throw new V.h("Password Protected tune: " + e3.getMessage());
        } catch (IOException e4) {
            Logger.getLogger(C0873l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new V.h("Unable to access msq for PcVarables.");
        }
    }

    @Override // ao.hF
    public void j(String str) throws V.h {
        try {
            C0171aa c0171aa = new C0171aa();
            c0171aa.a(h(), str, new C0876o(this));
            this.f6284b.h().g();
            if (this.f6304f != null && this.f6304f.startsWith("<page>")) {
                c0171aa.a(new File(str), this.f6304f);
            }
        } catch (Exception e2) {
            Logger.getLogger(C0873l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Failed to save tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    @Override // av.AbstractC0868g, ao.hF
    public boolean k(String str) {
        return true;
    }
}
