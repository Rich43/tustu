package J;

import G.C0123f;
import G.cT;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:J/b.class */
public class b implements cT, Serializable {

    /* renamed from: a, reason: collision with root package name */
    C0123f f1419a = new C0123f();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [int] */
    @Override // G.cT
    public byte[] a(byte[] bArr) {
        if (bArr.length == 1) {
            return bArr;
        }
        byte b2 = 0;
        for (byte b3 : bArr) {
            b2 += b3;
        }
        byte[] bArrA = this.f1419a.a(bArr.length + 1);
        System.arraycopy(bArr, 0, bArrA, 0, bArr.length);
        bArrA[bArrA.length - 1] = (byte) (b2 & 255);
        return bArrA;
    }
}
