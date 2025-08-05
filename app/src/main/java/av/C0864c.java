package av;

import G.T;
import W.C0200z;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/c.class */
public class C0864c extends AbstractC0868g {

    /* renamed from: a, reason: collision with root package name */
    protected static C0864c f6281a = null;

    public static C0864c f() {
        if (f6281a == null) {
            f6281a = new C0864c();
            f6281a.a(new C0865d());
        }
        return f6281a;
    }

    private C0864c() {
        i();
    }

    private void i() {
        a("SingleBigMlvConfigInstance", "./inc/BigStuffGen4.ini", null);
    }

    public void a(String str) throws V.h {
        String strA = C0200z.a(str);
        if (T.a().c() == null || !T.a().c().i().equals(strA)) {
            i();
        }
        try {
            new C0862a().a(str, this);
            new C0866e(this).start();
            g();
        } catch (V.a e2) {
            Logger.getLogger(C0864c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Unable to load tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ea  */
    @Override // av.AbstractC0868g
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void g() {
        /*
            Method dump skipped, instructions count: 499
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: av.C0864c.g():void");
    }

    @Override // ao.hF
    public void j(String str) throws V.h {
        try {
            new C0862a().b(str, this);
            this.f6284b.h().g();
        } catch (IOException e2) {
            Logger.getLogger(C0864c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Failed to save tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    @Override // av.AbstractC0868g, ao.hF
    public boolean k(String str) {
        return false;
    }
}
