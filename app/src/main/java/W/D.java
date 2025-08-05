package W;

import bH.InterfaceC0994b;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:W/D.class */
public class D implements InterfaceC0994b {

    /* renamed from: a, reason: collision with root package name */
    List f1917a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private static D f1918b = null;

    private D() {
    }

    public static D a() {
        if (f1918b == null) {
            f1918b = new D();
        }
        return f1918b;
    }

    public void a(File file) {
        this.f1917a.add(file);
    }

    @Override // bH.InterfaceC0994b
    public boolean b() {
        return true;
    }
}
