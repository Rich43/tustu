package G;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:G/L.class */
class L implements InterfaceC0139v {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f457a;

    L(J j2) {
        this.f457a = j2;
    }

    @Override // G.InterfaceC0139v
    public int[] a(byte[] bArr, int i2, int i3) throws V.b {
        try {
            return C0995c.b(this.f457a.a(bArr, 15L, i2, i3, (C0130m) null));
        } catch (V.d e2) {
            bH.C.c("Controller reported Comms error, during validation. " + e2.getMessage());
            throw new V.b(e2.getMessage());
        }
    }

    @Override // G.InterfaceC0139v
    public byte a() {
        return (byte) this.f457a.e().x();
    }
}
