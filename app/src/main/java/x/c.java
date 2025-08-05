package X;

import bH.C;
import bH.C0998f;
import bH.Z;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:X/c.class */
public class c {

    /* renamed from: b, reason: collision with root package name */
    String f2197b;

    /* renamed from: e, reason: collision with root package name */
    private static c f2194e = null;

    /* renamed from: c, reason: collision with root package name */
    public static String f2198c = ".cached";

    /* renamed from: f, reason: collision with root package name */
    private File f2195f = null;

    /* renamed from: a, reason: collision with root package name */
    C0998f f2196a = new C0998f();

    /* renamed from: g, reason: collision with root package name */
    private boolean f2199g = false;

    /* renamed from: h, reason: collision with root package name */
    private b f2200h = new d();

    /* renamed from: d, reason: collision with root package name */
    Map f2201d = new HashMap();

    private c() {
        a("JavaSerialization", this.f2200h);
        a("EFIA_Filtered_JSON", new a());
        this.f2196a.a(this.f2200h.a().getBytes());
        this.f2197b = Integer.toHexString(this.f2196a.b()).toUpperCase();
    }

    public static c a() {
        if (f2194e == null) {
            f2194e = new c();
        }
        return f2194e;
    }

    public void a(boolean z2) {
        this.f2199g = z2;
        Iterator it = this.f2201d.values().iterator();
        while (it.hasNext()) {
            ((b) it.next()).a(this.f2199g);
        }
    }

    public void a(String str, b bVar) {
        this.f2201d.put(str, bVar);
    }

    public void a(Object obj, String str, File file) {
        Z z2 = new Z();
        z2.a();
        this.f2200h.a(obj, a(file, str));
        C.d("Time to write cache Object: " + z2.d());
    }

    public Object a(String str, File file) {
        Object objA;
        Z z2 = new Z();
        z2.a();
        File fileA = a(file, str);
        if (fileA.exists()) {
            objA = this.f2200h.a(fileA);
            C.d("Time to read cache Object: " + z2.d());
        } else {
            objA = null;
        }
        return objA;
    }

    public void a(File file) {
        for (File file2 : file.listFiles()) {
            if (file2.getName().endsWith(f2198c)) {
                file2.delete();
            }
        }
    }

    private File a(File file, String str) {
        return new File(file, str + this.f2197b + f2198c);
    }

    public void b(File file) {
        this.f2195f = file;
    }
}
