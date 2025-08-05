package G;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:G/M.class */
class M implements InterfaceC0139v {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f458a;

    M(J j2) {
        this.f458a = j2;
    }

    @Override // G.InterfaceC0139v
    public int[] a(byte[] bArr, int i2, int i3) throws V.b {
        try {
            return C0995c.b(this.f458a.a(bArr, i2, 200L, i3, (C0130m) null));
        } catch (V.d e2) {
            throw new V.b(e2.getMessage());
        }
    }

    @Override // G.InterfaceC0139v
    public byte a() {
        return (byte) this.f458a.e().x();
    }
}
