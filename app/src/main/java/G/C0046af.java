package G;

import bH.C1007o;
import c.InterfaceC1386e;

/* renamed from: G.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/af.class */
public class C0046af implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    String f740a;

    /* renamed from: b, reason: collision with root package name */
    R f741b;

    public C0046af(R r2, String str) {
        this.f740a = null;
        this.f741b = null;
        this.f740a = str;
        this.f741b = r2;
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        try {
            return C1007o.a(this.f740a, this.f741b);
        } catch (V.g e2) {
            e2.printStackTrace();
            return true;
        }
    }
}
