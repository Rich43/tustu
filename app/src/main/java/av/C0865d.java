package av;

import G.C0072be;

/* renamed from: av.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/d.class */
final class C0865d implements InterfaceC0877p {
    C0865d() {
    }

    @Override // av.InterfaceC0877p
    public boolean a(C0072be c0072be) {
        return c0072be.aJ().toLowerCase().contains("fuel") || c0072be.aJ().toLowerCase().contains("spark") || c0072be.aJ().toLowerCase().contains("lambda");
    }
}
