package s;

import W.C0181g;
import bH.C;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: s.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:s/a.class */
public class C1812a extends HashMap {

    /* renamed from: a, reason: collision with root package name */
    private static C1812a f13515a = null;

    protected C1812a() {
    }

    private void b() {
        C0181g c0181g = new C0181g();
        File file = new File("./content/settingsHelp.res");
        if (file.exists()) {
            try {
                c0181g.a(this, file);
            } catch (Exception e2) {
                Logger.getLogger(C1812a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                C.a("Failed to load Contect Help File: \n" + ((Object) file) + ", Context Help will not be available.");
            }
        }
    }

    public static C1812a a() {
        if (f13515a == null) {
            f13515a = new C1812a();
            f13515a.b();
        }
        return f13515a;
    }
}
