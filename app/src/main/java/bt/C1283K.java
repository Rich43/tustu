package bt;

import G.C0088bu;
import java.util.ArrayList;
import java.util.List;

/* renamed from: bt.K, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/K.class */
public class C1283K {

    /* renamed from: a, reason: collision with root package name */
    private static C1283K f8677a = null;

    /* renamed from: b, reason: collision with root package name */
    private final List f8678b = new ArrayList();

    public static C1283K a() {
        if (f8677a == null) {
            f8677a = new C1283K();
        }
        return f8677a;
    }

    public void a(InterfaceC1284L interfaceC1284L) {
        this.f8678b.add(interfaceC1284L);
    }

    public InterfaceC1284L a(C0088bu c0088bu) {
        for (InterfaceC1284L interfaceC1284L : this.f8678b) {
            if (interfaceC1284L.a(c0088bu)) {
                return interfaceC1284L;
            }
        }
        return null;
    }
}
