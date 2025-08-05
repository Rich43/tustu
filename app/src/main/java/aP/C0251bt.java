package aP;

import bH.C1007o;
import c.InterfaceC1386e;

/* renamed from: aP.bt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bt.class */
class C0251bt implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    String f3096a;

    /* renamed from: b, reason: collision with root package name */
    G.R f3097b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0240bi f3098c;

    C0251bt(C0240bi c0240bi, String str, G.R r2) {
        this.f3098c = c0240bi;
        this.f3096a = null;
        this.f3097b = null;
        this.f3096a = str;
        this.f3097b = r2;
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        try {
            return C1007o.a(this.f3096a, this.f3097b);
        } catch (V.g e2) {
            e2.printStackTrace();
            return true;
        }
    }
}
