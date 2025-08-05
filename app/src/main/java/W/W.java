package W;

import ak.C0546f;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/W.class */
public class W {

    /* renamed from: a, reason: collision with root package name */
    private static W f1956a = null;

    /* renamed from: b, reason: collision with root package name */
    private Map f1957b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC0190p f1958c = null;

    public static W a() {
        if (f1956a == null) {
            f1956a = new W();
        }
        return f1956a;
    }

    public void a(String str, Class cls) {
        this.f1957b.put(str, cls);
    }

    private String c() {
        return b() == null ? "\t" : b().b();
    }

    private boolean d() {
        if (b() == null) {
            return true;
        }
        return b().a();
    }

    public V a(String str) throws V.a {
        if (this.f1957b.get(str) != null) {
            Class cls = (Class) this.f1957b.get(str);
            if (cls.equals(ak.R.class)) {
                return new ak.R(c(), d());
            }
            try {
                return (V) cls.newInstance();
            } catch (IllegalAccessException e2) {
                Logger.getLogger(W.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (InstantiationException e3) {
                Logger.getLogger(W.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (str.equals(X.f1959a)) {
            return new C0546f(c(), d());
        }
        throw new V.a("Unknown File Type!\nThis file format likely requires MegaLogViewer HD.");
    }

    public V a(File file) throws V.a {
        try {
            V vA = a(X.a(file));
            if (vA.a(file.getAbsolutePath())) {
                return vA;
            }
            try {
                vA.a();
                return null;
            } catch (Exception e2) {
                return null;
            }
        } catch (FileNotFoundException e3) {
            throw new V.a(e3.getMessage());
        }
    }

    public void a(InterfaceC0190p interfaceC0190p) {
        this.f1958c = interfaceC0190p;
    }

    public InterfaceC0190p b() {
        return this.f1958c;
    }
}
