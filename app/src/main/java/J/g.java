package J;

import G.C0123f;
import G.cT;
import bH.C0995c;
import bH.C0996d;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/g.class */
public class g implements cT, Serializable {

    /* renamed from: a, reason: collision with root package name */
    C0123f f1437a = new C0123f();

    /* renamed from: b, reason: collision with root package name */
    C0996d f1438b = new C0996d();

    @Override // G.cT
    public byte[] a(byte[] bArr) {
        int length = bArr.length;
        byte[] bArr2 = {(byte) ((length >> 8) & 255), (byte) (length & 255)};
        this.f1438b.reset();
        this.f1438b.update(bArr);
        byte[] bArrA = C0995c.a((int) this.f1438b.getValue(), this.f1437a.a(4), true);
        byte[] bArrA2 = this.f1437a.a(bArr2.length + bArr.length + bArrA.length);
        System.arraycopy(bArr2, 0, bArrA2, 0, bArr2.length);
        System.arraycopy(bArr, 0, bArrA2, bArr2.length, bArr.length);
        System.arraycopy(bArrA, 0, bArrA2, bArr2.length + bArr.length, bArrA.length);
        return bArrA2;
    }
}
