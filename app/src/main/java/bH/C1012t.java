package bH;

import W.C0184j;
import java.util.ArrayList;
import java.util.List;

/* renamed from: bH.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/t.class */
public class C1012t {
    public List a(C0184j c0184j, C0184j c0184j2, InterfaceC0999g interfaceC0999g, double[] dArr) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < c0184j.i() && i2 < c0184j2.i(); i2++) {
            if (interfaceC0999g == null || interfaceC0999g.a(i2)) {
                arrayList.add(Double.valueOf(c0184j.c(i2)));
                arrayList2.add(Double.valueOf(c0184j2.c(i2)));
            }
        }
        return a(arrayList, arrayList2, dArr);
    }

    public List a(List list, List list2, double[] dArr) {
        int iMin = Math.min(list.size(), list2.size());
        double[] dArr2 = new double[iMin];
        double[] dArr3 = new double[iMin];
        for (int i2 = 0; i2 < iMin; i2++) {
            dArr2[i2] = ((Double) list.get(i2)).doubleValue();
            dArr3[i2] = ((Double) list2.get(i2)).doubleValue();
        }
        bJ.c cVarA = a(dArr2, dArr3);
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < dArr.length; i3++) {
            arrayList.add(new C1013u(this, dArr[i3], cVarA.b(dArr[i3])));
        }
        return arrayList;
    }

    public bJ.c a(double[] dArr, double[] dArr2) {
        bJ.b bVar = new bJ.b();
        bVar.a(dArr2, dArr);
        return bVar;
    }
}
