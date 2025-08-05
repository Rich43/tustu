package K;

import G.InterfaceC0042ab;
import G.T;
import G.aM;
import G.bL;

/* loaded from: TunerStudioMS.jar:K/b.class */
public class b implements InterfaceC0042ab {

    /* renamed from: a, reason: collision with root package name */
    int f1497a = 0;

    /* renamed from: b, reason: collision with root package name */
    long f1498b = 0;

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        aM aMVarA = bL.a(T.a().c(str), i2, i3, i2);
        if (aMVarA != null && aMVarA.B()) {
            this.f1497a += iArr.length;
        }
        this.f1498b |= (long) Math.pow(2.0d, i2);
    }

    public int a() {
        return this.f1497a;
    }

    public int b() {
        return Long.bitCount(this.f1498b);
    }
}
