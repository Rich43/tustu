package aP;

import bt.C1350i;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Gauge;

/* loaded from: TunerStudioMS.jar:aP/Z.class */
public class Z implements com.efiAnalytics.apps.ts.dashboard.aG {

    /* renamed from: d, reason: collision with root package name */
    private static String f2789d = "gaugeNumber";

    /* renamed from: a, reason: collision with root package name */
    C1350i f2790a;

    /* renamed from: b, reason: collision with root package name */
    String f2791b;

    /* renamed from: c, reason: collision with root package name */
    C1425x f2792c;

    public Z(String str, C1425x c1425x) {
        this.f2790a = null;
        this.f2791b = null;
        this.f2792c = null;
        this.f2792c = c1425x;
        this.f2790a = new C1350i(str + "_" + c1425x.getName());
        this.f2791b = str;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aG
    public void a(int i2, String str, boolean z2) {
        if (z2) {
            this.f2790a.a(f2789d + i2, str);
        }
    }

    public void a(C1425x c1425x) {
        String strA;
        for (int i2 = 0; i2 < c1425x.getComponentCount(); i2++) {
            if ((c1425x.getComponent(i2) instanceof Gauge) && (strA = this.f2790a.a(f2789d + i2)) != null && !strA.equals("")) {
                c1425x.a((Gauge) c1425x.getComponent(i2), strA, this.f2791b);
            }
        }
    }

    public void b(C1425x c1425x) {
        AbstractC1420s[] abstractC1420sArrJ = c1425x.j();
        for (int i2 = 0; i2 < abstractC1420sArrJ.length; i2++) {
            this.f2790a.a(f2789d + i2, "");
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aG
    public void a() {
        b(this.f2792c);
    }
}
