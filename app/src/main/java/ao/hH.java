package ao;

import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1703u;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:ao/hH.class */
public class hH extends C1701s {

    /* renamed from: a, reason: collision with root package name */
    private int f6034a = -1;

    /* renamed from: b, reason: collision with root package name */
    private int f6035b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f6036c = -1;

    /* renamed from: d, reason: collision with root package name */
    private double f6037d = 0.0d;

    public void a(String[] strArr) {
        super.e(strArr);
    }

    public String[] c() {
        return super.a();
    }

    public void b(String[] strArr) {
        super.c(strArr);
    }

    public String[] d() {
        return super.b();
    }

    public hH e() {
        hH hHVar = new hH();
        String[] strArrD = d();
        String[] strArrC = c();
        hHVar.a(strArrC.length, strArrD.length);
        double[][] dArr = new double[strArrC.length][strArrD.length];
        hHVar.f("Default AFR");
        hHVar.d(h.i.a("yAxisField", "MAP"));
        hHVar.b(i());
        hHVar.a(1);
        hHVar.b(strArrD);
        hHVar.a(strArrC);
        String strE = h.i.e("defaultAfrValues", "");
        if (new StringTokenizer(strE, "\t").countTokens() - 1 == dArr.length * dArr[0].length) {
            StringTokenizer stringTokenizer = new StringTokenizer(strE, CallSiteDescriptor.OPERATOR_DELIMITER);
            for (int rowCount = hHVar.getRowCount() - 1; rowCount >= 0 && stringTokenizer.hasMoreTokens(); rowCount--) {
                StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), "\t");
                for (int i2 = 0; i2 < hHVar.getColumnCount() && stringTokenizer2.hasMoreTokens(); i2++) {
                    String strNextToken = stringTokenizer2.nextToken();
                    try {
                        hHVar.setValueAt(new Double(strNextToken), rowCount, i2);
                    } catch (Exception e2) {
                        System.out.println("bad Double " + strNextToken);
                    }
                }
            }
        } else {
            try {
                C1703u.b(hHVar);
            } catch (V.a e3) {
                e3.printStackTrace();
                bH.C.c("Error Generating AFR table, using method 2.");
                for (int i3 = 0; i3 < dArr.length; i3++) {
                    for (int i4 = 0; i4 < dArr[0].length; i4++) {
                        if (i3 < dArr.length / 3) {
                            dArr[i3][i4] = new Double(14.7d).doubleValue();
                        } else if (i3 < (2 * dArr.length) / 3 && i4 > 2) {
                            dArr[i3][i4] = new Double(13.9d).doubleValue();
                        } else if (i3 != (2 * dArr.length) / 3 || i4 <= dArr[0].length / 2) {
                            dArr[i3][i4] = new Double(12.5d).doubleValue();
                        } else {
                            dArr[i3][i4] = new Double(13.2d).doubleValue();
                        }
                    }
                }
                hHVar.a(dArr);
            }
        }
        hHVar.C();
        hHVar.q();
        return hHVar;
    }

    public void f() {
        String str = "";
        for (int i2 = 0; i2 < getRowCount(); i2++) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                str = str + ((Object) e(i2, i3)) + "\t";
            }
            str = str + CallSiteDescriptor.OPERATOR_DELIMITER;
        }
        h.i.c("defaultAfrValues", str);
    }

    public int g() {
        return this.f6034a == -1 ? o() : this.f6034a;
    }

    public void a(int i2) {
        this.f6034a = i2;
    }

    public int h() {
        if (this.f6035b == -1) {
            return 0;
        }
        return this.f6035b;
    }

    public int i() {
        return this.f6036c;
    }

    public void b(int i2) {
        this.f6036c = i2 == -1 ? 0 : i2;
    }

    public void a(double d2) {
        this.f6037d = d2;
    }

    public double j() {
        return this.f6037d;
    }
}
