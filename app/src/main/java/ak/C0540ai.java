package ak;

import W.InterfaceC0185k;
import java.util.Arrays;

/* renamed from: ak.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/ai.class */
class C0540ai implements InterfaceC0185k {

    /* renamed from: b, reason: collision with root package name */
    private int[] f4735b;

    /* renamed from: c, reason: collision with root package name */
    private String[] f4736c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Z f4737a;

    public C0540ai(Z z2, double[] dArr, String[] strArr) {
        this.f4737a = z2;
        this.f4735b = new int[dArr.length];
        this.f4736c = new String[dArr.length];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            this.f4735b[i2] = (int) dArr[i2];
            this.f4736c[i2] = strArr[i2];
        }
    }

    @Override // W.InterfaceC0185k
    public String a(float f2) {
        return this.f4736c[Arrays.binarySearch(this.f4735b, (int) f2)];
    }
}
