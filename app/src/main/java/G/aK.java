package G;

import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;

/* loaded from: TunerStudioMS.jar:G/aK.class */
public class aK extends aM {
    @Override // G.aM
    public int a() {
        return super.a();
    }

    @Override // G.aM
    public int b() {
        return super.b();
    }

    @Override // G.aM
    public void a(Y y2, double d2) {
    }

    @Override // G.aM
    public void a(String str) throws V.g {
        super.a(ControllerParameter.PARAM_CLASS_ARRAY);
    }

    @Override // G.aM
    public A c() {
        A aC2 = super.c();
        return new A(aC2.a(), aC2.b() / 2);
    }

    @Override // G.aM
    public double a(int i2) {
        return super.a(1 + (i2 * 2));
    }

    @Override // G.aM
    public double b(int i2) {
        return super.b(1 + (i2 * 2));
    }

    @Override // G.aM
    public int[][] a(Y y2) throws V.g {
        int iB;
        int iA;
        if (g() + (a() * m() * e()) > y2.c(d())) {
            throw new V.g("Attempt to retrieve data beyond page size!\n\tCheck offset and size for parameter:" + aJ());
        }
        int[][] iArr = new int[a()][m()];
        int iG = g() + e();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            for (int i3 = 0; i3 < iArr[0].length; i3++) {
                if (K()) {
                    iB = (c().b() - i2) - 1;
                    iA = (c().a() - i3) - 1;
                } else {
                    iB = i2;
                    iA = i3;
                }
                iArr[iB][iA] = C0995c.b(y2.b(d()), iG, e(), o(y2), z());
                if (N()) {
                    if (iArr[iB][iA] > ((int) Math.floor((float) ((b(iB) / E().a(iG)) - F().a())))) {
                        iArr[iB][iA] = (byte) iArr[iB][iA];
                    }
                }
                iG += e() * 2;
            }
        }
        return iArr;
    }

    @Override // G.aM
    public void a(Y y2, double[][] dArr) {
    }

    @Override // G.aM
    protected void a(Y y2, int i2, int i3, int[] iArr, boolean z2) {
    }
}
